package interactions.Patches;

import interactions.InteractionsMod;
import interactions.Settings;
import interactions.SettlerInteractions.SettlerDialogue;
import interactions.Util.Logger;
import interactions.Util.MobUtils;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.state.MainMenu;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.friendly.human.HumanMob;
import net.bytebuddy.asm.Advice;

import java.util.Optional;

@ModMethodPatch(target = HumanMob.class, name = "serverTick", arguments = {})
public class HumanServerTickMethodPatch {

    @Deprecated
    @Advice.OnMethodExit
    static void onExit(@Advice.This HumanMob t) {
        long time = t.getTime() - SettlerDialogue.LastInteractionTime;

        if (time < InteractionsMod.settings.InteractionCoolDown)
            return;

        // Nobody wants their NPCs constantly talking now do they
        if (GameRandom.globalRandom.nextFloat() < InteractionsMod.settings.InteractionChance) {
            return;
        }

        SettlerDialogue.LastInteractionTime = t.getTime();

        HumanMob newTarget = MobUtils.GetNearbyHumanMob(t, InteractionsMod.settings.MaxSpeechDistance, true);

        if (newTarget == null || newTarget.equals(t))
            return;

        SettlerDialogue.HandleDialogue(t, newTarget, "dialogue");
    }
}