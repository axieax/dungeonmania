package dungeonmania.model.entities;

import java.util.UUID;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Entity {

    private String id;
    private Position position;
    private boolean interactable;
    private boolean passable;
    private String prefix;

    public Entity(String prefix, Position position) {
        this.prefix = prefix;
        this.id = UUID.randomUUID().toString();
        this.position = position;
        this.interactable = false;
        this.passable = false;
    }

    public Entity(String prefix, Position position, boolean interactable, boolean passable) {
        this(prefix, position);
        this.interactable = interactable;
        this.passable = passable;
    }

    public String getPrefix() {
        return prefix;
    }

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

    public abstract void interact(Game game, MovingEntity character);
}
