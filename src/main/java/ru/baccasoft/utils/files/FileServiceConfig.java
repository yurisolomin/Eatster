package ru.baccasoft.utils.files;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@XmlRootElement(name = "fileServiceConfig")
public class FileServiceConfig {

	public String contentStoragePath;
	
	public String downloadUrlFormat;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString( this );
	}
}
