import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import javax.swing.*;

public class local
{
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

	public static BufferedImage toGreyScale(BufferedImage imgi) {
		BufferedImage img = deepCopy(imgi);

		int width = img.getWidth();
		int height = img.getHeight();

		int x,y,p,a,r,g,b,avg;

		for(x=0; x<width; x++)
        {
            for(y=0; y<height; y++)
            {
                p = img.getRGB(x,y);

                a = (p>>24)&0xff;
                r = (p>>16)&0xff;
                g = (p>>8)&0xff;
                b = (p)&0xff;

                avg = (r+g+b)/3;

                p = (a<<24) | (avg<<16) | (avg<<8) | avg;

                img.setRGB(x,y,p);
            }
        }
        return img;
	}
	public static BufferedImage localConv(BufferedImage x) {
		BufferedImage img = x;

		int width = img.getWidth();
		int height = img.getHeight();
		int mean1 = 0,mean2 = 0,mean3=0,mean4=0,p;

		int a = width/2, i;
		int b = height/2, j;

		for( i=0;i<a;i++)
		{
			for ( j=0; j<b; j++) 
			{
				mean1 += img.getRGB(i,j)&0xff;
			}
		}
		mean1 /= (a*b);

		// First Window
		for( i=0; i<a; i++)
        {
            for( j=0; j<b; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<mean1){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (255<<24) | (255<<16) | (255<<8) | 255;
                }

                img.setRGB(i,j,p);
            }
        }

        // Second Window
        for ( i=a; i<width; i++) 
		{
			for( j=0; j<b;j++)
			{
				mean2 += img.getRGB(i,j)&0xff;
			}
		}
		mean2 /= ((width-a)*b);
        
        for( i=a; i<width; i++)
        {
            for( j=0; j<b; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<mean2){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (255<<24) | (255<<16) | (255<<8) | 255;
                }

                img.setRGB(i,j,p);
            }
        }
		
		// Third Window
        for (i=0; i<a; i++) {
        	for (j=b; j<height; j++) {
        		mean3 += img.getRGB(i,j)&0xff;
        	}
        }
        mean3 /= (a*(height-b));

        for( i=0; i<a; i++)
        {
            for( j=b; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<mean3){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (255<<24) | (255<<16) | (255<<8) | 255;
                }

                img.setRGB(i,j,p);
            }
        }

        // Fourth Window
        for (i=a; i<width; i++) {
        	for (j=b; j<height; j++) {
        		mean4 += img.getRGB(i,j)&0xff;
        	}
        }
        mean4 /= (width-a)*(height-b);

        for( i=a; i<width; i++)
        {
            for( j=b; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<mean4){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (255<<24) | (255<<16) | (255<<8) | 255;
                }
                
                img.setRGB(i,j,p);
            }
        }
        System.out.println("mean1 = " + mean1);
        System.out.println("mean2 = " + mean2);
        System.out.println("mean3 = " + mean3);
        System.out.println("mean4 = " + mean4);

       	return img;
	}

	public static void main(String[] args) {
		File f = null, o=null;
		BufferedImage inImg = null, outImg = null;

		try {
			f = new File("In.jpg");
			inImg = ImageIO.read(f);
		} catch (IOException e)
		{
			System.out.println("ERROR : " + e);
		}
		outImg = toGreyScale(inImg);
		outImg = localConv(outImg);

		try {
			o = new File("local.jpg");
			ImageIO.write(outImg,"jpg",o);
		}
		catch (IOException e)
		{
			System.out.println("ERROR : " + e);
		}
		System.out.println(">> Width = " + inImg.getWidth() + ", Height = " + inImg.getHeight());
		System.out.println(">> DONE");
	}
}

