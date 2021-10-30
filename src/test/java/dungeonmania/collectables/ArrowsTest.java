package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.collectables.Arrows;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ArrowsTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Arrows("arrows1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("arrows1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "arrows1";

        Arrows item = new Arrows(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(dungeon, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
    }
}