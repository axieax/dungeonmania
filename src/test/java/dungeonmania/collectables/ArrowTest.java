package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.Arrow;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ArrowTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game(3, 3);
        game.addEntity(new Arrow("arrows1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(game.getEntity("arrows1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Game game = new Game(3, 3);

        String collectableId = "arrows1";

        Arrow item = new Arrow(collectableId, new Position(1, 1));

        game.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
    }
}