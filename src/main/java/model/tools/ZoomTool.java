package model.tools;

/*
AUTHOR: Henrik Tao, Anthony Tao
RESPONSIBILITY: Holding all zoom calculations and functionality.
USED BY: model.ImageModel
USES: IEditableByTool
 */

/**
 * Zoom tool function not correctly implemented, should resample image when zooming. at the moment it is only scaling, which results
 * in blurred pixels when zooming in to view in pixel level.
 */
public class ZoomTool implements ITool {

    public void onDrag(int x, int y, IEditableByTool imageModel, Boolean altDown){

    }
    public void onPress(int x, int y, IEditableByTool imageModel, Boolean altDown){
        
    }
    public void onRelease(int x, int y, IEditableByTool imageModel, Boolean altDown){
        if (altDown) {
            zoom(imageModel, true);
        } else {
            zoom(imageModel, false);
        }
    }

    private double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0) {
            return min;
        }

        if (Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }

    /**
     * Scales the canvas by delta in or out.
     * @param imageModel reference to the IEditableByTool interface.
     * @param zoomIn a boolean which if true, makes it zoom in, else zoom out.
     */
    private void zoom(IEditableByTool imageModel, Boolean zoomIn) {
        double MAX_SCALE = 15.0d;
        double MIN_SCALE = 0.1d;

        double delta = 1.05;

        double scale = imageModel.getZoomScale();

        if (zoomIn) {
            scale *= delta;
        } else {
            scale /= delta;
        }

        scale = clamp(scale, MIN_SCALE, MAX_SCALE);

        imageModel.setZoomScale(scale);
    }

    public void updateSettings(ToolSettings ts) {

    }

}