package kuchtastefan.quest.questGiver;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.hint.HintDB;
import kuchtastefan.hint.HintName;
import kuchtastefan.quest.Quest;
import kuchtastefan.quest.QuestDB;
import kuchtastefan.quest.QuestStatus;
import kuchtastefan.service.QuestMenuService;
import kuchtastefan.utility.ConsoleColor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestGiverCharacter {

    private int questGiverId;
    private String name;
    private final String baseName;
    private int[] questsId;
    private List<Quest> quests;


    public QuestGiverCharacter(String name) {
        this.name = name;
        this.quests = new ArrayList<>();
        this.baseName = name;
    }

    public void questGiverMenu(Hero hero) {
        HintDB.printHint(HintName.QUEST_HINT);

        setQuests();
        QuestMenuService questMenuService = new QuestMenuService();
        questMenuService.setQuestGiverCharacter(this);
        questMenuService.questGiverMenu(hero, this.quests);
        setNameBasedOnQuestsAvailable();
    }

    /**
     * Set name based on Quest progress
     * If quest is not accepted there will appear after name: - ! -
     * If quest is completed but not turned in yet, there will appear after name: - ? -
     * If quest is turned in there will appear after name: - Completed -
     */
    public void setNameBasedOnQuestsAvailable() {
        this.name = this.baseName + returnNameSuffix();
    }

    private String returnNameSuffix() {
        boolean haveQuestAvailable = false;
        boolean haveQuestUnavailable = false;

        for (Quest quest : this.quests) {
            if (quest.getStatus().equals(QuestStatus.UNAVAILABLE)) {
                haveQuestUnavailable = true;
            }

            if (quest.getStatus().equals(QuestStatus.AVAILABLE)) {
                haveQuestAvailable = true;
            }

            if (quest.getStatus().equals(QuestStatus.COMPLETED)) {
                return " -" + ConsoleColor.YELLOW_BOLD_BRIGHT + "?" + ConsoleColor.RESET + "-";
            }
        }

        if (haveQuestAvailable && haveQuestUnavailable) {
            return " -" + ConsoleColor.YELLOW_BOLD_BRIGHT + "!" + ConsoleColor.RESET + "-";
        }

        if (!haveQuestAvailable && haveQuestUnavailable) {
            return " -" + ConsoleColor.WHITE + "!" + ConsoleColor.RESET + "-";
        }

        if (haveQuestAvailable) {
            return ConsoleColor.RESET + " -" + ConsoleColor.YELLOW_BOLD_BRIGHT + "!" + ConsoleColor.RESET + "-";
        }

        return "";
    }

    public boolean checkIfAllAcceptedQuestsAreCompleted(Hero hero) {
        boolean questCompleted = true;
        for (Quest quest : QuestDB.getQuestListByIds(hero.getSaveGameEntities().getHeroQuests().getEntitiesIds())) {
            if (this.quests.contains(quest) && !quest.getStatus().equals(QuestStatus.TURNED_IN)) {
                questCompleted = false;
                break;
            }
        }

        for (Quest quest : this.quests) {
            if (!QuestDB.getQuestListByIds(hero.getSaveGameEntities().getHeroQuests().getEntitiesIds()).contains(quest)) {
                questCompleted = false;
                break;
            }
        }

        return questCompleted;
    }

    public void setQuests() {
        this.quests = QuestDB.getQuestListByIds(this.questsId);
    }
}















