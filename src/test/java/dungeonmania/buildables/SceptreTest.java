package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.buildables.Sceptre;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.SunStone;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
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
        Player player = new Player(new Position(0, 0));
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
        Player player = new Player(new Position(0, 0));
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
        Player player = new Player(new Position(0, 0));
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
        Player player = new Player(new Position(0, 0));
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
     * Test durability of Sceptre.
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Wood wood = new Wood(new Position(1, 0));
        Key key = new Key(new Position(1, 1), 1);
        SunStone sunstone = new SunStone(new Position(0, 1));

        game.addEntity(wood);
        game.addEntity(key);
        game.addEntity(sunstone);

        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);
        
        game.build("sceptre");

        // Durability of sceptre when built should be 5
        int initialDurability = 5;
        Sceptre sceptre = (Sceptre) player.findInventoryItem("sceptre");
        assertTrue(sceptre.getDurability() == initialDurability);

        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(0, 3), mode.damageMultiplier());
        game.addEntity(spawner);

        player.move(game, Direction.DOWN);

        // Player is now next to the zombie toast spawner and will proceed to destroy it with the sceptre
        // Durability of sceptre decreases by 1
        game.interact(spawner.getId());
        assertTrue(sceptre.getDurability() == initialDurability - 1);
    }

    /**
     * Test if Sceptre can be used in battles.
     */
    @Test
    public void battleTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Wood wood = new Wood(new Position(1, 0));
        Key key = new Key(new Position(1, 1), 1);
        SunStone sunstone = new SunStone(new Position(0, 1));

        game.addEntity(wood);
        game.addEntity(key);
        game.addEntity(sunstone);

        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.LEFT);
        
        game.build("sceptre");

        int initialDurability = 5;
        Sceptre sceptre = (Sceptre) player.findInventoryItem("sceptre");
        assertTrue(sceptre.getDurability() == initialDurability);

        Mercenary mercenary = new Mercenary(new Position(0, 2), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // Player moves to attack the mercenary with the sceptre
        player.move(game, Direction.DOWN);

        // Either the player or the mercenary should be dead
        // Durability of sceptre decreases by 1 each time it battles (within one tick)
        assertTrue((game.getEntity(mercenary.getId()) == null) || (game.getEntity(player.getId()) == null));
        assertTrue(sceptre == null || sceptre.getDurability() != initialDurability);
    }
}
