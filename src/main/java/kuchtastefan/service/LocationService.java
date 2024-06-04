package kuchtastefan.service;

import kuchtastefan.character.hero.Hero;
import kuchtastefan.character.spell.Spell;
import kuchtastefan.hint.HintDB;
import kuchtastefan.hint.HintName;
import kuchtastefan.quest.questObjectives.QuestObjective;
import kuchtastefan.quest.questObjectives.QuestObjectiveDB;
import kuchtastefan.utility.AutosaveCount;
import kuchtastefan.utility.InputUtil;
import kuchtastefan.utility.printUtil.CharacterPrint;
import kuchtastefan.utility.printUtil.PrintUtil;
import kuchtastefan.utility.printUtil.SpellAndActionPrint;
import kuchtastefan.world.location.Location;
import kuchtastefan.world.location.LocationStatus;
import kuchtastefan.world.location.QuestLocation;
import kuchtastefan.world.location.locationStage.LocationStage;
import kuchtastefan.world.location.locationStage.specificLocationStage.LocationStageQuestGiver;

public class LocationService {

    private final Location location;

    public LocationService(Location location) {
        this.location = location;
    }

    /**
     * Displays the menu for interacting with a specific location.
     * Allows the hero to explore the location, view hero menu options, or go back on the path.
     * Also displays discovered stages within the location and their completion status.
     *
     * @param hero            The hero exploring the location.
     * @param heroMenuService The service handling hero menu actions.
     */
    public void locationMenu(Hero hero, HeroMenuService heroMenuService) {
        HintDB.printHint(HintName.LOCATION_HINT);
        int index = 3;

        while (true) {
            location.questLocationStageSet(hero);

            CharacterPrint.printHeaderWithStatsBar(hero);
            SpellAndActionPrint.printBuffTable(hero);
            location.printHeader();
            location.printMenu(index);

            int choice = InputUtil.intScanner();
            if (choice < 0 || choice > location.getStageTotal() + 2) {
                System.out.println("prvy invalid");
                PrintUtil.printEnterValidInput();
            } else {
                if (choice == 0) {
                    break;
                }

                if (choice == 1) {
                    exploreLocationStage(hero, determineLocationStage(location.getStageCompleted()));
                    continue;
                }

                if (choice == 2) {
                    heroMenuService.heroCharacterMenu(hero);
                    continue;
                }

                if (location.getLocationStage(choice - index).isDiscovered() || location.getLocationStage(choice - index).isCleared()) {
                    exploreLocationStage(hero, location.getLocationStage(choice - index));
                } else {
                    System.out.println("druhy invalid");
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
     * @param hero Exploring LocationStage
     */
    public void exploreLocationStage(Hero hero, LocationStage locationStage) {
        AutosaveCount.checkAutosaveCount(hero);

        locationStage.discoverStage();
        if (!locationStage.canHeroEnterStage(hero)) {
            return;
        }

        // LocationStage header
        PrintUtil.printMenuHeader(locationStage.getStageName());

        // Explore location stage
        boolean isStageCompleted = locationStage.exploreStage(hero, location);

        // check if stage is successfully cleared
        if (isStageCompleted && !(locationStage.isCleared())) {
            discoverNextStage(location, location.getStageCompleted() + 1);
            locationStage.completeStage();
            hero.restoreHealthAndManaAfterTurn();
            hero.getCharacterSpellList().forEach(Spell::increaseSpellCoolDown);
        }

        // Completing all location stages
        if (location.getStageCompleted() == location.getStageTotal()) {
            location.setLocationStatus(LocationStatus.COMPLETED);

            if (location instanceof QuestLocation questLocation) {
                try {
                    QuestObjective questObjective = QuestObjectiveDB.getQuestObjectiveById(questLocation.getQuestObjectiveId());
                    questObjective.verifyQuestObjectiveCompletion(hero);
                    questObjective.printProgress(hero);
                } catch (IllegalArgumentException e) {
                    System.out.println("Quest objective not found");
                }
            }
        }
    }

    private void discoverNextStage(Location location, int locationStageOrder) {
        LocationStage nextLocationStage = location.getLocationStages().get(locationStageOrder);
        if (nextLocationStage != null) {
            nextLocationStage.discoverStage();
        }
    }

    private LocationStage determineLocationStage(int locationStageOrder) {
        LocationStage locationStage = location.getLocationStages().get(locationStageOrder);
        LocationStage nextLocationStage = location.getLocationStages().get(locationStageOrder);

        if (locationStage instanceof LocationStageQuestGiver) {
            if (locationStage.isDiscovered() && location.getLocationStage(locationStageOrder + 1) != null) {
                nextLocationStage = location.getLocationStages().get(locationStageOrder + 1);
            } else {
                discoverNextStage(location, locationStageOrder + 1);
            }
        } else {
            if (locationStage != null && locationStage.isCleared()) {
                nextLocationStage = location.getLocationStages().get(locationStageOrder + 1);
                discoverNextStage(location, locationStageOrder);
            }
        }

        if (nextLocationStage == null) {
            return location.getLocationStages().get(location.getStageCompleted() - 1);
        }

        return nextLocationStage;
    }
}


