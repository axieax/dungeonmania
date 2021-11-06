package dungeonmania.model.entities.movings.movement;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CircularMovementState implements MovementState {

    private MovingEntity enemy;
    private boolean initialMovement;
    private List<Direction> circularMovementPath;
    private boolean reverseMovement;
    private int indexOfNextMove;

    public CircularMovementState(MovingEntity enemy) {
        this.enemy = enemy;
        this.initialMovement = true;
        // Default "circling" movement
        this.circularMovementPath =
            Arrays.asList(
                Direction.RIGHT,
                Direction.DOWN,
                Direction.DOWN,
                Direction.LEFT,
                Direction.LEFT,
                Direction.UP,
                Direction.UP,
                Direction.RIGHT
            );
        // index of circularMovementPath
        this.indexOfNextMove = 0;
        this.reverseMovement = false;
    }

    /**
     *
     * @param game dungeon the entity is contained in
     */
    private void initialCircularMove(Game game) {
        // initially always move up if possible, else stay in spot
        Position newPos = enemy.getPosition().translateBy(Direction.UP);
        List<Entity> entitiesAtNewPos = game.getEntities(newPos);
        boolean canMove = entitiesAtNewPos.stream().allMatch(e -> !enemy.collision(e));
        if (canMove) {
            enemy.setPosition(newPos);
            this.initialMovement = false;
        }
    }

    /**
     * Enemy runs away from player
     */
    @Override
    public void move(Game game) {
        if (initialMovement) initialCircularMove(game);
        else moveCircular(game);

        Player player = (Player) game.getCharacter();
        if (player.getPosition().equals(enemy.getPosition())){
            player.battle(game, enemy);
        }
    }

    /**
     * Given the current position of the entity, moves onto the next tile,
     * ensuring that the entity maintains a "circular path" and reverses direction
     * if necessary.
     * @param game dungeon the entity is contained in
     */
    private void moveCircular(Game game) {
        Direction nextMoveInPath = circularMovementPath.get(indexOfNextMove);

        Position newPos = enemy.getPosition().translateBy(nextMoveInPath);
        List<Entity> entitiesAtNewPos = game.getEntities(newPos);
        boolean canMove = entitiesAtNewPos.stream().allMatch(e -> !enemy.collision(e));
        if (canMove) {
            enemy.setPosition(newPos);
        } else { // reverse direction
            this.reverseMovement = !this.reverseMovement;
            indexOfNextMove =  Math.abs(circularMovementPath.size() - (indexOfNextMove + 4)) % 8;
            Collections.reverse(this.circularMovementPath);
            return; // no movement occurs if blocked
        }

        if (indexOfNextMove >= circularMovementPath.size() - 1) { // end of movement path
            indexOfNextMove = 0;
        } else {
            indexOfNextMove += 1;
        }
    }
}
