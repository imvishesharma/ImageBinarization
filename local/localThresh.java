// R09321

import java.lang.Math;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class localThresh 
{
	static int WIDTH_WINDOW = 200;
	static int HEIGHT_WINDOW = 200;

	static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

	public static BufferedImage toGreayScale(BufferedImage img) 
	{
		BufferedImage pImg = deepCopy(img);

		int widthImg, heightImg;
		int row, col;
		int pixel, alpha, red, green, blue, avgPixel;

		widthImg = pImg.getWidth();
		heightImg = pImg.getHeight();

		for(row = 0; row < widthImg; row++)
		{
			for (col = 0; col < heightImg; col++) 
			{
				pixel = pImg.getRGB(row,col);

				alpha = (pixel>>24)&0xFF;
				red = (pixel>>16)&0xFF;
				green = (pixel>>8)&0xFF;
				blue = (pixel)&0xFF;


				avgPixel = (red+green+blue)/3;

				pixel = (alpha<<24) | (avgPixel<<16) | (avgPixel<<8) | (avgPixel);

				pImg.setRGB(row, col, pixel);
			}
		}
		return pImg;
	}

	
	public static BufferedImage localBin(BufferedImage pImg)
	{
		BufferedImage img = deepCopy(pImg);

		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh, pixel;

		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;

				meanPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						meanPixel += img.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-row) * (winColEnd-col));
				
				thresh = meanPixel;

				//System.out.println("meanPixel = " + meanPixel);

				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		return img;
	}
	
	public static BufferedImage localBinSauvola(BufferedImage pImg)
	{
		BufferedImage img = deepCopy(pImg);

		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh = 0, pixel;
		double standDev, coeff;

		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;

				meanPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						meanPixel += img.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-row) * (winColEnd-col));	// Caculation of Mean

				standDev = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						standDev += (pixel - meanPixel)*(pixel - meanPixel);
					}
				}
				standDev /= ((winRowEnd-row) * (winColEnd-col));	
				standDev = Math.sqrt(standDev);		// Calculation of Standard Deviation

				//coeff = 1 - 0.5*(1 - standDev/128);	
				coeff = 1 + 0.2*(standDev/128 - 1);
				meanPixel*=coeff;
				thresh = meanPixel;

				//System.out.println("Thresh = " + thresh);

				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		return img;
	}

	public static BufferedImage localBinNiblack(BufferedImage pImg)
	{
		BufferedImage img = deepCopy(pImg);

		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh = 0, pixel;
		double standDev, coeff;

		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;

				meanPixel = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						meanPixel += img.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-row) * (winColEnd-col));	// Caculation of Mean

				standDev = 0;
				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						standDev += (pixel - meanPixel)*(pixel - meanPixel);
					}
				}
				standDev /= ((winRowEnd-row) * (winColEnd-col));	
				standDev = Math.sqrt(standDev);		// Calculation of Standard Deviation

				thresh = meanPixel + ((int)(15*standDev))/100;

				//System.out.println("Thresh = " + thresh);

				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		return img;
	}

	public static BufferedImage localBinBrensen(BufferedImage pImg)
	{
		BufferedImage img = deepCopy(pImg);

		int widthImg = img.getWidth(), heightImg = img.getHeight();
		int widthWind = WIDTH_WINDOW; 
		int heightWind = HEIGHT_WINDOW; 
		int row,col, winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int minPixel, maxPixel, thresh, pixel;

		for(row = 0; row < widthImg; row += widthWind)
		{
			for(col = 0; col < heightImg; col += heightWind) 
			{
				winRowEnd = row + widthWind;
				winColEnd = col + heightImg;
				winRowEnd = winRowEnd > widthImg ? widthImg : winRowEnd;
				winColEnd = winColEnd > heightImg ? heightImg : winColEnd;

				minPixel = 255; maxPixel = 0;

				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;
						minPixel = minPixel > pixel ? pixel : minPixel;
						maxPixel = maxPixel < pixel ? pixel : maxPixel;
					}
				}

				thresh = (minPixel + maxPixel)/2;

				for(winRowStart = row; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = col; winColStart < winColEnd; winColStart++)
					{
						pixel = img.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}
						img.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		return img;
	}

	public static void main(String[] args) {
		BufferedImage inImg = null, greyImg = null, binImg = null, sauvolaImg = null, niblackImg = null, brensenImg = null;
		File i = null,o = null,c = null, s = null, n = null, b = null;

		try {
			i = new File("Img.jpg");         // FILE NAME of Image
			inImg = ImageIO.read(i);
		} catch (IOException e) {
			System.out.println("ERROR : " + e);
		}

		greyImg = toGreayScale(inImg);
		binImg = localBin(greyImg);
		sauvolaImg = localBinSauvola(greyImg);
		niblackImg = localBinNiblack(greyImg);
		brensenImg = localBinBrensen(greyImg);

		try {
			
			c = new File("localbin"+WIDTH_WINDOW+".jpg");
			ImageIO.write(binImg,"jpg",c);
			
			n = new File("niblack"+WIDTH_WINDOW+".jpg");
			ImageIO.write(niblackImg,"jpg",n);

			b = new File("brensen"+WIDTH_WINDOW+".jpg");
			ImageIO.write(binImg,"jpg",b);
			
			s = new File("sauvola"+WIDTH_WINDOW+".jpg");
			ImageIO.write(sauvolaImg,"jpg",s);

		} catch (IOException e) {
			System.out.println("ERROR : " + e);
		}
		System.out.println("DONE");
	}
}