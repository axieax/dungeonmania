package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Bomb;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.FloorSwitch;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class BombTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Bomb bomb = new Bomb(new Position(1, 1));
        game.addEntity(bomb);
        
        assertTrue(new Position(1, 1).equals(game.getEntity(bomb.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Bomb bomb = new Bomb(new Position(1, 1));
        game.addEntity(bomb);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(bomb.getId()) == null);
        assertTrue(player.getInventoryItem(bomb.getId()).equals(bomb));
    }

    /**
     * Test to place bomb
     */
    @Test
    public void placeBomb() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Bomb bomb = new Bomb(new Position(1, 1));
        game.addEntity(bomb);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);
        game.tick(null, Direction.RIGHT);

        assertTrue(player.getInventoryItem(bomb.getId()).equals(bomb));
        game.tick(null, Direction.RIGHT);
        // place bomb
        game.tick(bomb.getId(), Direction.NONE);
        assertTrue(game.getEntity(bomb.getId()) != null);
        Position pos = player.getPosition();
        assertEquals(game.getEntity(bomb.getId()).getPosition(), pos);

        // move away from the bomb. user cannot pass through bomb since it has been placed.
        game.tick(null, Direction.RIGHT);
        assertTrue(game.getEntity(bomb.getId()) != null);
        assertEquals(game.getEntity(bomb.getId()).getPosition(), pos);

        // bomb is not passable
        game.tick(null, Direction.LEFT);
        assertNotEquals(game.getEntity(bomb.getId()).getPosition(), player.getPosition());
    }

    /**
     * Test if bomb can trigger other bombs to be destroyed
     */
    @Test
    public void explodeBombToBombs() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Boulder boulder = new Boulder(new Position(1, 2));
        game.addEntity(boulder);

        Player player = new Player(new Position(0, 2));
        game.addEntity(player);

        FloorSwitch floorSwitch = new FloorSwitch(new Position(2, 2));
        game.addEntity(floorSwitch);

        Bomb bomb1 = new Bomb(new Position(2, 1));
        game.addEntity(bomb1);

        // create a wall on top of the bomb
        Wall wall1 = new Wall(new Position(1, 0));
        Wall wall2 = new Wall(new Position(2, 0));
        Wall wall3 = new Wall(new Position(3, 0));

        // create a wall to be destroyed by the chain of bombs
        Wall wall4 = new Wall(new Position(4, 1));
        Bomb bomb2 = new Bomb(new Position(3, 1));

        game.addEntity(wall1);
        game.addEntity(wall2);
        game.addEntity(wall3);
        game.addEntity(wall4);
        game.addEntity(bomb2);

        // get bomb
        game.tick(null, Direction.UP);
        game.tick(null, Direction.RIGHT);

        // place bomb
        game.tick(null, Direction.RIGHT);

        game.tick(null, Direction.RIGHT);
        game.tick(bomb2.getId(), Direction.NONE);

        // go to initial spawn spot
        game.tick(null, Direction.LEFT);
        game.tick(bomb1.getId(), Direction.NONE);
        game.tick(null, Direction.LEFT);
        game.tick(null, Direction.LEFT);
        game.tick(null, Direction.DOWN);

        // move boulder to trigger bomb explosion
        game.tick(null, Direction.RIGHT);

        // boulder explodes
        assertTrue(game.getEntity(wall1.getId()) == null);
        assertTrue(game.getEntity(wall2.getId()) == null);
        assertTrue(game.getEntity(wall3.getId()) == null);
        assertTrue(game.getEntity(wall4.getId()) == null);
        assertTrue(game.getEntity(bomb1.getId()) == null);
        assertTrue(game.getEntity(bomb2.getId()) == null);
        assertTrue(game.getEntity(player.getId()) != null);
    }
}