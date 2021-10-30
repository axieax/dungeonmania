package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Inventory;
import dungeonmania.util.Position;
public class Bow extends BuildableEquipment {

    private static final int WOOD_NEEDED = 1;
    private static final int ARROWS_NEEDED = 3;

    public Bow(String entityId, Position position) {
        super(entityId, position);
    }

    public static boolean isBuildable(Inventory inventory) {
        return inventory.hasItemQuantity("Wood", WOOD_NEEDED) && inventory.hasItemQuantity("Arrows", ARROWS_NEEDED);
    }

    public static void craft(Game game, Inventory inventory) {
        if (Bow.isBuildable(inventory)) {
            inventory.removeItemQuantity("Wood", WOOD_NEEDED);
            inventory.removeItemQuantity("Arrows", ARROWS_NEEDED);
            inventory.addItem(new Bow(game.generateEntityId(), null));
        }
    }
}
