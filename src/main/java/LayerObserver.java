package controller;

import model.PaintLayer;

public interface LayerObserver {

    void selectLayerUpdate(PaintLayer layer);

    void visibleLayerToggle(PaintLayer layer);

}
