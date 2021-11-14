package dungeonmania.model.entities.movings.movement;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Enemy;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.util.Position;
import java.util.Map;

public class FollowPlayerMovementState extends MovementState {

    public FollowPlayerMovementState(Enemy enemy) {
        super(enemy);
    }

    /**
     * Uses the Dijkstra's path finding algorithm to find the shortest path to the
     * player.
     *
     * @param game dungeon
     * @return Position to go to next
     */
    @Override
    public Position findNextPosition(Game game) {
        Player player = (Player) game.getPlayer();

        Position optimalPathPosition = this.getEnemy().getPosition();

        PositionGraph positionGraph = new PositionGraph(game, this.getEnemy());

        // Move the mercenary to the closest possible position to the player
        Map<Integer, Position> prev = positionGraph.dijkstra(this.getEnemy().getPosition());

        // traverse back path
        Position pos = prev.get(player.getPosition().hashCode());
        if (pos != null && pos.equals(optimalPathPosition)) {
            optimalPathPosition = player.getPosition();
        } else {
            Position curr = prev.get(player.getPosition().hashCode());
            while (curr != null && !this.getEnemy().getPosition().equals(curr)) {
                optimalPathPosition = curr;
                curr = prev.get(curr.hashCode());
            }
        }

        return optimalPathPosition;
    }
}
