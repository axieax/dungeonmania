package dungeonmania.statics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.model.Game;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.model.entities.statics.Boulder;
import dungeonmania.model.entities.statics.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.Test;

public class WallTest {

    /**
     * Test whether the entity instance has been created with the correct positions
     */
    @Test
    public void instanceTest() {
        Game game = new Game(3, 3);
        Wall wall = new Wall(new Position(1, 1));
        game.addEntity(wall);

        assertTrue(new Position(1, 1).equals(game.getEntity(wall.getId()).getPosition()));
    }

    /**
     * Test if wall blocks movement of a player.
     */
    @Test
    public void wallBlockPlayer() {
        Game game = new Game(5, 5);
        game.addEntity(new Wall(new Position(0, 0)));
        game.addEntity(new Wall(new Position(0, 1)));
        game.addEntity(new Wall(new Position(0, 2)));
        game.addEntity(new Wall(new Position(1, 0)));
        game.addEntity(new Wall(new Position(1, 2)));
        game.addEntity(new Wall(new Position(2, 0)));
        game.addEntity(new Wall(new Position(2, 1)));
        game.addEntity(new Wall(new Position(2, 2)));

        Player player = new Player(new Position(1, 1));
        game.addEntity(player);

        // If movement is blocked by wall, the player should remain in the same position

        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        player.move(game, Direction.LEFT);
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        player.move(game, Direction.UP);
        assertTrue(new Position(1, 1).equals(player.getPosition()));

        player.move(game, Direction.DOWN);
        assertTrue(new Position(1, 1).equals(player.getPosition()));
    }

    /**
     * Test if wall blocks movement of enemies.
     */
    @Test
    public void wallBlockEnemies() {
        Game game = new Game(5, 5);
        game.addEntity(new Wall(new Position(0, 0)));
        game.addEntity(new Wall(new Position(0, 1)));
        game.addEntity(new Wall(new Position(0, 2)));
        game.addEntity(new Wall(new Position(1, 0)));
        game.addEntity(new Wall(new Position(1, 2)));
        game.addEntity(new Wall(new Position(2, 0)));
        game.addEntity(new Wall(new Position(2, 1)));
        game.addEntity(new Wall(new Position(2, 2)));

        ZombieToast zombie = new ZombieToast(new Position(1, 1));
        game.addEntity(zombie);

        zombie.tick(game);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));

        zombie.tick(game);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));

        zombie.tick(game);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));

        zombie.tick(game);
        assertTrue(new Position(1, 1).equals(zombie.getPosition()));
    }

    /**
     * Test if wall blocks movement of moving boulders.
     */
    public void wallBlockBoulder() {
        Game game = new Game(5, 5);
        game.addEntity(new Wall(new Position(3, 0)));

        Boulder boulder = new Boulder(new Position(1, 0));
        game.addEntity(boulder);

        Player player = new Player(new Position(0, 0));
        game.addEntity(player);

        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(boulder.getPosition()));

        // Wall blocks movement of boulder
        player.move(game, Direction.RIGHT);
        assertTrue(new Position(1, 0).equals(player.getPosition()));
        assertTrue(new Position(2, 0).equals(boulder.getPosition()));
    }
}
