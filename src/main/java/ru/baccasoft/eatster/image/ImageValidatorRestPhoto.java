package ru.baccasoft.eatster.image;

import ru.baccasoft.eatster.appconfig.AppProp;

public class ImageValidatorRestPhoto extends ImageValidator {

    public ImageValidatorRestPhoto(AppProp appProp) {
        super(appProp, "restaurant.photo");
    }

}
