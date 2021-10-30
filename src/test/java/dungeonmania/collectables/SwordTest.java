package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class SwordTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Sword("sword1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("sword1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "sword1";

        Sword item = new Sword(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(dungeon, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
    }

    /**
     * Test durability of Sword
     */
    @Test
    public void durabilityTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "sword1";

        Sword item = new Sword(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(dungeon, Direction.RIGHT);

        // Durability of sword when picked up should be 5
        assertTrue(item.getDurability() == 5);

        ZombieToastSpawner zombie = new ZombieToastSpawner("zombie1", new Position(3, 1));
        dungeon.addEntity(zombie);

        player.move(dungeon, Direction.RIGHT);

        // Player is now next to the zombie toast spawner and will proceed to destroy it with the sword
        // This will cause the durability of the sword to decrease by 1
        assertTrue(item.getDurability() == 4);
    }

    /**
     * Test if Sword can be used in battles
     */
    @Test
    public void battleTest() {
        fail();
    }
}