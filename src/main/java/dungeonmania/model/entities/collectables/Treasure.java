package dungeonmania.model.entities.collectables;

import dungeonmania.model.Game;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.Tickable;
import dungeonmania.model.entities.movings.player.Player;
import dungeonmania.model.entities.statics.Consumable;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Position;
import java.util.Arrays;

public class Treasure extends Item implements Consumable, Tickable {

    private int animationTick = 0;

    public Treasure(Position position) {
        super("treasure", position);
    }

    public void tick(Game game) {
        animationTick += 1;
    }

    @Override
    public void consume(Game game, Player player) {
        player.removeInventoryItem(this.getId());
    }

    @Override
    public AnimationQueue getAnimation() {
        final int skin = animationTick % 4;
        return new AnimationQueue(
            "PostTick",
            getId(),
            Arrays.asList("sprite treasure_" + skin),
            false,
            -1
        );
    }
}
