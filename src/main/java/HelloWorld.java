import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HelloWorld
{
    public static void main(String[] args)
    {
        try{
            BufferedImage img = null;
            // TODO: change relative path
            img = ImageIO.read(new File("C:\\Users\\user\\Documents\\all\\programming\\bubble\\HackTheBubble2020\\src\\main\\java\\pic.jpg"));
            int width = img.getWidth();
            int height = img.getHeight();
            int p = img.getRGB(0,0);
        } catch (Exception e) {
            System.out.print(e);
        }

        
        
    }
}
