public class PencilTool extends AbstractTool implements ISize, IColor {

    private int size;
    private int[] brushBuffer;
    private int color;

    public PencilTool(int size){
        this.size = size;
    }

    @Override
    void onPress(int xPos, int yPos, PaintLayer layer) {
        paintArea(xPos, yPos, layer);
    }

    @Override
    void onDrag(int xPos, int yPos, PaintLayer layer) {
        paintArea(xPos, yPos, layer);
    }

    @Override
    void onRelease(int xPos, int yPos, PaintLayer layer) {
        paintArea(xPos, yPos, layer);
    }

    private void paintArea(int xPos, int yPos, PaintLayer layer){
        int diameter = size*2-1;
        for(int yc = 0; yc < diameter; yc++){
            for(int xc = 0; xc < diameter; xc++){
                if((brushBuffer[yc*diameter +xc] & 0xFF000000) != 0){
                    int pixelX = xc+xPos - diameter/2;
                    int pixelY = yc+yPos - diameter/2;
                    if(pixelX < 0 || pixelX >= layer.getWidth() || pixelY < 0 ||pixelY >= layer.getHeight()){
                        continue;
                    }
                    layer.setPixel(xc+xPos - diameter/2,yc+yPos - diameter/2,brushBuffer[yc*diameter + xc]);
                }
            }
        }
    }

    @Override
    public void updateColor(int color) {
        this.color = color;
        updateBrush();
    }

    @Override
    public void updateSize(int size) {
        this.size = size;
        updateBrush();
    }

    private void updateBrush(){
        int brushDiameter = size * 2 - 1;
        brushBuffer = new int[brushDiameter * brushDiameter];
        int midPoint = size -1;
        for(int y = 0; y < brushDiameter;y++){
            for(int x= 0; x < brushDiameter; x++){
                if (Math.sqrt((x-midPoint)*(x-midPoint)+(y-midPoint)*(y-midPoint)) > size -0.5){
                    brushBuffer[y*brushDiameter + x] = 0;
                } else {
                    brushBuffer[y*brushDiameter + x] = color;
                }

            }
        }
    }
}
