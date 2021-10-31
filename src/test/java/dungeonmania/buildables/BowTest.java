package dungeonmania.buildables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
        Game game = new Game(3, 3);

        // To craft a bow, we need 1 wood and 3 arrows
        Wood wood = new Wood(new Position(1, 0));
        Arrow arrow1 = new Arrow(new Position(1, 1));
        Arrow arrow2 = new Arrow(new Position(1, 2));
        Arrow arrow3 = new Arrow(new Position(1, 3));

        game.addEntity(wood);
        game.addEntity(arrow1);
        game.addEntity(arrow2);
        game.addEntity(arrow3);

        // Player picks up the wood and arrows
        Player player = new Player(new Position(0, 0));
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        // Player crafts a bow
        Bow bow = new Bow();
        player.craft(bow);

        // Check that the player has a bow
        assertTrue(player.getInventoryItem(bow.getId()).equals(bow));

        // Check that the player has no wood or arrows
        assertTrue(player.getInventoryItem(wood.getId()) == null);
        assertTrue(player.findInventoryItem("Arrow") == null);
    }

    /**
     * Test durability of Bow.
     */
    public void durabilityTest() {
        Game game = new Game(3, 3);

        Wood wood = new Wood(new Position(1, 0));
        Arrow arrow1 = new Arrow(new Position(1, 1));
        Arrow arrow2 = new Arrow(new Position(1, 2));
        Arrow arrow3 = new Arrow(new Position(1, 3));

        game.addEntity(wood);
        game.addEntity(arrow1);
        game.addEntity(arrow2);
        game.addEntity(arrow3);

        Player player = new Player(new Position(0, 0));
        player.move(game, Direction.RIGHT);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);
        player.move(game, Direction.DOWN);

        // Player crafts a bow
        Bow bow = new Bow();
        player.craft(bow);

        // Durability of bow when picked up should be 5
        Item item = player.getInventoryItem(bow.getId());
        assertTrue(((Bow) item).getDurability() == 5);

        Mercenary mercenary = new Mercenary(new Position(2, 3));
        game.addEntity(mercenary);

        // Player moves to attack (interact with) the mercenary with the bow
        // This will cause the durability of the bow to decrease by 1
        player.move(game, Direction.RIGHT);

        assertTrue(((Bow) item).getDurability() == 4);
    }

    /**
     * Test if Bow can be used in battles.
     */
    @Test
    public void battleTest() {
        fail();
    }
}
