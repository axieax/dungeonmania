package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.DefenceEquipment;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Inventory;

public class MidnightArmour extends DefenceEquipment implements Buildable {

    private static final int ARMOUR_NEEDED = 1;
    private static final int SUNSTONE_NEEDED = 1;

    private static final double DEFENCE_MULTIPLIER = 0.2;
    public static final int BONUS_ATTACK_DAMAGE = 15;

    public MidnightArmour() {
        super("midnight_armour", DEFENCE_MULTIPLIER);
    }

    @Override
    public int getBonusAttackDamage() {
        return BONUS_ATTACK_DAMAGE;
    }

    public boolean isBuildable(Game game, Inventory inventory) {
        // Midnight armour can only be built if there are no zombies currently in the dungeon
        return (
            !(game.getEntities().stream().anyMatch(entity -> entity instanceof ZombieToast)) &&
            inventory.hasItemQuantity("armour", ARMOUR_NEEDED) &&
            inventory.hasItemQuantity("sun_stone", SUNSTONE_NEEDED)
        );
    }

    public void craft(Inventory inventory) {
        inventory.removeItemQuantity("armour", ARMOUR_NEEDED);
        inventory.removeItemQuantity("sun_stone", SUNSTONE_NEEDED);
        inventory.addItem(new MidnightArmour());
    }

    public Buildable clone() {
        return new MidnightArmour();
    }
}
