package imgcompressor;
public class SquaredEuclideanMetric implements DistanceMetric_Inter{

    public double colorDistance(Pixel p1, Pixel p2){
        return (double)((p1.getRed() - p2.getRed())*(p1.getRed() - p2.getRed()) + (p1.getGreen() - p2.getGreen())*(p1.getGreen() - p2.getGreen()) + (p1.getBlue() - p2.getBlue())*(p1.getBlue() - p2.getBlue()));
    }

}