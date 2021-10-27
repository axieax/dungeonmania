package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class BoulderTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Boulder("boulder1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("boulder1").getPosition()));
    }

    /**
     * Test if Player interacts with the boulder. If player moves to the boulder,
     * the boulder should also move.
     */
    @Test
    public void boulderMoveByPlayer() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Boulder("boulder1", new Position(1, 1)));

        // dungeon.getEntity("boulder1").move(new Position(2, 2));
        fail();
    }

    /**
     * Test if Player interacts with the boulder. Player cannot push more than one boulder.
     */
    @Test
    public void boulderMoveByBoulder() {
        fail();
    }
}
