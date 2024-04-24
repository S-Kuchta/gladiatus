package kuchtastefan.service;

import kuchtastefan.ability.Ability;
import kuchtastefan.actions.Action;
import kuchtastefan.character.GameCharacter;
import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.npc.NonPlayerCharacter;
import kuchtastefan.character.npc.enemy.Enemy;
import kuchtastefan.character.spell.CharactersInvolvedInBattle;
import kuchtastefan.character.spell.Spell;
import kuchtastefan.gameSettings.GameSetting;
import kuchtastefan.gameSettings.GameSettingsDB;
import kuchtastefan.hint.HintDB;
import kuchtastefan.hint.HintName;
import kuchtastefan.item.itemFilter.ItemFilter;
import kuchtastefan.item.specificItems.consumeableItem.ConsumableItem;
import kuchtastefan.item.specificItems.consumeableItem.ConsumableItemType;
import kuchtastefan.utility.*;
import lombok.Getter;

import java.util.*;

@Getter
public class BattleService {

    private GameCharacter playerTarget;
    private String selectedEnemyForShowSelection;
    private final List<GameCharacter> enemyList;
    private final List<GameCharacter> alliesList;
    private final List<GameCharacter> tempCharacterList;
    private boolean heroPlay;

    public BattleService() {
        this.selectedEnemyForShowSelection = "A";
        this.enemyList = new ArrayList<>();
        this.alliesList = new ArrayList<>();
        this.tempCharacterList = new ArrayList<>();
        this.heroPlay = true;
    }


    public boolean battle(Hero hero, List<Enemy> enemies) {
        HintDB.printHint(HintName.BATTLE_HINT);

        // Initialize lists for battle
        enemyList.addAll(enemies);
        alliesList.add(hero);

        // Initialize variables for selected hero and enemy
        playerTarget = enemyList.getFirst();

        hero.getBuffsAndDebuffs().clear();

        // Main battle loop
        while (true) {
            battleTurnMechanic(alliesList, enemyList, hero);
            battleTurnMechanic(enemyList, alliesList, hero);

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            // Check if all enemies are defeated
            if (enemyList.isEmpty()) {
                return true; // Battle won
            }

            // Check if hero's health reaches zero
            if (hero.getEffectiveAbilityValue(Ability.HEALTH) <= 0) {
                return false; // Battle lost
            }
        }
    }

    private void battleTurnMechanic(List<GameCharacter> attackingCharacters, List<GameCharacter> defendingCharacters, Hero hero) {
        GameCharacter target = defendingCharacters.getFirst();

        Iterator<GameCharacter> iterator = attackingCharacters.iterator();
        while (iterator.hasNext()) {
            if (!defendingCharacters.isEmpty()) {
                target = defendingCharacters.getFirst();
            }

            GameCharacter attackingCharacter = iterator.next();

            if (attackingCharacter instanceof Hero) {
                heroPlay = true;
            } else {
                npcPrintHeader(attackingCharacter);
            }

            this.printAndPerformActionOverTime(attackingCharacter);
            // If character is alive
            if (attackingCharacter.getEffectiveAbilityValue(Ability.HEALTH) > 0) {

                // If character can't perform action, skip to next character
                if (!attackingCharacter.isCanPerformAction()) {
                    if (heroPlay) {
                        heroPlay = false;
                    }

                    continue;
                }

                // If character can perform action
                if (heroPlay) {
                    playerTurn(hero);
                    target = playerTarget;
                } else {
                    // Select random target
                    if (defendingCharacters.size() > 1) {
                        target = defendingCharacters.get(RandomNumberGenerator.getRandomNumber(0, defendingCharacters.size() - 1));
                    }

                    this.npcUseSpell(attackingCharacter, target, hero);
                }

                checkSpellsCoolDowns(attackingCharacter);
                restoreManaAfterTurn(attackingCharacter);
            }

            if (checkIfCharacterDied(target)) {
                defendingCharacters.remove(target);
                setTarget();
            }

            if (checkIfCharacterDied(attackingCharacter)) {
                iterator.remove();
                setTarget();
            }

            if (defendingCharacters.isEmpty()) {
                break;
            }

            heroPlay = false;
        }

        attackingCharacters.addAll(tempCharacterList);
        tempCharacterList.clear();
    }

    private boolean checkIfCharacterDied(GameCharacter characterToCheck) {
        if (characterToCheck.getEffectiveAbilityValue(Ability.HEALTH) <= 0) {
            System.out.println();
            System.out.println("\t" + ConsoleColor.RED + characterToCheck.getName() + " died!" + ConsoleColor.RESET);
            return true;
        } else {
            return false;
        }
    }

    private void setTarget() {
        if (!enemyList.isEmpty()) {
            playerTarget = enemyList.getFirst();
            selectedEnemyForShowSelection = "A";
        }
    }

    private void playerTurn(Hero hero) {
        InventoryService inventoryService = new InventoryService();

        // Loop for hero's actions
        while (true) {
            printBattleMenu(hero);

            // Get user's choice
            String choice = InputUtil.stringScanner().toUpperCase();
            if (choice.matches("\\d+")) {
                try {
                    PrintUtil.printExtraLongDivider();
                    int parsedChoice = Integer.parseInt(choice);
                    if (parsedChoice == hero.getCharacterSpellList().size()) {

                        // If choice is for consumable items, open inventory menu
                        if (hero.getHeroInventory().selectItem(hero, ConsumableItem.class, new ItemFilter(ConsumableItemType.POTION), inventoryService, 1)) {
                            break;
                        }

                    } else {
                        // If choice is for a spell, use the spell on the enemy
                        if (hero.getCharacterSpellList().get(parsedChoice).useSpell(
                                new CharactersInvolvedInBattle(hero, this.playerTarget, enemyList, alliesList, tempCharacterList))) {

                            break;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    PrintUtil.printEnterValidInput();
                }
            } else {
                // Handle special commands
                if (choice.equals("X")) {
                    GameSettingsDB.setTrueOrFalse(GameSetting.SHOW_INFORMATION_ABOUT_ACTION_NAME);
                } else if (choice.equals("Y")) {
                    GameSettingsDB.setTrueOrFalse(GameSetting.HIDE_SPELLS_ON_COOL_DOWN);
                } else {
                    try {
                        selectedEnemyForShowSelection = choice;
                        playerTarget = enemyList.get(LetterToNumber.valueOf(choice).getValue() - 1);
                    } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                        selectedEnemyForShowSelection = "A";
                        playerTarget = enemyList.getFirst();
                        PrintUtil.printEnterValidInput();
                    }
                }
            }
        }
    }

    private void npcPrintHeader(GameCharacter gameCharacter) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        PrintUtil.printLongDivider();
        System.out.println("\t\t" + ConsoleColor.YELLOW_UNDERLINED
                + gameCharacter.getName() + " Turn!"
                + ConsoleColor.RESET);
        System.out.println();
    }

    private void printAndPerformActionOverTime(GameCharacter gameCharacter) {
        if (!gameCharacter.getBuffsAndDebuffs().isEmpty()) {
            System.out.println("\t_____ " + gameCharacter.getName() + " buffs and debuffs _____");
            System.out.println();
        }

        gameCharacter.performActionsWithDuration(true);
    }

    private void restoreManaAfterTurn(GameCharacter gameCharacter) {
        gameCharacter.restoreHealthAndManaAfterTurn();
    }

    private void printBattleMenu(Hero hero) {
        // Print hero's header with stats and buffs
        PrintUtil.printCurrentAbilityPointsWithItems(hero);
        PrintUtil.printBattleBuffs(hero);

        // Print alies header with stats and buffs
        for (GameCharacter gameCharacter : alliesList) {
            if (!(gameCharacter instanceof Hero)) {
                PrintUtil.printHeaderWithStatsBar(gameCharacter);
                PrintUtil.printBattleBuffs(gameCharacter);
            }
        }

        // Print enemy's header with stats and buffs
        PrintUtil.printExtraLongDivider();
        System.out.printf("%70s %n", ConsoleColor.RED + "Enemy" + ConsoleColor.RESET);
        PrintUtil.printHeaderWithStatsBar(playerTarget);
        PrintUtil.printBattleBuffs(playerTarget);
        PrintUtil.printExtraLongDivider();

        int index = 1;
        // Print available enemies for selection
        for (GameCharacter enemyFromList : enemyList) {
            if (enemyFromList instanceof NonPlayerCharacter nonPlayerCharacter) {
                if (!nonPlayerCharacter.isDefeated()) {

                    ConsoleColor consoleColor = ConsoleColor.RESET;
                    ConsoleColor healthsColor = ConsoleColor.RESET;
                    if (Objects.equals(LetterToNumber.getStringFromValue(index), selectedEnemyForShowSelection)) {
                        consoleColor = ConsoleColor.WHITE_BRIGHT;
                        healthsColor = ConsoleColor.RED;
                    }

                    String enemy = consoleColor + enemyFromList.getName() + " - "
                            + nonPlayerCharacter.getCharacterRarity() + " - "
                            + healthsColor + "Healths: "
                            + enemyFromList.getEffectiveAbilityValue(Ability.HEALTH) + ConsoleColor.RESET;

                    PrintUtil.printIndexAndText(LetterToNumber.getStringFromValue(index), enemy);
                    index++;
                }
            }
        }

        PrintUtil.printSpellGameSettings();

        // Print hero's spells with descriptions
        int spellIndex = 0;
        System.out.println();
        for (Spell spell : hero.getCharacterSpellList()) {

            // Check if spells should be hidden when on CoolDown
            if (GameSettingsDB.returnGameSettingValue(GameSetting.HIDE_SPELLS_ON_COOL_DOWN)) {
                if (spell.isCanSpellBeCasted()) {
                    System.out.print(ConsoleColor.CYAN + "\t" + spellIndex + ". " + ConsoleColor.RESET);
                    PrintUtil.printSpellDescription(hero, this.playerTarget, spell);
                    System.out.println();
                }
            } else {
                // Print all spells
                System.out.print(ConsoleColor.CYAN + "\t" + spellIndex + ". " + ConsoleColor.RESET);
                PrintUtil.printSpellDescription(hero, this.playerTarget, spell);
                System.out.println();
            }

            spellIndex++;
        }

        // Print option for potions menu
        PrintUtil.printIndexAndText(String.valueOf(spellIndex), "Potions Menu");
        System.out.println();
    }

    /**
     * Determines the spell the enemy character will cast during battle based on certain criteria.
     * Evaluates factors such as potential damage, healing ability, and utility of each spell.
     * The spell with the highest priority points is selected for casting.
     *
     * @param spellCaster The enemy character casting the spell.
     * @param spellTarget The target of the spell (usually the player's character).
     */
    private void npcUseSpell(GameCharacter spellCaster, GameCharacter spellTarget, Hero hero) {
        Map<Spell, Integer> spells = new HashMap<>();

        Spell spellToCast = spellCaster.getCharacterSpellList().getFirst();

        int priorityPoints;

        // Evaluate each spell based on various criteria and assign priority points
        if (spellCaster.getCharacterSpellList().size() == 1) {
            spellToCast = spellCaster.getCharacterSpellList().getFirst();
        } else {
            for (Spell spell : spellCaster.getCharacterSpellList()) {
                spells.put(spell, 0);
            }

            for (Map.Entry<Spell, Integer> spellIntegerEntry : spells.entrySet()) {
                if (spellIntegerEntry.getKey().isCanSpellBeCasted()) {
                    priorityPoints = 0;
                    for (Action action : spellIntegerEntry.getKey().getSpellActions()) {
                        priorityPoints += action.returnPriorityPoints(spellCaster, spellTarget);
                    }

                    // Determine the spell with the highest priority points
                    spellIntegerEntry.setValue(spellIntegerEntry.getValue() + priorityPoints);
                    if (spellIntegerEntry.getValue() > spells.get(spellToCast)) {
                        spellToCast = spellIntegerEntry.getKey();
                    }
                }
            }
        }

        spellToCast.useSpell(new CharactersInvolvedInBattle(hero, spellCaster, spellTarget, alliesList, enemyList, tempCharacterList));
    }

    private void checkSpellsCoolDowns(GameCharacter gameCharacter) {
        for (Spell spell : gameCharacter.getCharacterSpellList()) {
            spell.increaseSpellCoolDown();
        }
    }

    public void resetSpellsCoolDowns(Hero hero) {
        for (Spell spell : hero.getCharacterSpellList()) {
            spell.setCurrentTurnCoolDown(spell.getTurnCoolDown() + 1);
            spell.checkSpellCoolDown();
        }
    }
}
