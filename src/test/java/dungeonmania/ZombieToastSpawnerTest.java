package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.characters.Player;
import dungeonmania.model.entities.staticEntity.ZombieToastSpawner;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ZombieToastSpawnerTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new ZombieToastSpawner("zombietoastspawner1", new Position(1, 1)));

        assertTrue(
            new Position(1, 1).equals(dungeon.getEntity("zombietoastspawner1").getPosition())
        );
    }

    /**
     * Test whether a ZombieToast spawn every 20 ticks in an open square cardinally adjacent to the spawner.
     */
    @Test
    public void zombieToastSpawnEveryCycle() {
        fail();
    }

    /**
     * Test whether a ZombieToast cannot spawn as there is no open square cardinally adjacent to the spawner.
     */
    @Test
    public void zombieToastCannotSpawn() {
        fail();
    }
    
    /**
     * Test if player can destroy spawner if they have a weapon and are cardinally adjacent to the spawner.
     */
    @Test
    public void zombieToastDestroySpanwer() {
        fail();
    }

}
