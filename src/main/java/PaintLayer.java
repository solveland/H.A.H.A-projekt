public class PaintLayer {

    private int[] pixelArray;
    private int height;
    private int width;

    public PaintLayer(int sizeX, int sizeY) {
        height = sizeY;
        width = sizeX;
        pixelArray = new int[sizeX * sizeY];
        // Temporary initialize layer with white pixels since it is used as a background for the moment
        for (int i = 0; i < sizeX * sizeY; i++)
        {
            pixelArray[i] = 0xFFFFFFFF;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPixel(int x, int y, int rgb) {
        pixelArray[y * width + x] = rgb;
    }

    public int getPixel(int x, int y) {
        return pixelArray[y*width+x];
    }

    // Temporary clear
    public void clearLayer() {
        for (int i = 0; i < height * width; i++) {
            pixelArray[i] = 0xFFFFFFFF;
        }
    }
}
