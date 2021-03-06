package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.TestHelpers;
import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Boulder boulder = new Boulder(new Position(1, 1));
        game.addEntity(boulder);

        Player player = new Player(new Position(2, 1), mode.initialHealth());

        game.addEntity(player);
        player.move(game, Direction.LEFT);

        // Player and boulder should both move to the left by one
        assertTrue(new Position(0, 1).equals(game.getEntity(boulder.getId()).getPosition()));
        assertTrue(new Position(1, 1).equals(player.getPosition()));
    }

    /**
     * Test if mercenary interacts with the boulder, nothing will happen
     */
    @Test
    public void boulderInteractByMercenary() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Boulder boulder = new Boulder(new Position(1, 1));
        game.addEntity(boulder);

        Player player = new Player(new Position(5, 1), mode.initialHealth());
        game.addEntity(player);

        Mercenary mercenary = new Mercenary(new Position(0, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);
        mercenary.interact(game, boulder);

        assertTrue(new Position(1, 1).equals(game.getEntity(boulder.getId()).getPosition()));

        // Since player is directly to the right of the mercenary, it would theoretically move right
        // However, the boulder is blocking the path, so it should not move there
        game.tick(null, Direction.NONE);
        assertTrue(
            new Position(0, 0).equals(game.getEntity(mercenary.getId()).getPosition()) ||
            new Position(0, 2).equals(game.getEntity(mercenary.getId()).getPosition())
        );
    }

    /**
     * Test that the player cannot push more than one boulder at once.
     */
    @Test
    public void boulderBlocksBoulderMovement() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Boulder boulder1 = new Boulder(new Position(1, 1));
        Boulder boulder2 = new Boulder(new Position(2, 1));
        game.addEntity(boulder1);
        game.addEntity(boulder2);

        Player player = new Player(new Position(3, 1), mode.initialHealth());

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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Boulder boulder = new Boulder(new Position(1, 2));
        game.addEntity(boulder);

        Player player = new Player(new Position(0, 2), mode.initialHealth());
        game.addEntity(player);

        FloorSwitch floorSwitch = new FloorSwitch(new Position(2, 2));
        game.addEntity(floorSwitch);

        Bomb bomb = new Bomb(new Position(2, 1));
        game.addEntity(bomb);

        // Create a wall on top of the bomb
        Wall wall1 = new Wall(new Position(1, 0));
        Wall wall2 = new Wall(new Position(2, 0));
        Wall wall3 = new Wall(new Position(3, 0));

        game.addEntity(wall1);
        game.addEntity(wall2);
        game.addEntity(wall3);

        // Check that walls exist
        assertTrue(game.getEntity(wall1.getId()) != null);
        assertTrue(game.getEntity(wall2.getId()) != null);
        assertTrue(game.getEntity(wall3.getId()) != null);

        // Get bomb
        game.tick(null, Direction.UP);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);

        // Place bomb
        game.tick(bomb.getId(), Direction.NONE);

        // Go to initial spawn spot
        game.tick(null, Direction.LEFT);
        game.tick(null, Direction.LEFT);
        game.tick(null, Direction.DOWN);

        // Move boulder to trigger bomb explosion
        game.tick(null, Direction.RIGHT);

        // Boulder explodes
        assertTrue(game.getEntity(wall1.getId()) == null);
        assertTrue(game.getEntity(wall2.getId()) == null);
        assertTrue(game.getEntity(wall3.getId()) == null);
        assertTrue(game.getEntity(bomb.getId()) == null);
        assertTrue(game.getEntity(boulder.getId()) == null);
        assertTrue(game.getEntity(floorSwitch.getId()) == null);
        assertTrue(game.getEntity(player.getId()) != null);
    }

    /**
     * Test player in blast radius of bombs
     */
    @Test
    public void boulderExplodePlayer() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Boulder boulder = new Boulder(new Position(1, 1));
        game.addEntity(boulder);

        Player player = new Player(new Position(1, 0), mode.initialHealth());
        game.addEntity(player);

        FloorSwitch floorSwitch = new FloorSwitch(new Position(1, 2));
        game.addEntity(floorSwitch);

        Position bombPos = new Position(2, 2);
        Bomb bomb = new Bomb(bombPos);
        game.addEntity(bomb);

        // Create a wall
        Wall wall1 = new Wall(new Position(3, 1));

        game.addEntity(wall1);

        // Get bomb
        game.tick(null, Direction.RIGHT);
        TestHelpers.gameTickMovement(game, Direction.DOWN, 2);

        // Place bomb
        game.tick(bomb.getId(), Direction.NONE);

        // Go to initial spawn spot
        TestHelpers.gameTickMovement(game, Direction.UP, 2);
        game.tick(null, Direction.LEFT);

        // Move boulder to trigger bomb explosion
        game.tick(null, Direction.DOWN);

        // Boulder explodes
        assertTrue(game.getEntity(wall1.getId()) == null);
        assertTrue(game.getEntity(bomb.getId()) == null);
        assertTrue(game.getEntity(boulder.getId()) == null);
        assertTrue(game.getEntity(floorSwitch.getId()) == null);
        assertTrue(game.getEntity(player.getId()) != null);
        assertTrue(Position.isAdjacent(bombPos, player.getPosition()));
    }
}
