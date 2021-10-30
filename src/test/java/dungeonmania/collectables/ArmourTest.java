package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.collectables.equipment.Armour;
import dungeonmania.model.entities.movings.Mercenary;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class ArmourTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Armour(new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("armour1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "armour1";

        Armour item = new Armour(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(dungeon, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
    }

    /**
     * Test durability of Armour
     */
    @Test
    public void durabilityTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "armour1";

        Armour item = new Armour(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(dungeon, Direction.RIGHT);

        // Durability of armour when picked up should be 5
        assertTrue(item.getDurability() == 5);

        Mercenary mercenary = new Mercenary("mercenary1", new Position(2, 1));
        dungeon.addEntity(mercenary);

        // Player moves to attack (interact with) the mercenary
        // This will cause the durability of the armour to decrease by 1
        player.move(dungeon, Direction.RIGHT);
        assertTrue(item.getDurability() == 4);
    }

    /**
     * Test armour protection for ZombieToast since zombies randomly spawn with armour.
     */
    @Test
    public void monsterArmourTest() {
        
    }

    /**
     * Test if the Player can take the Armour from a defeated ZombieToast/Mercenary
     */
    @Test
    public void takeArmourTest() {
        fail();
    }
}