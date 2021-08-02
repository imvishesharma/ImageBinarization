import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class toMirror
{
    public static void main(String args[])throws IOException
    {
        BufferedImage img = null;
        File f = null;

        try
        {
            f = new File("In.jpg");
            img = ImageIO.read(f);
            System.out.println("Reading Complete");
        }
        catch(IOException e)
        {
            System.out.println("Error : " + e);
        }

        int width,height;
        int a,b;

        width = img.getWidth();
        height = img.getHeight();
        System.out.println(width + ", " + height);

        for(int x=0; x<height; x++)
        {
            for(int y=0; y<width/2; y++)
            {
                a = img.getRGB(width-y-1,x);
                b = img.getRGB(y,x);

                img.setRGB(y,x,a);
                img.setRGB(width-y-1,x,b);
            }
        }

        try
        {
            f = new File("toMirror_Out.jpg");
            ImageIO.write(img,"jpg",f);
            System.out.println("Writing Complete");
        }
        catch(IOException e)
        {
            System.out.println("Error : "+e);
        }
    }
}