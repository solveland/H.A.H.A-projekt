package services;

import model.PaintLayer;

import java.io.File;
import java.util.List;

/*
AUTHOR: Hampus Ekberg
RESPONSIBILITY: Interface for saving and loading images
USED BY: ImageSaveLoad
USES: PaintLayer
 */

public interface ISaveLoad {

    List<PaintLayer> openFile(File file);

    void saveFile(File file, List<PaintLayer> layers);

}
