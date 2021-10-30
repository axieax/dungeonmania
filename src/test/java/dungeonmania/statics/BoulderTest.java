package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class BoulderTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Boulder("boulder1", new Position(1, 1)));

        assertTrue(new Position(1, 1).equals(dungeon.getEntity("boulder1").getPosition()));
    }

    /**
     * Test if player interacts with the boulder. If player moves onto the boulder,
     * the boulder should also move.
     */
    @Test
    public void boulderMoveByPlayer() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Boulder("boulder1", new Position(1, 1)));

        Player player = new Player("player1", new Position(2, 1));

        dungeon.addEntity(player);
        player.move(dungeon, Direction.LEFT);

        // Player and boulder should both move to the left by one 
        assertTrue(new Position(0, 1).equals(dungeon.getEntity("boulder1").getPosition()));
        assertTrue(new Position(1, 1).equals(player.getPosition()));
    }

    /**
     * Test that the player cannot push more than one boulder at once.
     */
    @Test
    public void boulderBlocksBoulderMovement() {
        Dungeon dungeon = new Dungeon(3, 3);
        dungeon.addEntity(new Boulder("boulder1", new Position(1, 1)));
        dungeon.addEntity(new Boulder("boulder2", new Position(2, 1)));

        Player player = new Player("player1", new Position(3, 1));

        dungeon.addEntity(player);
        player.move(dungeon, Direction.LEFT);

        // Since there are two boulders next to each other
        // Neither the player nor boulders should move
        assertTrue(new Position(1, 1).equals(dungeon.getEntity("boulder1").getPosition()));
        assertTrue(new Position(2, 1).equals(dungeon.getEntity("boulder2").getPosition()));
        assertTrue(new Position(3, 1).equals(player.getPosition()));
    }
}
