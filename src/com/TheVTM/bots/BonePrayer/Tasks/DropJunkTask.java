package com.TheVTM.bots.BonePrayer.Tasks;

import com.TheVTM.bots.BonePrayer.Constants;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * Created by VTM on 5/4/2016.
 */
public class DropJunkTask extends Task implements InventoryListener {

    /* FIELDS */
    private List<SpriteItem> junkList;

    /* LOGGER */
    private static final Logger LOGGER = Logger.getLogger(DropJunkTask.class.getName());
    

    public DropJunkTask(Task... a) {
        super(a);
        LOGGER.log(Level.FINEST, "Constructing DropJunkTask.");

        junkList = new ArrayList<>();

        // Register Inventory Listener
        Environment.getBot().getEventDispatcher().addListener(this);
    }

    @Override
    public boolean validate() {
        return !junkList.isEmpty();
    }

    @Override
    public void execute() {
        LOGGER.log(Level.FINEST, "Dropping junk items...");

        try {
            // Get the first item
            SpriteItem item = junkList.get(0);

            LOGGER.log(Level.INFO, String.format("Dropping %d [%08X] %s (%d).",
                    item.getQuantity(), item.hashCode(), item.getDefinition().getName(), item.getId()));

            // Verify if item is valid
            if(!item.isValid()) {
                LOGGER.log(Level.INFO, String.format("Invalid item %d [%08X] %s (%d).",
                        item.getQuantity(), item.hashCode(), item.getDefinition().getName(), item.getId()));
                junkList.remove(0);
                return;
            }

            // Drop item
            if(item.interact("Drop")) {
                LOGGER.log(Level.FINEST, "Successfully dropped item.");
                junkList.remove(0);
                return;
            }
            
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Unable to drop junk item.", e);
        }
    }

    @Override
    public void onItemAdded(ItemEvent itemEvent) {
        SpriteItem item = itemEvent.getItem();
        ItemDefinition itemDef = item.getDefinition();
        String name = itemDef.getName();

        LOGGER.log(Level.FINEST, String.format("%d [%08X] %s (%d) added to inventory. Quantity changed to %d.",
                item.getQuantity(), item.hashCode() , name, item.getId(), itemEvent.getQuantityChange()));

        // Ignore if it is a bone
        Matcher matcher = Constants.BONES_PATTERN.matcher(name);
        if(matcher.matches())
            return;

        // Ignore if it is a stackable item already present in inventory (not occupying an extra slot)
        if(itemDef.stacks()) {
            LOGGER.log(Level.FINEST, String.format("Found stackable item: %d [%08X] %s (%d).",
                    item.getQuantity(), item.hashCode() , name, item.getId()));

            if(itemEvent.getQuantityChange() != item.getQuantity()) { // Already has a stack of this item in the inventory
                LOGGER.log(Level.FINE, String.format("Ignoring stackable %s (%d). Already present in the inventory.",
                        name, item.getId()));

                return;
            }
        }
        
        // Add item to junk list
        LOGGER.log(Level.INFO, String.format("Found junk item %s (%d).", name, item.getId()));
        junkList.add(item);
    }

    @Override
    public void onItemRemoved(ItemEvent a) {

    }
}
