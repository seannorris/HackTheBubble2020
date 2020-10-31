import swing.Grid;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GetSetPixels
{
    public static void toAscii(BufferedImage img, int regionWidth, int regionHeight, Grid grid)
    {
        var array = grid.getGrid();
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        for(int i = 0; i < height / regionHeight; i++)
        {
            for(int j = 0; j < width / regionWidth; j++)
            {
                
                int color = img.getRGB(j * regionWidth, i * regionHeight); // pixel operations
                
                int red = (color >>> 16) & 0xFF;
                int green = (color >>> 8) & 0xFF;
                int blue = (color) & 0xFF;
                
                // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
                
                String character;
                if(luminance >= 0.9)
                {
                    character = (" ");
                }
                else if(luminance >= 0.8)
                {
                    character = (".");
                }
                else if(luminance >= 0.7)
                {
                    character = ("*");
                }
                else if(luminance >= 0.6)
                {
                    character = (":");
                }
                else if(luminance >= 0.5)
                {
                    character = ("o");
                }
                else if(luminance >= 0.4)
                {
                    character = ("&");
                }
                else if(luminance >= 0.3)
                {
                    character = ("8");
                }
                else if(luminance >= 0.2)
                {
                    character = ("#");
                }
                else
                {
                    character = ("@");
                }
                
                array[j][i].updateText(character, new Color(color));
            }
        }
    }
}