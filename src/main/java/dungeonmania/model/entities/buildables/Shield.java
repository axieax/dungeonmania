package dungeonmania.model.entities.buildables;

import dungeonmania.util.Position;

public class Shield extends BuildableEquipment {

    public Shield(String entityId, Position position) {
        super(entityId, position);
    }

    @Override
    public boolean isBuildable() {
        // TODO Auto-generated method stub
        return false;
    }
}
