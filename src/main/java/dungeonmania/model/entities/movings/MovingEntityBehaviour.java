package dungeonmania.model.entities.movings;

import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface MovingEntityBehaviour {
    public void kill();
    
    public boolean isAlive();

    public Direction getDirection();

    public boolean isCollidable(Entity entity);

    public void moveTo(Position position);
}