package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.SunStone;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class SunStoneTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        SunStone stone = new SunStone(new Position(1, 1));
        game.addEntity(stone);

        assertTrue(new Position(1, 1).equals(game.getEntity(stone.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        SunStone stone = new SunStone(new Position(1, 1));
        game.addEntity(stone);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));

        assertTrue(game.getEntity(stone.getId()) == null);
        assertTrue(player.getInventoryItem(stone.getId()).equals(stone));
    }

    /**
     * Test if SunStone can be used to open doors
     */
    @Test
    public void openDoor() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Position stonePos = new Position(1, 1);
        SunStone stone = new SunStone(new Position(1, 1));
        game.addEntity(stone);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);

        Door door = new Door(new Position(2, 1), 1);
        game.addEntity(door);

        // Player picks up sunstone
        game.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), stonePos);
        assertEquals(1, game.getEntities(player.getPosition()).size());
        assertTrue(player.getInventoryItem(stone.getId()).equals(stone));

        // Player unlocks door with sunstone
        game.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), door.getPosition());
        
        // Sunstone is not consumed
        assertTrue(player.getInventoryItem(stone.getId()).equals(stone));
    }

    /**
     * Test if SunStone is used over keys to open doors
     */
    @Test
    public void openDoorWithKey() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        Position stonePos = new Position(1, 1);
        SunStone stone = new SunStone(new Position(1, 1));
        game.addEntity(stone);

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);

        Door door = new Door(new Position(3, 1), 1);
        Key key = new Key(new Position(2, 1), 1);
        game.addEntity(door);
        game.addEntity(key);

        // player pick up sunstone
        game.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), stonePos);
        // player picked up sun stone
        assertEquals(1, game.getEntities(player.getPosition()).size());
        assertTrue(player.getInventoryItem(stone.getId()).equals(stone));

        // player picks up door key
        game.tick(null, Direction.RIGHT);
        assertTrue(player.getInventoryItem(key.getId()).equals(key));

        // player opens door using sunstone
        game.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), door.getPosition());
        assertTrue(player.getInventoryItem(key.getId()).equals(key));
        assertTrue(player.getInventoryItem(stone.getId()).equals(stone));
    }

    /**
     * Test if SunStone can be used in replacement of Treasure when building a Shield.
     */
    @Test
    public void buildWithSunStoneShield() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            Arrays.asList(
                new Wood(new Position(1, 1)),
                new Wood(new Position(2, 1)),
                new Key(new Position(3, 1), 1),
                new SunStone(new Position(4, 1))
                
            ),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);

        // Player picks up up items
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(4, 1));

        assertEquals(player.getInventoryResponses().size(), 4);

        // Building the shield
        assertDoesNotThrow(() -> game.build("shield"));

        // Check inventory to check all resources used correctly
        // Note that the key remains in the player's inventory since the sunstone takes priority
        assertEquals(player.getInventoryResponses().size(), 2);
        assertTrue(player.hasItemQuantity("sun_stone", 0));
        assertTrue(player.hasItemQuantity("key", 1));
        assertTrue(player.hasItemQuantity("shield", 1));
    }

    /**
     * Test if SunStone can be used in replacement of Treasure when building a Sceptre.
     */
    @Test
    public void buildWithSunStoneSceptre() {
        Mode mode = new Standard();
        Game game = new Game(
            "game",
            Arrays.asList(
                new Wood(new Position(1, 1)),
                new Treasure(new Position(2, 1)),
                new SunStone(new Position(4, 1)),
                new SunStone(new Position(4, 1))
                
            ),
            new ExitCondition(),
            mode
        );

        Player player = new Player(new Position(0, 1));
        game.addEntity(player);

        // Player picks up up items
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        game.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(4, 1));

        assertEquals(player.getInventoryResponses().size(), 4);
        
        // Building the shield
        assertDoesNotThrow(() -> game.build("sceptre"));

        // Check inventory to check all resources used correctly
        // Note that the treasure remains in the player's inventory since the sunstone takes priority
        assertEquals(player.getInventoryResponses().size(), 2);
        assertTrue(player.hasItemQuantity("sun_stone", 0));
        assertTrue(player.hasItemQuantity("treasure", 1));
        assertTrue(player.hasItemQuantity("sceptre", 1));
    }
}
