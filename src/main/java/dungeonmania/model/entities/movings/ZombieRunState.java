package dungeonmania.model.entities.movings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieRunState implements ZombieState {
    private ZombieToast zombie;

    public ZombieRunState(ZombieToast zombie) {
        this.zombie = zombie;
    }

    @Override
    public void move(Game game) {
        Position currPos = zombie.getPosition();

        List<Direction> possibleDirections = Arrays.asList(
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
        );

        List<Position> possiblePositionsToMove = game.getMoveablePositions(this.zombie);

        int optimalPathLength = -1;
        Position optimalPathPosition;

        PositionGraph positionGraph = new PositionGraph(game, this.zombie);

        for (Position position: possiblePositionsToMove) {
            int pathLen = positionGraph.BFS(this.zombie.getPosition(), game.getCharacter().getPosition());
            if (pathLen > optimalPathLength) {
                optimalPathLength = pathLen;
                optimalPathPosition = position;
            }
        }

        if (optimalPathPosition != null) this.zombie.setPosition(optimalPathPosition);
    }

}
