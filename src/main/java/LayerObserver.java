import Model.PaintLayer;

public interface LayerObserver {

    void selectLayerUpdate(PaintLayer layer);

    void visibleLayerToggle(PaintLayer layer);

}
