package model;

import model.pixel.PaintColor;

import java.util.List;

/*
AUTHOR: Anthony Tao, Henrik Tao
RESPONSIBILITY: Interface for the view and controller to listen to the model
USED BY: controller.PaintController, PaintView
USES: PaintLayer, PaintColor, PaintOverlay
 */

public interface ImageModelObserver {

   void update(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, PaintColor color, PaintOverlay overlay, String id);
}
