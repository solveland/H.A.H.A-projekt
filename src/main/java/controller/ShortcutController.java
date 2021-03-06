package controller;


import javafx.application.Platform;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/*
AUTHOR: Anthony Tao, Henrik Tao
RESPONSIBILITY: Adds shortcuts in the form of key combinations to the program
USED BY: PaintController
USES: ILayerListController, IPaintController
*/

/**
 * ShortcutController handles the shortcuts to activate specific tools in the application.
 */

public class ShortcutController {

    private IPaintController pC;

    protected ShortcutController(IPaintController pC, ILayerListController layerLC) {
        this.pC = pC;

        // Tools shortcuts
        setButtonShortcut(pC.getBrushButton(), KeyCode.B);
        setButtonShortcut(pC.getEraserButton(), KeyCode.E);
        setButtonShortcut(pC.getEyedropperButton(), KeyCode.I);
        setButtonShortcut(pC.getFillButton(), KeyCode.G);
        setButtonShortcut(pC.getSelectButton(), KeyCode.M);
        setButtonShortcut(pC.getPencilButton(), KeyCode.P);
        setButtonShortcut(pC.getShapeButton(), KeyCode.U);
        setButtonShortcut(pC.getZoomButton(), KeyCode.Z);
        setButtonShortcut(pC.getUndoButton(), KeyCode.Z, KeyCombination.CONTROL_DOWN);

        // Layer list shortcuts
        setButtonShortcut(layerLC.getDeleteLayerButton(), KeyCode.DELETE);
    }



    private void setButtonShortcut(ButtonBase b, KeyCode k) {
        Platform.runLater(()-> {
            KeyCodeCombination kC = new KeyCodeCombination(k);

            b.getScene().getAccelerators().put(
                    kC, () -> {
                        if (!pC.getMouseDown()) {
                            b.fire();
                        }
                    });
        });
    }

    private void setButtonShortcut(ButtonBase b, KeyCode k, KeyCombination.Modifier m) {
        Platform.runLater(()-> {
            KeyCodeCombination kC = new KeyCodeCombination(k, m);

            b.getScene().getAccelerators().put(
                    kC, () -> {
                        if (!pC.getMouseDown()) {
                            b.fire();
                        }
                    });
        });
    }

}
