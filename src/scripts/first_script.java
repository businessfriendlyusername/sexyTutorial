package scripts;
import org.tribot.api2007.NPCs;
import org.tribot.script.Script;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.Objects;

//lumbridge chickens outside gate (3237,3295,0)
public class first_script extends Script {
    private boolean isAtChickens(){
        final RSNPC[] chickens = NPCs.findNearest("Chicken");
        if (chickens.length < 1){
            return false;
        }
        for(int i = 0; i < chickens.length; i++){

        }
        return true;//comment me out plz
    }

    @Override
    public void run(){
        while (true){
            sleep(1000);
            System.out.println(Objects.findNearest(20, "Chicken"));
        }
    }
}
