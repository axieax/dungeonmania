package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.potion.HealthPotion;
import dungeonmania.model.entities.collectables.potion.InvincibilityPotion;
import dungeonmania.model.entities.collectables.potion.InvisibilityPotion;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.movings.movement.RandomMovementState;
import dungeonmania.model.entities.movings.movement.RunMovementState;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        HealthPotion healthPotion = new HealthPotion(new Position(1, 1));
        game.addEntity(healthPotion);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(new Position(1, 1));
        game.addEntity(invincibilityPotion);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
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
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(new Position(1, 1));
        game.addEntity(invisibilityPotion);

        Player player = new Player(new Position(0, 1), mode.initialHealth());
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(invisibilityPotion.getId()) == null);
        assertTrue(player.getInventoryItem(invisibilityPotion.getId()).equals(invisibilityPotion));
    }

    /**
     * Test runaway effects of invincibility potion
     */
    @Test
    public void runawayInvincibilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);
        InvincibilityPotion potion = new InvincibilityPotion(new Position(1, 0));
        game.addEntity(potion);

        // create a row of walls on y = 1
        for (int i = 0; i < 50; i++) {
            game.addEntity(new Wall(new Position(i, 1)));
        }

        Player player = new Player(new Position(0, 0), mode.initialHealth());
        game.addEntity(player);
        game.tick(null, Direction.RIGHT);

        assertTrue(player.findInventoryItem("invincibility_potion") != null);

        ZombieToast zombie = new ZombieToast(new Position(5, 0), mode.damageMultiplier(), player);
        game.addEntity(zombie);
        assertTrue(zombie.getMovementState() instanceof RandomMovementState);

        game.tick(potion.getId(), Direction.NONE);
        // zombie can only move right to run away
        assertTrue(zombie.getMovementState() instanceof RunMovementState);
        assertTrue(new Position(6, 0).equals(zombie.getPosition()));        

        game.tick(null, Direction.NONE);
        assertTrue(new Position(7, 0).equals(zombie.getPosition())); 
        assertTrue(zombie.getMovementState() instanceof RunMovementState); 

        game.tick(null, Direction.NONE);
        assertTrue(new Position(8, 0).equals(zombie.getPosition())); 
        assertTrue(zombie.getMovementState() instanceof RunMovementState); 

        game.tick(null, Direction.NONE);
        // after 3 ticks, zombie should be in random state
        assertTrue(zombie.getMovementState() instanceof RandomMovementState);        
    }
}