package kuchtastefan.utility;

import kuchtastefan.actions.Action;
import kuchtastefan.actions.actionsWIthDuration.*;
import kuchtastefan.actions.instantActions.ActionDealDamage;
import kuchtastefan.actions.instantActions.ActionInstantStun;
import kuchtastefan.actions.instantActions.ActionRestoreHealth;
import kuchtastefan.actions.instantActions.ActionRestoreMana;
import kuchtastefan.items.Item;
import kuchtastefan.items.consumeableItem.ConsumableItem;
import kuchtastefan.items.craftingItem.CraftingReagentItem;
import kuchtastefan.items.junkItem.JunkItem;
import kuchtastefan.items.questItem.QuestItem;
import kuchtastefan.items.wearableItem.WearableItem;
import kuchtastefan.quest.questObjectives.QuestBringItemObjective;
import kuchtastefan.quest.questObjectives.QuestClearLocationObjective;
import kuchtastefan.quest.questObjectives.QuestKillObjective;
import kuchtastefan.quest.questObjectives.QuestObjective;
import kuchtastefan.regions.locations.LocationStage;
import kuchtastefan.regions.locations.LocationStageCombat;
import kuchtastefan.regions.locations.LocationStageFindTreasure;
import kuchtastefan.regions.locations.LocationStageQuestGiver;
import kuchtastefan.service.RuntimeTypeAdapterFactory;


public class RuntimeTypeAdapterFactoryUtil {

    public static final RuntimeTypeAdapterFactory<Action> actionsRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory
            .of(Action.class)
            .registerSubtype(ActionWithDuration.class)
            .registerSubtype(ActionRestoreHealth.class)
            .registerSubtype(ActionDealDamage.class)
            .registerSubtype(ActionDealDamageOverTime.class)
            .registerSubtype(ActionRestoreHealthOverTime.class)
            .registerSubtype(ActionIncreaseAbilityPoint.class)
            .registerSubtype(ActionAbsorbDamage.class)
            .registerSubtype(ActionRestoreMana.class)
            .registerSubtype(ActionRestoreManaOverTime.class)
            .registerSubtype(ActionStun.class)
            .registerSubtype(ActionInstantStun.class)
            .registerSubtype(ActionInvulnerability.class);

    public static final RuntimeTypeAdapterFactory<ActionWithDuration> actionsWithDurationTypeAdapterFactory = RuntimeTypeAdapterFactory
            .of(ActionWithDuration.class)
            .registerSubtype(ActionDealDamageOverTime.class, "ActionDealDamageOverTimeWithDuration")
            .registerSubtype(ActionRestoreHealthOverTime.class, "ActionRestoreHealthWithDuration")
            .registerSubtype(ActionAbsorbDamage.class, "ActionAbsorbDamageWithDuration")
            .registerSubtype(ActionIncreaseAbilityPoint.class, "ActionIncreaseAbilityPointWithDuration")
            .registerSubtype(ActionRestoreManaOverTime.class, "ActionRestoreManaWithDuration")
            .registerSubtype(ActionStun.class, "ActionStunWithDuration")
            .registerSubtype(ActionInvulnerability.class, "ActionInvulnerabilityWithDuration");

    public static final RuntimeTypeAdapterFactory<QuestObjective> questObjectiveRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory
            .of(QuestObjective.class)
            .registerSubtype(QuestKillObjective.class)
            .registerSubtype(QuestBringItemObjective.class)
            .registerSubtype(QuestClearLocationObjective.class);

    public static final RuntimeTypeAdapterFactory<? extends Item> itemsRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory
            .of(Item.class)
            .registerSubtype(ConsumableItem.class)
            .registerSubtype(CraftingReagentItem.class)
            .registerSubtype(JunkItem.class)
            .registerSubtype(QuestItem.class)
            .registerSubtype(WearableItem.class);

    public static final RuntimeTypeAdapterFactory<? extends LocationStage> locationStageRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory
            .of(LocationStage.class)
            .registerSubtype(LocationStageQuestGiver.class)
            .registerSubtype(LocationStageCombat.class)
            .registerSubtype(LocationStageFindTreasure.class);


//    public static final RuntimeTypeAdapterFactory<Spell> spellObjectiveRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory
//            .of(Spell.class)
//            .registerSubtype(Spell.class);
//            .registerSubtype(MageSpell.class)
//            .registerSubtype(WarriorSpell.class);
}
