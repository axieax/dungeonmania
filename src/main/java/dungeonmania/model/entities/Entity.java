package dungeonmania.model.entities;

import dungeonmania.util.Position;

public abstract class Entity {
    private String entityId;
    private Position position;

    public Entity(String entityId, Position position) {
        this.entityId = entityId;
        this.position = position;
    }

    public String getEntityId() {
        return entityId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }
}
