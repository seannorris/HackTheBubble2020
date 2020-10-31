import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GetSetPixels{
    public static void drawFrame(BufferedImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
    
    
        for (int i = 0 ; i < height / 10 ; i ++) {
            for (int j = 0 ; j < width / 10 ; j ++) {
            
                int color = img.getRGB(j * 10, i * 10); // pixel operations
            
                int red   = (color >>> 16) & 0xFF;
                int green = (color >>>  8) & 0xFF;
                int blue  = (color) & 0xFF;
            
                // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
            
                // choose brightness threshold as appropriate:
//                if (luminance >= 0.5f) {
//                    // bright color
//                    System.out.print("@ ");
//                } else {
//                    // dark color
//                    System.out.print("* ");
//                }
            
                if (luminance >= 0.9) {
                    System.out.print(' ');
                } else if (luminance >= 0.8) {
                    System.out.print('.');
                } else if (luminance >= 0.7) {
                    System.out.print('*');
                } else if (luminance >= 0.6) {
                    System.out.print(':');
                } else if (luminance >= 0.5) {
                    System.out.print('o');
                } else if (luminance >= 0.4) {
                    System.out.print('&');
                } else if (luminance >= 0.3) {
                    System.out.print('8');
                } else if (luminance >= 0.2) {
                    System.out.print('#');
                } else {
                    System.out.print('@');
                }

//                System.out.print(luminance);
//               if (luminance != 0) System.out.print("* ");
//                else System.out.print("  ");
            }
            System.out.println();
        }
    }
}