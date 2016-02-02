package ru.baccasoft.eatster.image;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import ru.baccasoft.utils.files.dto.FileWithContent;

public class ImageResource {
    FileWithContent fwc;
    String urlParams;
    public ImageResource(FileWithContent fwc, String urlParams) {
        this.fwc = fwc;
        this.urlParams = urlParams;
    }
    public String getUrlParams() {
        return urlParams;
    }
    public long getFileinfoId() {
        return fwc.id;
    }
    @Override
    public String toString() {
        ReflectionToStringBuilder rsb = new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        return rsb.toString();
    }
}
