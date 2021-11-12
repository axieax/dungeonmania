package dungeonmania.model.entities.movings.movement;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;

public abstract class MovementState {
    private Enemy enemy;

    public MovementState(Enemy enemy) {
        this.enemy = enemy;
    }

    
    /** 
     * Get enemy attribute.
     * 
     * @return Enemy
     */
    public Enemy getEnemy() {
        return enemy;
    }

    
    /** 
     * Performs the necessary actions of a movement.
     * 
     * @param game
     */
    public void move(Game game) {
        Position optimalPathPosition = this.findNextPosition(game);
        this.setEnemyDirection(optimalPathPosition);
        this.interact(game);
        this.updatePosition();
    }


    /** 
     * Given a new position, find and set the direction that the entity is going to.
     * @param newPos
     */
    public void setEnemyDirection(Position newPos) {
        Position offset = Position.calculatePositionBetween(
            enemy.getPosition(),
            newPos
        );
        if (Direction.LEFT.getOffset().equals(offset)) enemy.setDirection(Direction.LEFT);
        else if (Direction.UP.getOffset().equals(offset)) enemy.setDirection(Direction.UP);
        else if (Direction.RIGHT.getOffset().equals(offset)) enemy.setDirection(Direction.RIGHT);
        else if (Direction.DOWN.getOffset().equals(offset)) enemy.setDirection(Direction.DOWN);
        else enemy.setDirection(Direction.NONE);
    }

    
    /** 
     * Interacts with all entities at the current entity position.
     * 
     * @param game
     */
    public void interact(Game game) {
        // Interact with all entities in that direction
        List<Entity> entities = game.getEntities(enemy.getPosition().translateBy(enemy.getDirection()));
        entities.forEach(entity -> entity.interact(game, enemy));
    }

    /**
     * Update the position of the entity.
     */
    public void updatePosition() {
        if (!enemy.getDirection().equals(Direction.NONE)) {
            enemy.setPosition(enemy.getPosition().translateBy(enemy.getDirection()));
        }
    }

    /**
     * Finds the next availiable position that the entity can make.
     * 
     * @param game
     * @return
     */
    public abstract Position findNextPosition(Game game);
}
