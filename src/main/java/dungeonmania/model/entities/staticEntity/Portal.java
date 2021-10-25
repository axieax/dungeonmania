package dungeonmania.model.entities.staticEntity;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

public class Portal extends Entity {

    private String colour;

    public Portal(String entityId, Position position, String colour) {
        super(entityId, position);
        this.colour = colour;
    }}