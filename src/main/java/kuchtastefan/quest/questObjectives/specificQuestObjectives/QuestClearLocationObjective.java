package kuchtastefan.quest.questObjectives.specificQuestObjectives;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.quest.questObjectives.QuestObjective;
import kuchtastefan.world.location.LocationDB;
import kuchtastefan.world.location.LocationStatus;
import lombok.Getter;

@Getter
public class QuestClearLocationObjective extends QuestObjective {
    private final Integer locationId;

    public QuestClearLocationObjective(String questObjectiveName, Integer locationId, int id) {
        super(id, questObjectiveName);
        this.locationId = locationId;
    }

    @Override
    public void printQuestObjectiveProgress(Hero hero) {
        String cleared = this.isCompleted() ? "Cleared" : "Not Cleared Yet";
        System.out.println("\tClear " + LocationDB.returnLocation(this.locationId).getLocationName() + " -> " + cleared);
    }

    @Override
    public void verifyQuestObjectiveCompletion(Hero hero) {
        if (!hero.getDiscoveredLocationList().isEmpty() && hero.getDiscoveredLocationList().get(this.locationId) != null) {
            if (hero.getDiscoveredLocationList().get(this.locationId).isCompleted()) {
                this.setCompleted(hero, true);
            }
        }
    }
}
