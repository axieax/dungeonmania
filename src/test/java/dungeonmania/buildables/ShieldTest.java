package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.buildables.Shield;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Spider;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ShieldTest {

    /**
     * Test whether the buildable entity can be built by the Player.
     */
    @Test
    public void buildTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a shield, we need 2 wood and 1 treasure
        Wood wood1 = new Wood(new Position(1, 0));
        Wood wood2 = new Wood(new Position(2, 0));
        Treasure treasure = new Treasure(new Position(2, 1));

        game.addEntity(wood1);
        game.addEntity(wood2);
        game.addEntity(treasure);

        // Player picks up the wood and treasure
        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        assertTrue(player.findInventoryItem("shield") == null);

        // Player builds a shield
        game.build("shield");

        // Check that the player has a shield
        assertTrue(player.hasItemQuantity("shield", 1));

        // Check that the player has no wood or treasure
        assertTrue(player.findInventoryItem("wood") == null);
        assertTrue(player.getInventoryItem(treasure.getId()) == null);
    }

    /**
     * Test whether the buildable entity can be built by the Player alternatively.
     */
    @Test
    public void buildTestV2() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        // To build a shield, we need 2 wood and 1 key
        Wood wood1 = new Wood(new Position(1, 0));
        Wood wood2 = new Wood(new Position(2, 0));
        Key key = new Key(new Position(2, 1), 1);

        game.addEntity(wood1);
        game.addEntity(wood2);
        game.addEntity(key);

        // Player picks up the wood and key
        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        // Player builds a shield
        game.build("shield");

        // Check that the player has a shield
        assertTrue(player.hasItemQuantity("shield", 1));

        // Check that the player has no wood or key
        assertTrue(player.getInventoryItem("Wood") == null);
        assertTrue(player.getInventoryItem("Key") == null);

        assertTrue(player.getPosition().equals(new Position(2, 1)));

        // As a confirmation, check that a door (with the same key) cannot be opened
        Door door = new Door(new Position(2, 2), 1);
        game.addEntity(door);

        player.move(game, Direction.DOWN);

        assertTrue(player.getPosition().equals(new Position(2, 1)));
        assertFalse(door.isOpen());
    }

    /**
     * Test durability of Shield.
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Wood wood1 = new Wood(new Position(1, 0));
        Wood wood2 = new Wood(new Position(2, 0));
        Key key = new Key(new Position(2, 1), 1);

        game.addEntity(wood1);
        game.addEntity(wood2);
        game.addEntity(key);

        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        game.build("shield");

        // Durability of shield when built should be 5
        int initialDurability = 5;
        Shield shield = (Shield) player.findInventoryItem("shield");
        assertTrue(shield.getDurability() == initialDurability);

        Spider spider = new Spider(new Position(3, 1), mode.damageMultiplier());
        game.addEntity(spider);

        // Player moves to defend against the spider with the shield
        // Durability of shield decreases by 1 each time it battles (within one tick)
        player.move(game, Direction.RIGHT);

        assertTrue(shield == null || shield.getDurability() != initialDurability);
    }

    /**
     * Test if Shield can be used in battles.
     */
    @Test
    public void battleTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Wood wood1 = new Wood(new Position(1, 0));
        Wood wood2 = new Wood(new Position(2, 0));
        Key key = new Key(new Position(2, 1), 1);

        game.addEntity(wood1);
        game.addEntity(wood2);
        game.addEntity(key);

        Player player = new Player(new Position(0, 0));
        game.addEntity(player);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        game.build("shield");

        int initialDurability = 5;
        Shield shield = (Shield) player.findInventoryItem("shield");
        assertTrue(shield.getDurability() == initialDurability);

        Mercenary mercenary = new Mercenary(new Position(3, 1), mode.damageMultiplier(), player);
        game.addEntity(mercenary);

        // Player moves to defend against the mercenary with the shield
        player.move(game, Direction.RIGHT);

        // Either the player or the mercenary should be dead
        // Durability of shield decreases by 1 each time it battles (within one tick)
        assertTrue((game.getEntity(mercenary.getId()) == null) || (game.getEntity(player.getId()) == null));
        assertTrue(shield == null || shield.getDurability() != initialDurability);
    }
}
