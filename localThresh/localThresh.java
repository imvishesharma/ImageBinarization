/*
 * TITLE  : Local Thresholding Methods for Image Binarization
 * AUTHOR : VISHESH SHARMA
 * 
 */

// Importing JAVA : File & Exception Library
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;

// Importing JAVA : BufferedImage & ImageIO Library
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

// Importing JAVA : Math Library
import java.lang.Math;


public class localThresh {

	// WINDOW_WIDTH -> imageX of the Window 
	static private int WINDOW_WIDTH = 200;

	// WINDOW_HEIGHT -> imageY of the Window
	static private int WINDOW_HEIGHT = 200;

	// Method for Deep Copying Old Image to New One
	/*
	static BufferedImage copyImage(BufferedImage oldImage) {
		ColorModel colorModel = oldImage.getColorModel();
		WritableRaster raster = oldImage.copyData(null);
		boolean isAlphaPremultiplied = oldImage.isAlphaPremultiplied();

		return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
	}
	*/

	// Method for Converting Color Image to Grey Image
	static BufferedImage toGreyImage(BufferedImage colorImage)
	{
		int widthImage = colorImage.getWidth();
		int heightImage = colorImage.getHeight();

		BufferedImage greyImage = new BufferedImage(widthImage, heightImage, colorImage.getType());

		int pixel, alpha, red, blue, green, avgPixel;

		// Converting Every Pixel to Average of Red, Green, Blue
		for(int imageX = 0; imageX < widthImage; imageX++) {

			for (int imageY = 0; imageY < heightImage; imageY++) {

				pixel = colorImage.getRGB(imageX, imageY);

				alpha = (pixel>>24)&0xFF;
				red = (pixel>>16)&0xFF;
				green = (pixel>>8)&0xFF;
				blue = (pixel)&0xFF;

				avgPixel = (red + green + blue)/3;

				pixel = (alpha<<24) | (avgPixel<<16) | (avgPixel<<8) | (avgPixel);

				greyImage.setRGB(imageX, imageY, pixel);
			}
		}
		return greyImage;
	}


	public static BufferedImage localBinNiblack(BufferedImage greyImage)
	{

		int widthImage = greyImage.getWidth();
		int heightImage = greyImage.getHeight();

		int widthWindow = WINDOW_WIDTH; 
		int heightWindow = WINDOW_HEIGHT; 

		int imageX,imageY;
		int winRowStart=0, winColStart=0, winRowEnd=0, winColEnd=0;
		int meanPixel, thresh, pixel;
		double standDev, coeff;

		BufferedImage binaryImage = new BufferedImage(widthImage, heightImage, greyImage.getType());

		for(imageX = 0; imageX < widthImage; imageX += widthWindow)
		{
			for(imageY = 0; imageY < heightImage; imageY += heightWindow) 
			{
				winRowEnd = imageX + widthWindow;
				winColEnd = imageY + heightImage;
				winRowEnd = Math.min(widthImage, winRowEnd);
				winColEnd = Math.min(heightImage,winColEnd);

				meanPixel = 0;
				for(winRowStart = imageX; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = imageY; winColStart < winColEnd; winColStart++)
					{
						meanPixel += greyImage.getRGB(winRowStart, winColStart)&0xFF;
					}
				}
				meanPixel /= ((winRowEnd-imageX) * (winColEnd-imageY));	// Caculation of Mean

				standDev = 0;
				for(winRowStart = imageX; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = imageY; winColStart < winColEnd; winColStart++)
					{
						pixel = greyImage.getRGB(winRowStart, winColStart)&0xFF;
						standDev += (pixel - meanPixel)*(pixel - meanPixel);
					}
				}
				standDev /= ((winRowEnd-imageX) * (winColEnd-imageY));	
				standDev = Math.sqrt(standDev);		// Calculation of Standard Deviation

				thresh = meanPixel + ((int)(20*standDev))/100;
				//thresh = meanPixel*(1 + (int)(0.4*(standDev/128 - 1)));
				//System.out.println("Thresh = " + thresh);

				for(winRowStart = imageX; winRowStart < winRowEnd; winRowStart++)
				{
					for(winColStart = imageY; winColStart < winColEnd; winColStart++)
					{
						pixel = greyImage.getRGB(winRowStart, winColStart)&0xFF;

						if(pixel < thresh) {
							pixel = (0<<24) | (0<<16) | (0<<8) | 0;
						} else {
							pixel = (255<<24) | (255<<16) | (255<<8) | 255;
						}

						binaryImage.setRGB(winRowStart, winColStart, pixel);
					}
				}
			}
		}
		return binaryImage;
	}


	public static void main(String[] args) throws IOException {

		if(args.length != 0) { 
			System.out.println(args[0]);

			File colorImgFile = new File(args[0]+".jpg");
			//File greyImgFile = new File("grey"+args[0]+".jpg");
			File binImgFile = new File("bin"+args[0]+".jpg");

			BufferedImage colorImage = ImageIO.read(colorImgFile);
			BufferedImage greyImage = toGreyImage(colorImage);
			BufferedImage binImage = localBinNiblack(greyImage);

			//ImageIO.write(greyImage,"jpg",greyImgFile);
			ImageIO.write(binImage,"jpg",binImgFile);
		}
	}
}