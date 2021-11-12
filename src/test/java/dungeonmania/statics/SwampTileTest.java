package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.SwampTile;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;


public class SwampTileTest {
    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        SwampTile swampTile = new SwampTile(new Position(1, 1), 2);
        game.addEntity(swampTile);

        assertTrue(new Position(1, 1).equals(game.getEntity(swampTile.getId()).getPosition()));
    }

    /**
     * Test if swamp time does not affect player movement
     */
    @Test
    public void noEffectPlayer() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        SwampTile swampTile = new SwampTile(new Position(1, 1), 5);
        game.addEntity(swampTile);
        Player player = new Player(new Position(0, 1));
        game.addEntity(player);

        // player move right to swamp tile
        game.tick(null, Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
        assertTrue(swampTile.getPosition().equals(player.getPosition()));

        // player move right to leave swamp tile
        game.tick(null, Direction.RIGHT);
        assertTrue(new Position(2, 1).equals(player.getPosition()));
    }

    /**
     * Test if swamp tile has affect to spiders.
     */
    @Test
    public void hasEffectSpiders() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(10, 10));
        game.addEntity(player);

        // create a 9x9 swamp tiles
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game.addEntity(new SwampTile(new Position(i, j), 2));
            }
        }

        // create spider in middle of swamp tile
        Spider spider = new Spider(new Position(1, 1), mode.damageMultiplier());
        game.addEntity(spider);

        // takes two ticks for spider to move
        game.tick(null, Direction.NONE);
        assertEquals(new Position(1, 1), spider.getPosition());

        // spider moves up
        game.tick(null, Direction.NONE);
        assertEquals(new Position(1, 0), spider.getPosition());

        // takes two ticks for spider to move
        game.tick(null, Direction.NONE);
        assertEquals(new Position(1, 0), spider.getPosition());

        // spider moves right
        game.tick(null, Direction.NONE);
        assertEquals(new Position(2, 0), spider.getPosition());
    }

    /**
     * Test if swamp tile has affect to zombie.
     */
    @Test
    public void hasEffectZombies() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(10, 10));
        game.addEntity(player);

        // create a 9x9 swamp tiles
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game.addEntity(new SwampTile(new Position(i, j), 2));
            }
        }

        // create spider in middle of swamp tile
        ZombieToast zombie = new ZombieToast(new Position(1, 1), mode.damageMultiplier(), player);
        game.addEntity(zombie);

        // takes two ticks for zombie to move
        game.tick(null, Direction.NONE);
        assertEquals(new Position(1, 1), zombie.getPosition());

        // zombie move random direction
        game.tick(null, Direction.NONE);
        assertNotEquals(new Position(1, 0), zombie.getPosition());
    }

    /**
     * Test if swamp tile has high movement factor.
     */
    @Test
    public void testHighMovementFactor() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(10, 10));
        game.addEntity(player);

        // create a 9x9 swamp tiles
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game.addEntity(new SwampTile(new Position(i, j), 99));
            }
        }

        // create spider in middle of swamp tile
        Spider spider = new Spider(new Position(1, 1), mode.damageMultiplier());
        game.addEntity(spider);

        // takes 99 ticks for spider to move
        for (int i = 1; i < 99; i++) {
            game.tick(null, Direction.NONE);
            assertEquals(new Position(1, 1), spider.getPosition());
        }

        // on the 100th tick spider should be able to move up
        game.tick(null, Direction.NONE);
        assertEquals(new Position(1, 0), spider.getPosition());
    }

    /**
     * Test if mercenary pathfinding takes into account swamp tiles
     */
    @Test
    public void swampPathFinding() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        
        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        // create a 100 movement factor swamp tile below player
        SwampTile swampTile = new SwampTile(new Position(1, 2), 100);
        game.addEntity(swampTile);

        // create mercenary below swamp tile
        Mercenary mercenary = new Mercenary(new Position(1, 3), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        game.tick(null, Direction.NONE);

        // check if mercenary did not go to swamp tile but rather around it
        assertNotEquals(swampTile.getPosition(), mercenary.getPosition());
        assertTrue(mercenary.getPosition().equals(new Position(0, 3)) || mercenary.getPosition().equals(new Position(2, 3)));

        // mercenary should be adjacent to swamp tile
        game.tick(null, Direction.NONE);
        assertTrue(Position.isAdjacent(swampTile.getPosition(), mercenary.getPosition()));
        assertTrue(mercenary.getPosition().equals(new Position(0, 2)) || mercenary.getPosition().equals(new Position(2, 2)));

        // mercenary should be adjacent to player
        game.tick(null, Direction.NONE);
        assertTrue(Position.isAdjacent(mercenary.getPosition(), player.getPosition()));
        assertTrue(mercenary.getPosition().equals(new Position(0, 1)) || mercenary.getPosition().equals(new Position(2, 1)));

        // mercenary should be move to player's position
        game.tick(null, Direction.NONE);
        assertEquals(player.getPosition(), mercenary.getPosition());
    }
}
