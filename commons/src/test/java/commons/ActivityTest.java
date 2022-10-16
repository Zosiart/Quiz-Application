package commons;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    /**
     * Tests Constructor with all parameters
     */
    @Test
    void constructorTestEmpty() {
        Activity activity = new Activity();
        assertNotNull(activity);
    }

    @Test
    void constructorTest1() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertNotNull(activity);
    }

    /**
     * Tests constructor with no ID
     */
    @Test
    void constructorTest2() {
        Activity activity = new Activity("image_a", "a", 10L, "a");
        assertNotNull(activity);
    }

    /**
     * Tests the toString method
     */
    @Test
    void readabilityTest() {
        Activity activity = new Activity("image_a", "a", 1L, "a");
        assertEquals("Activity(id=null, image_path=image_a, title=a, consumption_in_wh=1, source=a)", activity.toString());
    }

    /**
     * tests if object equals itself
     */
    @Test
    void equalsTest1() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals(activity, activity);
    }

    /**
     * Tests if object equals identical object
     */
    @Test
    void equalsTest2() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        Activity activity2 = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals(activity, activity2);
    }

    /**
     * Tests if object doesnt equal different object
     */
    @Test
    void equalsTest3() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        Activity activity2 = new Activity("1L", "image_a", "b", 1L, "a");
        assertNotEquals(activity, activity2);
    }

    /**
     * Test for getId
     */
    @Test
    void getIdTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("1L", activity.getId());
    }

    /**
     * Test for setId
     */
    @Test
    void setIdTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setId("2L");
        assertEquals("2L", activity.getId());
    }

    /**
     * Test for getImagePath
     */
    @Test
    void getImagePathTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("image_a", activity.getImage_path());
    }

    /**
     * Test for setImagePath
     */
    @Test
    void setImagePathTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setImage_path("image_b");
        assertEquals("image_b", activity.getImage_path());
    }

    /**
     * Test for getTitle
     */
    @Test
    void getTitleTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("a", activity.getTitle());
    }

    /**
     * Test for setTitle
     */
    @Test
    void setTitleTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setTitle("b");
        assertEquals("b", activity.getTitle());
    }

    /**
     * Test for getConsumption_in_wh
     */
    @Test
    void getConsumption_in_whTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals(1, activity.getConsumption_in_wh());
    }

    /**
     * Test for setConsumption_in_wh
     */
    @Test
    void setConsumption_in_whTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setConsumption_in_wh(2L);
        assertEquals(2, activity.getConsumption_in_wh());
    }

    /**
     * Test for getSource
     */
    @Test
    void getSourceTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        assertEquals("a", activity.getSource());
    }

    /**
     * Test for setSource
     */
    @Test
    void setSourceTest() {
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.setSource("c");
        assertEquals("c", activity.getSource());
    }

    /**
     * Tests that image is not set after inputting an invalid image
     */
    @Test
    void initializeImageTestNoImage(){
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        activity.initializeImage(new File("asd"));
        assertNull(activity.getImage());
    }

    /**
     * Tests that there is an image after inputting a valid image
     */
    @Test
    void initializeImageTest(){
        Activity activity = new Activity("1L", "image_a", "a", 1L, "a");
        try {
            // Create new temporary file
            File temp = File.createTempFile("test", ".png");
            // Create a new image with width = 1, height = 1, type = RGB
            BufferedImage bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            // write image to the temp file
            ImageIO.write(bf, "jpg", temp);
            // set temp to delete on exit
            temp.deleteOnExit();

            activity.initializeImage(temp);
            assertNotNull(activity.getImage());
        } catch (IOException e) {
        }
    }
}