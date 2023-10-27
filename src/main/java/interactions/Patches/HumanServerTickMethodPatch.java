package interactions.Patches;

import interactions.InteractionsMod;
import interactions.SettlerInteractions.CustomInteractionsHandler;
import interactions.SettlerInteractions.SettlerDialogue;
import interactions.Util.MobUtils;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.friendly.human.HumanMob;
import net.bytebuddy.asm.Advice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ModMethodPatch(target = HumanMob.class, name = "serverTick", arguments = {})
public class HumanServerTickMethodPatch {

    @Advice.OnMethodExit
    static void onExit(@Advice.This HumanMob t) {

        // Nobody wants their NPCs constantly talking now do they
        if (GameRandom.globalRandom.nextInt(10000) > InteractionsMod.SpeechChance) {
            return;
        }

        // Workaround to talking in sleep and during combat
        if (t.getWorldEntity().isNight() || (!InteractionsMod.DebugEnabled && (t.commandMoveToGuardPoint || t.commandFollowMob != null))) {
            return;
        }

//        Optional<Mob> newTarget = MobUtils.GetNearbyHumanMob(t, InteractionsMod.MaxInteractionDistance);
//
//        if (!newTarget.isPresent() || newTarget.get().equals(t))
//            return;
//
//        Mob target = newTarget.get();
//
//        SettlerDialogue.HandleSettlerConversation(t,(HumanMob) target);
    }
}