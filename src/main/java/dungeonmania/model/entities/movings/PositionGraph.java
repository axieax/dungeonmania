package dungeonmania.model.entities.movings;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PositionGraph {

    private Game game;
    private List<Position> nodes;
    private Entity entity;

    public PositionGraph(Game game, Entity entity) {
        this.game = game;
        this.entity = entity;
        this.nodes = this.getAllFreePositions();
    }

    /**
     * Gets all free positions that the entity can can go to
     *
     * @param game
     * @param entity
     * @return
     */
    private List<Position> getAllFreePositions() {
        LinkedList<Position> positionsToEvaluate = new LinkedList<>();
        LinkedList<Position> freePositions = new LinkedList<>();

        positionsToEvaluate.add(entity.getPosition());
        freePositions.add(entity.getPosition());

        if (entity instanceof MovingEntity) {
            int i = 0;
            while (i < positionsToEvaluate.size()) {
                Position currPosition = positionsToEvaluate.get(i);
                List<Position> moveToPositions = game.getMoveablePositions(
                    (MovingEntity) entity,
                    currPosition
                );
                for (Position currMoveToPosition : moveToPositions) {
                    if (!positionsToEvaluate.contains(currMoveToPosition)) {
                        freePositions.add(currMoveToPosition);
                        positionsToEvaluate.add(currMoveToPosition);
                    }
                }
                i++;
            }
        }

        return freePositions;
    }

    /**
     * Returns the shortest path length from src to dest using BFS algorithm.
     *
     * @param src Position
     * @param dest Position
     * @return
     */
    public int BFS(Position src, Position dest) {
        LinkedList<Position> queue = new LinkedList<Position>();

        Map<Position, Boolean> visited = new HashMap<>();
        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> pred = new HashMap<>();

        for (Position currNode : nodes) {
            visited.put(currNode, false);
            dist.put(currNode, Integer.MAX_VALUE);
            pred.put(currNode, null);
        }

        visited.put(src, true);
        dist.put(src, 0);
        queue.add(src);

        if (entity instanceof MovingEntity) {
            // Breadth First Search Algorithm to find shortest path length
            while (!queue.isEmpty()) {
                Position vertex = queue.remove();
                List<Position> adjacentPositions = game.getMoveablePositions(
                    (MovingEntity) this.entity,
                    vertex
                );

                for (Position currNode : adjacentPositions) {
                    if (!visited.get(currNode)) {
                        visited.put(currNode, true);
                        dist.put(currNode, dist.get(vertex) + 1);
                        pred.put(currNode, vertex);
                        queue.add(currNode);
                        if (currNode.equals(dest)) return dist.get(currNode);
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }
}
