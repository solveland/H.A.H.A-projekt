package model;

import model.pixel.PaintColor;

import java.util.List;

public interface ModelObserver {

   void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, PaintColor color, String id);
}
