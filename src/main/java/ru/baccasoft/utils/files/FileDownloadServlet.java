package ru.baccasoft.utils.files;

import com.vaadin.spring.annotation.SpringComponent;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import ru.baccasoft.utils.files.dto.FileWithContent;
import ru.baccasoft.utils.logging.Logger;

/**
 * Чтобы спринг заработал, следует объявить в <code>web.xml</code> сервлет с именем компонента
 * и классом {@link  org.springframework.web.context.support.HttpRequestHandlerServlet}:
 * <p/>
 * 
 * <pre>
 * &lt;servlet>
 *     &lt;servlet-name>ru.baccasoft.utils.files.FileDownloadServlet&lt;/servlet-name>
 *     &lt;servlet-class>org.springframework.web.context.support.HttpRequestHandlerServlet&lt;/servlet-class>
 * &lt;/servlet>
 * 
 * &lt;servlet-mapping>
 *     &lt;servlet-name>ru.baccasoft.utils.files.FileDownloadServlet&lt;/servlet-name>
 *     &lt;url-pattern>/servlets/file&lt;/url-pattern>
 * &lt;/servlet-mapping>
 * </pre>
 */
/*
	<servlet>
	    <servlet-name>ru.baccasoft.utils.files.FileDownloadServlet</servlet-name>
	    <servlet-class>org.springframework.web.context.support.HttpRequestHandlerServlet</servlet-class>
	</servlet>
	<servlet-mapping>
	    <servlet-name>ru.baccasoft.utils.files.FileDownloadServlet</servlet-name>
		<url-pattern>/servlets/file</url-pattern>
	</servlet-mapping>
 */
//@Service("ru.baccasoft.utils.files.FileDownloadServlet")
@SpringComponent("ru.baccasoft.utils.files.FileDownloadServlet")
public class FileDownloadServlet implements HttpRequestHandler {
	
	private static final Logger log = Logger.getLogger( FileDownloadServlet.class );

	@Override
	public void handleRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
			IOException {
		long infoId;
		{
			String infoIdString = request.getParameter( "i" );
			if( StringUtils.isEmpty( infoIdString )) {
				sendError( response, SC_BAD_REQUEST, "Empty i", null );
				return;
			}
			try {
				infoId = Long.valueOf( infoIdString );
			} catch( NumberFormatException e ) {
				sendError( response, SC_BAD_REQUEST, "Error parsing "+infoIdString+"\n", e );
				return;
			}
		}
		
		long contentId;
		{
			String contentIdString = request.getParameter("c");
			if( StringUtils.isEmpty( contentIdString )) {
				sendError( response, SC_BAD_REQUEST, "Empty c", null );
				return;
			}
			try {
				contentId = Long.valueOf( contentIdString );
			} catch( NumberFormatException e ) {
				sendError( response, SC_BAD_REQUEST, "Error parsing "+contentIdString+"\n", e );
				return;
			}
		}
		
		FileWithContent file = service.getFile( infoId );
		if( file == null ) {
			sendError( response, SC_NOT_FOUND, "Not found "+infoId, null);
			return;
		}
		if( file.contentId != contentId ) {
			sendError( response, SC_BAD_REQUEST, "Invalid id "+infoId+":"+contentId, null);
			return;
		}
		
		int contentLength = service.getContentSize( file );
		String filename = file.filename;
		String contentType = file.contentType;
		InputStream in = service.getContentStream( file );
		try {
			response.setStatus( SC_OK );
			response.setContentLength( contentLength );
			response.setContentType(
					StringUtils.isEmpty( contentType )
					? "application/octet-stream"
					: contentType );
			if( !StringUtils.isEmpty( filename ) ) {
				final String userAgent = request.getHeader("User-Agent");
				response.setHeader("Content-Disposition", contentDisposition(filename, userAgent));
			}
			
			IOUtils.copy( in, response.getOutputStream() );
		} finally {
			IOUtils.closeQuietly( in );
		}
		
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
	
	@Autowired
	private FilesService service;

	private String contentDisposition( String name, String userAgent ) throws UnsupportedEncodingException {
		if( userAgent == null ) {
			userAgent = "";
		}

		String encoded = (userAgent.indexOf("MSIE") < 0 ) 
				? encode( name ) 
				: hackFilenameWindozeStyle( name ); 
		
		encoded = encoded.replace('+', '_');
		return MessageFormat.format("attachment; filename=\"{0}\"", encoded);
	}
	
	private static String encode( String name ) throws UnsupportedEncodingException {
		return URLEncoder.encode(name, "UTF-8");
	}
	
    private static final Map<Character, String> translator = new HashMap<Character, String>() {
        {
            put('А', "A");
            put('Б', "B");
            put('В', "V");
            put('Г', "G");
            put('Д', "D");
            put('У', "E");
            put('Ё', "E");
            put('Ж', "J");
            put('З', "Z");
            put('И', "I");
            put('Й', "I");
            put('К', "K");
            put('Л', "L");
            put('М', "M");
            put('Н', "N");
            put('О', "O");
            put('П', "P");
            put('Р', "R");
            put('С', "S");
            put('Т', "T");
            put('У', "U");
            put('Ф', "F");
            put('Х', "Kh");
            put('Ц', "Ts");
            put('Ч', "Ch");
            put('Ш', "Sh");
            put('Щ', "Sch");
            put('Ъ', "'");
            put('Ы', "Y");
            put('Ь', "'");
            put('Э', "E");
            put('Ю', "Yu");
            put('Я', "Ya");
            put('а', "a");
            put('б', "b");
            put('в', "v");
            put('г', "g");
            put('д', "d");
            put('е', "e");
            put('ё', "e");
            put('ж', "j");
            put('з', "z");
            put('и', "i");
            put('й', "i");
            put('к', "k");
            put('л', "l");
            put('м', "m");
            put('н', "n");
            put('о', "o");
            put('п', "p");
            put('р', "r");
            put('с', "s");
            put('т', "t");
            put('у', "u");
            put('ф', "f");
            put('х', "kh");
            put('ц', "ts");
            put('ч', "ch");
            put('ш', "sh");
            put('щ', "sch");
            put('ъ', "'");
            put('ы', "y");
            put('ь', "'");
            put('э', "e");
            put('ю', "yu");
            put('я', "ya");
        }
    };

    private static String transliterate(String original) {
        StringBuffer result = new StringBuffer();
        for (int pos = 0; pos < original.length(); ++pos) {
            char ch = original.charAt(pos);
            String replacement = translator.get(ch);
            if (replacement == null) {
                result.append(ch);
            } else {
                result.append(replacement);
            }
        }
        return result.toString();
    }

    private static String[] splitNameAndExtension(String original) {
        int lastCharIndex = original.lastIndexOf('.');
        if (lastCharIndex <= 0) {
            return new String[]{original, ""};
        } else {
            return new String[]{original.substring(0, lastCharIndex), original.substring(lastCharIndex)};
        }
    }
    private static final int MAX_WINDOZE_FILENAME_LENGTH = 64;

    private static String hackFilenameWindozeStyle(String original) throws UnsupportedEncodingException {
        String optimisticResult = encode(original);

        if (optimisticResult.length() <= MAX_WINDOZE_FILENAME_LENGTH) {
            return optimisticResult;
        }

        String name = transliterate(original);
        optimisticResult = encode(name);

        if (optimisticResult.length() <= MAX_WINDOZE_FILENAME_LENGTH) {
            return optimisticResult;
        }


        String[] nameAndExtension = splitNameAndExtension(name);
        int pos = nameAndExtension[0].length() - 1;

        for (;;) {
            String cutDownName = nameAndExtension[0].substring(0, pos) + "[...]" + nameAndExtension[1];
            String result = encode(cutDownName);
            if (result.length() <= MAX_WINDOZE_FILENAME_LENGTH) {
                log.info("Cut down name is '{}'", cutDownName);
                return result;
            }
            pos -= 1;
        }
    }
}
