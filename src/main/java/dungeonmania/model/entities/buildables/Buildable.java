package dungeonmania.model.entities.buildables;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.entities.movings.player.Inventory;

public interface Buildable {
    
    public abstract boolean isBuildable(Inventory inventory);

    public abstract void craft(Inventory inventory);

    public abstract Buildable clone();
}
