package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Game;
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
        Game game = new Game(3, 3);
        game.addEntity(new HealthPotion("health1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(game.getEntity("health1").getPosition()));
    }

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceInvincibilityPotionTest() {
        Game game = new Game(3, 3);
        game.addEntity(new InvincibilityPotion("invincibility1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(game.getEntity("invincibility1").getPosition()));
    }

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceInvisibilityPotionTest() {
        Game game = new Game(3, 3);
        game.addEntity(new InvisibilityPotion("invisibility1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(game.getEntity("invisibility1").getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectHealthPotionTest() {
        Game game = new Game(3, 3);

        String collectableId = "health1";

        HealthPotion item = new HealthPotion(collectableId, new Position(1, 1));

        game.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectInvincibilityPotionTest() {
        Game game = new Game(3, 3);

        String collectableId = "invincibility1";

        InvincibilityPotion item = new InvincibilityPotion(collectableId, new Position(1, 1));

        game.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectInvisibilityPotionTest() {
        Game game = new Game(3, 3);

        String collectableId = "invisibility1";

        InvisibilityPotion item = new InvisibilityPotion(collectableId, new Position(1, 1));

        game.addEntity(item);

        Player player = new Player("player1", new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(collectableId) == null);
        assertTrue(player.getInventoryItem(collectableId).equals(item));
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