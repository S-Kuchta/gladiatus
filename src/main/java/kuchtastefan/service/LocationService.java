package kuchtastefan.service;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.hint.HintDB;
import kuchtastefan.hint.HintName;
import kuchtastefan.quest.questGiver.QuestGiverCharacterDB;
import kuchtastefan.utility.AutosaveCount;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.PrintUtil;
import kuchtastefan.world.location.Location;
import kuchtastefan.world.location.locationStage.LocationStage;
import kuchtastefan.world.location.locationStage.specificLocationStage.LocationStageQuestGiver;

public class LocationService {

    /**
     * Displays the menu for interacting with a specific location.
     * Allows the hero to explore the location, view hero menu options, or go back on the path.
     * Also displays discovered stages within the location and their completion status.
     *
     * @param hero            The hero exploring the location.
     * @param location        The location being explored.
     * @param heroMenuService The service handling hero menu actions.
     */
    public void locationMenu(Hero hero, Location location, HeroMenuService heroMenuService) {
        HintDB.printHint(HintName.LOCATION_HINT);
        int index = 3;

        while (true) {
            QuestGiverCharacterDB.setAllQuestGiversName(hero);
            location.printLocationHeader();
            location.printLocationMenu(index);

            int choice = InputUtil.intScanner();
            if (choice < 0 || choice > location.getLocationStages().size() + 2) {
                PrintUtil.printEnterValidInput();
            } else {
                if (choice == 0) {
                    break;
                }

                if (choice == 1) {
                    if (location.getStageTotal() == location.getStageDiscovered()) {
                        exploreLocationStage(hero, location, location.getStageDiscovered() - 1);
                    } else {
                        exploreLocationStage(hero, location, location.getStageDiscovered());
                    }

                    continue;
                }

                if (choice == 2) {
                    heroMenuService.heroCharacterMenu(hero);
                    continue;
                }

                if (location.getLocationStage(choice - index).isStageDiscovered()) {
                    exploreLocationStage(hero, location, choice - index);
                } else {
                    PrintUtil.printEnterValidInput();
                }
            }
        }
    }

    /**
     * Method is responsible for exploring LocationStage.
     * You can explore LocationStage only if certain conditions are fulfilled
     * If stage is successfully completed, increase counter of stageCompleted located in Location
     * If stageCompleted meet value of stageTotal, location is completed and rewards are granted
     *
     * @param hero               Exploring LocationStage
     * @param location           which stage belong
     * @param locationStageOrder order in location
     */
    public void exploreLocationStage(Hero hero, Location location, int locationStageOrder) {
        LocationStage locationStage = location.getLocationStages().get(locationStageOrder);
        AutosaveCount.checkAutosaveCount(hero);

        if (locationStage instanceof LocationStageQuestGiver) {
            if (!locationStage.isStageDiscovered()) {
                location.incrementStageDiscovered();
            }

            discoverNextStage(location, locationStageOrder);
        }

        if (!location.getLocationStage(0).isStageDiscovered()) {
            location.getLocationStage(0).setStageDiscovered(true);
            location.incrementStageDiscovered();
        }

        System.out.println("location stage discovered: " + location.getStageDiscovered());
        if (!locationStage.canHeroEnterStage(hero)) {
            return;
        }

        // LocationStage header
        PrintUtil.printLongDivider();
        System.out.println("\t\t\t" + locationStage.getStageName());
        PrintUtil.printLongDivider();

        // Explore location stage
        boolean isStageCompleted = locationStage.exploreStage(hero, location);

        // check if stage is successfully cleared
        if (isStageCompleted && !locationStage.isStageCompleted()) {
            location.incrementStageCompleted();
            location.incrementStageDiscovered();
            discoverNextStage(location, locationStageOrder);
            locationStage.completeStage();
        }

        // Completing all location stages
        if (location.getStageCompleted() == location.getStageTotal()) {
            location.setCleared(true);
            hero.checkIfQuestObjectivesAndQuestIsCompleted();
        }

        hero.restoreHealthAndManaAfterTurn();
    }

    private void discoverNextStage(Location location, int locationStageOrder) {
        if (location.getLocationStages().get(locationStageOrder + 1) != null) {
            location.getLocationStages().get(locationStageOrder + 1).setStageDiscovered(true);
        }
    }
}


