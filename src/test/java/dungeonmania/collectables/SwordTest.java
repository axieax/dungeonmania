package dungeonmania.collectables;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import dungeonmania.model.Game;
import dungeonmania.model.entities.collectables.equipment.Sword;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.ZombieToastSpawner;
import dungeonmania.model.goal.ExitCondition;
import dungeonmania.model.mode.Mode;
import dungeonmania.model.mode.Standard;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class SwordTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Sword sword = new Sword(new Position(1, 1));
        game.addEntity(sword);
        String swordId = sword.getId();

        assertTrue(new Position(1, 1).equals(game.getEntity(swordId).getPosition()));
    }

    /**
     * Test whether the collectable entity can be picked up by the Player.
     */
    @Test
    public void collectTest() {
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), new Standard());
        Sword sword = new Sword(new Position(1, 1));
        game.addEntity(sword);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        assertTrue(new Position(1, 1).equals(player.getPosition()));        

        assertTrue(game.getEntity(sword.getId()) == null);
        assertTrue(player.getInventoryItem(sword.getId()).equals(sword));
    }

    /**
     * Test durability of Sword
     */
    @Test
    public void durabilityTest() {
        Mode mode = new Standard();
        Game game = new Game("game", new ArrayList<>(), new ExitCondition(), mode);

        Sword sword = new Sword(new Position(1, 1));
        game.addEntity(sword);

        Player player = new Player(new Position(0, 1));
        player.move(game, Direction.RIGHT);

        // Durability of sword when picked up should be 5
        assertTrue(sword.getDurability() == 5);

        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(3, 1), mode.damageMultiplier());
        game.addEntity(spawner);

        player.move(game, Direction.RIGHT);

        // Player is now next to the zombie toast spawner and will proceed to destroy it with the sword
        // This will cause the durability of the sword to decrease by 1
        assertTrue(sword.getDurability() == 4);
    }

    /**
     * Test if Sword can be used in battles
     */
    @Test
    public void battleTest() {
        fail();
    }
}