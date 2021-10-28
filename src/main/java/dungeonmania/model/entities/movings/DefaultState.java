package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.entities.Equipment;

public class DefaultState implements PlayerState {
    Player player;

    public DefaultState(Player player) {
        this.player = player;
    }

    @Override
    public void battle(MovingEntity opponent) {
        int damageToOpponent = player.getHealth() * player.getAttackDamage() / 5;
        int damageToPlayer = opponent.getHealth() * opponent.getAttackDamage() / 10;
                
        List<AttackEquipment> attackEquip = player.getEquipment();
        List<DefenceEquipment> defenceEquip = player.getEquipment();
        
        // any extra attack damage provided by equipment
        for(AttackEquipment e: attackEquip) {
            damageToOpponent = e.setAttackMultiplier(damageToOpponent);
        }
        
        // any extra attack damage provided by allies
        List<MovingEntity> allies = player.getAllies();
        for(MovingEntity a: allies) {
            damageToOpponent += (a.getHealth() *  a.getAttackDamage() / 5);
        }
        
        // any extra defence provided by equipment
        for(DefenceEquipment e: defenceEquip) {
            damageToPlayer = e.setDefenceMultiplier(damageToPlayer);
        }

        int playerHealth = player.getHealth();
        int opponentHealth = opponent.getHealth();
        // battles only last a single tick
        while(playerHealth >= 0 && opponentHealth >= 0) {
            opponent.setHealth(opponent.getHealth() - damageToOpponent);
            player.setHealth(player.getHealth() - damageToPlayer);

            playerHealth = player.getHealth();
            opponentHealth = opponent.getHealth();
        }
        
    }
    
}
