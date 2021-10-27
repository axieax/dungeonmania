package dungeonmania.model.entities.movings;

public class DefaultState implements PlayerState {
    Player player;

    public DefaultState(Player player) {
        this.player = player;
    }

    @Override
    public void battle(MovingEntity opponent) {
        // TODO: go through inventory of player and apply attack() on any items that
        //       can be used to attack enemies (e.g. through use of instanceof Attackable)
        //       in addition to normal attack.
        //          e.g. sword, bow
        //       Bow/sword should only provide the extra attack damage they give the player
        //       instead of the total attack damage
        //          e.g. if a player can attack an enemy with 15 attack dmg while it has a
        //               a sword, and 10 without the sword,
        //               the sword's attack function should give 5 attack dmg.
        
        player.setHealth(
            player.getHealth() - opponent.getHealth() * opponent.getAttackDamage() / 10 
        );

        opponent.setHealth(
            opponent.getHealth() - player.getHealth() * player.getAttackDamage() / 5
        );
    }
    
}
