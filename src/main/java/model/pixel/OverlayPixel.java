package model.pixel;

import java.util.ArrayList;
import java.util.List;

public class OverlayPixel {
    private List<Pixel> overlay;
    private List<Pixel> oldOverlay;

    private Boolean changedPixels;

    public OverlayPixel(){
        overlay = new ArrayList<>();
        oldOverlay = new ArrayList<>();
        changedPixels = false;
    }

    public List<Pixel> getOverlay() {
        return overlay;
    }

    public List<Pixel> getOldOverlay() {
        return oldOverlay;
    }

    public Boolean getChanged() {
        return changedPixels;
    }

    public void setChanged(Boolean changedPixels) {
        this.changedPixels = changedPixels;
    }



}
