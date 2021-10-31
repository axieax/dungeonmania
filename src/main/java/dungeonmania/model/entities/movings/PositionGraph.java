package dungeonmania.model.entities.movings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Position;

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
        
        while (!positionsToEvaluate.isEmpty()) {
            Position currPosition = positionsToEvaluate.remove();
            List<Position> moveToPositions = game.getMoveablePositions(entity, currPosition);
            for (Position currMoveToPosition: moveToPositions) {
                freePositions.add(currMoveToPosition);
                positionsToEvaluate.add(currMoveToPosition);
            }
            positionsToEvaluate.remove(currPosition);
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

        // Breadth First Search Algorithm to find shortest path length
        while (!queue.isEmpty()) {
            Position vertex = queue.remove();
            List<Position> adjacentPositions = game.getCardinallyAdjacentMoveablePositions(this.entity, vertex);

            for (Position currNode: adjacentPositions) {
                if (visited.get(currNode)) {
                    visited.put(currNode, true);
                    dist.put(currNode, dist.get(currNode) + 1);
                    pred.put(currNode, vertex);

                    if (currNode == dest) return dist.get(currNode);
                }
            }
        }
        return -1;
    }
}
