package dungeonmania.model.entities.buildables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.player.Inventory;

public interface Buildable {
    
    /**
     * Check if the equipment is buildable.
     * @param inventory
     * @return true if all the requirements to build the equipment are met.
     */
    public abstract boolean isBuildable(Game game, Inventory inventory);

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
