package ru.baccasoft.eatster.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import ru.baccasoft.utils.logging.Logger;

public class ImageBuffer {

    private static final Logger LOG = Logger.getLogger(ImageValidator.class);
    private byte[] inputArray;
    private String fileName;

    public ImageBuffer(String fileName, InputStream fileInputStream) {
        this.fileName = fileName;
        this.inputArray = null;
        try {
            inputArray = IOUtils.toByteArray(fileInputStream);
        } catch (IOException ex) {
            LOG.warn("ImageBuffer: Error on toByteArray(..)! FileName={0},Error={1}", fileName, ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public InputStream getInputStream() {
        if (inputArray == null) {
            return null;
        }
        return new ByteArrayInputStream(inputArray);
    }

    public String getFileName() {
        return fileName;
    }

    private String changeExtension(String originalName, String newExtension) {
        int lastDot = originalName.lastIndexOf(".");
        if (lastDot != -1) {
            return originalName.substring(0, lastDot) + newExtension;
        } else {
            return originalName + newExtension;
        }
    }//end changeExtension

    public void resizeImage(ImageValidator imageValidator) throws IOException {
        LOG.debug("resizeImage: fileName={0}", fileName);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputArray);
        BufferedImage sourceImage = ImageIO.read(inputStream);
        BufferedImage resultImage = imageValidator.resizeImage(sourceImage);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resultImage, "JPG", outputStream);
        inputArray = outputStream.toByteArray();
        fileName = changeExtension(fileName, ".JPG");
        LOG.debug("resizeImage: Ok.");
    }
}
