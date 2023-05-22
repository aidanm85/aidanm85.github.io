package imgcompressor;

import java.util.*;
public class BucketingMapGenerator implements ColorMapGenerator_Inter{
	
	DistanceMetric_Inter dm;

    public BucketingMapGenerator(DistanceMetric_Inter dm){
        this.dm = dm;
    }

    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors){
        
        Pixel[] colorPalette = new Pixel[numColors];

        int bucketSize = 16777216/numColors;

        for(int i = 0; i < numColors; i++){
			int center = i * bucketSize + bucketSize/2;

            String centerBS = Integer.toBinaryString(center);
            char[] centerArr = new char[24];
			
			for(int k = 0; k < centerBS.length(); k++){
				centerArr[k] = centerBS.charAt(k);
			}
    
            String redString = "";
            String greenString = "";
            String blueString = "";
			
			int red = 0;
			int green = 0;
			int blue = 0;

            for(int j = 0; j < 24; j++){
                if(j < 8){
                    redString = redString + centerArr[j];
                }
                else if(j < 16){
                    greenString = greenString + centerArr[j];
                }
                else if(j < 24){
                    blueString = blueString + centerArr[j];
                }
            }
            centerBS = centerArr.toString();
             
            int add = 1;
            for(int m = 7; m >= 0; m--){
                if(redString.charAt(m) == '1') red += add;
                if(greenString.charAt(m) == '1') green += add;
                if(blueString.charAt(m) == '1') blue += add;
                add *= 2;
            }
            Pixel pix = new Pixel(red, green, blue);

            colorPalette[i] = pix;

        }

        return colorPalette;

    }

    public Map<Pixel,Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette){
		Pixel[] pal = initialColorPalette;
        HashMap<Pixel, Pixel> reducedColorMap = new HashMap<Pixel, Pixel>();
        for(int i = 0; i < pixelArr.length; i++){
            for(int j = 0; j < pixelArr[0].length; j++){
                Pixel pix = pixelArr[i][j];
                String colorString = "" + pix.getRed() + pix.getGreen() + pix.getBlue();
                int color = Integer.parseInt(colorString);
                
				double distance = Double.POSITIVE_INFINITY;
				Pixel reducedPix = new Pixel(255, 255, 255);
                for(int c = 0; c < pal.length; c++){
                    if(dm.colorDistance(pix, pal[c]) <= distance){
                        distance = dm.colorDistance(pix, pal[c]);
                        reducedPix = pal[c];
                    }
                }
				reducedColorMap.put(pix, reducedPix);
			}
		}
        return reducedColorMap;
    }

    
}