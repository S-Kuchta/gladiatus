package kuchtastefan.quest.questObjectives;

import kuchtastefan.characters.hero.Hero;
import kuchtastefan.items.questItem.QuestItem;
import kuchtastefan.regions.locations.LocationType;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class QuestBringItemObjective extends QuestObjective {

    private final QuestItem itemDropNeeded;
    private final QuestEnemy[] itemDropFromEnemy;
    private final int itemDropCountNeeded;
    private final LocationType[] locationType;


    public QuestBringItemObjective(String questObjectiveName, QuestEnemy[] itemDropFromEnemy,
                                   LocationType[] locationType, QuestItem itemDropNeeded, int itemDropCountNeeded) {
        super(questObjectiveName);
        this.locationType = locationType;
        this.itemDropNeeded = itemDropNeeded;
        this.itemDropCountNeeded = itemDropCountNeeded;
        this.itemDropFromEnemy = itemDropFromEnemy;
    }

    @Override
    public void printQuestObjectiveAssignment(Hero hero) {

        hero.getHeroInventory().getHeroInventory().putIfAbsent(this.itemDropNeeded, 0);
        if (hero.getHeroInventory().getHeroInventory().get(this.itemDropNeeded) < this.itemDropCountNeeded) {
            System.out.println("\tBring " + this.itemDropCountNeeded + "x " + this.itemDropNeeded.getName() + " - You have: "
                    + hero.getHeroInventory().getHeroInventory().get(this.itemDropNeeded) + " / " + this.itemDropCountNeeded);
        } else {
            System.out.println("\tBring " + this.itemDropCountNeeded + "x " + this.itemDropNeeded.getName() + " - You have: "
                    + this.itemDropCountNeeded + " / " + this.itemDropCountNeeded);
        }
    }

    @Override
    public void checkIfQuestObjectiveIsCompleted(Hero hero) {
        if (hero.getHeroInventory().checkIfHeroInventoryContainsNeededItemsIfTrueRemoveIt(
                new HashMap<>(Map.of(this.itemDropNeeded, this.itemDropCountNeeded)), false)) {

            System.out.println("\t--> You completed " + getQuestObjectiveName() + " quest objective <--");
            setCompleted(true);
        } else {
            setCompleted(false);
        }
    }

    @Override
    public void removeCompletedItemsOrEnemies(Hero hero) {
        hero.getHeroInventory().checkIfHeroInventoryContainsNeededItemsIfTrueRemoveIt(
                new HashMap<>(Map.of(this.itemDropNeeded, this.itemDropCountNeeded)), true);
    }

    public boolean checkLocation(LocationType locationTypeParam) {
        return Arrays.asList(this.locationType).contains(locationTypeParam);
    }

    public boolean checkEnemy(QuestEnemy enemyName) {
        return Arrays.asList(this.itemDropFromEnemy).contains(enemyName);
    }
}
