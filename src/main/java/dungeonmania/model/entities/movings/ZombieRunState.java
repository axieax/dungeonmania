package dungeonmania.model.entities.movings;

import java.util.Arrays;
import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieRunState implements ZombieState {
    private ZombieToast zombie;

    public ZombieRunState(ZombieToast zombie) {
        this.zombie = zombie;
    }

    @Override
    public void move(Dungeon dungeon) {
        Position currPos = zombie.getPosition();

        List<Direction> possibleDirections = Arrays.asList(
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
        );

        // TODO:
        // use BFS with destination as position of player where the x and y coordinates are swapped
    }
}
