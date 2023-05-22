package imgcompressor;
import java.util.*;

public class ClusteringMapGenerator implements ColorMapGenerator_Inter{
    
    DistanceMetric_Inter dm;

    public ClusteringMapGenerator(DistanceMetric_Inter dm){
        this.dm = dm;
    }

    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors){

        boolean[][] visitedArr = new boolean[pixelArr.length][pixelArr[0].length];
        Pixel[] colorPalette = new Pixel[numColors];
        colorPalette[0] = pixelArr[0][0];
        visitedArr[0][0] = true;
        int count = 1;

        for(int paletteIndex = 1; paletteIndex < numColors; paletteIndex++){
            double max = 0;
            int maxRGB = 0;
            int row = 0;
            int col = 0;
            String maxRGBstring = "";

            for(int i = 0; i < pixelArr.length; i++){
                for(int j = 0; j < pixelArr[0].length; j++){
                    double distance = Double.POSITIVE_INFINITY;
                    for(int pal = 0; pal < count; pal++){
                        if(dm.colorDistance(colorPalette[pal], pixelArr[i][j]) < distance) distance = dm.colorDistance(colorPalette[pal], pixelArr[i][j]);
                    }
                    if(distance > max && visitedArr[i][j] == false){
                        max = distance;
                        maxRGBstring = "" + pixelArr[i][j].getRed() + pixelArr[i][j].getGreen() + pixelArr[i][j].getBlue();
                        maxRGB = Integer.parseInt(maxRGBstring);
                        row = i;
                        col = j;
                    }
                    else if(distance == max && visitedArr[i][j] == false){
                        String currRGBstring = "" + pixelArr[i][j].getRed() + pixelArr[i][j].getGreen() + pixelArr[i][j].getBlue();
                        int currRGB = Integer.parseInt(currRGBstring);
                        if(currRGB > maxRGB){
                            maxRGB = currRGB;
                            row = i;
                            col = j;
                        }
                    }
                }
            }
            colorPalette[paletteIndex] = pixelArr[row][col];
            count++;
            visitedArr[row][col] = true;
        }
        return colorPalette;
    }

    public Map<Pixel,Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette){
        Pixel[] centroids = initialColorPalette;
        Pixel[][] centroidMap = new Pixel[pixelArr.length][pixelArr[0].length];
        for(int i = 0; i < pixelArr.length; i++){
            for(int j = 0; j < pixelArr[0].length; j++){
                Pixel centroid = centroids[0];
                double distance = Double.POSITIVE_INFINITY;
                for(int c = 0; c < centroids.length; c++){
                    if(dm.colorDistance(centroids[c], pixelArr[i][j]) <= distance){
                        distance = dm.colorDistance(centroids[c], pixelArr[i][j]);
                        centroid = centroids[c];
                    }
                }
                centroidMap[i][j] = centroid;
            }
        }
        boolean converged = false;
        while(! converged){
            converged = true;
            for(int c = 0; c < centroids.length; c++){
                int totalRed = 0;
                int totalGreen = 0;
                int totalBlue = 0;
                int count = 0;
                for(int i = 0; i < pixelArr.length; i++){
                    for(int j = 0; j < pixelArr[0].length; j++){
                        if(centroidMap[i][j].toString().equals(centroids[c].toString())){
                            totalRed += pixelArr[i][j].getRed();
                            totalGreen += pixelArr[i][j].getGreen();
                            totalBlue += pixelArr[i][j].getBlue();
                            count++;
                        }
                    }
                }
    
                int averageRed = totalRed/count; 
                int averageGreen = totalGreen/count;
                int averageBlue = totalBlue/count;

              
                Pixel newCentroid = new Pixel(averageRed, averageGreen, averageBlue);
                centroids[c] = newCentroid;
            }
            for(int i = 0; i < pixelArr.length; i++){
                for(int j = 0; j < pixelArr[0].length; j++){
                    Pixel centroid = centroids[0];
                    double distance = Double.POSITIVE_INFINITY;
                    for(int c = 0; c < centroids.length; c++){
                        if(dm.colorDistance(centroids[c], pixelArr[i][j]) < distance){
                            distance = dm.colorDistance(centroids[c], pixelArr[i][j]);
                            centroid = centroids[c];
                        }
                    }
                    if(centroid.toString().equals(centroidMap[i][j].toString()) == false){
                        converged = false;
                    }
                    centroidMap[i][j] = centroid;
                }
            }
        }
        HashMap<Pixel, Pixel> clusteredColorMap = new HashMap<Pixel, Pixel>();
        for(int i = 0; i < pixelArr.length; i++){
            for(int j = 0; j < pixelArr[0].length; j++){
                clusteredColorMap.put(pixelArr[i][j], centroidMap[i][j]);
            }
        }
        return clusteredColorMap;
    }
}