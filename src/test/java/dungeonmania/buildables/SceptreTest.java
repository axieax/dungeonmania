package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.SunStone;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class SceptreTest {

    /**
     * Test whether the buildable entity can be built by the Player.
     */
    @Test
    public void buildTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a sceptre, we need 1 wood, 1 key and 1 sunstone
        Wood wood = new Wood(new Position(1, 0));
        Key key = new Key(new Position(1, 1), 1);
        SunStone sunstone = new SunStone(new Position(0, 1));

        game.addEntity(wood);
        game.addEntity(key);
        game.addEntity(sunstone);

        // Player picks up all the items
        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);

        assertTrue(player.findInventoryItem("sceptre") == null);

        // Player builds a sceptre
        game.build("sceptre");

        // Check that the player has a sceptre
        assertTrue(player.hasItemQuantity("sceptre", 1));

        // Check that the player has no wood, key or sunstone
        assertTrue(player.getInventoryItem(wood.getId()) == null);
        assertTrue(player.getInventoryItem(key.getId()) == null);
        assertTrue(player.getInventoryItem(sunstone.getId()) == null);
    }

    /**
     * Test whether the buildable entity can be built by the Player alternatively.
     */
    @Test
    public void buildTestV2() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a sceptre, we need 2 arrows, 1 key and 1 sunstone
        Arrow arrow1 = new Arrow(new Position(1, 0));
        Arrow arrow2 = new Arrow(new Position(2, 0));
        Key key = new Key(new Position(2, 1), 1);
        SunStone sunstone = new SunStone(new Position(1, 1));

        game.addEntity(arrow1);
        game.addEntity(arrow2);
        game.addEntity(key);
        game.addEntity(sunstone);

        // Player picks up all the items
        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);

        assertTrue(player.findInventoryItem("sceptre") == null);

        // Player builds a sceptre
        game.build("sceptre");

        // Check that the player has a sceptre
        assertTrue(player.hasItemQuantity("sceptre", 1));

        // Check that the player has no arrows, key or sunstone
        assertTrue(player.findInventoryItem("arrow") == null);
        assertTrue(player.getInventoryItem(key.getId()) == null);
        assertTrue(player.getInventoryItem(sunstone.getId()) == null);
    }

    /**
     * Test whether the buildable entity can be built by the Player alternatively.
     */
    @Test
    public void buildTestV3() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a sceptre, we need 1 wood, 1 treasure and 1 sunstone
        Wood wood = new Wood(new Position(1, 0));
        Treasure treasure = new Treasure(new Position(1, 1));
        SunStone sunstone = new SunStone(new Position(0, 1));

        game.addEntity(wood);
        game.addEntity(treasure);
        game.addEntity(sunstone);

        // Player picks up all the items
        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);

        assertTrue(player.findInventoryItem("sceptre") == null);

        // Player builds a sceptre
        game.build("sceptre");

        // Check that the player has a sceptre
        assertTrue(player.hasItemQuantity("sceptre", 1));

        // Check that the player has no wood, treasure or sunstone
        assertTrue(player.getInventoryItem(wood.getId()) == null);
        assertTrue(player.getInventoryItem(treasure.getId()) == null);
        assertTrue(player.getInventoryItem(sunstone.getId()) == null);
    }

    /**
     * Test whether the buildable entity can be built by the Player alternatively.
     */
    @Test
    public void buildTestV4() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a sceptre, we need 2 arrows, 1 treasure and 1 sunstone
        Arrow arrow1 = new Arrow(new Position(1, 0));
        Arrow arrow2 = new Arrow(new Position(2, 0));
        Treasure treasure = new Treasure(new Position(2, 1));
        SunStone sunstone = new SunStone(new Position(1, 1));

        game.addEntity(arrow1);
        game.addEntity(arrow2);
        game.addEntity(treasure);
        game.addEntity(sunstone);

        // Player picks up all the items
        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);

        assertTrue(player.findInventoryItem("sceptre") == null);

        // Player builds a sceptre
        game.build("sceptre");

        // Check that the player has a sceptre
        assertTrue(player.hasItemQuantity("sceptre", 1));

        // Check that the player has no arrows, treasure or sunstone
        assertTrue(player.findInventoryItem("arrow") == null);
        assertTrue(player.getInventoryItem(treasure.getId()) == null);
        assertTrue(player.getInventoryItem(sunstone.getId()) == null);
    }

    /**
     * Test interaction with Zombie Toast Spawner.
     */
    @Test
    public void zombieToastSpawnerTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Wood wood = new Wood(new Position(1, 0));
        Key key = new Key(new Position(1, 1), 1);
        SunStone sunstone = new SunStone(new Position(0, 1));

        game.addEntity(wood);
        game.addEntity(key);
        game.addEntity(sunstone);

        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);

        game.build("sceptre");

        ZombieToastSpawner spawner = new ZombieToastSpawner(
            new Position(0, 3),
            mode.damageMultiplier()
        );
        game.addEntity(spawner);

        player.move(game, Direction.DOWN);

        // Player is now next to the zombie toast spawner but cannot destroy it with the sceptre (not a weapon)
        assertThrows(InvalidActionException.class, () -> game.interact(spawner.getId()));
    }
}
