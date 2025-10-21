package interactions.SettlerInteractions;

import interactions.Util.MobUtils;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.inventory.item.placeableItem.consumableItem.food.FoodConsumableItem;

public class SettlerDialogue {

    public static Long LastInteractionTime = 0L;

    /**
     * Iterates over possible interactions and checks conditions, as well as run a chance check.
     * Query is set to the first interaction that successfully meets these conditions,
     * and response is set to the best response for that interaction
     * @param mob Any mob that extends HumanMob.Class, generally settlers or village NPCs but never raiders or pirates
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
        }
        if (response != null) {
            String message = ParsePlaceholders(response.GetRandomMessage(), target, mob);
            MobUtils.SendMobMessage(target, message);
        }
    }

    public static <T extends HumanMob> String ParsePlaceholders(String message, T mob, T target) {
        FoodConsumableItem lastMeal = target.lastFoodEaten;

        return message.replace("<NAME>", mob.getSettlerName())
                .replace("<TARGET_NAME>", target.getSettlerName())
                .replace("<LAST_FOOD_EATEN>", lastMeal == null ? "bagel" : lastMeal.getTranslatedTypeName());
    }
}
