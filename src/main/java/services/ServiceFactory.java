package services;

/*
AUTHOR: Hampus Ekberg
RESPONSIBILITY: Factory for services
USED BY: PaintController
USES: ImageSaveLoad
 */

public class ServiceFactory {



    private static ISaveLoad saveAndLoader = new ImageSaveLoad();

    public static ISaveLoad getSaveAndLoader() {
        return saveAndLoader;
    }

}
