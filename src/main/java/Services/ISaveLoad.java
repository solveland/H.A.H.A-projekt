package Services;

import model.PaintLayer;

import java.io.File;
import java.util.List;

public interface ISaveLoad {

    List<PaintLayer> openFile(File file);

    void saveFile(File file, List<PaintLayer> layers);

}
