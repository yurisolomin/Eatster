package ru.baccasoft.eatster.image;

import com.vaadin.spring.annotation.SpringComponent;
import java.io.InputStream;
import ru.baccasoft.utils.files.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.baccasoft.utils.files.dto.FileWithContent;
import ru.baccasoft.utils.logging.Logger;
/*
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import ru.baccasoft.salonadvisor.image.DisplaySize;
import ru.baccasoft.utils.files.FilesService.DeletedStatus;
*/
@SpringComponent
public class ImageService {
    private static final Logger LOG = Logger.getLogger(ImageService.class);
    //public enum DISPLAYSIZE {FULL,MINI};
    @Autowired
    private FilesService filesService;
    
    public ImageResource uploadImage(String auxBusinessType, String fileName, InputStream fileInputStream) {
        LOG.debug("uploadImage: fileName={0},auxBusinessType={1}",fileName,auxBusinessType);
	FileWithContent fwc = filesService.createFile( fileInputStream, fileName, "IMAGE", auxBusinessType );
        LOG.debug("Create file with fwc.id={0},fwc.contentId={1},fwc.contentType={2},fwc.auxBusinessType={3}",fwc.id, fwc.contentId,fwc.contentType,fwc.auxBusinessType);
        String imageUrl = filesService.getDownloadUrl(fwc);
        LOG.debug("imageUrl={0}",imageUrl);
        String params[] = imageUrl.split("\\?");
        if (params.length < 2) {
            throw new RuntimeException( "Fail! filesService.getDownloadUrl returned URL without parameters (No '?'). URL="+imageUrl);
        }
        String urlParams = params[1];
        LOG.debug("Ok. Return param={0}",urlParams);
        return new ImageResource(fwc,urlParams);
    }
/*
    public ImageResource uploadScaledImage(String auxBusinessType, String fileName, byte[] inputArray, DisplaySize displaySize) {
        LOG.debug("uploadScaledImage: auxBusinessType={0}, fileName={1}, displaySize={2}",auxBusinessType,fileName,displaySize);
        //преобразуем картинку в нужный формат
        InputStream fileInputStream = scaleImageByDisplaySize( inputArray, displaySize );
        //если по каким-то причинам преобразовать не удалось, то возвращаем пустой url
        if (fileInputStream == null) {
            return null;
        }
        return uploadImage(auxBusinessType, fileName, fileInputStream);
    }
*/    
    public void deleteImage(long fileinfoId) {
        LOG.debug("deleteImage: fileinfoId={0}",fileinfoId);
        if (fileinfoId == 0) {
            LOG.debug("Fail.Skip delete file.");
            return;
        }
        filesService.deleteFile(fileinfoId);
        LOG.debug("Ok.");
    }
  /*  
    public OutputStream getOutputStream(long fileinfoId) {
        LOG.debug("getOutputStream: fileinfoId={0}",fileinfoId);
        FileWithContent file = filesService.getFile( fileinfoId );
        if (file == null) {
            throw new RuntimeException( "Fail! fileinfoId not found");
        }
    }
*/    
    /**
     * 
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
    public List<ImageResource> uploadPhoto(long objectId, String objectType,InputStream fileInputStream, String fileName
            ,DisplaySize displayFull
            ,DisplaySize displayMini
            ) 
        {
        LOG.debug("uploadPhoto: objectId={0},objectType={1},fileName={2},displayFull={3},displayMini={4}",objectId,objectType,fileName,displayFull,displayMini);
        fileName = fileName.toLowerCase();
        //имя файла для миниматюры
        String mini_fileName = "mini_"+fileName;
        List<ImageResource> listImageResource = new ArrayList();
        //скинем картинку в память для многократного использования
        byte[] inputArray;
        try {
            inputArray = IOUtils.toByteArray(fileInputStream);
        } catch (IOException ex) {
            LOG.error("Error uploadPhoto on toByteArray(..)! FileName={0},Error={1}",fileName,ex.getMessage());
            return listImageResource;
        }
        String auxBusinessType = objectType+"."+objectId;
        ImageResource imageResource = uploadScaledImage(auxBusinessType, fileName, inputArray, displayFull);
        listImageResource.add(imageResource);
        LOG.debug("loaded imageResource.full={0}",imageResource);
        if (displayMini != null) {
            auxBusinessType = "mini."+objectType+"."+objectId;
            imageResource = uploadScaledImage(auxBusinessType, mini_fileName, inputArray, displayMini);
            LOG.debug("loaded imageResource.mini={0}",imageResource);
        }
        LOG.debug("Ok.");
        return listImageResource;
    }
*/
/*    
    private static BufferedImage scaleImageTo(BufferedImage sourceImage, DisplaySize targetSize) {
        if (targetSize.width == sourceImage.getWidth() || targetSize.height == sourceImage.getHeight()) {
            return sourceImage;
        }
        double srcWidth = sourceImage.getWidth();
        double srcHeight = sourceImage.getHeight();
        double destWidth = targetSize.width;
        double destHeight = targetSize.height;
        double widthRatio = destWidth / srcWidth;
        double heightRatio = destHeight / srcHeight;
        final Scalr.Mode resizeMode;
        int resizeToDimension;
        if (widthRatio < heightRatio) {
            resizeMode = Scalr.Mode.FIT_TO_WIDTH;
            resizeToDimension = targetSize.width;
        } else {
            resizeMode = Scalr.Mode.FIT_TO_HEIGHT;
            resizeToDimension = targetSize.height;
        }
        BufferedImage result = Scalr.resize(sourceImage, Method.ULTRA_QUALITY, resizeMode, resizeToDimension);
        return result;
    }
*/    
/*    
    public DisplaySize getDisplaySizeForAction(DISPLAYSIZE size) {
        int width,height;
        if (size == DISPLAYSIZE.FULL) {
            width = appProp.getPropertyIntDef("action.photo.full.width",1024);
            height = appProp.getPropertyIntDef("action.photo.full.height",512);
        } else {
            width = appProp.getPropertyIntDef("action.photo.mini.width",470);
            height = appProp.getPropertyIntDef("action.photo.mini.height",246);
        }
        return new DisplaySize( width, height);
    }
*/    
/*    
    public InputStream scaleImageByDisplaySize(byte[] inputArray, DisplaySize displaySize) {
        LOG.debug("scaleImageByDisplaySize:");
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(inputArray);
            BufferedImage sourceImage = ImageIO.read(byteInputStream);
            LOG.debug("sourceSize={0},sourceWidth={1},sourceHeight={2},new displaysize={3}",inputArray.length,sourceImage.getWidth(),sourceImage.getHeight(),displaySize);
            BufferedImage resultImage = scaleImageTo(sourceImage,displaySize);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resultImage, "JPG", outputStream);
            byte[] resultArray = outputStream.toByteArray();
            InputStream resultInputStream = new ByteArrayInputStream(resultArray);
            LOG.debug("Ok. outputSize={0},resultWidth={1},resultHeight={2}",resultArray.length,resultImage.getWidth(),resultImage.getHeight());
            return resultInputStream;
        } catch (IOException ex) {
            LOG.error("Error scaleImageByDisplaySize! Error={0}",ex.getMessage());
            return null;
        }
    }
*/    
/*    
    
    private static final String PREF_SALON_PHOTO = "SALON.PHOTO.";
    private static final String PREF_SALON_GALLERY_PHOTO = "SALON.GALLERYPHOTO.";
    private static final String PREF_EMPLOYEE_PHOTO = "EMPLOYEE.PHOTO.";

    
    public void deleteSalonLogo(long salonId) {
        LOG.debug("deleteSalonLogo: salonId={0}",salonId);
        String auxBusinessType = PREF_SALON_LOGO+salonId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        //удалим старые логотипы
        List<Long> activeLogoIds = filesService.getFileIds( DeletedStatus.ACTIVE_ONLY, null, auxBusinessType);
        for(Long id: activeLogoIds) {
            LOG.debug("delete old logo with id={0}",id);
            filesService.deleteFile(id);
        }
    }

    public String uploadSalonLogo(long salonId, byte[] inputArray, String fileName, DISPLAYSIZE size) {
        //deleteSalonLogo(salonId);
        LOG.debug("uploadSalonLogo: salonId={0}, fileName={1}",salonId,fileName);
        String auxBusinessType = PREF_SALON_LOGO+salonId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        //преобразуем картинку в нужный формат
        InputStream fileInputStream = scaleImageByDisplaySize( inputArray, getDisplaySizeForSalon(size) );
        //если по каким-то причинам преобразовать не удалось, то возвращаем пустой url
        if (fileInputStream == null) {
            return "";
        }
        return upload(fileInputStream, fileName, auxBusinessType);
    }
    
    public void deleteSalonPhoto(long salonId,String urlParams) {
        LOG.debug("deleteSalonPhoto: salonId={0},urlParams={1}",salonId,urlParams);
        String auxBusinessType = PREF_SALON_PHOTO+salonId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        //удалим старое фото
        List<FileWithContent> activePhotoIds = filesService.getFiles( DeletedStatus.ACTIVE_ONLY, null, auxBusinessType);
        for(FileWithContent fwc: activePhotoIds) {
            String url = filesService.getDownloadUrl(fwc);
            if (url.toLowerCase().trim().endsWith(urlParams.toLowerCase())) {
                LOG.debug("delete old photo with id={0}, urlParams={1}",fwc.id,urlParams);
                filesService.deleteFile(fwc.id);
            }
        }
    }
    
    public String uploadSalonPhoto(long salonId, byte[] inputArray, String fileName, int maxPhotos, DISPLAYSIZE size) {
        LOG.debug("uploadSalonPhoto: salonId={0}, fileName={1}",salonId,fileName);
        String auxBusinessType = PREF_SALON_PHOTO+salonId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        if (maxPhotos > 0) {
            List<FileWithContent> activePhotoIds = filesService.getFiles( DeletedStatus.ACTIVE_ONLY, null, auxBusinessType);
            LOG.debug("Number images = {0}",activePhotoIds.size());
            if (activePhotoIds.size() >= maxPhotos) {
                LOG.warn("Number images ({0}) exceeds or equal {1}",activePhotoIds.size(),maxPhotos);
                return "";
            }
        }
        //преобразуем картинку в нужный формат
        InputStream fileInputStream = scaleImageByDisplaySize( inputArray, getDisplaySizeForSalon(size) );
        //если по каким-то причинам преобразовать не удалось, то возвращаем пустой url
        if (fileInputStream == null) {
            return "";
        }
        return upload(fileInputStream, fileName, auxBusinessType);
    }
    
    
    public void deleteSalonGalleryPhoto(long salonId,String urlParams) {
        LOG.debug("deleteSalonGalleryPhoto: salonId={0},urlParams={1}",salonId,urlParams);
        String auxBusinessType = PREF_SALON_GALLERY_PHOTO+salonId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        //удалим старое фото
        List<FileWithContent> activePhotoIds = filesService.getFiles( DeletedStatus.ACTIVE_ONLY, null, auxBusinessType);
        for(FileWithContent fwc: activePhotoIds) {
            String url = filesService.getDownloadUrl(fwc);
            if (url.toLowerCase().trim().endsWith(urlParams.toLowerCase())) {
                LOG.debug("delete old gallery photo with id={0}, urlParams={1}",fwc.id,urlParams);
                filesService.deleteFile(fwc.id);
            }
        }
    }
        
    public String uploadSalonGalleryPhoto(long salonId, byte[] inputArray, String fileName, DISPLAYSIZE size) {
        LOG.debug("uploadSalonGalleryPhoto: salonId={0}, fileName={1}",salonId,fileName);
        String auxBusinessType = PREF_SALON_GALLERY_PHOTO+salonId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        List<FileWithContent> activePhotoIds = filesService.getFiles( DeletedStatus.ACTIVE_ONLY, null, auxBusinessType);
        LOG.debug("Number images = {0}",activePhotoIds.size());
        
        //преобразуем картинку в нужный формат
        InputStream fileInputStream = scaleImageByDisplaySize( inputArray, getDisplayGallerySizeForSalon(size) );
        //если по каким-то причинам преобразовать не удалось, то возвращаем пустой url
        if (fileInputStream == null) {
            return "";
        }
        return upload(fileInputStream, fileName, auxBusinessType);
    }
    
    public void deleteEmployeePhoto(long employeeId) {
        LOG.debug("deleteEmployeePhoto: employeeId={0}",employeeId);
        String auxBusinessType = PREF_EMPLOYEE_PHOTO+employeeId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        //удалим старые логотипы
        List<Long> activePhotoIds = filesService.getFileIds( DeletedStatus.ACTIVE_ONLY, null, auxBusinessType);
        for(Long id: activePhotoIds) {
            LOG.debug("delete old photo with id={0}",id);
            filesService.deleteFile(id);
        }
    }
    public String uploadEmployeePhoto(long employeeId, byte[] inputArray, String fileName, DISPLAYSIZE size) {
        LOG.debug("uploadEmployeePhoto: employeeId={0}, fileName={1}",employeeId,fileName);
        String auxBusinessType = PREF_EMPLOYEE_PHOTO+employeeId;
        LOG.debug("auxBusinessType={0}",auxBusinessType);
        //преобразуем картинку в нужный формат
        InputStream fileInputStream = scaleImageByDisplaySize( inputArray, getDisplaySizeForEmployee(size) );
        //если по каким-то причинам преобразовать не удалось, то возвращаем пустой url
        if (fileInputStream == null) {
            return "";
        }
        return upload(fileInputStream, fileName, auxBusinessType);
    }

    
    
    
    public DisplaySize getDisplayGallerySizeForSalon(DISPLAYSIZE size) {
        int width,height;
        if (size == DISPLAYSIZE.FULL) {
            width = appProp.getPropertyIntDef("salon.galleryphoto.full.width",1024);
            height = appProp.getPropertyIntDef("salon.galleryphoto.full.height",512);
        } else {
            width = appProp.getPropertyIntDef("salon.galleryphoto.mini.width",470);
            height = appProp.getPropertyIntDef("salon.galleryphoto.mini.height",246);
        }
        return new DisplaySize( width, height);
    }
        
    public DisplaySize getDisplaySizeForEmployee(DISPLAYSIZE size) {
        int width,height;
        if (size == DISPLAYSIZE.FULL) {
            width = appProp.getPropertyIntDef("employee.photo.full.width",1024);
            height = appProp.getPropertyIntDef("employee.photo.full.height",512);
        } else {
            width = appProp.getPropertyIntDef("employee.photo.mini.width",470);
            height = appProp.getPropertyIntDef("employee.photo.mini.height",246);
        }
        return new DisplaySize( width, height);
    }
    
*/    
/*
    private long getImageSize(BufferedImage img) {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", tmp);
            tmp.close();
            return tmp.size();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
*/    
}
