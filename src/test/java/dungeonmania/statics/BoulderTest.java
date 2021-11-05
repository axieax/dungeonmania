package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class BoulderTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Boulder boulder = new Boulder(new Position(1, 1));
        game.addEntity(boulder);

        assertTrue(new Position(1, 1).equals(game.getEntity(boulder.getId()).getPosition()));
    }

    /**
     * Test if player interacts with the boulder. If player moves onto the boulder,
     * the boulder should also move.
     */
    @Test
    public void boulderMoveByPlayer() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Boulder boulder = new Boulder(new Position(1, 1));
        game.addEntity(boulder);

        Player player = new Player(new Position(2, 1));

        game.addEntity(player);
        player.move(game, Direction.LEFT);

        // Player and boulder should both move to the left by one 
        assertTrue(new Position(0, 1).equals(game.getEntity(boulder.getId()).getPosition()));
        assertTrue(new Position(1, 1).equals(player.getPosition()));
    }

    /**
     * Test that the player cannot push more than one boulder at once.
     */
    @Test
    public void boulderBlocksBoulderMovement() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Boulder boulder1 = new Boulder(new Position(1, 1));
        Boulder boulder2 = new Boulder(new Position(2, 1));
        game.addEntity(boulder1);
        game.addEntity(boulder2);

        Player player = new Player(new Position(3, 1));

        game.addEntity(player);
        player.move(game, Direction.LEFT);

        // Since there are two boulders next to each other
        // Neither the player nor boulders should move
        assertTrue(new Position(1, 1).equals(game.getEntity(boulder1.getId()).getPosition()));
        assertTrue(new Position(2, 1).equals(game.getEntity(boulder2.getId()).getPosition()));
        assertTrue(new Position(3, 1).equals(player.getPosition()));
    }

    /**
     * Test boulder move onto a switch and explode bombs
     */
    @Test
    public void boulderExplodeBombs() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Boulder boulder = new Boulder(new Position(1, 2));
        game.addEntity(boulder);

        Player player = new Player(new Position(0, 2));
        game.addEntity(player);

        FloorSwitch floorSwitch = new FloorSwitch(new Position(2, 2));
        game.addEntity(floorSwitch);

        Bomb bomb = new Bomb(new Position(1, 2));
        game.addEntity(bomb);

        // create a wall on top of the bomb
        Wall wall1 = new Wall(new Position(1, 0));
        Wall wall2 = new Wall(new Position(2, 0));
        Wall wall3 = new Wall(new Position(3, 0));

        game.addEntity(wall1);
        game.addEntity(wall2);
        game.addEntity(wall3);

        // assert that walls exists
        assertTrue(game.getEntity(wall1.getId()) != null);
        assertTrue(game.getEntity(wall2.getId()) != null);
        assertTrue(game.getEntity(wall3.getId()) != null);

        game.tick("", Direction.RIGHT);

        // boulder explodes
        assertTrue(game.getEntity(wall1.getId()) == null);
        assertTrue(game.getEntity(wall2.getId()) == null);
        assertTrue(game.getEntity(wall3.getId()) == null);
        assertTrue(game.getEntity(bomb.getId()) == null);
        assertTrue(game.getEntity(player.getId()) != null);
    }
}
