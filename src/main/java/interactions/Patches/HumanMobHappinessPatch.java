package interactions.Patches;

import interactions.SettlerInteractions.SettlerDialogue;
import interactions.Util.Tuple;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.friendly.human.HappinessModifier;
import necesse.entity.mobs.friendly.human.HumanMob;
import net.bytebuddy.asm.Advice;

import java.util.List;

@ModMethodPatch(target = HumanMob.class, name = "getHappinessModifiers", arguments = {})
public class HumanMobHappinessPatch {

    @Advice.OnMethodExit
    static void OnExit(@Advice.This Mob mob, @Advice.Return(readOnly = false) List<HappinessModifier> modifiers) {
        Tuple<HappinessModifier, HappinessModifier> dialogueMods = SettlerDialogue.GetDialogueMemoryHappiness(mob);

        if (dialogueMods.First() != null)
            modifiers.add(dialogueMods.First());
        if (dialogueMods.Second() != null)
            modifiers.add(dialogueMods.Second());
    }
}
