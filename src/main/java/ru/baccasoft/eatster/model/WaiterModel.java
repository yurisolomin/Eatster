package ru.baccasoft.eatster.model;

public class WaiterModel extends CommonNamedModel {
    private long restaurantId;
    private String login;
    private String password;

    public WaiterModel() {
        super();
        restaurantId = 0;
        login = "";
        password = "";
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
