package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.monster.Spider;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class SpiderTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Spider("spider1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("spider1").getPosition()));
    }

    /**
     * Test case:
     * - spider move circle - test movement
     * - tranverse though traversable entities
     * - cannot traverse walls -> reverse direction
     * - spawn max spiders
     * - can or cannot traverse through other entities??
     */
}
