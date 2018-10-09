package model;

import java.util.List;

public interface ModelObserver {

   void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, String id);

}
