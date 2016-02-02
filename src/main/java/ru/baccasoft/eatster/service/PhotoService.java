package ru.baccasoft.eatster.service;

import com.vaadin.spring.annotation.SpringComponent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import ru.baccasoft.eatster.image.DisplaySize;
import ru.baccasoft.eatster.image.ImageResource;
import ru.baccasoft.eatster.image.ImageService;
import ru.baccasoft.eatster.model.PhotoModel;
import ru.baccasoft.eatster.model.RestaurantPhotosModel;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class PhotoService {
    private static final Logger LOG = Logger.getLogger(PhotoService.class);
    
    private JdbcTemplate jdbc;
    private static final String SQL_TABLE = "photo";
    private static final String SQL_COLUMNS = "id,deleted,object_id,object_type,file_info_id,photo_urlparams,file_info_mini_id,photo_urlparams_mini,status";
    private static final String SQL_SELECT_ALL = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where deleted = false order by id";
    private static final String SQL_SELECT_ITEM_BY_OBJECTTYPE = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where object_id = ? and object_type = ? and deleted = false order by id desc";
    private static final String SQL_SELECT_ITEM_BY_STATUS = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where object_id = ? and status = ? and deleted = false order by id desc";
    private static final String SQL_SELECT_ALL_TO_MODERATION = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where object_type in (?,?) and status = ? and deleted = false order by id desc";
//    private static final String SQL_SELECT_FOR_ACTMODERATION = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where object_type in ('"+PhotoModel.TYPE_ACT_PHOTO+"') status = '"+PhotoModel.STAT_MODERATION+"' and deleted = false order by id desc";
//    private static final String SQL_SELECT_FOR_RESTMODERATION = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where object_type in ('"+PhotoModel.TYPE_REST_BACK+"') status = '"+PhotoModel.STAT_MODERATION+"' and deleted = false order by id desc";
    private static final String SQL_INSERT_ITEM = "insert into "+SQL_TABLE+" (deleted,modified,object_id,object_type,file_info_id,photo_urlparams,file_info_mini_id,photo_urlparams_mini,status) values(false,now(),?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE_ITEM = "update "+SQL_TABLE+" set modified=now(),object_id=?,object_type=?,file_info_id=?,photo_urlparams=?,file_info_mini_id=?,photo_urlparams_mini=?,status=? where id = ?";
    private static final String SQL_DELETE_ITEM = "update "+SQL_TABLE+" set modified=now(),deleted=true where id = ?" ;
//    private static final String SQL_SELECT_BY_REST = "select "+SQL_COLUMNS+" from "+SQL_TABLE+" where deleted = false and restaurant_id = ? and status = ? order by id";
    private static final PhotoMapper MAPPER = new PhotoMapper();

    @Autowired
    ImageService imageService;
    
    @Autowired
    public void setDataSource( DataSource dataSource ) {
        jdbc = new JdbcTemplate( dataSource );
    }
    
    private static final class PhotoMapper implements RowMapper<PhotoModel> {
        @Override
        public PhotoModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            PhotoModel item = new PhotoModel();
            int index = 0;
            item.setId(rs.getInt(++index));
            item.setDeleted(rs.getBoolean(++index));
            item.setObjectId(rs.getInt(++index));
            item.setObjectType(rs.getString(++index));
            item.setFileinfoId(rs.getInt(++index));
            item.setPhotoUrlParams(rs.getString(++index));
            item.setFileinfoMiniId(rs.getInt(++index));
            item.setPhotoUrlParamsMini(rs.getString(++index));
            item.setStatus(rs.getString(++index));
            return item;
        }
    }	
    @Transactional(readOnly=true)
    public List<PhotoModel> findAll() {
        LOG.debug("findAll:");
        List<PhotoModel> list = jdbc.query(SQL_SELECT_ALL, MAPPER);	
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }

    public List<PhotoModel> findByObjectType(long objectId, String objectType) {
        LOG.debug("findByObject: findByObjectType={0}, objectType={1}",objectId,objectType);
        List<PhotoModel> list = jdbc.query(SQL_SELECT_ITEM_BY_OBJECTTYPE, new Object[]{objectId,objectType}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
    
    public List<PhotoModel> findByStatus(long objectId, String status) {
        LOG.debug("findByStatus: objectId={0}, status={1}",objectId,status);
        List<PhotoModel> list = jdbc.query(SQL_SELECT_ITEM_BY_STATUS, new Object[]{objectId,status}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
/*    
    public List<PhotoModel> findActPhotosToModeration() {
        LOG.debug("findActPhotosToModeration:");
        List<PhotoModel> list = jdbc.query(SQL_SELECT_ALL_TO_MODERATION, 
                new Object[]{PhotoModel.TYPE_ACT_PHOTO,PhotoModel.TYPE_ACT_PHOTO,PhotoModel.STAT_MODERATION}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }
*/    
    public List<PhotoModel> findRestPhotosToModeration() {
        LOG.debug("findRestPhotosToModeration:");
        List<PhotoModel> list = jdbc.query(SQL_SELECT_ALL_TO_MODERATION, 
                new Object[]{PhotoModel.TYPE_REST_BACK,PhotoModel.TYPE_REST_PHOTO,PhotoModel.STAT_MODERATION}, MAPPER );
        LOG.debug("Ok. return items {0}",list.size());
        return list;
    }

    @Transactional(readOnly=true)
    public PhotoModel getItem(long objectId, String objectType) {
        LOG.debug("getItem: objectId={0}, objectType={1}",objectId,objectType);
        List<PhotoModel> list = jdbc.query(SQL_SELECT_ITEM_BY_OBJECTTYPE, new Object[]{objectId,objectType}, MAPPER );
        if (list.size() == 1) {
            LOG.debug("Ok. item={0}",list.get(0));
            return list.get(0);
        }
        if (list.size() > 1) {
            LOG.warn("Warn. Found items={0}. Return first item={1}",list.size(),list.get(0));
            return list.get(0);
        }
        LOG.warn("Fail. item not found");
        return null;
    }
    
    @Transactional(readOnly=true)
    public List<PhotoModel> findByRestaurant(long restaurantId) {
        return findByObjectType(restaurantId,PhotoModel.TYPE_REST_PHOTO);
    }
    
    @Transactional(readOnly=true)
    public PhotoModel findRestaurantBackground(long restaurantId) {
        return getItem(restaurantId,PhotoModel.TYPE_REST_BACK);
    }
        
    @Transactional(readOnly=true)
    public PhotoModel findRestaurantLogo(long restaurantId) {
        return getItem(restaurantId,PhotoModel.TYPE_REST_LOGO);
    }
        
    @Transactional
    public long insertItem(final PhotoModel item) {
        LOG.debug("insertItem: item={0}",item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM, new String[] {"id"});
                    int index = 0;
                    ps.setLong(++index,   item.getObjectId());
                    ps.setString(++index, item.getObjectType());
                    ps.setLong(++index,   item.getFileinfoId());
                    ps.setString(++index, item.getPhotoUrlParams());
                    ps.setLong(++index,   item.getFileinfoMiniId());
                    ps.setString(++index, item.getPhotoUrlParamsMini());
                    ps.setString(++index, item.getStatus());
                    return ps;
                }
            },
        keyHolder); 
        LOG.debug("Ok. return id={0}",keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    @Transactional
    public int updateItem(PhotoModel item) {
        LOG.debug("updateItem: item={0}",item);
        int result = jdbc.update(SQL_UPDATE_ITEM, 
                new Object[]{
                    item.getObjectId(),
                    item.getObjectType(),
                    item.getFileinfoId(),
                    item.getPhotoUrlParams(),
                    item.getFileinfoMiniId(),
                    item.getPhotoUrlParamsMini(),
                    item.getStatus(),
                    item.getId()} 
        );
        LOG.debug("Ok. return {0}",result);
        return result;
    }

    @Transactional
    private int deleteItem(long id) {
        LOG.debug("deleteItem: id={0}",id);
        int result = jdbc.update(SQL_DELETE_ITEM, new Object[]{id} );
        LOG.debug("Ok. return {0}",result);
        return result;
    }

    
    /*
     * Загрузка фото и миниатюры в файловое хранилище
     * @param objectId идентификатор объекта
     * @param objectType тип объекта
     * @param fileInputStream входящий файл
     * @param fileName имя входящего файла
     * @param displayFull преобразование к размеру максимальной картинки
     * @param displayMini преобразование к размеру минмальной картинки (может быть null)
     * @return 1й элемент это полноценная картинка,2й элемент это миниматюра
     */
    /*
    @Transactional
    public PhotoModel uploadScaledPhoto(long objectId, String objectType,InputStream fileInputStream, String fileName
            ,DisplaySize displayFull
            ,DisplaySize displayMini
            ) 
        {
        LOG.debug("uploadScaledPhoto: objectId={0},objectType={1},fileName={2},displayFull={3},displayMini={4}",objectId,objectType,fileName,displayFull,displayMini);
        PhotoModel photoModel = getItem(objectId, objectType);
        if (photoModel != null) {
            imageService.deleteImage(photoModel.getFileinfoId());
            imageService.deleteImage(photoModel.getFileinfoMiniId());
            photoModel.setFileinfoId(0);
            photoModel.setFileinfoMiniId(0);
            photoModel.setPhotoUrlParams("");
            photoModel.setPhotoUrlParamsMini("");
        } else {
            photoModel = new PhotoModel();
            photoModel.setObjectId(objectId);
            photoModel.setObjectType(objectType);
        }
        fileName = fileName.toLowerCase();
        //имя файла для миниматюры
        String mini_fileName = "mini_"+fileName;
        //скинем картинку в память для многократного использования
        byte[] inputArray;
        try {
            inputArray = IOUtils.toByteArray(fileInputStream);
        } catch (IOException ex) {
            LOG.error("Error uploadPhoto on toByteArray(..)! FileName={0},Error={1}",fileName,ex.getMessage());
            return null;
        }
        ImageResource imageResource;
        String auxBusinessType = objectType+"."+objectId;
        //если надо сжать картинку, то сжимаем
        if (displayFull != null) {
            imageResource = imageService.uploadScaledImage(auxBusinessType, fileName, inputArray, displayFull);
        } else {
            imageResource = imageService.uploadImage(auxBusinessType, fileName, new ByteArrayInputStream(inputArray));
        }
        photoModel.setFileinfoId(imageResource.getFileinfoId());
        photoModel.setPhotoUrlParams(imageResource.getUrlParams());
        LOG.debug("loaded imageResource.full={0}",imageResource);
        //если нужна миниатюра
        if (displayMini != null) {
            auxBusinessType = "mini."+objectType+"."+objectId;
            imageResource = imageService.uploadScaledImage(auxBusinessType, mini_fileName, inputArray, displayMini);
            photoModel.setFileinfoMiniId(imageResource.getFileinfoId());
            photoModel.setPhotoUrlParamsMini(imageResource.getUrlParams());
            LOG.debug("loaded imageResource.mini={0}",imageResource);
        }
        if (photoModel.getId() == 0) {
            long insertedId = insertItem(photoModel);
            photoModel.setId(insertedId);
        } else {
            updateItem(photoModel);
        }
        LOG.debug("Ok.");
        return photoModel;
    }
    */
    public void deletePhoto(PhotoModel photoModel)  {
        LOG.debug("deletePhoto: photoModel={0}",photoModel);
        long fileinfoId = photoModel.getFileinfoId();
        //удалим старый файл (хотя можно и не удалять, т.к. на диске он все равно остается)
        if (fileinfoId != 0) {
            imageService.deleteImage(fileinfoId);
        }
        deleteItem(photoModel.getId());
        LOG.debug("Ok.");
    }
    
    @Transactional
    public PhotoModel uploadPhoto(PhotoModel photoModel,InputStream fileInputStream, String fileName)  {
        LOG.debug("uploadPhoto: photoModel={0},fileName={1}",photoModel,fileName);
        long objectId = photoModel.getObjectId();
        String objectType = photoModel.getObjectType();
        long prevFileinfoId = photoModel.getFileinfoId();
        fileName = fileName.toLowerCase();
        String auxBusinessType = objectType+"."+objectId;
        ImageResource imageResource = imageService.uploadImage(auxBusinessType, fileName, fileInputStream);
        photoModel.setFileinfoId(imageResource.getFileinfoId());
        photoModel.setPhotoUrlParams(imageResource.getUrlParams());
        LOG.debug("loaded imageResource={0}",imageResource);
        if (photoModel.getId() == 0) {
            long insertedId = insertItem(photoModel);
            photoModel.setId(insertedId);
        } else {
            updateItem(photoModel);
        }
        //удалим старый файл (хотя можно и не удалять, т.к. на диске он все равно остается)
        if (prevFileinfoId != 0) {
            imageService.deleteImage(prevFileinfoId);
        }
        LOG.debug("Ok.");
        return photoModel;
    }
    
/*
    @Transactional
    public PhotoModel uploadPhoto(long objectId, String objectType,InputStream fileInputStream, String fileName) 
        {
        LOG.debug("uploadPhoto: objectId={0},objectType={1},fileName={2}",objectId,objectType,fileName);
        PhotoModel photoModel = getItem(objectId, objectType);
        if (photoModel != null) {
            imageService.deleteImage(photoModel.getFileinfoId());
            imageService.deleteImage(photoModel.getFileinfoMiniId());
            photoModel.setFileinfoId(0);
            photoModel.setFileinfoMiniId(0);
            photoModel.setPhotoUrlParams("");
            photoModel.setPhotoUrlParamsMini("");
        } else {
            photoModel = new PhotoModel();
            photoModel.setObjectId(objectId);
            photoModel.setObjectType(objectType);
        }
        fileName = fileName.toLowerCase();
        String auxBusinessType = objectType+"."+objectId;
        ImageResource imageResource = imageService.uploadImage(auxBusinessType, fileName, fileInputStream);
        photoModel.setFileinfoId(imageResource.getFileinfoId());
        photoModel.setPhotoUrlParams(imageResource.getUrlParams());
        LOG.debug("loaded imageResource={0}",imageResource);
        LOG.debug("Ok.");
        return photoModel;
    }
*/
    

    @Transactional(readOnly=true)
    public RestaurantPhotosModel findPublishedByRestaurant(long restaurantId) {
        LOG.debug("findPublishedByRestaurant: restaurantId={0}",restaurantId);
        RestaurantPhotosModel restPhotos = new RestaurantPhotosModel();
        List<PhotoModel> list = findByStatus(restaurantId,PhotoModel.STAT_PUBLISHED);
        for(PhotoModel photo: list) {
            String urlParams = photo.getPhotoUrlParams();
            if (photo.getObjectType().equals(PhotoModel.TYPE_REST_LOGO)) {
                restPhotos.setLogo(urlParams);
            }
            if (photo.getObjectType().equals(PhotoModel.TYPE_REST_BACK)) {
                restPhotos.setMainPhoto(urlParams);
            }
            if (photo.getObjectType().equals(PhotoModel.TYPE_REST_PHOTO)) {
                restPhotos.getPhotos().add(urlParams);
            }
        }
        LOG.debug("Ok. return items {0}",restPhotos);
        return restPhotos;
    }    
        
/*
    public RestaurantPhotosModel findPublishedByRestaurant(long restaurantId) {
        return findByRestaurant(restaurantId,PhotoModel.STAT_PUBLISHED);
    }    
*/
}
