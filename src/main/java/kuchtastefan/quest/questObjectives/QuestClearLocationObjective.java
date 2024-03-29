package kuchtastefan.quest.questObjectives;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.world.location.LocationDB;
import kuchtastefan.utility.ConsoleColor;
import lombok.Getter;

@Getter
public class QuestClearLocationObjective extends QuestObjective {
    private final Integer locationId;

    public QuestClearLocationObjective(String questObjectiveName, Integer locationId) {
        super(questObjectiveName);
        this.locationId = locationId;
    }

    @Override
    public void checkIfQuestObjectiveIsCompleted(Hero hero) {
        if (!hero.getDiscoveredLocationList().isEmpty()
                && hero.getDiscoveredLocationList().get(this.locationId) != null) {

            if (hero.getDiscoveredLocationList().get(this.locationId).isCleared()) {
                System.out.println("\t" + " You completed "
                        + ConsoleColor.YELLOW + getQuestObjectiveName() + ConsoleColor.RESET
                        + " quest objective ");
                this.setCompleted(true);
            }
        }
    }

    @Override
    public void printQuestObjectiveAssignment(Hero hero) {
        String cleared = this.isCompleted() ? "Cleared" : "Not Cleared Yet";
        System.out.println("\tClear "
                + ConsoleColor.YELLOW
                + LocationDB.returnLocation(this.locationId).getLocationName()
                + ConsoleColor.RESET
                + " -> " + cleared);
    }
}
