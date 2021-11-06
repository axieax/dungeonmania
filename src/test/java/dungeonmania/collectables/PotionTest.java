package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.potion.HealthPotion;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.collectables.potion.InvisibilityPotion;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class PotionTest {

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceHealthPotionTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        HealthPotion healthPotion = new HealthPotion(new Position(1, 1));
        game.addEntity(healthPotion);

        assertTrue(new Position(1, 1).equals(game.getEntity(healthPotion.getId()).getPosition()));
    }

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceInvincibilityPotionTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new Position(1, 1));
        game.addEntity(invincibilityPotion);

        assertTrue(new Position(1, 1).equals(game.getEntity(invincibilityPotion.getId()).getPosition()));
    }

    /**
     * Test whether the entity instance has been created with the correct positions.
     */
    @Test
    public void instanceInvisibilityPotionTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(new Position(1, 1));
        game.addEntity(invisibilityPotion);

        assertTrue(new Position(1, 1).equals(game.getEntity(invisibilityPotion.getId()).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectHealthPotionTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        HealthPotion healthPotion = new HealthPotion(new Position(1, 1));
        game.addEntity(healthPotion);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(healthPotion.getId()) == null);
        assertTrue(player.getInventoryItem(healthPotion.getId()).equals(healthPotion));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectInvincibilityPotionTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new Position(1, 1));
        game.addEntity(invincibilityPotion);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(invincibilityPotion.getId()) == null);
        assertTrue(player.getInventoryItem(invincibilityPotion.getId()).equals(invincibilityPotion));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectInvisibilityPotionTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(new Position(1, 1));
        game.addEntity(invisibilityPotion);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(invisibilityPotion.getId()) == null);
        assertTrue(player.getInventoryItem(invisibilityPotion.getId()).equals(invisibilityPotion));
    }
}