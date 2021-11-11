package dungeonmania.model.entities.movings.movement;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.List;
import java.util.Map;

public class AttackMovementState implements MovementState {

    private Enemy enemy;

    public AttackMovementState(Enemy enemy) {
        this.enemy = enemy;
    }

    /**
     * Enemy runs away from player
     */
    @Override
    public void move(Game game) {
        Player player = (Player) game.getCharacter();

        Position optimalPathPosition = enemy.getPosition();

        PositionGraph positionGraph = new PositionGraph(game, enemy);

        // Move the mercenary to the closest possible position to the player
        Map<Integer, Position> prev = positionGraph.dijkstra(enemy.getPosition());
        
        // traverse back path
        Position pos = prev.get(player.getPosition().hashCode());
        if (pos != null && pos.equals(optimalPathPosition)) {
            optimalPathPosition = player.getPosition();
        } else {
            Position curr = prev.get(player.getPosition().hashCode());
            while (curr != null && !enemy.getPosition().equals(curr)) {
                optimalPathPosition = curr;
                curr = prev.get(curr.hashCode());
            }
        }

        Position offset = Position.calculatePositionBetween(
            enemy.getPosition(),
            optimalPathPosition
        );
        if (Direction.LEFT.getOffset().equals(offset)) enemy.setDirection(Direction.LEFT);
        else if (Direction.UP.getOffset().equals(offset)) enemy.setDirection(Direction.UP);
        else if (Direction.RIGHT.getOffset().equals(offset)) enemy.setDirection(Direction.RIGHT);
        else if (Direction.DOWN.getOffset().equals(offset)) enemy.setDirection(Direction.DOWN);
        else enemy.setDirection(Direction.NONE);
        
        // Interact with all entities in that direction
        List<Entity> entities = game.getEntities(enemy.getPosition().translateBy(enemy.getDirection()));
        entities.forEach(entity -> entity.interact(game, enemy));

        if (!enemy.getDirection().equals(Direction.NONE)) {
            enemy.setPosition(enemy.getPosition().translateBy(enemy.getDirection()));
        }   
    }
}
