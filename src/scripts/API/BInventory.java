package scripts.API;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import java.util.LinkedList;

public class BInventory {
    //drop all except one of each item passed in
    public static void dropAllExceptOne(String ...names){
        RSItem[] inven = Inventory.getAll();
        LinkedList<RSItem> drop = new LinkedList<RSItem>();
        for (RSItem item : inven){//iterate through inventory
            boolean toDrop = true;//assume that we will drop the item
            for (int i = 0; i < names.length; i++){//iterate through items we will only keep one of
                if(item.getDefinition().getName().equals(names[i])) {//if this item is one we need to keep...
                    toDrop = false;//we will not drop this item
                    names[i] = "";//we won't keep this item in future iterations, change it's value to an empty string
                }
            }
            if(toDrop)//we decided to drop the item
                drop.add(item);
        }
        for (RSItem item : drop)//drop all unwanted items
            Inventory.drop(item);
    }
}