package ru.baccasoft.utils.files;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import ru.baccasoft.utils.files.dto.FileWithContent;
import ru.baccasoft.utils.logging.Logger;

/**
 * Чтобы спринг заработал, следует объявить в <code>web.xml</code> сервлет с именем компонента
 * и классом {@link  org.springframework.web.context.support.HttpRequestHandlerServlet}.
 */

/*
to web.xml

<servlet>
	<servlet-name>*полное имя класса-наследника*</servlet-name>
	<servlet-class>org.springframework.web.context.support.HttpRequestHandlerServlet</servlet-class>		
</servlet>

*/
public abstract class AbstractFileUploadServlet implements HttpRequestHandler {

	private static Logger log = Logger.getLogger(AbstractFileUploadServlet.class);

	@Autowired
	protected FilesService service;
	
	@Override
	public void handleRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
			IOException {
		try{
			log.info("Upload request received from " + request.getRemoteAddr() + " to " + request.getRequestURL());
			
			try {
				analyzeRequest(request);
			} catch (Throwable t) {
				sendError( response, SC_BAD_REQUEST, "Error while parameter handling", t);
				return;
			}
			
			List<FileItem> items = castList(FileItem.class, new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request));
			
			if( items.size() == 0 ) {
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				response.flushBuffer();
				return;
			}
			
			for (FileItem item : items) {
				
		        String fileName = FilenameUtils.getName(item.getName());
		        String fileContentType = item.getContentType();

		        FileWithContent fwc = service.createFile( item.getInputStream(), fileName, fileContentType, null );
		        
		        processSavedFile(fwc);
		        
		        log.info("Uploaded file saved to:" + fwc.content.storageUri);
		    }
			
			response.setStatus( HttpServletResponse.SC_OK );
			response.flushBuffer();
		} catch (Throwable th) {
			log.error(th, "Problem while uploading file: " + th.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, th.getMessage());
		}
		
	}
	
	/**
	 * функция для хэндлинга параметров в классе-наследнике
	 * @param request реквест, полученный сервлетом
	 * @throws Throwable в случае, если параметры не верны, сервлет посылает ответ с ошибкой
	 */
	protected abstract void analyzeRequest(HttpServletRequest request) throws Throwable;
	
	/**
	 * функция, вызываемая для каждого полученного файла
	 * @param fwc занесенный в базу класс 
	 * @param downloadUrl урл для скачивания
	 */
	protected abstract void processSavedFile(FileWithContent fwc ) throws Throwable;
	
	private static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
	    List<T> r = new ArrayList<T>(c.size());
	    for(Object o: c)
	      r.add(clazz.cast(o));
	    return r;
	}
	
	private void sendError( HttpServletResponse response, int code, String message, Throwable e ) {
		try {
			response.setStatus( code );
			response.setContentType( "text/plain;charset=UTF-8");
			if( message != null ) {
				response.getWriter().print(message);
			}
			if( e != null ) {
				e.printStackTrace( response.getWriter());
			}
			response.flushBuffer();
		} catch( IOException e1 ) {
			log.warn("IO error occured while sending error response", e1);
			log.warn( e, "The original error may have been lost: ''{0}''", message );
		}
	}

}
