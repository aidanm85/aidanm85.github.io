package imgcompressor;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.Color;

public class ColorQuantizer implements ColorQuantizer_Inter{

    Pixel[][] arr;
    ColorMapGenerator_Inter cm;
    BufferedImage image;

    public ColorQuantizer(File file, ColorMapGenerator_Inter cm){
        this.cm = cm;

         try {
            this.image = ImageIO.read(file);
        } catch (Exception e) {
            System.out.println(e);
        }

        this.arr = convertBitmapToPixelMatrix(image);
    }

    public ColorQuantizer(Pixel[][] pixArr, ColorMapGenerator_Inter cm){
        this.arr = pixArr;
        this.cm = cm;
    }

    public Pixel[][] quantizeTo2DArray(int numColors){
        Pixel[][] quantizedPixArr = new Pixel[arr.length][arr[0].length];
        Pixel[] initialColorPalette = cm.generateColorPalette(arr, numColors);
        Map<Pixel, Pixel> pixelMap = cm.generateColorMap(arr, initialColorPalette);

        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
                quantizedPixArr[i][j] = pixelMap.get(arr[i][j]);
            }
        }

        return quantizedPixArr;
    }

    public void quantizeToBMP(String fileName, int numColors){
        Pixel[][] quantizedPixArr = quantizeTo2DArray(numColors);

        savePixelMatrixToBitmap(fileName, quantizedPixArr);
    }
	public static void savePixelMatrixToBitmap(String filePath, Pixel[][] pixelMatrix) {
        int width = pixelMatrix.length;
        int height = pixelMatrix[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Pixel pixel;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel = pixelMatrix[x][y];
                Color color = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
                image.setRGB(x, y, color.getRGB());
            }
        }
        try {
            File file = new File(filePath);
            ImageIO.write(image, "bmp", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public static void savePixelMatrixToFile(String filePath, Pixel[][] matrix) {

        try {
            // Open file for writing
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            // Write matrix to file
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(matrix[i][j] + String.valueOf('\t'));
                }
                writer.newLine();
            }

            // Close file
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     public static Pixel[][] convertBitmapToPixelMatrix(BufferedImage image) {
        Pixel[][] pixelMatrix = new Pixel[image.getWidth()][image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                pixelMatrix[x][y] = new Pixel(red, green, blue);
            }
        }

        return pixelMatrix;
    }
}