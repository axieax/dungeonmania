package dungeonmania.model.entities;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity {

    private String id;
    private Position position;
    private boolean isInteractable;

    public Entity(String id, Position position) {
        this.id = id;
        this.position = position;
        this.isInteractable = false;
    }

    public Entity(String id, Position position, boolean isInteractable) {
        this(id, position);
        this.isInteractable = isInteractable;
    }

    public String getId() {
        return id;
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
            id,
            this.getClass().getSimpleName(),
            position,
            isInteractable
        );
    }
}
