package Services;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import model.PaintLayer;
import model.pixel.PaintColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class ImageSaveLoad implements ISaveLoad {
    @Override
    public List<PaintLayer> openFile(File file) {
        BufferedImage loadedImage;
        try {
            loadedImage = ImageIO.read(file);
        } catch (java.io.IOException e) {
            //Todo: Handle this exception
            return null;
        }
        List<PaintLayer> layerList = new ArrayList<>();
        int width = loadedImage.getWidth();
        int height = loadedImage.getHeight();
        layerList.add(new PaintLayer(width,height, PaintColor.blank,"Background"));
        for(int x = 0; x < width;x++){
            for(int y = 0; y < height;y++){
                layerList.get(0).setPixel(x,y,new PaintColor(loadedImage.getRGB(x,y)));
            }
        }
        return layerList;
    }

    @Override
    public void saveFile(File file, List<PaintLayer> layers) {
        PaintLayer renderedImage = new PaintLayer(layers.get(0).getWidth(),layers.get(0).getHeight(),PaintColor.blank,null);
        for(int x = 0; x < renderedImage.getWidth();x++){
            for(int y = 0; y < renderedImage.getHeight();y++){
                for(int i = layers.size() - 1; i >= 0;i--){
                    renderedImage.setPixel(x,y,PaintColor.alphaBlend(layers.get(i).getPixel(x,y),renderedImage.getPixel(x,y)));
                }
            }
        }
        WritableImage image = new WritableImage(layers.get(0).getWidth(),layers.get(0).getHeight());
        for(int x = 0; x < image.getWidth();x++){
            for(int y = 0; y < image.getHeight();y++){
                image.getPixelWriter().setArgb(x,y,renderedImage.getPixel(x,y).getValue());
            }
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image,null), "png", file);
        } catch (java.io.IOException e) {
            //Todo: Handle this exception
            return;
        }
    }
}
