package ru.baccasoft.eatster.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import ru.baccasoft.eatster.appconfig.AppProp;
import ru.baccasoft.utils.logging.Logger;

public class ImageValidator {

    public static final Logger LOG = Logger.getLogger(ImageValidator.class);
    private final AppProp appProp;
    private final String propNamePrefix;

    public ImageValidator(AppProp appProp, String propNamePrefix) {
        this.appProp = appProp;
        this.propNamePrefix = propNamePrefix;
    }

    public class ImageValidatorException extends Exception {

        private static final long serialVersionUID = 1L;

        public ImageValidatorException(String message) {
            super(message);
        }

    }

    public int getWidthMax() {
        return appProp.getPropertyIntDef(propNamePrefix + ".width.max", 0);
    }

    public int getHeightMax() {
        return appProp.getPropertyIntDef(propNamePrefix + ".height.max", 0);
    }

    public int getWidthMin() {
        return appProp.getPropertyIntDef(propNamePrefix + ".width.min", 0);
    }

    public int getHeightMin() {
        return appProp.getPropertyIntDef(propNamePrefix + ".height.min", 0);
    }

    //соотношение сторон
    public float getWidthHeightRatio() {
        if (getHeightMax() == 0) {
            return 1f;
        }
        return (float) getWidthMax() / getHeightMax();
    }

    public String getExtPattern() {
        return appProp.getPropertyDef(propNamePrefix + ".ext.pattern", "");
    }

    public void validateFileName(String fileName) throws ImageValidatorException {
//	Pattern VALID_IMAGE_EXT_PATTERN = Pattern.compile(".*.(png|jpg|jpeg)");
        LOG.debug("validateFileName: fileName={0}", fileName);
        String extPattern = getExtPattern().trim();
        LOG.debug("extPattern={0}", extPattern);
        if (extPattern.isEmpty()) {
            LOG.debug("validateImageSize: Ok. Pattern is empty");
        }
        Pattern pattern = Pattern.compile(".*.(" + extPattern + ")");
        LOG.debug("pattern={0}", pattern);
        boolean isValid = pattern.matcher(fileName.toLowerCase()).matches();
        LOG.debug("isValid={0}", isValid);
        if (!isValid) {
            throw new ImageValidatorException("Недопустимое расширение файла. Ожидается " + extPattern);
        }
        LOG.debug("validateFileName: Ok.");
    }

    public void validateImageSize(InputStream fileInputStream) throws ImageValidatorException {
        LOG.debug("validateImageSize:");
        try {
            BufferedImage image = ImageIO.read(fileInputStream);
            int width = image.getWidth();
            int height = image.getHeight();
            LOG.debug("width={0},height={1}", width, height);
            int widthMax = getWidthMax();
            int heightMax = getHeightMax();
            LOG.debug("widthMax={0},heightMax={1}", widthMax, heightMax);
            int widthMin = getWidthMin();
            int heightMin = getHeightMin();
            LOG.debug("widthMax={0},heightMax={1}", widthMax, heightMax);
            String sizeInfo = "Размер загружаемого изображения " + width + "x" + height + " (ШxВ). ";
            String sizeInfoMax = "Максимальный размер " + widthMax + "x" + heightMax + " (ШxВ). ";
            String sizeInfoMin = "Минимальный размер " + widthMin + "x" + heightMin + " (ШxВ). ";
            String messagePref = "Недопустимый размер изображения. ";
            if (width > widthMax || height > heightMax) {
                LOG.warn("validateImageSize: Fail. Size photo exceeds the maximum");
                throw new ImageValidatorException(messagePref + sizeInfo + sizeInfoMax);
            }
            if (width < widthMin || height < heightMin) {
                LOG.warn("validateImageSize: Fail. Size photo less the minimum");
                throw new ImageValidatorException(messagePref + sizeInfo + sizeInfoMin);
            }
        } catch (IOException ex) {
            LOG.error("Error on validate image: {0}", ex.getMessage());
            throw new ImageValidatorException("Неизвестная ошибка ввода/вывода. Попробуйте позднее.");
        }
        LOG.debug("validateImageSize: Ok.");
    }

    //пробразовать картинку по соотношению сторон
    public BufferedImage resizeImage(BufferedImage sourceImage) {
        LOG.debug("resizeImage:");
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
        //то будем резать высоту
        if (srcAspectRatio < maxAspectRatio) {
            newHeight = srcWidth / maxAspectRatio;
        } else { //иначе будем резать ширину
            newWidth = srcHeight * maxAspectRatio;
        }
        double newAspectRatio = newWidth / newHeight;
        LOG.debug("newWidth={0},newHeight={1},newAspectRatio={2}", newWidth, newHeight, newAspectRatio);
        //отрезаемый размер с каждого боку
        int cutWidth = (int) (srcWidth - newWidth) / 2;
        int cutHeight = (int) (srcHeight - newHeight) / 2;
        LOG.debug("cutWidth={0},cutHeight={1}", cutWidth, cutHeight);
        BufferedImage outImage = sourceImage.getSubimage(cutWidth, cutHeight, (int) srcWidth - 2 * cutWidth, (int) srcHeight - 2 * cutHeight);
        LOG.debug("Ok.");
        return outImage;
    }
    
}
