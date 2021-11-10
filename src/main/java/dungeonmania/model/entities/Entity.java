package dungeonmania.model.entities;

import dungeonmania.model.Game;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.UUID;
import org.json.JSONObject;

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

    public String getType() {
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

    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, this.getType(), position, interactable);
    }

    public AnimationQueue getAnimation() {
        return null;
    }

    public JSONObject toJSON() {
        JSONObject entity = new JSONObject();
        entity.put("x", (position != null) ? getX() : 0);
        entity.put("y", (position != null) ? getY() : 0);
        entity.put("type", getType());
        return entity;
    }

    public abstract void interact(Game game, Entity character);
}
