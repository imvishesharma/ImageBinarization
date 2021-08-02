import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class toGrayScale
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

		int histogram[256];
        for(int i=0;i<256;i++)
        {
        	histogram[i]=0;
        }

        int width,height;
        int p,a,r,g,b,avg;

        width = img.getWidth();
        height = img.getHeight();

        for(int x=0; x<width; x++)
        {
            for(int y=0; y<height; y++)
            {
                p = img.getRGB(x,y);

                a = (p>>24)&0xff;
                r = (p>>16)&0xff;
                g = (p>>8)&0xff;
                b = (p)&0xff;

                avg = (r+g+b)/3;

                p = (a<<24) | (avg<<16) | (avg<<8) | avg;

                histogram[p]++;
                
                img.setRGB(x,y,p);
            }
        }
        
        try
        {
            f = new File("toGrayScale_Out.jpg");
            ImageIO.write(img,"jpg",f);
            System.out.println("Writing Complete");
        }
        catch(IOException e)
        {
            System.out.println("Error : "+e);
        }
    }
}