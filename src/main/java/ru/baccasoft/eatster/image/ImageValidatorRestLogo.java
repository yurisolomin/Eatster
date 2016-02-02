package ru.baccasoft.eatster.image;

import java.awt.image.BufferedImage;
import ru.baccasoft.eatster.appconfig.AppProp;

public class ImageValidatorRestLogo extends ImageValidator {

    public ImageValidatorRestLogo(AppProp appProp) {
        super(appProp, "restaurant.logo");
    }
    //для логотипа ничего делать не будем, т.к. изображение растянется по факту
    @Override
    public BufferedImage resizeImage(BufferedImage sourceImage) {
        LOG.debug("resizeImage: for Logo skip resize");
        LOG.debug("Ok.");
        return sourceImage;
    }
    
/*    
    //для логотипа растянем изображение
    @Override
    public BufferedImage resizeImage(BufferedImage sourceImage) {
        LOG.debug("resizeImage:");
        int srcWidthInt = sourceImage.getWidth();
        int srcHeightInt = sourceImage.getHeight();
        double srcWidth = sourceImage.getWidth();
        double srcHeight = sourceImage.getHeight();
        double srcAspectRatio = srcWidth / srcHeight;
        LOG.debug("srcWidth={0},srcHeight={1},srcAspectRatio={2}", srcWidth, srcHeight, srcAspectRatio);
        double maxWidth = getWidthMax();
        double maxHeight = getHeightMax();
        double maxAspectRatio = maxWidth / maxHeight;
        LOG.debug("maxWidth={0},maxHeight={1},maxAspectRatio={2}", maxWidth, maxHeight, maxAspectRatio);
        double newWidth = srcWidth;
        double newHeight = srcHeight;
        //если соотношение ширина/высота источника < ширина/высота приемника
        //то будем увеличивать ширину
        if (srcAspectRatio < maxAspectRatio) {
            newWidth = srcHeight * maxAspectRatio;
        } else { //иначе будем увеличивать высоту
            newHeight = srcWidth / maxAspectRatio;
        }        
        double newAspectRatio = newWidth / newHeight;
        LOG.debug("newWidth={0},newHeight={1},newAspectRatio={2}", newWidth, newHeight, newAspectRatio);
        int newWidthInt = (int)newWidth;
        int newHeightInt = (int)newHeight;
        if (newWidthInt == srcWidthInt && newHeightInt == srcHeightInt) {
            return sourceImage;
        }
        BufferedImage result = Scalr.resize(sourceImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, newWidthInt, newHeightInt);
        LOG.debug("resizeImage: Ok. newWidth={0}, newHeight={1}",newWidthInt,newHeightInt);
        return result;
    }
*/    
}
