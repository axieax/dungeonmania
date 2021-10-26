package dungeonmania.model.entities.characters;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public abstract class Character extends Entity {

    public Character(String entityId, Position position) {
        super(entityId, position);
        //TODO Auto-generated constructor stub
    }

    public Character(String entityId, Position position, boolean isInteractable) {
        super(entityId, position, isInteractable);
        //TODO Auto-generated constructor stub
    }
    // Health & Attack attributes are common between the player and the enemies
}
