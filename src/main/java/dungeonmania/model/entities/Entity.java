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
    private String type;

    public Entity(String prefix, Position position) {
        this.type = prefix;
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

    /**
     * Get the type of an Entity
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Get the interactable status of an Entity
     *
     * @return true if interactable, false otherwise`
     */
    public boolean isInteractable() {
        return interactable;
    }

    /**
     * Get the passable status of an Entity
     *
     * @return true if passable, false otherwise
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Set the passable status of an Entity
     *
     * @param true if passable, false otherwise
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * Get the id of an Entity
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Get the position of an Entity
     *
     * @return entity's position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the position of an Entity translated by a given Direction
     *
     * @param direction direction to translate an Entity
     *
     * @return entity's translated position
     */
    public Position getOffsetPosition(Direction direction) {
        return this.position.translateBy(direction);
    }

    /**
     * Set the position of an Entity
     *
     * @param position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Get x-coordinate of an Entity
     *
     * @return x-coordinate
     */
    public int getX() {
        return this.position.getX();
    }

    /**
     * Get y-coordinate of an Entity
     *
     * @return y-coordinate
     */
    public int getY() {
        return this.position.getY();
    }

    /**
     * Get EntityResponse of an Entity
     *
     * @return EntityResponse
     */
    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, this.getType(), position, interactable);
    }

    /**
     * Get animation of an Entity
     *
     * @return AnimationQueue
     */
    public AnimationQueue getAnimation() {
        return null;
    }

    /**
     * JSONObject representation of an Entity
     *
     * @return JSONObject representation
     */
    public JSONObject toJSON() {
        JSONObject entity = new JSONObject();
        entity.put("x", (position != null) ? getX() : 0);
        entity.put("y", (position != null) ? getY() : 0);
        entity.put("type", getType());
        return entity;
    }

    /**
     * Interacts witth a given entity
     *
     * @param game game state
     * @param character to interact with
     */
    public abstract void interact(Game game, Entity character);
}
