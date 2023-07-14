package imgcompressor;
public class CosineDistanceMetric implements DistanceMetric_Inter{

    public double colorDistance(Pixel p1, Pixel p2){
        double num = (p1.getRed()*p2.getRed() + p1.getGreen()*p2.getGreen() + p1.getBlue()*p2.getBlue());
        double denom1 = Math.sqrt(p1.getRed()*p1.getRed() + p1.getGreen()*p1.getGreen() + p1.getBlue()*p1.getBlue());
        double denom2 = Math.sqrt(p2.getRed()*p2.getRed() + p2.getGreen()*p2.getGreen() + p2.getBlue()*p2.getBlue());
        double denom = denom1 * denom2;
        return (double)(1 - num/denom);
    }

}