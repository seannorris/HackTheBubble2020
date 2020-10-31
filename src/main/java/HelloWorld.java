public class HelloWorld
{
    public static void main(String[] args)
    {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("pic.jpg"));
        } catch (IOException e) {
        }
    }
}
