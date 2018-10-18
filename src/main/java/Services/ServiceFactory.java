package Services;

public class ServiceFactory {

    private static ISaveLoad saveAndLoader = new ImageSaveLoad();

    public static ISaveLoad getSaveAndLoader() {
        return saveAndLoader;
    }

}
