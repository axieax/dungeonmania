package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.collectables.potion.HealthPotion;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.collectables.potion.InvisibilityPotion;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class PotionTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceHealthPotionTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new HealthPotion("health1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("health1").getPosition()));
    }

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceInvincibilityPotionTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new InvincibilityPotion("invincibility1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("invincibility1").getPosition()));
    }

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceInvisibilityPotionTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new InvisibilityPotion("invisibility1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("invisibility1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectHealthPotionTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "health1";

        HealthPotion item = new HealthPotion(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        player.collect(dungeon);

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getItem(collectableId).equals(item));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectInvincibilityPotionTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "invincibility1";

        InvincibilityPotion item = new InvincibilityPotion(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        player.collect(dungeon);

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getItem(collectableId).equals(item));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectInvisibilityPotionTest() {
        Dungeon dungeon = new Dungeon(3, 3);

        String collectableId = "invisibility1";

        InvisibilityPotion item = new InvisibilityPotion(collectableId, new Position(1, 1));

        dungeon.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        player.collect(dungeon);

        assertTrue(dungeon.getEntity(collectableId) == null);
        assertTrue(player.getItem(collectableId).equals(item));
    }

    /**
     * Test the effects of a HealthPotion and if it can only be used once.
     */
    @Test
    public void effectHealthPotionTest() {
        fail();
    }

    /**
     * Test the effects of a InvincibilityPotion and if it lasts for a limited time.
     */
    @Test
    public void effectInvincibilityPotionTest() {
        fail();
    }

    /**
     * Test the effects of a InvisibilityPotion.
     */
    @Test
    public void effectInvisibilityPotionTest() {
        fail();
    }
}