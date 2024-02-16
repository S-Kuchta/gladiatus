package kuchtastefan.characters.spell;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kuchtastefan.characters.hero.Hero;
import kuchtastefan.gameSettings.GameSettings;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.LetterToNumberSpellLevel;
import kuchtastefan.utility.PrintUtil;
import kuchtastefan.utility.RuntimeTypeAdapterFactoryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeroSpellManager {

    public void spellMenu(Hero hero) {
        PrintUtil.printIndexAndText("0", "Go back");
        System.out.println();
        PrintUtil.printIndexAndText("1", "Learn new Spell");
        System.out.println();
        PrintUtil.printIndexAndText("2", "Reset spells");
        System.out.println();
        PrintUtil.printIndexAndText("3", "Show learned spells");
        System.out.println();
        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> learnNewSpell(hero);
            case 2 -> resetSpells(hero);
            case 3 -> checkLearnedSpells(hero);
            default -> PrintUtil.printEnterValidInput();
        }
    }

    /**
     * Allows the hero to learn new spells based on their class and level. Displays a list of available spells
     * according to the hero's class and current level. The hero can select a spell to learn, provided they meet
     * the minimum level requirement. Additionally, the hero can toggle options to show/hide spell descriptions
     * and spells on CoolDown. The method continuously prompts the hero for input until they choose to go back.
     *
     * @param hero The hero who is learning new spells.
     */
    private void learnNewSpell(Hero hero) {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(
                RuntimeTypeAdapterFactoryUtil.actionsRuntimeTypeAdapterFactory).create();

        List<Spell> spellsByClass = new ArrayList<>();
        for (Spell spellForAdd : SpellsList.getSpellList()) {
            if (spellForAdd.getSpellClass().equals(hero.getCharacterClass())) {
                spellsByClass.add(spellForAdd);
            }
        }

        int levelChosen = 1;
        List<Spell> spellsByClassAndLevel = returnListWithSpellsFulfillingRequirements(levelChosen, spellsByClass);

        while (true) {
            PrintUtil.printExtraLongDivider();
            for (LetterToNumberSpellLevel letterToNumberSpellLevel : LetterToNumberSpellLevel.values()) {
                PrintUtil.printIndexAndText(letterToNumberSpellLevel.name(), "Spell level: " + letterToNumberSpellLevel.getValue() + ", ");
            }

            System.out.println();
            PrintUtil.printIndexAndText("X", "Hide action description - ");
            PrintUtil.printGameSettings(GameSettings.isShowInformationAboutActionName());
            System.out.print("\t");
            PrintUtil.printIndexAndText("Y", "Hide spells on CoolDown - ");
            PrintUtil.printGameSettings(GameSettings.isHideSpellsOnCoolDown());

            System.out.print("\n\n");
            PrintUtil.printIndexAndText("0", "Go back");
            System.out.println();

            int index = 1;
            for (Spell spell : spellsByClassAndLevel) {
                PrintUtil.printIndexAndText(String.valueOf(index), "");
                PrintUtil.printSpellDescription(hero, spell);
                index++;
                System.out.println();
            }

            String choice = InputUtil.stringScanner().toUpperCase();
            if (choice.matches("\\d+")) {
                try {
                    int parsedChoice = Integer.parseInt(choice);
                    System.out.println(parsedChoice);

                    if (parsedChoice == 0) {
                        hero.getCharacterSpellList().removeIf(spell -> spell.getSpellLevel() != 0);
                        for (Map.Entry<Integer, Spell> spellEntry : hero.getLearnedSpells().entrySet()) {
                            hero.getCharacterSpellList().add(gson.fromJson(gson.toJson(spellEntry.getValue()), Spell.class));
                        }
                        break;
                    }

                    Spell spellChosen = spellsByClassAndLevel.get(parsedChoice - 1);
                    if (spellChosen.getSpellLevel() > hero.getLevel()) {
                        System.out.println("\tYou don't meet minimum level requirements!");
                    } else {
                        if (hero.getLearnedSpells().containsKey(spellChosen.getSpellLevel())) {
                            System.out.println("\tYou unlearned " + hero.getLearnedSpells().get(levelChosen).getSpellName());
                        }
                        System.out.println("\tYou learned " + spellChosen.getSpellName());
                        hero.getLearnedSpells().put(spellChosen.getSpellLevel(), spellChosen);
                    }
                } catch (IndexOutOfBoundsException e) {
                    PrintUtil.printEnterValidInput();
                }
            } else {
                try {
                    if (choice.equals("X")) {
                        GameSettings.setShowInformationAboutActionName();
                    } else if (choice.equals("Y")) {
                        GameSettings.setHideSpellsOnCoolDown();
                    } else {
                        levelChosen = LetterToNumberSpellLevel.valueOf(choice).getValue();
                        spellsByClassAndLevel = returnListWithSpellsFulfillingRequirements(levelChosen, spellsByClass);
                    }
                } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                    PrintUtil.printEnterValidInput();
                }
            }
        }
    }

    private List<Spell> returnListWithSpellsFulfillingRequirements(int level, List<Spell> spellsByClass) {
        List<Spell> spellListByClassAndLevel = new ArrayList<>();
        for (Spell spell : spellsByClass) {
            if (spell.getSpellLevel() == level) {
                spellListByClassAndLevel.add(spell);
            }
        }
        return spellListByClassAndLevel;
    }

    private void resetSpells(Hero hero) {
        hero.getLearnedSpells().clear();
        hero.getCharacterSpellList().removeIf(spell -> spell.getSpellLevel() != 0);
    }

    private void checkLearnedSpells(Hero hero) {
        for (Spell spell : hero.getCharacterSpellList()) {
            System.out.print("\t");
            PrintUtil.printSpellDescription(hero, spell);
            System.out.println();
        }
    }
}
