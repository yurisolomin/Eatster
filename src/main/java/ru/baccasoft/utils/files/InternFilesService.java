package ru.baccasoft.utils.files;

import com.vaadin.spring.annotation.SpringComponent;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import ru.baccasoft.utils.files.dto.FileWithContent;

/**
 * Костыль для экзотических случаев, когда требуется получить контент в виде потока на сервере. Вынесен в отдельный
 * сервис, т.к. в большинстве случаев это не требуется (более того, будет скорее всего архитектурной ошибкой).
 */
@SpringComponent
public class InternFilesService {
	
	@Autowired
	private FilesService filesService;
	
	public InputStream getFileStream(FileWithContent fwc) {
		return filesService.getContentStream(fwc);
	}
	
	public FilesService getFilesService() {
		return filesService;
	}
}
