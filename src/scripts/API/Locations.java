package scripts.API;

public class Locations {
    private static Locations ourInstance = new Locations();

    public static Locations getInstance() {
        return ourInstance;
    }

    private Locations() {
    }
}
