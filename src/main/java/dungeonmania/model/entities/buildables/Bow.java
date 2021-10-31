package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.AttackEquipment;
import dungeonmania.model.entities.movings.Inventory;
import dungeonmania.util.Position;
public class Bow extends BuildableEquipment implements AttackEquipment {

    private static final int WOOD_NEEDED = 1;
    private static final int ARROWS_NEEDED = 3;

    public Bow() {
        super("bow", null);
    }

    @Override
    public boolean isBuildable(Inventory inventory) {
        return inventory.hasItemQuantity("wood", WOOD_NEEDED) && inventory.hasItemQuantity("arrow", ARROWS_NEEDED);
    }

    @Override
    public void craft(Inventory inventory) {
        if (isBuildable(inventory)) {
            inventory.removeItemQuantity("Wood", WOOD_NEEDED);
            inventory.removeItemQuantity("Arrows", ARROWS_NEEDED);
            inventory.addItem(new Bow());
        }
    }

    @Override
    public int setAttackMultiplier(int attackDamage) {
        // TODO Auto-generated method stub
        return 0;
    }
}
