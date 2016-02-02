package ru.baccasoft.eatster.thrift.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServlet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.HttpRequestHandler;

public abstract class AbstractThriftServlet implements HttpRequestHandler, InitializingBean {
	
	protected abstract TProcessor createProcessor();
	
	private TServlet servlet;

	@Override
	public void handleRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		servlet.service( request, response );
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		servlet = 
				new TServlet( 
					createProcessor(), 
					new TBinaryProtocol.Factory( true, true ), 
					new TBinaryProtocol.Factory( true, true )
				);
	}
}