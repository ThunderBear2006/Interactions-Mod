package interactions.SettlerInteractions;

import interactions.InteractionsMod;
import interactions.Util.MobUtils;
import interactions.Util.Tuple;
import necesse.entity.mobs.friendly.human.HumanMob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SettlerDialogue {

    private static final List<Interaction> PossibleInteractions = new ArrayList<>();

    public static void AddNewInteraction(Interaction interaction){
        if (InteractionsMod.DebugEnabled)
            System.out.println("[Interactions Mod] Added: " + interaction.MessageKeyBase + " interaction to possible interactions");
        PossibleInteractions.add(interaction);
    }

    /**
     * Iterates over possible interactions and checks conditions, as well as run a chance check.
     * Query is set to the first interaction that successfully meets these conditions,
     * and response is set to the best response for that interaction
     * @param mob Any mob that extends HumanMob.class, generally settlers or village NPCs but never raiders or pirates
     * @param receiver Same as mob
     */
    public static <T extends HumanMob> void HandleSettlerConversation(T mob, T receiver){
        Interaction query = null;
        Interaction response = null;

        Collections.shuffle(PossibleInteractions);

        for (Interaction interaction : PossibleInteractions){
            if (interaction.Predicate.test(new Tuple<>(mob, receiver)) && !interaction.WasUsedRecently(mob.getWorldEntity().getTime())){
                query = interaction;
                response = interaction.GetBestResponse(receiver, mob);
                break;
            }
        }

        if (query != null) {
            String message = query.GetRandomMessage(mob.getWorldEntity().getTime()).replace("<NAME>", receiver.getSettlerName());
            MobUtils.SendMobMessage(mob, message);
        }
        if (response != null) {
            String message = response.GetRandomMessage(mob.getWorldEntity().getTime()).replace("<NAME>", mob.getSettlerName());
            MobUtils.SendMobMessage(receiver, message);
        }
    }
}
