import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Scanner;

public class imgConversion {
	public static BufferedImage toGreyScale(BufferedImage img)
	{
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

	public static BufferedImage toFHGreyScale(BufferedImage img)
	{
		int width = img.getWidth()/2 + 1;
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
    
    public static int[] histogram(BufferedImage img)
    {
        int[] x = new int[256];
        int i,j;

        for(i=0;i<256;i++)
        {
            x[i]=0;
        }

        int width = img.getWidth();
        int height = img.getHeight();
        int p;

        for(i=0; i<width; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                x[p]++;
            }
        }

        return x;
    }

    public static int[] histogramFH(BufferedImage img)
    {
        int[] x = new int[256];
        int i,j;

        for(i=0;i<256;i++)
        {
            x[i]=0;
        }

        int width = img.getWidth()/2 + 1;
        int height = img.getHeight();
        int p;

        for(i=0; i<width; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                x[p]++;
            }
        }

        return x;
    }

    public static BufferedImage toBinThres(BufferedImage img, int thres)
    {
        int i,j,p;
        int width = img.getWidth();
        int height = img.getHeight();

        for(i=0; i<width; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<thres){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (255<<24) | (255<<16) | (255<<8) | 255;
                }

                img.setRGB(i,j,p);
            }
        }

        return img;
    }
    public static BufferedImage toBinThresBlue(BufferedImage img, int thres)
    {
        int i,j,p;
        int width = img.getWidth();
        int height = img.getHeight();

        for(i=0; i<width; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<thres){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (0<<24) | (0<<16) | (255<<8) | 255;
                }

                img.setRGB(i,j,p);
            }
        }

        return img;
    }
    public static BufferedImage toBinThresRed(BufferedImage img, int thres)
    {
        int i,j,p;
        int width = img.getWidth();
        int height = img.getHeight();

        for(i=0; i<width; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<thres){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (0<<24) | (255<<16) | (0<<8) | 56;
                }

                img.setRGB(i,j,p);
            }
        }

        return img;
    }
    public static BufferedImage toBinThresGreen(BufferedImage img, int thres)
    {
        int i,j,p;
        int width = img.getWidth();
        int height = img.getHeight();

        for(i=0; i<width; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<thres){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (0<<24) | (255<<16) | (0<<8) | 0;
                }

                img.setRGB(i,j,p);
            }
        }

        return img;
    }

    public static BufferedImage toBinInvThres(BufferedImage img,int thres)
    {
        int i,j,p;
        int width = img.getWidth();
        int height = img.getHeight();

        for(i=0; i<width; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p>=thres){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (255<<24) | (255<<16) | (255<<8) | 255;
                }

                img.setRGB(i,j,p);
            }
        }

        return img;
    }

    public static int otsuThreshold(int histData[],int totalPixel)
    {
        float sum=0;
        for(int i=0;i<256;i++)
        {
            sum += i*histData[i];
        }

        float sumB=0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t=0 ; t<256 ; t++) 
        {
            wB += histData[t];               // Weight Background
            if (wB == 0) continue;

            wF = totalPixel - wB;                 // Weight Foreground
            if (wF == 0) break;

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) 
            {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }

    public static BufferedImage toFHBinThres(BufferedImage img,int thres)
    {
        int i,j,p;
        int width = img.getWidth();
        int height = img.getHeight();

        for(i=0; i<width/2+1; i++)
        {
            for(j=0; j<height; j++)
            {
                p = img.getRGB(i,j)&0xff;
                if(p<thres){
                    p = (0<<24) | (0<<16) | (0<<8) | 0;
                }
                else {
                    p = (255<<24) | (255<<16) | (255<<8) | 255;
                }

                img.setRGB(i,j,p);
            }
        }

        return img;
    }
    
	public static void main(String args[])throws IOException
	{
        Scanner inObj = new Scanner(System.in);
        String inFile, choice;
		BufferedImage inImg=null, greyImg = null, outImg = null, greyImgFH = null;
		File f = null,o = null;   

        System.out.print("\n>> Enter Image FIle Name : ");
        inFile = inObj.nextLine();

        System.out.print("\n>> Enter Choice (bin,otsu,invbin) : ");
        choice = inObj.nextLine();

		try {
			f = new File(inFile+".jpg");
			inImg = ImageIO.read(f);
			System.out.println("\t<< Reading Complete >>");
		}
		catch(IOException e)
        {
            System.out.println("Error : " + e);
        }


        if(choice.equals("otsu"))
        {
            int[] hist;
            int thres, totalPixel;

            // Converting To Grey Scale Image
        	greyImg = toGreyScale(inImg);

            // Calculating total Pixel
            totalPixel = greyImg.getWidth()*greyImg.getHeight();

            // Calculating Histogram
            hist = histogram(greyImg);

            // Calculating Otsu's Threshold Value
            thres = otsuThreshold(hist,totalPixel);
        
            System.out.println("Otsu's Threshold Value = " + thres);

            outImg = toBinThres(greyImg,thres);
            try {
                o = new File("otsu" + inFile + ".jpg");
                ImageIO.write(outImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
                System.out.println("Error : " + e);
            }
            
        }
        else if(choice.equals("bin"))
        {
        	// Converting To Grey Scale Image
        	greyImg = toGreyScale(inImg);
            outImg = toBinThres(greyImg,127);

            try {
                o = new File("bin" + inFile + ".jpg");
                ImageIO.write(outImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
            System.out.println("Error : " + e);
            }
        }
        else if(choice.equals("invbin"))
        {
        	// Converting To Grey Scale Image
        	greyImg = toGreyScale(inImg);
            outImg = toBinInvThres(greyImg,127);

            try {
                o = new File("invbin" + inFile + ".jpg");
                ImageIO.write(outImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
            System.out.println("Error : " + e);
            }
        }
        else if(choice.equals("grey"))
        {
        	// Converting To Grey Scale Image
        	greyImg = toGreyScale(inImg);
            outImg = toBinInvThres(greyImg,127);
            
            try {
                o = new File("grey" + inFile + ".jpg");
                ImageIO.write(greyImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
                System.out.println("Error : " + e);
            }
        }
        else if(choice.equals("red"))
        {
            int[] hist;
            int thres, totalPixel;

            // Converting To Grey Scale Image
        	greyImg = toGreyScale(inImg);
            outImg = toBinInvThres(greyImg,127);
            
            // Calculating total Pixel
            totalPixel = greyImg.getWidth()*greyImg.getHeight();

            // Calculating Histogram
            hist = histogram(greyImg);

            // Calculating Otsu's Threshold Value
            thres = otsuThreshold(hist,totalPixel);
        
            System.out.println("Otsu's Threshold Value = " + thres);

            outImg = toBinThresRed(greyImg,thres);
            try {
                o = new File("red" + inFile + ".jpg");
                ImageIO.write(outImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
                System.out.println("Error : " + e);
            }
        }
        else if(choice.equals("green"))
        {
            int[] hist;
            int thres, totalPixel;

            // Converting To Grey Scale Image
        	greyImg = toGreyScale(inImg);
            outImg = toBinInvThres(greyImg,127);
            

            // Calculating total Pixel
            totalPixel = greyImg.getWidth()*greyImg.getHeight();

            // Calculating Histogram
            hist = histogram(greyImg);

            // Calculating Otsu's Threshold Value
            thres = otsuThreshold(hist,totalPixel);
        
            System.out.println("Otsu's Threshold Value = " + thres);

            outImg = toBinThresGreen(greyImg,thres);
            try {
                o = new File("green" + inFile + ".jpg");
                ImageIO.write(outImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
                System.out.println("Error : " + e);
            }
        }
        else if(choice.equals("blue"))
        {
            int[] hist;
            int thres, totalPixel;

            // Converting To Grey Scale Image
        	greyImg = toGreyScale(inImg);
            outImg = toBinInvThres(greyImg,127);
            

            // Calculating total Pixel
            totalPixel = greyImg.getWidth()*greyImg.getHeight();

            // Calculating Histogram
            hist = histogram(greyImg);

            // Calculating Otsu's Threshold Value
            thres = otsuThreshold(hist,totalPixel);
        
            System.out.println("Otsu's Threshold Value = " + thres);

            outImg = toBinThresBlue(greyImg,thres);
            try {
                o = new File("blue" + inFile + ".jpg");
                ImageIO.write(outImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
                System.out.println("Error : " + e);
            }
        }
        else if(choice.equals("fhbin"))
        {
            int[] hist;
            int thres, totalPixel;

            greyImgFH = toFHGreyScale(inImg);

            // Calculating total Pixel
            totalPixel = (greyImgFH.getWidth()/2 + 1)*greyImgFH.getHeight();

            // Calculating Histogram
            hist = histogramFH(greyImgFH);

            // Calculating Otsu's Threshold Value
            thres = otsuThreshold(hist,totalPixel);
        
            System.out.println("Otsu's Threshold Value = " + thres);

            outImg = toFHBinThres(greyImgFH,thres);
            try {
                o = new File("fhBin" + inFile + ".jpg");
                ImageIO.write(outImg,"jpg",o);
                System.out.println("\t<< Writing Complete >>");
            }
            catch(IOException e)
            {
                System.out.println("Error : " + e);
            }
        }
	}
}

