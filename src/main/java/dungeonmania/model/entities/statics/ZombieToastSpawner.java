package dungeonmania.model.entities.statics;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.model.Game;
import dungeonmania.model.entities.Entity;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Tickable;
import dungeonmania.model.entities.movings.MovingEntity;
import dungeonmania.model.entities.movings.Player;
import dungeonmania.model.entities.movings.SubjectPlayer;
import dungeonmania.model.entities.movings.ZombieToast;
import dungeonmania.util.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ZombieToastSpawner extends Entity implements Tickable {

    private int tickRate;
    private int currTickRate;

    public ZombieToastSpawner(Position position, int tickRate) {
        super("zombie_toast_spawner", position);
        this.tickRate = tickRate;
        this.currTickRate = 0;
    }

    /**
     * If the Player interacts with the ZombieToastSpawner with a weapon equipped,
     * the player destroys the spawner and the weapon loses durability.
     */
    @Override
    public void interact(Game game, MovingEntity character) throws InvalidActionException {
        if (character instanceof Player) {
            Player player = (Player) character;
            if (player.hasWeapon()) {
                Equipment weapon = player.getWeapon();
                weapon.useEquipment(player);
                game.removeEntity(this);
            } else {
                throw new InvalidActionException("You need to have a weapon to destroy a zombie toast spawner");
            }
        }
    }

    @Override
    public void tick(Game game) {
        if (currTickRate % tickRate == 0) {
            int x = this.getX();
            int y = this.getY();
            List<Position> positions = Arrays.asList(
                new Position(x, y + 1),
                new Position(x - 1, y),
                new Position(x + 1, y),
                new Position(x, y - 1)
            );
            List<Position> openSquares = new ArrayList<>();
            positions
                .stream()
                .forEach(
                    position -> {
                        if (game.getEntities(position) == null) openSquares.add(position);
                    }
                );

            if (!openSquares.isEmpty()) {
                Random rand = new Random();
                Position randPosition = openSquares.get(rand.nextInt(openSquares.size()));
                game.addEntity(
                    new ZombieToast(
                        randPosition,
                        game.getMode().damageMultiplier(),
                        (SubjectPlayer) game.getCharacter()
                    )
                );
            }
        }
        currTickRate++;
    }
}
