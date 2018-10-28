package view;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.ImageModelObserver;
import model.PaintLayer;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import model.pixel.PaintColor;
import model.PaintOverlay;
import model.pixel.Pixel;

import java.util.List;

/*
AUTHOR: Anthony Tao, August Sölveland
RESPONSIBILITY: Rendering the image that is being edited and populating some combo boxes
USED BY: PaintController, and ImageModel uses it through the observer interface
USES: PaintLayer, PaintColor, PaintOverlay
 */

public class PaintView implements ImageModelObserver {

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
    public void notifyObservers(PaintLayer renderedImage, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, PaintColor color, PaintOverlay overlay, String id)
    {
        if (id.equals("imageUpdate")) {
            PixelWriter pw = image.getPixelWriter();
            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    pw.setArgb(x, y, renderedImage.getPixel(x, y).getValue());
                }
            }
        } else if (id.equals("overlayUpdate")) {
            PixelWriter pw = image.getPixelWriter();

            for (Pixel p : overlay.getOldOverlay()) {
                // Delete the old overlay pixels
                pw.setArgb(p.getX(), p.getY(), renderedImage.getPixel(p.getX(), p.getY()).getValue());
            }
                // Paint the new overlay pixels
            for (Pixel p : overlay.getOverlay()) {
                pw.setArgb(p.getX(), p.getY(), p.getColor().getValue());
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


    public void populateSizeComboBox(ComboBox<String> shapeBox) {
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

                                    case "Small":
                                        iconPath = "Line1.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Medium":
                                        iconPath = "Line2.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Large":
                                        iconPath = "Line3.png";
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

    public void populateShapeComboBox1(ComboBox<String> shapeBox) {
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

                                    case "Line":
                                        iconPath = "Line2.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Ellipse":
                                        iconPath = "Ellipse.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Triangle":
                                        iconPath = "Triangle.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Rectangle":
                                        iconPath = "Rectangle.png";
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
