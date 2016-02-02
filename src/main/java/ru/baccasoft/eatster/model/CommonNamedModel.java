package ru.baccasoft.eatster.model;

public class CommonNamedModel extends CommonModel {
    private String name;

    public CommonNamedModel() {
        super();
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
