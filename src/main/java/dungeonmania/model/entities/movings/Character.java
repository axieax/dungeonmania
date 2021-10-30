package dungeonmania.model.entities.movings;

import java.util.List;

import dungeonmania.model.Dungeon;
import dungeonmania.model.entities.Equipment;
import dungeonmania.model.entities.Item;
import dungeonmania.model.entities.buildables.BuildableEquipment;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

public interface Character {
    public void battle(Dungeon dungeon, MovingEntity opponent);

    public void collect(Item item);

    public void build(String itemId);

    public void consume(Item item);

    public Item getInventoryItem(String itemId);
    
    public List<Equipment> getEquipment();

    public List<AttackEquipment> getAttackEquipment();
    
    public List<DefenceEquipment> getDefenceEquipment();
    
    public boolean checkBuildable(BuildableEquipment equipment);

    public List<ItemResponse> getInventoryResponses();

    public int getCurrentAttackDamage();

    public int applyDefenceToOpponentAttack(int opponentAttackDamag);

    public void addAlly(MovingEntity ally);

    public List<MovingEntity> getAllies();
    
    public void move(Dungeon dungeon, Direction direction);

}
