package ru.baccasoft.eatster.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CommonIdModel {

    private long id;

    public CommonIdModel() {
        id = 0;
    }

    public CommonIdModel(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        ReflectionToStringBuilder rsb = new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        //rsb.setAppendStatics(false);
        //rsb.setAppendTransients(true);
        //rsb.setUpToClass(this.getClass());
        //rsb.setExcludeFieldNames(null);
        return rsb.toString();
    }
}
