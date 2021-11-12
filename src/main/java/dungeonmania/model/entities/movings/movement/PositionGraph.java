package dungeonmania.model.entities.movings.movement;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.util.Position;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PositionGraph {

    private Game game;
    private List<Position> nodes;
    private MovingEntity entity;

    public PositionGraph(Game game, MovingEntity entity) {
        this.game = game;
        this.entity = entity;
        this.nodes = this.getAllFreePositions();
    }

    /**
     * Gets all free positions that the moving entity can can go to.
     *
     * @param game
     * @param entity
     * @return List<Position> all free position nodes on the dungeon map
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
     * @return int shortest path length from src to dest
     */
    public int bfs(Position src, Position dest) {
        if (src.equals(dest)) return 0;

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

        // Breadth First Search Algorithm to find shortest path length
        while (!queue.isEmpty()) {
            Position vertex = queue.remove();
            List<Position> adjacentPositions = game.getMoveablePositions(
                (MovingEntity) this.entity,
                vertex
            );

            for (Position currNode : adjacentPositions) {
                if (visited.get(currNode) != null && !visited.get(currNode)) {
                    visited.put(currNode, true);
                    dist.put(currNode, dist.get(vertex) + 1);
                    pred.put(currNode, vertex);
                    queue.add(currNode);
                    if (currNode.equals(dest)) return dist.get(currNode);
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Performs the Dijkstra's algorithm to find the shortest costing path.
     * 
     * @param src
     * @return HashMap<Integer, Position> pred that gives us the shortest path
     * to traverse from. The key in the HashMap is the hashcode of the position.
     */
    public HashMap<Integer, Position> dijkstra(Position src) {
        Queue<Position> queue = new LinkedList<Position>();

        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer, Position> pred = new HashMap<>();

        for (Position currNode : nodes) {
            dist.put(currNode.hashCode(), Integer.MAX_VALUE);
            pred.put(currNode.hashCode(), null);
        }

        dist.put(src.hashCode(), 0);
        queue.add(src);

        while (!queue.isEmpty()) {
            Position vertex = queue.remove();
            List<Position> adjacentPositions = game.getMoveablePositions(
                (MovingEntity) this.entity,
                vertex
            );

            for (Position currNode : adjacentPositions) {
                if (dist.get(vertex.hashCode()) != null && dist.get(currNode.hashCode()) != null) {
                    int cost = 1;
                    if (game.getSwampTile(currNode) != null) cost =
                        game.getSwampTile(currNode).getMovementFactor();
                    if (dist.get(vertex.hashCode()) + cost < dist.get(currNode.hashCode())) {
                        dist.put(currNode.hashCode(), dist.get(vertex.hashCode()) + cost);
                        pred.put(currNode.hashCode(), vertex);
                        queue.add(currNode);
                    }
                }
            }
        }
        return pred;
    }
}
