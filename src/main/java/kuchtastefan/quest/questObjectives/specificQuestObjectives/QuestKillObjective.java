package kuchtastefan.quest.questObjectives.specificQuestObjectives;

import kuchtastefan.character.npc.CharacterDB;
import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.npc.NonPlayerCharacter;
import kuchtastefan.quest.questObjectives.QuestObjective;
import kuchtastefan.utility.ConsoleColor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestKillObjective extends QuestObjective {
    private final Integer questEnemyId;
    private final int countEnemyToKill;
    private int currentCountEnemyProgress;


    public QuestKillObjective(String questObjectiveName, Integer questEnemyId, int countEnemyToKill) {
        super(questObjectiveName);
        this.questEnemyId = questEnemyId;
        this.countEnemyToKill = countEnemyToKill;
    }

    @Override
    public void printQuestObjectiveAssignment(Hero hero) {
        NonPlayerCharacter enemy = CharacterDB.CHARACTER_DB.get(this.questEnemyId);
//        if (currentCountEnemyProgress <= this.countEnemyToKill) {
            System.out.println("\tKill " + this.countEnemyToKill + "x " + enemy.getCharacterRarity() + " "
                    + enemy.getName() + " - " + "You have " + this.currentCountEnemyProgress + " / " + this.countEnemyToKill + " killed ");
//        }
    }

    @Override
    public void checkIfQuestObjectiveIsCompleted(Hero hero) {
        if (countEnemyToKill == currentCountEnemyProgress) {
            System.out.println("\t" + " You completed " + this.getQuestObjectiveName() + " quest objective");
            setCompleted(true);
        }
    }

    public void increaseCurrentCountEnemyProgress() {
        this.currentCountEnemyProgress++;
    }
}
