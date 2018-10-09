
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.ModelObserver;
import model.PaintLayer;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.List;

public class PaintView implements ModelObserver {

    private WritableImage image;

    public PaintView(int sizeX, int sizeY) {
        setSize(sizeX,sizeY);
    }

    public WritableImage getImage() {
        return image;
    }

    public void setSize(int x, int y){
        image = new WritableImage(x, y);
    }

    /*
    // Draw the rendered image on update from model.
     */
    @Override
    public void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, String id)
    {
        if (id.equals("imageUpdate")) {
            PixelWriter pw = image.getPixelWriter();
            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    pw.setArgb(x, y, layer.getPixel(x, y).getValue());
                }
            }
        }
    }

    public void populateShapeComboBox(ComboBox<String> shapeBox) {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Circle":
                                        iconPath = "circle.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Square":
                                        iconPath = "square.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        shapeBox.setButtonCell(cellFactory.call(null));
        shapeBox.setCellFactory(cellFactory);
    }
}
