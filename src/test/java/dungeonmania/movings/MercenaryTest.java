package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.monster.Mercenary;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class MercenaryTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Mercenary("mercenary1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("mercenary1").getPosition()));
    }

    /**
     * Test case:
     * - ???
     */
}
