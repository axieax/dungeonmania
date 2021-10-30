package dungeonmania.model.goal;

import org.json.JSONObject;

public interface Goal {
    public boolean evaluate();

    public String getOperator();

    public String toString();

    public JSONObject toJSON();
}
