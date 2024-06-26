package kuchtastefan.service;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.hero.HeroAbilityManager;
import kuchtastefan.character.spell.HeroSpellManager;
import kuchtastefan.gameSettings.GameSetting;
import kuchtastefan.gameSettings.GameSettingsDB;
import kuchtastefan.hint.HintDB;
import kuchtastefan.hint.HintName;
import kuchtastefan.utility.ConsoleColor;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.printUtil.CharacterPrint;
import kuchtastefan.utility.printUtil.GameSettingsPrint;
import kuchtastefan.utility.printUtil.PrintUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class HeroMenuService {
    private final InventoryMenuService inventoryMenuService;
    private final HeroAbilityManager heroAbilityManager;
    private final QuestMenuService questMenuService;
    private final HeroSpellManager heroSpellManager;

    public HeroMenuService(HeroAbilityManager heroAbilityManager) {
        this.inventoryMenuService = new InventoryMenuService();
        this.heroAbilityManager = heroAbilityManager;
        this.questMenuService = new QuestMenuService();
        this.heroSpellManager = new HeroSpellManager();
    }

    public void heroCharacterMenu(Hero hero) {
        HintDB.printHint(HintName.HERO_MENU);

        PrintUtil.printLongDivider();
        System.out.printf("%30s %n", ConsoleColor.YELLOW + "Hero menu" + ConsoleColor.RESET);
        PrintUtil.printLongDivider();
        PrintUtil.printMenuOptions("Go back", "Hero Info", "Inventory", "Abilities", "Quests", "Spells", "Game Settings");

        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> {
                showHeroInfo(hero);
                heroCharacterMenu(hero);
            }
            case 2 -> this.inventoryMenuService.mainMenu(hero);
            case 3 -> this.abilityMenu(hero);
            case 4 -> this.questMenuService.heroAcceptedQuestMenu(hero);
            case 5 -> this.heroSpellManager.spellMenu(hero);
            case 6 -> this.gameSettingsMenu();
            default -> PrintUtil.printEnterValidInput();
        }
    }

    private void abilityMenu(Hero hero) {

        PrintUtil.printMenuOptions("Go back", "Spend points (" + hero.getUnspentAbilityPoints() + " points left)", "Remove points");

        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> heroCharacterMenu(hero);
            case 1 -> this.heroAbilityManager.spendAbilityPoints();
            case 2 -> this.heroAbilityManager.removeAbilityPoints();
            default -> PrintUtil.printEnterValidInput();
        }
    }

    private void showHeroInfo(Hero hero) {
        CharacterPrint.printHeaderWithStatsBar(hero);
        CharacterPrint.printEffectiveAbilityPoints(hero);
        CharacterPrint.printCurrentWearingArmor(hero);
    }

    private void gameSettingsMenu() {
        while (true) {
            List<GameSetting> gameSettingList = new ArrayList<>();
            PrintUtil.printIndexAndText("0", "Go back");
            System.out.println();

            int index = 1;
            for (Map.Entry<GameSetting, Boolean> gameSettings : GameSettingsDB.getGAME_SETTINGS_DB().entrySet()) {
                gameSettingList.add(gameSettings.getKey());
                PrintUtil.printIndexAndText(String.valueOf(index), gameSettings.getKey().toString() + " - ");
                GameSettingsPrint.printYesNoSelection(gameSettings.getValue());
                System.out.println();
                index++;
            }

            PrintUtil.printIndexAndText(String.valueOf(index), "Reset all Hints");
            System.out.println();

            int choice = InputUtil.intScanner();
            if (choice == 0) {
                break;
            } else if (choice == index) {
                HintDB.resetAllHints();
            } else {
                GameSettingsDB.setTrueOrFalse(gameSettingList.get(choice - 1));
            }
        }
    }
}
