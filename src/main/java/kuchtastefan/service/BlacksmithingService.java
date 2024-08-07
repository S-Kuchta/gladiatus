package kuchtastefan.service;

import com.google.gson.Gson;
import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.hero.inventory.UsingHeroInventory;
import kuchtastefan.hint.HintDB;
import kuchtastefan.hint.HintName;
import kuchtastefan.item.Item;
import kuchtastefan.item.ItemAndCount;
import kuchtastefan.item.itemFilter.*;
import kuchtastefan.item.specificItems.wearableItem.WearableItem;
import kuchtastefan.item.specificItems.wearableItem.WearableItemQuality;
import kuchtastefan.utility.ConsoleColor;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.printUtil.PrintUtil;

public class BlacksmithingService implements UsingHeroInventory {

    @Override
    public void mainMenu(Hero hero) {
        HintDB.printHint(HintName.BLACKSMITH_HINT);
        PrintUtil.printMenuOptions("Go back", "Hero Inventory");

        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
            }
            case 1 -> hero.getHeroInventoryManager().selectItem(hero, this, new ItemFilter(
                    new ItemClassFilter(WearableItem.class),
                    new ItemTypeFilter(true),
                    new ItemLevelFilter(hero.getLevel()),
                    new WearableItemQualityFilter(WearableItemQuality.BASIC)));

            default -> PrintUtil.printEnterValidInput();
        }
    }

    /**
     * This method displays a menu for a specific wearable item.
     * The user can choose to go back, refine the item, or dismantle the item.
     * The user's choice is handled by a switch statement.
     *
     * @param hero The hero who owns the item.
     * @param item The item to be managed.
     * @return True if the item was successfully managed, false otherwise.
     */
    @Override
    public boolean itemOptions(Hero hero, Item item) {
        ItemAndCount neededToRefine = ((WearableItem) item).reagentNeededToRefine();

        PrintUtil.printMenuHeader(item.getName());
        PrintUtil.printMenuOptions("Go back",
                "Refinement item - You Need: " + neededToRefine.item().getName() + " - " + neededToRefine.count() + "/" + getReagentCountColor(hero, neededToRefine.item(), neededToRefine.count()),
                "Dismantle item");

        final int choice = InputUtil.intScanner();
        switch (choice) {
            case 0 -> {
                return false;
            }
            case 1 -> {
                return this.refineItemQuality(hero, (WearableItem) item);
            }
            case 2 -> {
                return this.dismantleItem(hero, (WearableItem) item);
            }
            default -> PrintUtil.printEnterValidInput();
        }

        return false;
    }

    private String getReagentCountColor(Hero hero, Item reagent, int count) {
        int countHave = hero.getHeroInventoryManager().getItemCount(reagent);
        return hero.getHeroInventoryManager().hasRequiredItems(reagent, count)
                ? ConsoleColor.GREEN + String.valueOf(countHave) + ConsoleColor.RESET
                : ConsoleColor.RED + String.valueOf(countHave) + ConsoleColor.RESET;
    }

    /**
     * This method is used to dismantle a wearable item for a hero.
     * The item is dismantled into a reagent, which is then added to the hero's inventory.
     *
     * @param hero The hero for whom the item is to be dismantled.
     * @param item The wearable item to be dismantled.
     */
    private boolean dismantleItem(Hero hero, WearableItem item) {
        ItemAndCount reagent = item.dismantle();
        if (hero.isItemEquipped(item)) {
            hero.unEquipItem(item);
        }
        hero.getHeroInventoryManager().removeItem(item, 1);
        hero.getHeroInventoryManager().addItem(reagent.item(), reagent.count());
        return true;
    }

    /**
     * This method is used to refine the quality of a wearable item for a hero.
     * The item can only be refined if its quality is BASIC.
     * The hero's inventory must contain the required reagents for the refinement process.
     * If the item is successfully refined, the refined item is added to the hero's inventory,
     * and the original item and the required reagents are removed from the hero's inventory.
     *
     * @param hero The hero for whom the item is to be refined.
     * @param item The wearable item to be refined.
     */
    private boolean refineItemQuality(Hero hero, WearableItem item) {
        if (item.getWearableItemQuality() != WearableItemQuality.BASIC) {
            System.out.println("\tYou can not refine your item. It's not basic quality.");
            return false;
        }

        ItemAndCount requiredReagent = item.reagentNeededToRefine();
        if (!hero.getHeroInventoryManager().hasRequiredItems(requiredReagent.item(), requiredReagent.count())) {
            System.out.println("\tYou don't have enough reagents. Your can't refine your item.");
            return false;
        }

        WearableItem refinedItem = new Gson().fromJson(new Gson().toJson(item), WearableItem.class);
        refinedItem.refine();
        hero.getHeroInventoryManager().removeItem(item, 1);
        hero.getHeroInventoryManager().removeItem(requiredReagent.item(), requiredReagent.count());
        hero.getHeroInventoryManager().addItem(refinedItem, 1);

        System.out.println("\tYou refinement your item " + refinedItem.getName() + " to " + refinedItem.getWearableItemQuality() + " quality");
        if (hero.isItemEquipped(item)) {
            hero.unEquipItem(item);
            hero.equipItem(refinedItem);
        }

        return true;
    }
}
