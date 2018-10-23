import model.PaintLayer;

public interface LayerItemObserver {

    void layerUpdate(PaintLayer layer, String id);

}
