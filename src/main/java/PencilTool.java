import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PencilTool extends AbstractTool implements ISizeAndColor {

    private int size;
    private int[] brushBuffer;

    public PencilTool(int size){
        this.size = size;
    }

    @Override
    void onPress(int xPos, int yPos, WritableImage image) {
        paintArea(xPos, yPos, image);
    }

    @Override
    void onDrag(int xPos, int yPos, WritableImage image) {
        paintArea(xPos, yPos, image);
    }

    @Override
    void onRelease(int xPos, int yPos, WritableImage image) {
        paintArea(xPos, yPos, image);
    }

    @Override
    public void updateSizeAndColor(int size, int color){
        this.size = size;
        int brushDiameter = size * 2 - 1;
        brushBuffer = new int[brushDiameter * brushDiameter];
        int midPoint = size -1;
        for(int y = 0; y < brushDiameter;y++){
            for(int x= 0; x < brushDiameter; x++){
                if (Math.sqrt((x-midPoint)*(x-midPoint)+(y-midPoint)*(y-midPoint)) > size){
                    brushBuffer[y*brushDiameter + x] = 0;
                } else {
                    brushBuffer[y*brushDiameter + x] = color;
                }

            }
        }

    }

    private void paintArea(int xPos, int yPos, WritableImage image){
        PixelWriter pw = image.getPixelWriter();
        int diameter = size*2-1;
        for(int yc = 0; yc < diameter; yc++){
            for(int xc = 0; xc < diameter; xc++){
                if((brushBuffer[yc*diameter +xc] & 0xFF000000) != 0){
                    int pixelX = xc+xPos - diameter/2;
                    int pixelY = yc+yPos - diameter/2;
                    if(pixelX < 0 || pixelX >= image.getWidth() || pixelY < 0 ||pixelY >= image.getHeight()){
                        continue;
                    }
                    pw.setArgb(xc+xPos - diameter/2,yc+yPos - diameter/2,brushBuffer[yc*diameter + xc]);
                }
            }
        }
    }
}
