import java.awt.image.BufferedImage;

public class GetSetPixels{
    public static String toAscii(BufferedImage img, int regionWidth, int regionHeight)
    {
        var out = new StringBuilder();
        
        int width = img.getWidth();
        int height = img.getHeight();
    
        for (int i = 0 ; i < height / regionHeight ; i ++) {
            for (int j = 0 ; j < width / regionWidth ; j ++) {
            
                int color = img.getRGB(j * regionWidth, i * regionHeight); // pixel operations
            
                int red   = (color >>> 16) & 0xFF;
                int green = (color >>>  8) & 0xFF;
                int blue  = (color) & 0xFF;
            
                // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
            
                // choose brightness threshold as appropriate:
//                if (luminance >= 0.5f) {
//                    // bright color
//                    out.append("@ ");
//                } else {
//                    // dark color
//                    out.append("* ");
//                }
            
                if (luminance >= 0.9) {
                    out.append(' ');
                } else if (luminance >= 0.8) {
                    out.append('.');
                } else if (luminance >= 0.7) {
                    out.append('*');
                } else if (luminance >= 0.6) {
                    out.append(':');
                } else if (luminance >= 0.5) {
                    out.append('o');
                } else if (luminance >= 0.4) {
                    out.append('&');
                } else if (luminance >= 0.3) {
                    out.append('8');
                } else if (luminance >= 0.2) {
                    out.append('#');
                } else {
                    out.append('@');
                }

//                out.append(luminance);
//               if (luminance != 0) out.append("* ");
//                else out.append("  ");
            }
            out.append(System.lineSeparator());
            
        }
        return out.toString();
    }
}