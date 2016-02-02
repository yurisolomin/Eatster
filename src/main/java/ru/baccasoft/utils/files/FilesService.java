package ru.baccasoft.utils.files;

import com.vaadin.spring.annotation.SpringComponent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.baccasoft.utils.files.dto.FileContent;
import ru.baccasoft.utils.files.dto.FileWithContent;
import ru.baccasoft.utils.logging.Logger;

@SpringComponent
public class FilesService implements InitializingBean {

	private static final Logger log = Logger.getLogger( FilesService.class );

	@Autowired
	private FilesDao mapper;

	public List<FileWithContent> getChanges( Date since ) {
		return mapper.getArbitraryFiles( DeletedStatus.ACTIVE_ONLY, since );
	}
	
	/**
	 * Выбирает из базы файлы по заданному параметрами условию
	 * 
	 * @param deletedStatus как учитывать (и учитывать ли) удаленность объектов
	 * @param since если не пусто, то только измененные не ранее чем since
	 * @param auxTypes если не пусто, то только имеющие aux_business_type из заданного множества строк.
	 */
	public List<FileWithContent> getFiles( DeletedStatus deletedStatus, Date since, String...auxTypes) {
		return mapper.getArbitraryFiles( deletedStatus, since, auxTypes );
	}
	
	/**
	 * Выбирает из базы идентификаторы по заданному параметрами условию
	 * 
	 * @param deletedStatus как учитывать (и учитывать ли) удаленность объектов
	 * @param since если не пусто, то только измененные не ранее чем since
	 * @param auxTypes если не пусто, то только имеющие aux_business_type из заданного множества строк.
	 */
	public List<Long> getFileIds( DeletedStatus deletedStatus, Date since, String...auxTypes) {
		return mapper.getArbitraryIds( deletedStatus, since, auxTypes );
	}
	
	public static enum DeletedStatus {
		DELETED_ONLY, ACTIVE_ONLY, ANY
	}

	public List<Long> getDeleted( Date since ) {
		return mapper.getArbitraryIds( DeletedStatus.DELETED_ONLY, since );
	}
	
	@Transactional
	public void deleteFile( long id ) {
		mapper.deleteFile( id );
	}
	
	public FileWithContent getFile( long id ) {
		return mapper.getFile( id );
	}
	
	protected InputStream getContentStream(FileWithContent fwc) {
		try {
			return new FileInputStream( cfg.contentStoragePath+fwc.content.storageUri);
		} catch( FileNotFoundException e ) {
			throw new RuntimeException(e);
		}
	}
	
	protected int getContentSize(FileWithContent fwc) {
		long result = new File(cfg.contentStoragePath+fwc.content.storageUri).length();
		if( result > Integer.MAX_VALUE ) {
			throw new RuntimeException( "Length too large to fit into int: "+result);
		}
		return (int) result;
	}
	
	public String getDownloadUrl(FileWithContent fwc) {
		return MessageFormat.format( cfg.downloadUrlFormat, fwc.id, fwc.contentId);
	}

	@Transactional
	public FileWithContent createFile( 
			InputStream data, 
			String filename, 
			String contentType, 
			String auxBusinessType ) {
		
		if (filename == null) {
			filename = "";
		}
		if (contentType == null) {
			contentType = "";
		}
		if (auxBusinessType == null) {
			auxBusinessType = "";
		}

		long contentId = mapper.getId();
		String uri = buildUri( contentId );
		int bytes;
		OutputStream out = null;
		try {
			out = createOutputStream( uri );
			bytes = IOUtils.copy( data, out );
		} catch( IOException e ) {
			log.warn( e, "Error writing data to " + uri );
			throw new RuntimeException( "Error writing data to " + uri, e );
		} finally {
			IOUtils.closeQuietly( data );
			IOUtils.closeQuietly( out );
		}

		if (bytes == 0) {
			log.warn( "Cowardly refusing to store zero-length input." );
			throw new RuntimeException( "Cowardly refusing to store zero-length input." );
		}
		
		FileContent content = new FileContent();
		content.id = contentId;
		content.previousVersionId = null;
		content.storageUri = uri;
		mapper.createContent( content );
		
		FileWithContent result = new FileWithContent();
		result.id = 0;
		result.contentId = contentId;
		result.deleted = false;
		result.filename = filename;
		result.contentType = contentType;
		result.auxBusinessType = auxBusinessType;
		mapper.createFile( result );
		
		result.content = content;
		return result;
	}
	
	private OutputStream createOutputStream( String uri ) throws IOException {
		File targetFile = new File( cfg.contentStoragePath + uri );
		if (targetFile.exists()) {
			log.fatal( "{0} already exists. THIS IS A SYMPTOM OF SEVERE MISCONFIGURATION!", targetFile );
			throw new RuntimeException( "SEVERE MISCONFIGURATION! File exists: " + targetFile );
		}
		File parentDir = targetFile.getParentFile();
		if (parentDir.exists()) {
			if (!parentDir.isDirectory()) {
				log.fatal( "Parent dir of {0} is not a directory. THIS IS A SYMPTOM OF SEVERE MISCONFIGURATION!",
						targetFile );
				throw new RuntimeException( "SEVERE MISCONFIGURATION! Parent is not a directory: " + targetFile );
			}
		} else {
			parentDir.mkdirs();
			if (!parentDir.exists()) {
				String message = "Failed to create parent dir for " + targetFile;
				log.warn( message );
				throw new RuntimeException( message );
			}
		}

		if (!targetFile.createNewFile()) {
			String message = "Failed to create empty file " + targetFile;
			log.warn( message );
			throw new RuntimeException( message );
		}
		
		return new FileOutputStream(targetFile);
	}

	private FileServiceConfig cfg;
	
	@Autowired( required = false )
	public void setConfig( IFilesServiceConfigProvider provider ) { 
		cfg = provider.getFileServiceConfig();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if( cfg == null ) {
			log.info( "No explicit configuration set up, falling back to FileServiceConfig.xml" );
			InputStream configFile = getClass().getClassLoader().getResourceAsStream( "FileServiceConfig.xml" );
			Unmarshaller unmarshaller = JAXBContext.newInstance( FileServiceConfig.class ).createUnmarshaller();
			cfg = (FileServiceConfig) unmarshaller.unmarshal( configFile );
		}


		File contentStorage = new File( cfg.contentStoragePath );
		if (contentStorage.exists() && contentStorage.isDirectory() && contentStorage.canWrite()) {
			cfg.contentStoragePath = contentStorage.getCanonicalPath();
		} else {
			final String message = "Invalid content storage path '" + cfg.contentStoragePath + "'";
			log.warn( message );
			throw new RuntimeException( message );
		}
		if (!cfg.downloadUrlFormat.startsWith( "http://" ) && !cfg.downloadUrlFormat.startsWith( "https://" )) {
			final String message = "Invalid protocol specification in url format '" + cfg.downloadUrlFormat + "'";
			log.warn( message );
			throw new RuntimeException( message );
		}
		log.info( "Files service config is {0}", cfg );
	}

	// нагло-американское billion для русского читателя неоднозначно, что бы ни
	// говорила об этом педивикия
	protected static final long МИЛЛИАРД = 1000 * 1000 * 1000;

	protected static String buildUri( long id ) {
		if (id >= МИЛЛИАРД) {
			long prefix = id / МИЛЛИАРД;
			long suffix = id % МИЛЛИАРД;
			return "/XXL/" + prefix + buildUri( suffix );
		}

		StringBuilder buff = doBuildUri( id );
		if (id < 1000) {
			return "/000/000" + buff.toString();
		} else if (id < 1000 * 1000) {
			return "/000" + buff.toString();
		} else {
			return buff.toString();
		}
	}

	private static StringBuilder doBuildUri( long id ) {
		if (id < 1000) {
			String tail = String.valueOf( id );
			StringBuilder buff = new StringBuilder();
			buff.append( '/' );
			for( int radix = tail.length(); radix < 3; ++radix ) {
				buff.append( '0' );
			}
			buff.append( tail );
			return buff;
		} else {
			return doBuildUri( id / 1000 ).append( doBuildUri( id % 1000 ) );
		}
	}
}
