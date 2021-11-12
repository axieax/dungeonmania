package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Inventory;

public interface Buildable {
    
    /**
     * Check if the equipment is buildable.
     * @param inventory
     * @return true if the player has enough resources to build the object.
     */
    public abstract boolean isBuildable(Inventory inventory);

    /**
     * Check if there are any zombies currently in the dungeon.
     * @param game
     * @param inventory
     * @return true if there are no zombies in the dungeon.
     */
    public abstract boolean checkNoZombies(Game game, Inventory inventory);

    /**
     * Remove the resources used for building from the player's inventory.
     * @param inventory
     */
    public abstract void craft(Inventory inventory);

    /**
     * Create a copy of the object for the Entity Factory.
     * @return an instance of the equipment
     */
    public abstract Buildable clone();
}
