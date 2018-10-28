package model.pixel;

/*
AUTHOR: Hampus Ekberg
RESPONSIBILITY: To define color
USED BY: Most parts of the model
USES:
 */

public class PaintColor {
    public static PaintColor blank = new PaintColor(0,0,0,0);

    private int colorValue;

    public PaintColor(int colorValue){
        this.colorValue = colorValue;
    }

    public PaintColor(int red, int green, int blue){
        this(red,green,blue,255);
    }

    public PaintColor(int red, int green, int blue, int alpha){
        red = Math.max(0,Math.min(255,red));
        blue = Math.max(0,Math.min(255,blue));
        green = Math.max(0,Math.min(255,green));
        alpha = Math.max(0,Math.min(255,alpha));
        colorValue = (alpha << 24) | (red << 16) | (green << 8) | (blue);
    }

    public PaintColor(double red, double green, double blue, double alpha){
        this((int)(red * 255),(int)(green * 255),(int)(blue * 255),(int)(alpha*255));
    }

    public int getValue(){
        return colorValue;
    }

    public int getGreen() {
        return (colorValue & 0x0000FF00) >>> 8;
    }

    public int getRed(){
        return (colorValue & 0x00FF0000) >>> 16;
    }

    public int getBlue(){
        return (colorValue & 0x000000FF);
    }

    public int getAlpha() {
        return (colorValue & 0xFF000000) >>> 24;
    }

    public double getAlphaRatio(){
        return ((double)getAlpha())/255;
    }

    public double getGreenRatio(){
        return ((double)getGreen())/255;
    }

    public double getBlueRatio(){
        return ((double)getBlue())/255;
    }

    public double getRedRatio(){
        return ((double)getRed())/255;
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof PaintColor)){
            return false;
        }

        PaintColor c = (PaintColor)o;

        return c.getValue() == getValue();
    }

    /**
     * Mixes two colors based on their alpha values
     * @param src The foreground color
     * @param dst The background color
     * @return The mix between the foreground color and the background color
     */

    public static PaintColor alphaBlend(PaintColor src, PaintColor dst){
        if (src.getAlphaRatio() > 0.9999){
            return src;
        }
        if (src.getAlphaRatio() < 0.0001){
            return dst;
        }
        double alpha = src.getAlphaRatio() + dst.getAlphaRatio()*(1.0 - src.getAlphaRatio());
        if (alpha < 0.0001){
            return PaintColor.blank;
        }
        double red = (src.getRedRatio()*src.getAlphaRatio() + dst.getRedRatio() * dst.getAlphaRatio()*(1 - src.getAlphaRatio()))/alpha;
        double green = (src.getGreenRatio()*src.getAlphaRatio() + dst.getGreenRatio() * dst.getAlphaRatio()*(1 - src.getAlphaRatio()))/alpha;
        double blue = (src.getBlueRatio()*src.getAlphaRatio() + dst.getBlueRatio() * dst.getAlphaRatio()*(1 - src.getAlphaRatio()))/alpha;
        return new PaintColor(red,green,blue,alpha);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + colorValue;
        return hash;
    }

}
