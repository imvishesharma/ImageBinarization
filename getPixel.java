import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class getPixel
{
    public static void main(String args[])throws IOException
    {
        BufferedImage img = null;
        File f = null;

        try
        {
            f = new File("In.jpg");
            img = ImageIO.read(f);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

        int width = img.getWidth();
        int height = img.getHeight();

        int p = img.getRGB(0,0);

        int a,r,g,b;
        a = (p>>24) & 0xff;
        r = (p>>16) & 0xff;
        g = (p>>8) & 0xff;
        b = (p) & 0xff;

        System.out.println(a + ", " + r + ", " + g + ", " + b);
    }
}