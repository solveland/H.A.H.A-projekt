package model;

import model.pixel.Pixel;

import java.util.ArrayList;
import java.util.List;

public class PaintOverlay {
    private List<Pixel> overlay;
    private List<Pixel> oldOverlay;

    public PaintOverlay(){
        overlay = new ArrayList<>();
        oldOverlay = new ArrayList<>();
    }

    public List<Pixel> getOverlay() {
        return overlay;
    }

    public List<Pixel> getOldOverlay() {
        return oldOverlay;
    }

}
