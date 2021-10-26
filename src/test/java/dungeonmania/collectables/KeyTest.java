package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.collectables.Key;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class KeyTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Key("key1", new Position(1, 1), 1));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("key1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "key1";

        Key item = new Key(collectableId, new Position(1, 1), 1);

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        player.collect(dungeon);

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getItem(collectableId).equals(item));
    }

    /**
     * Test if player can only carry one key.
     */
    @Test
    public void carryLimit() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId1 = "key1";
        String collectableId2 = "key2";

        Key key1 = new Key(collectableId1, new Position(1, 1), 1);
        Key key2 = new Key(collectableId2, new Position(2, 1), 2);

        dungeon.addEntity(key1);

        Player player = new Player("player1", new Position(0, 1)); 

        // collect the first key
        player.move(Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));       
        player.collect(dungeon);

        assertTrue(dungeon.getEntity(collectableId1) == null);
        assertTrue(dungeon.getEntity(collectableId2) == key2);
        assertTrue(player.getItem(collectableId1).equals(key1));

        // attempt to collect the second key
        player.move(Direction.RIGHT);
        assertTrue(new Position(2, 1).equals(player.getPosition()));       
        player.collect(dungeon);

        // check if the second key is still in the dungeon
        assertTrue(dungeon.getEntity(collectableId2) == key2);
        assertTrue(player.getItem(collectableId2) == null);
    }

    /**
     * Test if the key disappears after it has been used to its corresponding doors.
     */
    @Test
    public void keyDisappears() {
        fail();
    }
}