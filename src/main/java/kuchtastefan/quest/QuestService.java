package kuchtastefan.quest;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.item.Item;
import kuchtastefan.item.ItemDB;
import kuchtastefan.quest.questGiver.QuestGiverCharacter;
import kuchtastefan.quest.questObjectives.QuestBringItemFromEnemyObjective;
import kuchtastefan.quest.questObjectives.QuestKillObjective;
import kuchtastefan.quest.questObjectives.QuestObjective;
import kuchtastefan.quest.questObjectives.RemoveObjectiveProgress;
import kuchtastefan.utility.ConsoleColor;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class QuestService {

    private QuestGiverCharacter questGiverCharacter;

    public void questGiverMenu(Hero hero, List<Quest> quests, String name) {
        PrintUtil.printDivider();
        System.out.println("\t\t\t" + name);
        PrintUtil.printDivider();

        printQuestsMenu(hero, quests);
        while (true) {
            try {
                int choice = InputUtil.intScanner();
                if (choice == 0) {
                    break;
                }

                if (canBeQuestAccepted(quests.get(choice - 1), hero)) {
                    if (hero.getHeroAcceptedQuest().containsValue(quests.get(choice - 1))) {
                        this.selectedQuestForQuestGiverMenu(hero.getHeroAcceptedQuest()
                                .get(quests.get(choice - 1).getQuestId()), hero, quests, name);
                    } else {
                        this.selectedQuestForQuestGiverMenu(quests.get(choice - 1), hero, quests, name);
                    }
                } else {
                    System.out.println(ConsoleColor.RED + "\tQuest can not be accepted yet!" + ConsoleColor.RESET);
                    this.questGiverMenu(hero, quests, name);
                }

                this.questGiverCharacter.setNameBasedOnQuestsAvailable(hero);
                break;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("\tEnter valid input");
            }
        }
    }

    public void heroAcceptedQuestMenu(Hero hero, Map<Integer, Quest> questMap) {
        List<Quest> quests = new ArrayList<>();
        for (Map.Entry<Integer, Quest> questEntry : questMap.entrySet()) {
            quests.add(questEntry.getValue());
        }

        PrintUtil.printLongDivider();
        System.out.println("\t-- Quest Details --");
        PrintUtil.printLongDivider();

        while (true) {
            try {
                PrintUtil.printLongDivider();
                printQuestsMenu(hero, quests);
                int choice = InputUtil.intScanner();
                if (choice == 0) {
                    break;
                }

                if (!quests.get(choice - 1).isTurnedIn()) {
                    printQuestDetails(quests.get(choice - 1), hero);
                } else {
                    System.out.println("\t-- Quest " + quests.get(choice - 1).getQuestName() + " Completed --");
                }

            } catch (IndexOutOfBoundsException e) {
                System.out.println("\tEnter valid input");
            }
        }
    }

    private void printQuestsMenu(Hero hero, List<Quest> quests) {
        PrintUtil.printIndexAndText("0", "Go back");
        System.out.println();

        int index = 1;
        for (Quest quest : quests) {
            if (canBeQuestAccepted(quest, hero)) {

                PrintUtil.printIndexAndText(String.valueOf(index),
                        quest.getQuestName() + " (recommended level: " + quest.getQuestLevel() + ")");
                if (quest.isTurnedIn()) {
                    System.out.print(" -- Completed --");
                } else if (!hero.getHeroAcceptedQuest().containsValue(quest)) {
                    System.out.print(" - " + ConsoleColor.YELLOW_BOLD_BRIGHT + "!" + ConsoleColor.RESET + " - ");
                } else if (hero.getHeroAcceptedQuest().containsValue(quest) && quest.isQuestCompleted()) {
                    System.out.print(" - " + ConsoleColor.YELLOW_BOLD_BRIGHT + "?" + ConsoleColor.RESET + " - ");
                }
            } else {
                PrintUtil.printIndexAndText(String.valueOf(index),
                        quest.getQuestName() + " (recommended level: " + quest.getQuestLevel() + ")");
                System.out.print(ConsoleColor.WHITE + " - ! - " + ConsoleColor.RESET);
            }

            index++;
            System.out.println();
        }
    }

    /**
     * Method is responsible for Accepting or Completing selected quest.
     * If hero does not contain selected quest you can to take it
     * If you have quest completed but not turned it yet, you can turn in
     *
     * @param quest  quest which come dynamically depending on you choice
     * @param quests only needed for switching between menus
     * @param name   same as quests
     */
    private void selectedQuestForQuestGiverMenu(Quest quest, Hero hero, List<Quest> quests, String name) {

        if (!quest.isTurnedIn()) {
            this.printQuestDetails(quest, hero);
            PrintUtil.printIndexAndText("0", "Go back");
            System.out.println();
            if (!quest.isQuestCompleted() && !hero.getHeroAcceptedQuest().containsValue(quest)) {
                PrintUtil.printIndexAndText("1", "Accept quest");
                System.out.println();
            } else if (!quest.isTurnedIn() && quest.isQuestCompleted()) {
                PrintUtil.printIndexAndText("1", "Complete quest");
                System.out.println();
            }
        } else {
            System.out.println("\t-- Quest " + quest.getQuestName() + " Completed --");
            PrintUtil.printIndexAndText("0", "Go back");
            System.out.println();
        }

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> this.questGiverMenu(hero, quests, name);
            case 1 -> {
                if (!quest.isQuestCompleted() && !hero.getHeroAcceptedQuest().containsValue(quest)) {
                    System.out.println("\t\t --> Quest accepted <--");
                    this.startTheQuest(quest, hero);
                    this.questGiverMenu(hero, quests, name);
                } else if (!quest.isTurnedIn() && quest.isQuestCompleted()) {
                    this.turnInTheQuest(quest, hero);
                    this.questGiverMenu(hero, quests, name);
                }
            }
            default -> System.out.println("\tEnter valid input");
        }
    }

    private void startTheQuest(Quest quest, Hero hero) {
        if (!hero.getHeroAcceptedQuest().containsValue(quest)) {
            hero.getHeroAcceptedQuest().put(quest.getQuestId(), quest);
            hero.checkIfQuestObjectivesAndQuestIsCompleted();
        }
    }

    /**
     * Turn in the quest, give quest reward to hero and remove items/killed enemy etc. from hero
     *
     * @param quest quest to turn in
     */
    private void turnInTheQuest(Quest quest, Hero hero) {
        quest.turnInTheQuestAndGiveReward(hero);
        quest.setTurnedIn(true);
        for (QuestObjective questObjective : quest.getQuestObjectives()) {
            if (questObjective instanceof RemoveObjectiveProgress) {
                ((RemoveObjectiveProgress) questObjective).removeCompletedQuestObjectiveAssignment(hero);
            }
        }
    }

    private void printQuestDetails(Quest quest, Hero hero) {
        PrintUtil.printLongDivider();
        System.out.println("\t\t\t\t------ " + quest.getQuestName() + " ------");
        System.out.print("\t");
        PrintUtil.printTextWrap(quest.getQuestDescription());
        System.out.println();

        for (QuestObjective questObjective : quest.getQuestObjectives()) {
            questObjective.printQuestObjectiveAssignment(hero);
        }
    }

    private boolean canBeQuestAccepted(Quest quest, Hero hero) {

        if (quest instanceof QuestChain) {
            return ((QuestChain) quest).canBeQuestAccepted(hero);
        }

        return (quest.getQuestLevel() - 1) <= hero.getLevel();
    }

    /**
     * check if enemy killed in CombatEvent belongs to some of accepted Quest.
     * If yes increase current count progress in questObjective
     * and print QuestObjectiveAssignment with QuestObjective progress.
     * If you need to use this method, Use it always before checkIfQuestObjectivesAndQuestIsCompleted() method.
     */
    public void checkQuestProgress(Integer questEnemyId, Map<Integer, Quest> heroAcceptedQuests, Hero hero) {
        for (Map.Entry<Integer, Quest> questMap : heroAcceptedQuests.entrySet()) {
            for (QuestObjective questObjective : questMap.getValue().getQuestObjectives()) {
                if (!questObjective.isCompleted()) {
                    if (questObjective instanceof QuestKillObjective
                            && ((QuestKillObjective) questObjective).getQuestEnemyId().equals(questEnemyId)) {

                        ((QuestKillObjective) questObjective).increaseCurrentCountEnemyProgress();
                        questObjective.printQuestObjectiveAssignment(hero);
                    }

                    if (questObjective instanceof QuestBringItemFromEnemyObjective
                            && ((QuestBringItemFromEnemyObjective) questObjective).checkEnemy(questEnemyId)) {

                        Item questItem = ItemDB.returnItemFromDB(
                                ((QuestBringItemFromEnemyObjective) questObjective).getObjectiveItemId());
                        System.out.println("\t-- You loot " + (questItem.getName() + " --"));

                        hero.getHeroInventory().addItemWithNewCopyToItemList((questItem));
                        questObjective.printQuestObjectiveAssignment(hero);
                    }
                }
            }
        }
    }
}
