package ru.baccasoft.eatster.ui.component;

// Implement both receiver that saves upload in a file and
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

class ImageUploader extends Embedded {
/*
    private static final long serialVersionUID = 1L;

    ImageUploader(String caption) {
        super(caption);

        // Show uploaded file in this placeholder
        setVisible(false);

// listener for successful upload
        class ImageUploader1 implements Receiver, SucceededListener {

            private static final long serialVersionUID = 1L;

            public File file;

            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    file = new File("/tmp/uploads/" + filename);
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                // Show the uploaded file in the image viewer
                this.setVisible(true);
                this.setSource(new FileResource(file));
            }
        };
    }
*/
}
