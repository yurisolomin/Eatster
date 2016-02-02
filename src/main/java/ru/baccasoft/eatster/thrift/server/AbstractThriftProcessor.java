
package ru.baccasoft.eatster.thrift.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.slf4j.Logger;

//import pro.bacca.uralairlines.utils.Utils;

/* package */ abstract class AbstractThriftProcessor<TheException extends Exception, TheBase> {
	/**
	 * Обертка для операции сервиса.
	 */
	public interface IOperationProcessor<TheException extends Exception, TheBase, Input, Output> {
		/** Собственно, операция */
		Output process( Input input ) throws TheException, Throwable;
		
		/** Костыль для извлечения t.r.b. */
		TheBase extractBase( Input input );

		/** Имя операции (для логгирования) */
		String getOperationName();
		
		String getLoggingPrefix( TheBase base );
		
		String getProtocolVersion( TheBase base );
		
		TheException createVersionMismatch(String message );
		TheException createUndefinedException(String message );
		
		boolean isThriftException( Throwable t );
	}
	
	protected abstract Logger LOG();
	
	/**
	 * Текущая серверная версия thrift-протокола
	 */
	protected abstract String SERVICE_VERSION();

	protected final <Input, Output> 
			Output process( 
					Input input, 
					IOperationProcessor<TheException, TheBase, Input, Output> processor 
				) throws TheException {
		String loggingPrefix = "Processing "+processor.getOperationName() + ": ";
		boolean isOk = false;
		try {
			final TheBase base = processor.extractBase(input);
			loggingPrefix = processor.getLoggingPrefix(base);
			
			// в лог выдается также идентификатор потока, по сочетанию поток+уид мы сможем идентифицировать
			// запросы к боевому стенду при наличии проблем и параллельной работе нескольких клиентов
			if( LOG().isDebugEnabled() ) {
				LOG().debug( loggingPrefix + "request is: "+ input);
			} else {
				LOG().info(loggingPrefix + "...");
			}

			if( !SERVICE_VERSION().equals(processor.getProtocolVersion(base)) ) {
				String message = MessageFormat.format( "{0}Service version mismatch: expected ''{1}'' but received ''{2}''.", 
						loggingPrefix, SERVICE_VERSION(), processor.getProtocolVersion(base) );
				LOG().error(message);
				throw processor.createVersionMismatch(message);
			}

			final Output outputEvent = processor.process(input);
			if( LOG().isDebugEnabled()) {
				LOG().debug( loggingPrefix + "Returning "+ outputEvent);
			}

			isOk  = true;
			return outputEvent;

		} catch( Throwable e ) {
			if( processor.isThriftException(e) ) {
				// it must have been already logged
				throw castToTheException(e);
			}
//			Utils.logSqlException( LOG(), loggingPrefix, e );
			logSqlException( LOG(), loggingPrefix, e );
			LOG().error( loggingPrefix+ "An error occured ", e );
			throw processor.createUndefinedException(printStackTrace(e));
		} finally {
			LOG().info( loggingPrefix+ "Finished with "+ (isOk ? "SUCCESS" : "ERROR") );
		}
	}
	
	/** Костыль, чтобы не запрещать полезный ворнинг у {@link #process(Object, IOperationProcessor)}. */
	@SuppressWarnings("unchecked")
	private TheException castToTheException( Throwable t ) {
		return (TheException) t;
	}
	
	public static String printStackTrace( Throwable t ) {
		StringWriter result = new StringWriter();
		t.printStackTrace(new PrintWriter(result));
		return result.toString();
		
	}

    /**
     * Spring заворачивает SQLException-ы так глубоко, что без поллитры до исходной ошибки не докопаешься.
     * Костыль с той самой поллитрой, чтобы исходная ошибка долетала ло лога.
     * <p/> 
     * TODO копипаст из коммонзовского AbstractThriftProcessor-а, надо объединить, но ломает ради этого 
     *         фиксировать версию мавен-артефакта  
     */
    public static void logSqlException( Logger theLogger, String logPrefix, Throwable t) {
        if( t.getCause() != null  && (t.getCause() instanceof SQLException) ) {
            SQLException e = (SQLException) t.getCause();
            while( e.getNextException() != null ) {
                e = e.getNextException();
            }
            theLogger.error( logPrefix + "INFO: nested SQLException", e );
        }
    }
    
}

