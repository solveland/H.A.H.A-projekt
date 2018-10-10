import Model.PaintLayer;

public interface LayerObserver {

    void layerUpdate(PaintLayer layer, String id);

}
