package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class FloorSwitchTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());

        FloorSwitch floorSwitch = new FloorSwitch(new Position(1, 1));

        game.addEntity(floorSwitch);

        assertTrue(new Position(1, 1).equals(game.getEntity(floorSwitch.getId()).getPosition()));
    }

    /**
     * Test if FloorSwitch behaves like an empty square.
     */
    @Test
    public void floorSwitchEmptySquare() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        FloorSwitch floorSwitch = new FloorSwitch(new Position(1, 1));
        game.addEntity(floorSwitch);

        Player player = new Player(new Position(0, 1), mode.damageMultiplier());
        game.addEntity(player);

        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
        assertTrue(new Position(1, 1).equals(floorSwitch.getPosition()));

        player.move(game, Direction.UP);

        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(1, 1).equals(floorSwitch.getPosition()));
    }

    /**
     * Test if FloorSwitch is triggered by boulder and is untriggered.
     */
    @Test
    public void wallBlockEnemies() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        FloorSwitch floorSwitch = new FloorSwitch(new Position(2, 0));
        game.addEntity(floorSwitch);

        Player player = new Player(new Position(0, 0), mode.damageMultiplier());
        game.addEntity(player);

        Boulder boulder = new Boulder(new Position(1, 0));
        game.addEntity(boulder);

        // Move boulder on top of switch
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(floorSwitch.getPosition()));
        assertTrue(new Position(2, 0).equals(boulder.getPosition()));

        assertTrue(floorSwitch.isTriggered(game));

        // Untrigger the floor switch
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(2, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(floorSwitch.getPosition()));
        assertTrue(new Position(3, 0).equals(boulder.getPosition()));

        assertFalse(floorSwitch.isTriggered(game));
    }
}
