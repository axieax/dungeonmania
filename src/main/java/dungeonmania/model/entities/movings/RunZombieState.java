package dungeonmania.model.entities.movings;

import java.util.Arrays;
import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RunZombieState implements ZombieState {
    ZombieToast zombie;

    public RunZombieState(ZombieToast zombie) {
        this.zombie = zombie;
    }

    @Override
    public void move(Dungeon dungeon) {
        Position currPos = zombie.getPosition();

        List<Direction> possibleDirections = Arrays.asList(
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
        );

        // either keep the same distance or increase the distance
        // from the player (always preferring to increase distance)
    }
}
