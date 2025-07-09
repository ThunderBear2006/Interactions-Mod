package interactions.SettlerInteractions;

import interactions.InteractionsMod;
import interactions.SettlerInteractions.Conditions.ConditionHandler;
import interactions.Util.Logger;
import interactions.Util.MobUtils;
import interactions.Util.Tuple;
import necesse.engine.localization.message.LocalMessage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.friendly.human.HappinessModifier;
import necesse.entity.mobs.friendly.human.HumanMob;

import java.util.*;
import java.util.stream.Collectors;

public class SettlerDialogue {

    /**
     * Each mob that can interact with another has a memory of recent interactions.
     * This memory is used to check if the mob has interacted too recently and
     * for modifying settler happiness
     */
    //TODO: Save this data
    public static final HashMap<Integer, List<DialogueHistoryEntry>> DIALOGUE_MEMORY = new HashMap<>();

    /**
     * How long until a mob forgets a previous interaction (thus not counting it towards overall happiness)
     */
    //TODO: Make this a setting
    public static final long FORGET_TIME = 50000;

    public static <T extends Mob> void AddDialogueHistoryEntry(T mob, int happiness) {
        if (!DIALOGUE_MEMORY.containsKey(mob.getID()))
            DIALOGUE_MEMORY.put(mob.getID(), new ArrayList<>());

        DialogueHistoryEntry entry = new DialogueHistoryEntry(mob.getWorldTime(), happiness);

        DIALOGUE_MEMORY.get(mob.getID()).add(entry);
    }

    public static <T extends Mob> Tuple<HappinessModifier, HappinessModifier> GetDialogueMemoryHappiness(T mob) {
        if (DIALOGUE_MEMORY.isEmpty() || !DIALOGUE_MEMORY.containsKey(mob.getID()))
            return new Tuple<>(null, null);

        List<DialogueHistoryEntry> list = DIALOGUE_MEMORY.get(mob.getID()).stream().filter(
                (e) -> mob.getWorldTime() - e.time < FORGET_TIME).collect(Collectors.toList()
        );

        int happiness = 0;
        int sadness = 0;

        for (DialogueHistoryEntry dialogueHistoryEntry : list) {
            if (dialogueHistoryEntry.modifier > 0) {
                happiness += dialogueHistoryEntry.modifier;
            }
            else if (dialogueHistoryEntry.modifier < 0) {
                sadness += dialogueHistoryEntry.modifier;
            }
        }

        // Update the list to remove old "memories"
        DIALOGUE_MEMORY.put(mob.getID(), list);

        return new Tuple<>(
                happiness != 0 ? new HappinessModifier(happiness, new LocalMessage("hapMod", "good_chat")) : null,
                sadness != 0 ? new HappinessModifier(sadness, new LocalMessage("hapMod", "bad_chat")) : null
        );
    }

    public static <T extends Mob> boolean HasInteractedRecently(T mob) {
        List<DialogueHistoryEntry> list = DIALOGUE_MEMORY.get(mob.getID());

        return list != null && list.stream().anyMatch((e) -> mob.getWorldTime() - e.time < InteractionsMod.settings.InteractionCoolDown);
    }

    /**
     * Iterates over possible interactions and checks conditions, as well as run a chance check.
     * Query is set to the first interaction that successfully meets these conditions,
     * and response is set to the best response for that interaction
     * @param mob Any mob that extends HumanMob.class, generally settlers or village NPCs but never raiders or pirates
     * @param target Same as mob
     * @param type type of interaction
     */
    public static <T extends HumanMob> void HandleDialogue(T mob, T target, String type){
        Dialogue query = null;
        Dialogue response = null;

        for (Dialogue dialogue : DialogueRegistry.GetShuffledDialogueList(type)){
            if (dialogue.CheckConditions(mob, target)){
                query = dialogue;
                response = DialogueRegistry.REGISTRY.get("response").get(dialogue.GetBestResponse(target, mob));
                break;
            }
        }

        if (query != null) {
            String message = ParsePlaceholders(query.GetRandomMessage(), mob, target);
            MobUtils.SendMobMessage(mob, message);
            if (target.isServer())
                AddDialogueHistoryEntry(target, query.HappinessMod);
        }
        if (response != null) {
            String message = ParsePlaceholders(response.GetRandomMessage(), target, mob);
            MobUtils.SendMobMessage(target, message);
            if (mob.isServer())
                AddDialogueHistoryEntry(mob, response.HappinessMod);
        }
    }

    public static <T extends HumanMob> String ParsePlaceholders(String message, T mob, T target) {
        return message.replace("<NAME>", mob.getSettlerName())
                .replace("<TARGET_NAME>", target.getSettlerName())
                .replace("<LAST_FOOD_EATEN>", target.lastFoodEaten.getTranslatedTypeName());
    }

    public static class DialogueHistoryEntry {
        public long time; // Time at which the event occurred
        public int modifier;

        public DialogueHistoryEntry(long time, int mod) {
            this.time = time;
            this.modifier = mod;
        }
    }
}
