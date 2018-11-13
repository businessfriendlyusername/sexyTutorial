package scripts.API;

import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

public class Fishing {
//~~~~~~~~~~~~~~~~Fishing Animations~~~~~~~~~~~~~~~~~
    public enum ANIMATION{//TODO fill out animation vals

        SMALL(621),
        BIG(0),
        ROD(623),
        POT(0),
        HARPOON(0);

        private int animationAsInt;

        public int asInt(){
            return animationAsInt;
        }

        ANIMATION(int num){
            this.animationAsInt = num;
    }
}

//~~~~~~~~~~~~~~~~~Fishing Locations~~~~~~~~~~~~~~~~~
    public enum LOCATION{
        LUMBRIDGE_SWAMP(new RSTile(3241, 3149, 0));


        private RSTile location;

        public RSTile getRSTile(){
            return location;
        }

        LOCATION(RSTile tile){
            location = tile;
        }
}

    public static boolean isAtFish(int distance){
        RSNPC[] fishingSpots = NPCs.findNearest("Rod Fishing spot");
        if(fishingSpots.length < 1)
            return false;
        else if (Player.getPosition().distanceTo(fishingSpots[0]) <= distance)
            return true;
        else
            return false;
    }

    public static boolean isAtFish(){
        RSNPC[] fishingSpots = NPCs.findNearest("Rod Fishing spot");
        if(fishingSpots.length < 1)
            return false;
        else
            return true;
    }
}
