package dungeonmania.model.entities.buildables;

import dungeonmania.model.entities.movings.Inventory;

public interface Buildable {
    
    public abstract boolean isBuildable(Inventory inventory);

    public abstract void craft(Inventory inventory);

    public abstract Buildable clone();
}
