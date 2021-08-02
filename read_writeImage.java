import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class read_writeImage
{
    public static void main(String args[])throws IOException
    {
        int width = 500;
        int height = 355;

        BufferedImage image = null;

        try
        {
            File input_file = new File("In.jpg");

            image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

            image = ImageIO.read(input_file);

            System.out.println("Reading Complete");
        } 
        catch(IOException e)
        {
            System.out.println("ERROR : "+e);
        }
        
        try
        {
            File output_file = new File("read_writeImage_Out.jpg");

            ImageIO.write(image,"jpg",output_file);

            System.out.println("Writing Complete");
        } 
        catch(IOException e)
        {
            System.out.println("ERROR : "+e);
        }
    }
}