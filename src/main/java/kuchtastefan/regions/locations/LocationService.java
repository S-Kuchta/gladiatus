package kuchtastefan.regions.locations;

import kuchtastefan.characters.hero.Hero;
import kuchtastefan.items.ItemsLists;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;

import java.util.Map;

public class LocationService {

    public void locationMenu(Hero hero, Location location) {
        while (true) {
            PrintUtil.printLongDivider();
            System.out.println("\t" + location.getLocationName() + "\tLocation level: "
                    + location.getLocationLevel() + " "
                    + "\tStages completed " + location.stageCompleted + " / " + location.stageTotal);
            PrintUtil.printLongDivider();

            System.out.println("\tWhat do you want to do?");
            PrintUtil.printIndexAndText(String.valueOf(0), "Go back on the path");
            System.out.println();
            PrintUtil.printIndexAndText(String.valueOf(1), "Explore location");
            System.out.println();

            int index = 2;
            for (Map.Entry<Integer, LocationStage> locationStage : location.locationStages.entrySet()) {
                if (locationStage.getValue().isStageDiscovered()) {
                    String completed = locationStage.getValue().isStageCompleted() ? " - COMPLETED -" : "";
                    PrintUtil.printIndexAndText(String.valueOf(index + locationStage.getKey()),
                            locationStage.getValue().getStageName() + " " + completed);

                    System.out.println();
                }
            }

            try {
                int choice = InputUtil.intScanner();
                if (choice == 0) {
                    System.out.println("\tGoing back on path");
                    break;
                } else if (choice == 1) {
                    exploreLocation(hero, location, location.stageCompleted);
                } else {
                    if (!location.getLocationStages().get(choice - index).isStageDiscovered()) {
                        PrintUtil.printEnterValidInput();
                        continue;
                    }

                    exploreLocation(hero, location, choice - index);
                }
            } catch (IndexOutOfBoundsException e) {
                PrintUtil.printEnterValidInput();
            }
        }
    }

    public void exploreLocation(Hero hero, Location location, int locationStageOrder) {
        LocationStage locationStage = location.locationStages.get(locationStageOrder);

        if (location.getLocationStages().get(locationStageOrder + 1) != null) {
            if (locationStageOrder == 0) {
                location.getLocationStages().get(locationStageOrder + 1).setStageDiscovered(true);

            } else if (location.getLocationStages().get(locationStageOrder - 1) != null
                    && location.getLocationStages().get(locationStageOrder - 1).isStageCompleted()) {

                location.getLocationStages().get(locationStageOrder + 1).setStageDiscovered(true);
            }
        }

        if (location.isCleared()) {
            System.out.println("\tLocation Cleared!");
            return;
        }

        if (!locationStage.isStageDiscovered()) {
            locationStage.setStageDiscovered(true);
        }

        if (!locationStage.canHeroEnterStage(hero)) {
            System.out.println("\tYou don't have needed keys to enter location! You need: ");
            for (Integer i : locationStage.getItemsIdNeededToEnterStage()) {
                System.out.println("\t" + ItemsLists.getItemMapIdItem().get(i).getName());
            }
            return;
        }

        if (locationStage.isStageCompleted()) {
            System.out.println("\t" + locationStage.getStageName() + " is cleared!");
            return;
        }

        PrintUtil.printLongDivider();
        System.out.println("\t\t\t" + locationStage.getStageName());
        PrintUtil.printLongDivider();

        boolean isStageCompleted = locationStage.exploreStage(hero, location);


        if (isStageCompleted) {
            location.stageCompleted++;
            locationStage.setStageCompleted(true);
            locationStage.completeStage();
        } else {
            return;
        }

        if (location.stageCompleted == location.stageTotal) {
            location.setCleared(true);
            location.rewardAfterCompletedAllStages(hero);
            location.setCanLocationBeExplored(false);
            hero.checkIfQuestObjectivesAndQuestIsCompleted();
        }
    }
}


