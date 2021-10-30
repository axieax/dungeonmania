package dungeonmania.model.entities;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.movings.MovingEntityBehaviour;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Entity {

    private String id;
    private Position position;
    private boolean interactable;
    private boolean passable;

    public boolean isInteractable() {
        return interactable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public Entity(String id, Position position) {
        this.id = id;
        this.position = position;
        this.interactable = false;
        this.passable = false;
    }

    public Entity(String id, Position position, boolean interactable, boolean passable) {
        this(id, position);
        this.interactable = interactable;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public Position getOffsetPosition(Direction direction) {
        return this.position.translateBy(direction);
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
            interactable
        );
    }

    public abstract void interact(Dungeon dungeon, MovingEntityBehaviour character);
}
