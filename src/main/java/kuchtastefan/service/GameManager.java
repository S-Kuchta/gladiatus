package kuchtastefan.service;

import kuchtastefan.character.hero.*;
import kuchtastefan.character.npc.vendor.VendorDB;
import kuchtastefan.character.npc.vendor.specificVendorCharacter.ConsumableVendorCharacter;
import kuchtastefan.character.npc.vendor.specificVendorCharacter.CraftingReagentItemVendorCharacter;
import kuchtastefan.character.npc.vendor.specificVendorCharacter.JunkVendorCharacter;
import kuchtastefan.character.spell.Spell;
import kuchtastefan.character.spell.SpellDB;
import kuchtastefan.constant.Constant;
import kuchtastefan.gameSettings.GameSettingsDB;
import kuchtastefan.hint.HintName;
import kuchtastefan.hint.HintDB;
import kuchtastefan.item.ItemDB;
import kuchtastefan.item.consumeableItem.ConsumableItem;
import kuchtastefan.item.consumeableItem.ConsumableItemType;
import kuchtastefan.item.craftingItem.CraftingReagentItem;
import kuchtastefan.item.craftingItem.CraftingReagentItemType;
import kuchtastefan.item.junkItem.JunkItem;
import kuchtastefan.quest.QuestDB;
import kuchtastefan.quest.questGiver.QuestGiverCharacterDB;
import kuchtastefan.utility.ConsoleColor;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;
import kuchtastefan.world.region.RegionDB;
import kuchtastefan.world.region.RegionService;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Hero hero;
    private final HeroAbilityManager heroAbilityManager;
    private final FileService fileService;
    private final BlacksmithService blacksmithService;
    private final HeroMenuService heroMenuService;


    public GameManager() {
        this.hero = new Hero("");
        this.fileService = new FileService();
        this.heroAbilityManager = new HeroAbilityManager(this.hero);
        this.blacksmithService = new BlacksmithService();
        this.heroMenuService = new HeroMenuService(this.heroAbilityManager);
    }

    public void startGame() {
        this.initGame();

        while (true) {
            this.fileService.autoSave(this.hero);
            QuestGiverCharacterDB.setAllQuestGiversName(this.hero);

            PrintUtil.printLongDivider();
            System.out.println("\t\t\t\t\t\t\t------ Mystic Hollow ------");
            PrintUtil.printLongDivider();
            PrintUtil.printIndexAndText("0", "Explore surrounding regions");
            System.out.println();
            PrintUtil.printIndexAndText("1", "Hero menu");
            System.out.println();
            PrintUtil.printIndexAndText("2", "Junk Merchant");
            System.out.println();
            PrintUtil.printIndexAndText("3", "Tavern");
            System.out.println();
            PrintUtil.printIndexAndText("4", "Alchemist");
            System.out.println();
            PrintUtil.printIndexAndText("5", "Blacksmith");
            System.out.println();
            PrintUtil.printIndexAndText("6", "Save game");
            System.out.println();
            PrintUtil.printIndexAndText("7", "Exit game");
            System.out.println();

            final int choice = InputUtil.intScanner();
            switch (choice) {
                case 0 -> exploreSurroundingRegions();
                case 1 -> this.heroMenuService.heroCharacterMenu(this.hero);
                case 2 -> {
//                    JunkVendorCharacter junkVendorCharacter = new JunkVendorCharacter("Dazres Heitholt", 8,
//                            ItemDB.returnJunkItemListByItemLevel(this.hero.getLevel(), 0), JunkItem.class);
//                    junkVendorCharacter.vendorMenu(this.hero);
                }
                case 3 -> this.tavernMenu();
                case 4 -> this.alchemistMenu();
                case 5 -> this.blacksmithService.blacksmithMenu(this.hero);
                case 6 -> this.fileService.saveGame(this.hero);
                case 7 -> {
                    System.out.println("Are you sure?");
                    PrintUtil.printIndexAndText("0", "No");
                    System.out.println();
                    PrintUtil.printIndexAndText("1", "Yes");
                    System.out.println();
                    final int exitChoice = InputUtil.intScanner();
                    if (exitChoice == 0) {
                        continue;
                    }
                    if (exitChoice == 1) {
                        System.out.println("\tBye");
                        return;
                    }
                }
                default -> PrintUtil.printEnterValidInput();
            }
        }
    }

    private void exploreSurroundingRegions() {
        PrintUtil.printIndexAndText("0", "Go back to the city");
        System.out.println();
        PrintUtil.printIndexAndText("1", "Go to " + RegionDB.returnRegionName(0));
        System.out.println();
        PrintUtil.printIndexAndText("2", "Go to highlands");
        System.out.println();

        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> new RegionService().adventuringAcrossTheRegion(heroMenuService, RegionDB.returnRegion(0), this.hero);
            default -> PrintUtil.printEnterValidInput();
        }
    }

    private void tavernMenu() {
//        final ConsumableVendorCharacter cityFoodVendor = new ConsumableVendorCharacter("Ved Of Kaedwen", 8,
//                ItemDB.returnConsumableItemListByTypeAndItemLevel(ConsumableItemType.FOOD, this.hero.getLevel(), null), ConsumableItem.class);

        PrintUtil.printDivider();
        System.out.println("\t\tTavern");
        PrintUtil.printDivider();

        PrintUtil.printIndexAndText("0", "Go back");
        System.out.println();
//        PrintUtil.printIndexAndText("1", cityFoodVendor.getName() + " (Food Merchant)");
        System.out.println();
        PrintUtil.printIndexAndText("2", QuestGiverCharacterDB.returnQuestGiverName(1));
        System.out.println();
        PrintUtil.printIndexAndText("3", QuestGiverCharacterDB.returnQuestGiverName(2));
        System.out.println();

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
//            case 1 -> cityFoodVendor.vendorMenu(this.hero);
            case 2 -> QuestGiverCharacterDB.returnQuestGiverMenu(1, this.hero);
            case 3 -> QuestGiverCharacterDB.returnQuestGiverMenu(2, this.hero);
        }
    }

    private void alchemistMenu() {
//        final CraftingReagentItemVendorCharacter cityAlchemistReagentVendor = new CraftingReagentItemVendorCharacter("Meeden", 8,
//                ItemDB.returnCraftingReagentItemListByTypeAndItemLevel(CraftingReagentItemType.ALCHEMY_REAGENT, this.hero.getLevel(), 0), CraftingReagentItem.class);
//        final ConsumableVendorCharacter cityPotionsVendor = new ConsumableVendorCharacter("Etaefush", 8,
//                ItemDB.returnConsumableItemListByTypeAndItemLevel(ConsumableItemType.POTION, this.hero.getLevel(), null), ConsumableItem.class);

        PrintUtil.printDivider();
        System.out.println("\t\tAlchemist shop");
        PrintUtil.printDivider();

        PrintUtil.printIndexAndText("0", "Go back");
        System.out.println();
        PrintUtil.printIndexAndText("1", "Create potion");
        System.out.println();
//        PrintUtil.printIndexAndText("2", cityAlchemistReagentVendor.getName() + " (Alchemy reagents Merchant)");
//        System.out.println();
//        PrintUtil.printIndexAndText("3", cityPotionsVendor.getName() + " (Potions Merchant)");
        System.out.println();
        PrintUtil.printIndexAndText("4", QuestGiverCharacterDB.returnQuestGiverName(0));
        System.out.println();

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> System.out.println("Work in progress");
//            case 2 -> cityAlchemistReagentVendor.vendorMenu(this.hero);
//            case 3 -> cityPotionsVendor.vendorMenu(this.hero);
            case 4 -> QuestGiverCharacterDB.returnQuestGiverMenu(0, this.hero);
            default -> PrintUtil.printEnterValidInput();
        }
    }

    private void initGame() {
        this.fileService.importWearableItemsFromFile();
        this.fileService.importCraftingReagentItemsFromFile();
        this.fileService.importConsumableItemsFromFile();
        this.fileService.importQuestItemsFromFile();
        this.fileService.importJunkItemsFromFile();

        GameSettingsDB.initializeGameSettings();

        SpellDB.SPELL_LIST.addAll(this.fileService.importSpellsFromFile());

        this.fileService.importQuestsListFromFile();
        this.fileService.importLocationsFromFile();
        this.fileService.importEnemyGroupFromFile();
        this.fileService.importQuestGiverFromFile();
        this.fileService.importCreaturesFromFile();
        this.fileService.importRegionsFromFile();
        this.fileService.importNamesFromFile();
        this.fileService.importVendorsFromFile();
        this.fileService.importVendorItemListsFromFile();

        HintDB.initializeHintList();

        System.out.println(ConsoleColor.YELLOW_UNDERLINED + "\t\tWelcome to the Eldarion!\t\t\n" + ConsoleColor.RESET);
        PrintUtil.printIndexAndText("0", "Start new game");
        System.out.println();
        PrintUtil.printIndexAndText("1", "Load game");
        System.out.println();

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> System.out.println("\tLet's go then!");
            case 1 -> {
                final GameLoaded gameLoaded = fileService.loadGame();
                if (gameLoaded != null) {
                    this.hero = gameLoaded.getHero();
                    this.hero.setLevel(gameLoaded.getHero().getLevel());
                    this.heroAbilityManager.setHero(gameLoaded.getHero());
                    HintDB.getHINT_DB().putAll(gameLoaded.getHintUtil());
                    QuestDB.setInitialQuestsStatus(this.hero);
                    QuestDB.loadQuests(this.hero);
                    this.hero.getRegionActionsWithDuration().addAll(gameLoaded.getRegionActionsWithDuration());

                    return;
                }
            }
            default -> PrintUtil.printEnterValidInput();
        }

        System.out.println("\tEnter your name: ");
        final String name = InputUtil.stringScanner();
        PrintUtil.printLongDivider();

        System.out.println("\tSelect your class: ");
        int index = 0;
        List<CharacterClass> characterClassList = new ArrayList<>();
        for (CharacterClass characterClass : CharacterClass.values()) {
            if (!characterClass.equals(CharacterClass.NPC)) {
                System.out.println("\t" + index + ". " + characterClass.name());
                characterClassList.add(characterClass);
                index++;
            }
        }

        while (true) {
            try {
                final int heroClassChoice = InputUtil.intScanner();
                this.hero.setCharacterClass(characterClassList.get(heroClassChoice));
                break;
            } catch (IndexOutOfBoundsException e) {
                PrintUtil.printEnterValidInput();
            }
        }

        for (Spell spell : SpellDB.SPELL_LIST) {
            if (spell.getSpellLevel() == 0 && spell.getSpellClass().equals(this.hero.getCharacterClass())) {
                this.hero.getCharacterSpellList().add(spell);
            }
        }

        this.hero.setName(name);
        this.hero.setLevel(Constant.INITIAL_LEVEL);
        this.hero.gainExperiencePoints(0);
        QuestDB.setInitialQuestsStatus(this.hero);
        VendorDB.setCurrentVendorCharacterItemListId(this.hero.getLevel());

        System.out.println("\t\tHello " + this.hero.getName() + ", Your class is: " + this.hero.getCharacterClass() + ". Let's start the game!");
        PrintUtil.printLongDivider();

        this.hero.setInitialEquip();
        this.heroAbilityManager.spendAbilityPoints();

        HintDB.printHint(HintName.WELCOME);
    }
}
