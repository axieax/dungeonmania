package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface MovingEntityBehaviour {
    
    /**
     * Move to the specified direction.
     * @param direction
     */
    public void move(Direction direction);

    /**
     * Move to the specified position.
     * @param position
     */
    public void moveTo(Position position);

    /**
     * Simulates a battle with the entity
     * @param entity
     */
    public void battle(MovingEntityBehaviour entity);


    /**
     * Indicates whether this character should be terminated i.e. killed.
     */
    public void terminate();

    /**
     * Checks if this character is alive.
     * @return
     */
    public boolean isAlive();

    /**
     * Gets the direction that this entity is heading towards.
     * @return Direction
     */
    public Direction getDirection();

    /**
     * Determines if the character can go through the given entity. i.e. is passable.
     * @param entity
     * @return
     */
    public boolean isCollidable(Entity entity);
}

