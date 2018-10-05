package Model;

public class PaintLayer {

    private int[] pixelArray;
    private int height;
    private int width;
    private int changedMinX,changedMinY,changedMaxX,changedMaxY;
    private boolean changed = false;
    private boolean visible;
    private int bgColor;
    private String name;

    public PaintLayer(int sizeX, int sizeY, int bgColor, String name) {
        height = sizeY;
        width = sizeX;
        pixelArray = new int[sizeX * sizeY];
        this.bgColor = bgColor;
        visible = true;
        this.name = name;

        if (bgColor != 0)
        for (int i = 0; i < sizeX * sizeY; i++)
        {
            pixelArray[i] = bgColor;
        }
        changedMinX = width;
        changedMinX = 0;
        changedMinY = 0;
        changedMaxY = height;
        changed = true;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPixel(int x, int y, int rgb) {
        pixelArray[y * width + x] = rgb;
        if (!changed){
            changedMinX = x;
            changedMaxX = x + 1;
            changedMinY = y;
            changedMaxY = y + 1;
            changed = true;
        } else {
            if (x < changedMinX){
                changedMinX = x;
            } else if(x >= changedMaxX){
                changedMaxX = x + 1;
            }

            if (y < changedMinY){
                changedMinY = y;
            } else if(y >= changedMaxY){
                changedMaxY = y + 1;
            }
        }
    }

    public int getPixel(int x, int y) {
        return pixelArray[y*width+x];
    }

    // Temporary clear
    public void clearLayer() {
        for (int i = 0; i < height * width; i++) {
            pixelArray[i] = bgColor;
        }
        changed = true;
        changedMinX = 0;
        changedMinY = 0;
        changedMaxY = height;
        changedMaxX = width;
    }

    public void resetChangeTracker(){
        changedMinX = 0;
        changedMaxX = 0;
        changedMinY = 0;
        changedMaxY = 0;
        changed = false;
    }

    public int getChangedMinX() {
        return changedMinX;
    }

    public int getChangedMinY() {
        return changedMinY;
    }

    public int getChangedMaxX() {
        return changedMaxX;
    }

    public int getChangedMaxY() {
        return changedMaxY;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isVisible() { return visible; }

    public void toggleVisible() {
        visible = !visible;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
