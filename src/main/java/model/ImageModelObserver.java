package model;

import model.pixel.PaintColor;

import java.util.List;

public interface ImageModelObserver {

   void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, PaintColor color, PaintOverlay overlay, String id);
}
