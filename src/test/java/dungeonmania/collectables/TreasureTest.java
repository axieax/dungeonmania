package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.Player;
import dungeonmania.model.entities.item.Treasure;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class TreasureTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Treasure("treasure1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("treasure1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "treasure1";

        Treasure item = new Treasure(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        player.collect(dungeon);

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getItem(collectableId).equals(item));
    }
}