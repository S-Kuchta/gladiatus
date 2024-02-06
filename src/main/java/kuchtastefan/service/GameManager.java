package kuchtastefan.service;

import kuchtastefan.characters.QuestGiverCharacter;
import kuchtastefan.characters.enemy.EnemyList;
import kuchtastefan.characters.hero.*;
import kuchtastefan.characters.spell.Spell;
import kuchtastefan.characters.spell.SpellsList;
import kuchtastefan.characters.vendor.ConsumableVendorCharacter;
import kuchtastefan.characters.vendor.CraftingReagentItemVendorCharacter;
import kuchtastefan.characters.vendor.JunkVendorCharacter;
import kuchtastefan.constant.Constant;
import kuchtastefan.hint.HintUtil;
import kuchtastefan.items.ItemsLists;
import kuchtastefan.items.consumeableItem.ConsumableItemType;
import kuchtastefan.items.craftingItem.CraftingReagentItemType;
import kuchtastefan.quest.QuestList;
import kuchtastefan.regions.ForestRegionService;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;

import java.util.ArrayList;
import java.util.List;


public class GameManager {
    private Hero hero;
    private final HeroAbilityManager heroAbilityManager;
    private int currentLevel;
    private final FileService fileService;
    private final BlacksmithService blacksmithService;
    private ForestRegionService forestRegionService;
    private final HeroCharacterService heroCharacterService;


    public GameManager() {
        this.hero = new Hero("");
        this.currentLevel = Constant.INITIAL_LEVEL;
        this.fileService = new FileService();
        this.heroAbilityManager = new HeroAbilityManager(this.hero);
        this.blacksmithService = new BlacksmithService();
        this.heroCharacterService = new HeroCharacterService(this.heroAbilityManager);
    }

    public void startGame() {
        this.initGame();

        while (true) {
            PrintUtil.printLongDivider();
            System.out.println("\t\t\t\t\t\t\t------ Mystic Hollow ------");
            PrintUtil.printLongDivider();
            System.out.println("\t0. Explore surrounding regions");
            System.out.println("\t1. Hero menu");
            System.out.println("\t2. Junk Merchant");
            System.out.println("\t3. Tavern");
            System.out.println("\t4. Alchemist");
            System.out.println("\t5. Blacksmith");
            System.out.println("\t6. Save game");
            System.out.println("\t7. Exit game");

            final int choice = InputUtil.intScanner();
            switch (choice) {
                case 0 -> exploreSurroundingRegions();
                case 1 -> this.heroCharacterService.heroCharacterMenu(this.hero);
                case 2 -> {
                    JunkVendorCharacter junkVendorCharacter = new JunkVendorCharacter("Dazres Heitholt", 8,
                            ItemsLists.returnJunkItemListByItemLevel(this.hero.getLevel(), 0));
                    junkVendorCharacter.vendorMenu(this.hero);
                }
                case 3 -> this.tavernMenu();
                case 4 -> this.alchemistMenu();
                case 5 -> this.blacksmithService.blacksmithMenu(this.hero);
                case 6 -> this.fileService.saveGame(this.hero, this.currentLevel, this.forestRegionService);
                case 7 -> {
                    System.out.println("Are you sure?");
                    System.out.println("0. No");
                    System.out.println("1. Yes");
                    final int exitChoice = InputUtil.intScanner();
                    if (exitChoice == 0) {
                        continue;
                    }
                    if (exitChoice == 1) {
                        System.out.println("Bye");
                        return;
                    }
                }
                default -> PrintUtil.printEnterValidInput();
            }
        }
    }

    private void exploreSurroundingRegions() {
        System.out.println("\t0. Go back to the city");
        System.out.println("\t1. Go to " + this.forestRegionService.getRegionName());
        System.out.println("\t2. Go to highlands");
        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> this.forestRegionService.adventuringAcrossTheRegion(this.heroCharacterService);
            default -> PrintUtil.printEnterValidInput();
        }
    }

    private void tavernMenu() {
        final ConsumableVendorCharacter cityFoodVendor = new ConsumableVendorCharacter("Ved Of Kaedwen", 8,
                ItemsLists.returnConsumableItemListByTypeAndItemLevel(ConsumableItemType.FOOD, this.hero.getLevel(), null));

        QuestGiverCharacter questGiverCharacter = new QuestGiverCharacter("Freya", 8);
        questGiverCharacter.addQuest(QuestList.questList.get(0));
        questGiverCharacter.setNameBasedOnQuestsAvailable(this.hero);

        QuestGiverCharacter questGiverCharacter1 = new QuestGiverCharacter("Devom Of Cremora", 8);
        questGiverCharacter1.addQuest(QuestList.questList.get(2));
        questGiverCharacter1.setNameBasedOnQuestsAvailable(this.hero);


        PrintUtil.printDivider();
        System.out.println("\t\tTavern");
        PrintUtil.printDivider();

        System.out.println("\t0. Go back");
        System.out.println("\t1. " + cityFoodVendor.getName() + " (Food Merchant)");
        System.out.println("\t2. " + questGiverCharacter.getName());
        System.out.println("\t3. " + questGiverCharacter1.getName());

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> cityFoodVendor.vendorMenu(this.hero);
            case 2 -> questGiverCharacter.questGiverMenu(this.hero);
            case 3 -> questGiverCharacter1.questGiverMenu(this.hero);
        }
    }

    private void alchemistMenu() {
        final CraftingReagentItemVendorCharacter cityAlchemistReagentVendor = new CraftingReagentItemVendorCharacter("Meeden", 8,
                ItemsLists.returnCraftingReagentItemListByTypeAndItemLevel(CraftingReagentItemType.ALCHEMY_REAGENT, this.hero.getLevel(), 0));
        final ConsumableVendorCharacter cityPotionsVendor = new ConsumableVendorCharacter("Etaefush", 8,
                ItemsLists.returnConsumableItemListByTypeAndItemLevel(ConsumableItemType.POTION, this.hero.getLevel(), null));

        QuestGiverCharacter questGiverCharacter = new QuestGiverCharacter("High Priestess Elara", 8);
        questGiverCharacter.addQuest(QuestList.questList.get(3));
        questGiverCharacter.setNameBasedOnQuestsAvailable(this.hero);

        PrintUtil.printDivider();
        System.out.println("\t\tAlchemist shop");
        PrintUtil.printDivider();

        System.out.println("\t0. Go back");
        System.out.println("\t1. Create potion");
        System.out.println("\t2. " + cityAlchemistReagentVendor.getName() + " (Alchemy reagents Merchant)");
        System.out.println("\t3. " + cityPotionsVendor.getName() + " (Potions Merchant)");
        System.out.println("\t4. " + questGiverCharacter.getName());

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> System.out.println("Work in progress");
            case 2 -> cityAlchemistReagentVendor.vendorMenu(this.hero);
            case 3 -> cityPotionsVendor.vendorMenu(this.hero);
            case 4 -> questGiverCharacter.questGiverMenu(this.hero);
            default -> PrintUtil.printEnterValidInput();
        }
    }

    private void initGame() {
        ItemsLists.getWearableItemList().addAll(fileService.importWearableItemsFromFile());
        ItemsLists.getCraftingReagentItems().addAll(fileService.importCraftingReagentItemsFromFile());
        ItemsLists.getConsumableItems().addAll(fileService.importConsumableItemsFromFile());
        ItemsLists.getQuestItems().addAll(fileService.importQuestItemsFromFile());
        ItemsLists.getJunkItems().addAll(fileService.importJunkItemsFromFile());
        ItemsLists.initializeAllItemsMapToStringItemMap();

        QuestList.questList.addAll(this.fileService.importQuestsListFromFile());

        SpellsList.getSpellList().addAll(this.fileService.importSpellsFromFile());
        EnemyList.getEnemyList().addAll(this.fileService.importCreaturesFromFile());

        this.forestRegionService = new ForestRegionService("Silverwood Glade", "Magic forest", this.hero, 1, 5);

        HintUtil.initializeHintList();


        System.out.println("Welcome to the Gladiatus game!");
        System.out.println("0. Start new game");
        System.out.println("1. Load game");

        int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> System.out.println("Let's go then!");
            case 1 -> {
                final GameLoaded gameLoaded = fileService.loadGame();
                if (gameLoaded != null) {
                    this.hero = gameLoaded.getHero();
                    this.currentLevel = gameLoaded.getLevel();
                    this.heroAbilityManager.setHero(gameLoaded.getHero());
                    HintUtil.getHintList().putAll(gameLoaded.getHintUtil());
                    this.hero.getRegionActionsWithDuration().addAll(gameLoaded.getRegionActionsWithDuration());
                    this.forestRegionService.setHero(this.hero);
                    this.forestRegionService.getDiscoveredLocations().addAll(gameLoaded.getForestRegionDiscoveredLocation());
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

        for (Spell spell : SpellsList.getSpellList()) {
            if (spell.getSpellLevel() == 0 && spell.getSpellClass().equals(this.hero.getCharacterClass())) {
                this.hero.getCharacterSpellList().add(spell);
            }
        }


        this.hero.setName(name);
        System.out.println("\t\tHello " + this.hero.getName() + ", Your class is: " + this.hero.getCharacterClass() + ". Let's start the game!");
        PrintUtil.printLongDivider();

        this.heroAbilityManager.spendAbilityPoints();
    }
}
