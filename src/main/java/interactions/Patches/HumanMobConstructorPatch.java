package interactions.Patches;

import interactions.Jobs.TalkToNPCLevelJob;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.entity.mobs.friendly.human.HumanMob;
import net.bytebuddy.asm.Advice;

@ModConstructorPatch(target = HumanMob.class, arguments = {})
public class HumanMobConstructorPatch {

    @Advice.OnMethodExit
    static <T extends HumanMob> void onExit(@Advice.This T mob) {
        mob.jobTypeHandler.setJobHandler(TalkToNPCLevelJob.class, 0, 0, 0, 0,
                () -> true,
                (foundJob) -> TalkToNPCLevelJob.getJobSequence(mob, foundJob)
        );
        mob.jobTypeHandler.getJobHandler(TalkToNPCLevelJob.class).searchInLevelJobData = false;
        mob.jobTypeHandler.getJobHandler(TalkToNPCLevelJob.class).extraJobStreamer = TalkToNPCLevelJob.getJobStreamer();
    }
}
