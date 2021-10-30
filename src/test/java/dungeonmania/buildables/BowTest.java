package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.Bow;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.collectables.Wood;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class BowTest {
    /**
     * Test whether the buildable entity can be crafted up by the Player.
     */
    @Test
    public void buildTest() {
        Game game = new Game();
        Dungeon dungeon = new Dungeon(3, 3);

        // To craft a bow, we need 1 wood and 3 arrows
        Wood wood = new Wood("wood1", new Position(1, 0));
        Arrow arrow1 = new Arrow("arrow1", new Position(1, 1));
        Arrow arrow2 = new Arrow("arrow2", new Position(1, 2));
        Arrow arrow3 = new Arrow("arrow3", new Position(1, 3));

        dungeon.addEntity(wood);
        dungeon.addEntity(arrow1);
        dungeon.addEntity(arrow2);
        dungeon.addEntity(arrow3);

        // Player picks up the wood and arrows
        Player player = new Player("player", new Position(0, 0));
        player.move(dungeon, Direction.RIGHT);
        player.move(dungeon, Direction.DOWN);
        player.move(dungeon, Direction.DOWN);
        player.move(dungeon, Direction.DOWN);

        // Player crafts a bow
        // This needs to be changed according to how we are integrating game with the dungeon
        player.craft(game, "Bow");

        // Check that the player has a bow
        assertTrue(player.findInventoryItem("Bow") instanceof Bow);

        // Check that the player has no wood or arrows
        assertTrue(player.findInventoryItem("Wood") == null);
        assertTrue(player.findInventoryItem("Arrow") == null);
    }

    /**
     * Test durability of Bow
     */
    public void durabilityTest() {
        Game game = new Game();
        Dungeon dungeon = new Dungeon(3, 3);

        // To craft a bow, we need 1 wood and 3 arrows
        Wood wood = new Wood("wood1", new Position(1, 0));
        Arrow arrow1 = new Arrow("arrow1", new Position(1, 1));
        Arrow arrow2 = new Arrow("arrow2", new Position(1, 2));
        Arrow arrow3 = new Arrow("arrow3", new Position(1, 3));

        dungeon.addEntity(wood);
        dungeon.addEntity(arrow1);
        dungeon.addEntity(arrow2);
        dungeon.addEntity(arrow3);

        // Player picks up the wood and arrows
        Player player = new Player("player", new Position(0, 0));
        player.move(dungeon, Direction.RIGHT);
        player.move(dungeon, Direction.DOWN);
        player.move(dungeon, Direction.DOWN);
        player.move(dungeon, Direction.DOWN);

        // Player crafts a bow
        // This needs to be changed according to how we are integrating game with the dungeon
        player.craft(game, "Bow");

        // Durability of bow when picked up should be 5
        Item item = player.findInventoryItem("Bow");
        assertTrue(((Bow) item).getDurability() == 5);

        Mercenary mercenary = new Mercenary("mercenary1", new Position(2, 3));
        dungeon.addEntity(mercenary);

        // Player moves to attack (interact with) the mercenary with the bow
        // This will cause the durability of the bow to decrease by 1
        player.move(dungeon, Direction.RIGHT);

        assertTrue(((Bow) item).getDurability() == 4);
    }

    /**
     * Test if Sword can be used in battles
     */
    @Test
    public void battleTest() {
        fail();
    }
}
