package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.Shield;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.collectables.Treasure;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Door;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ShieldTest {
    /**
     * Test whether the buildable entity can be crafted up by the Player.
     */
    @Test
    public void buildTest() {
        Game game = new Game(3, 3);

        // To craft a shield, we need 2 wood and 1 treasure
        Wood wood1 = new Wood(new Position(1, 0));
        Wood wood2 = new Wood(new Position(2, 0));
        Treasure treasure = new Treasure(new Position(2, 1));

        game.addEntity(wood1);
        game.addEntity(wood2);
        game.addEntity(treasure);

        // Player picks up the wood and treasure
        Player player = new Player(new Position(0, 0));
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        // Player crafts a shield
        Shield shield = new Shield();
        player.craft(shield);

        // Check that the player has a shield
        assertTrue(player.getInventoryItem(shield.getId()).equals(shield));

        // Check that the player has no wood or treasure
        assertTrue(player.findInventoryItem("Wood") == null);
        assertTrue(player.getInventoryItem(treasure.getId()) == null);
    }

    /**
     * Test whether the buildable entity can be crafted up by the Player.
     */
    @Test
    public void buildTestAlternate() {
        Game game = new Game(3, 3);

        // To craft a shield, we need 2 wood and 1 key
        Wood wood1 = new Wood(new Position(1, 0));
        Wood wood2 = new Wood(new Position(2, 0));
        Key key = new Key(new Position(2, 1), 1);

        game.addEntity(wood1);
        game.addEntity(wood2);
        game.addEntity(key);

        // Player picks up the wood and key
        Player player = new Player(new Position(0, 0));
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        // Player crafts a shield
        Shield shield = new Shield();
        player.craft(shield);

        // Check that the player has a shield
        assertTrue(player.getInventoryItem("Shield") instanceof Shield);

        // Check that the player has no wood or key
        assertTrue(player.getInventoryItem("Wood") == null);
        assertTrue(player.getInventoryItem("Key") == null);

        // As a confirmation, check that a door (with the same key) cannot be opened
        Door door = new Door(new Position(2, 2), 1);
        game.addEntity(door);

        player.move(game, Direction.DOWN);
        assertTrue(new Position(2, 2).equals(player.getPosition()));
        assertFalse(door.isOpen());
    }

    /**
     * Test durability of Shield.
     */
    public void durabilityTest() {
        Game game = new Game(3, 3);

        Wood wood1 = new Wood(new Position(1, 0));
        Wood wood2 = new Wood(new Position(2, 0));
        Key key = new Key(new Position(2, 1), 1);

        game.addEntity(wood1);
        game.addEntity(wood2);
        game.addEntity(key);

        Player player = new Player(new Position(0, 0));
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);

        Shield shield = new Shield();
        player.craft(shield);

        // Durability of shield when picked up should be 5
        Item item = player.getInventoryItem(shield.getId());
        assertTrue(((Shield) item).getDurability() == 5);

        Mercenary mercenary = new Mercenary(new Position(2, 2));
        game.addEntity(mercenary);

        // Player moves to attack (interact with) the mercenary with the shield
        // This will cause the durability of the shield to decrease by 1
        player.move(game, Direction.RIGHT);

        assertTrue(((Shield) item).getDurability() == 4);
    }

    /**
     * Test if Shield can be used in battles.
     */
    @Test
    public void battleTest() {
        fail();
    }
}
