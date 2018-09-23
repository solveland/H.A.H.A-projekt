import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageModel {

    private WritableImage image;
    private AbstractTool activeTool;

    int toolSize = 15;
    int color = 0xFF000000;

    public ImageModel(int sizeX, int sizeY) {
        image = new WritableImage(sizeX, sizeY);
        activeTool = new PencilTool(toolSize);
        if(activeTool instanceof ISizeAndColor){
            ((ISizeAndColor) activeTool).updateSizeAndColor(toolSize, color);
        }
    }

    public void updateColor(Color color){
        this.color = 0xFF000000 | ((int)(color.getRed() * 255) << 16) | ((int)(color.getGreen() * 255) << 8) | ((int)(color.getBlue() * 255));
        if(activeTool instanceof ISizeAndColor){
            ((ISizeAndColor) activeTool).updateSizeAndColor(toolSize, this.color);
        }
    }
    public void onDrag(int x, int y){
        activeTool.onDrag(x, y, image);
    }
    public WritableImage getImage() {
        return image;
    }

    public int getToolSize() {
        return toolSize;
    }

    public void setActiveTool(AbstractTool activeTool) {
        this.activeTool = activeTool;
    }
}
