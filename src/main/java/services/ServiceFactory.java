package services;

/*
AUTHOR: Hampus Ekberg
RESPONSIBILITY: Factory for services
USED BY: controller.PaintController
USES: ImageSaveLoad
 */

public class ServiceFactory {



    private static ISaveLoad saveAndLoader = new ImageSaveLoad();

    public static ISaveLoad getSaveAndLoader() {
        return saveAndLoader;
    }

}
