import swing.Char;
import swing.Grid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class GetSetPixels
{
    public static String toAscii(BufferedImage img, int regionWidth, int regionHeight)
    {
        var out = new StringBuilder();
        out.append("\u001b[H");
        
        int width = img.getWidth() / regionWidth;
        int height = img.getHeight() / regionHeight;
        
        var downScaledImage = resize(img, width, height * 2);
        
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                
                int color1 = downScaledImage.getRGB(j, i * 2); // pixel operations
                
                int red1 = (color1 >>> 16) & 0xFF;
                int green1 = (color1 >>> 8) & 0xFF;
                int blue1 = (color1) & 0xFF;
    
                int color2 = downScaledImage.getRGB(j, i * 2 + 1); // pixel operations
    
                int red2 = (color2 >>> 16) & 0xFF;
                int green2 = (color2 >>> 8) & 0xFF;
                int blue2 = (color2) & 0xFF;
                
                String colourCode = String.format("\u001b[48;2;%d;%d;%dm\u001b[38;2;%d;%d;%dm", red1, green1, blue1, red2, green2, blue2);
                
                /*
                // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
                
                char character;
                if(luminance >= 0.9)
                {
                    character = (' ');
                }
                else if(luminance >= 0.8)
                {
                    character = ('.');
                }
                else if(luminance >= 0.7)
                {
                    character = ('*');
                }
                else if(luminance >= 0.6)
                {
                    character = (':');
                }
                else if(luminance >= 0.5)
                {
                    character = ('o');
                }
                else if(luminance >= 0.4)
                {
                    character = ('&');
                }
                else if(luminance >= 0.3)
                {
                    character = ('8');
                }
                else if(luminance >= 0.2)
                {
                    character = ('#');
                }
                else
                {
                    character = ('@');
                }
                
                 */
                
                out.append(colourCode);
                out.append('▄');
            }
            out.append(System.lineSeparator());
        }
        out.append("\u001b[0m");
        return out.toString();
    }
    
    private static BufferedImage resize(BufferedImage original, int newWidth, int newHeight)
    {
        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(),
                original.getHeight(), null);
        g.dispose();
        return resized;
    }
}