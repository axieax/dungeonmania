package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.movings.Inventory;

public class Bow extends BuildableEquipment {

    private static final int WOOD_NEEDED = 1;
    private static final int ARROWS_NEEDED = 3;

    public Bow() {
        super("bow", null);
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return inventory.hasItemQuantity("Wood", WOOD_NEEDED) && inventory.hasItemQuantity("Arrows", ARROWS_NEEDED);
    }

    @Override
    public void craft(Inventory inventory) {
        if (isBuildable(inventory)) {
            inventory.removeItemQuantity("Wood", WOOD_NEEDED);
            inventory.removeItemQuantity("Arrows", ARROWS_NEEDED);
            inventory.addItem(new Bow());
        }
    }
}
