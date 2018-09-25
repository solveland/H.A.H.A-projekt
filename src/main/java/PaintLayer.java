public class Layer {

    private int[] pixelArray;
    private int height;
    private int width;

    public Layer(int sizeX, int sizeY) {
        height = sizeY;
        width = sizeX;
        pixelArray = new int[sizeX * sizeY];
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
}
