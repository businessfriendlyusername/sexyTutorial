package scripts.API;

import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;

public class Firemaking {
    //returns true if the player is standing on a fire
    public static boolean standingOnFire(){
        return Objects.isAt(Player.getPosition(), 26185);
    }
}
