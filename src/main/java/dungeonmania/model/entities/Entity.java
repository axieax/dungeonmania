package dungeonmania.model.entities;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity {

    private String entityId;
    private Position position;
    private boolean isInteractable;

    public Entity(String entityId, Position position) {
        this.entityId = entityId;
        this.position = position;
        this.isInteractable = false;
    }

    public Entity(String entityId, Position position, boolean isInteractable) {
        this(entityId, position);
        this.isInteractable = isInteractable;
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

    public EntityResponse getInfo() {
        return new EntityResponse(
            entityId,
            this.getClass().getSimpleName(),
            position,
            isInteractable
        );
    }
}
