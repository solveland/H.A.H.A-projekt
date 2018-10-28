package model;

import model.pixel.Pixel;

import java.util.ArrayList;
import java.util.List;

/*
AUTHOR: Anthony Tao, Henrik Tao, Hampus Ekberg
RESPONSIBILITY: Composition of two lists of pixels, one list is pixels that need to be rendered as an overlay, the other list is pixels that need to be cleared from the rendered image
USED BY: ImageModel, PaintView, controller.PaintController, controller.LayerListController
USES: Pixel
 */

public class PaintOverlay {
    private List<Pixel> overlay;
    private List<Pixel> oldOverlay;

    PaintOverlay(){
        overlay = new ArrayList<>();
        oldOverlay = new ArrayList<>();
    }

    public List<Pixel> getOverlay() {
        return overlay;
    }

    public List<Pixel> getOldOverlay() {
        return oldOverlay;
    }

    void setNewOverlay(List<Pixel> pixels){
        oldOverlay = overlay;
        overlay = pixels;
    }

}
