package dungeonmania.movings;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.monster.ZombieToast;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ZombieToastTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new ZombieToast("zombietoast1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("zombietoast1").getPosition()));
    }

    /**
     * Test case:
     * - test movement? still bound by walls, etc same as player.
     * - test portals have no effect.
     */
}
