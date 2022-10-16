package commons;

import lombok.Data;
import lombok.ToString;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Activity class
 * Activities are stored in the database
 */
@Data
@Entity
public class Activity {

    @Id
    private String id;

    private String image_path;
    private String title;
    private Long consumption_in_wh;
    private String source;
    @ToString.Exclude // Image is not included in the toString method (would make it too long)
    @Transient // Image is not stored in the database
    private byte[] image; // image is a byte array because it's a pretty efficient way to send it (I hope)

    /**
     * Empty constructor
     * Used by Jackson to create object from JSON
     */
    public Activity() {
    }

    /**
     * Constructor with all parameters except ID
     *
     * @param image_path        path of the image
     * @param title             Activity text used in questions
     * @param consumption_in_wh Consumption in Wh
     * @param source            Source of the activity consumption
     */
    public Activity(String image_path, String title, Long consumption_in_wh, String source) {
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    /**
     * Constructor with all parameters
     *
     * @param id                ID of the activity in the database
     * @param image_path        path of the image
     * @param title             Activity text used in questions
     * @param consumption_in_wh Consumption in Wh
     * @param source            Source of the activity consumption
     */
    public Activity(String id, String image_path, String title, Long consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    /**
     * Initializes the image parameter
     * Gets file as input, converts that into a byte array which is sent over http
     * Should be run manually in endpoints which should return activities with their images
     *
     * @param imageFile image file to use.
     */
    public void initializeImage(File imageFile) {
        try {
            // Reads the image from the file to a BufferedImage
            BufferedImage img = ImageIO.read(imageFile);
            // Creates a new ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Writes the image to the output stream. Sending the image gives an error (because it's inefficient and shouldn't be done)
            ImageIO.write(img, "png", outputStream); // jpg would be faster but png supports transparency
            this.image = outputStream.toByteArray();
        } catch (IOException | IllegalArgumentException e) { // Catches an error if an image couldn't be found
            System.err.println("Couldn't find picture at " + imageFile.toString());
            this.image = null;
        }
    }


}
