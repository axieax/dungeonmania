package dungeonmania.model.goal;

import dungeonmania.model.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GoalComposite implements Goal {

    private final List<Goal> goals = new ArrayList<>();

    /**
     * Gets the list of Goals
     *
     * @return list of Goals
     */
    public final List<Goal> getGoals() {
        return goals;
    }

    /**
     * Adds a Goal to a Composite Goal
     *
     * @param goal Goal to be added
     */
    public final void addGoal(Goal goal) {
        goals.add(goal);
    }

    public String getOperator() {
        return getClass().getSimpleName().replace("Composite", "").toUpperCase();
    }

    @Override
    public String toString(Game game) {
        // only include incomplete goals
        String delimiter = String.format(" %s ", getOperator());
        return goals
            .stream()
            .filter(goal -> !goal.isComplete(game))
            .map(Goal::toString)
            .collect(Collectors.joining(delimiter, "(", ")"));
    }
}
