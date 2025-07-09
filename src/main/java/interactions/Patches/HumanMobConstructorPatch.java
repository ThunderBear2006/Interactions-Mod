package interactions.Patches;

import interactions.Jobs.TalkToNPCLevelJob;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.registries.LevelJobRegistry;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.entity.mobs.job.EntityJobWorker;
import necesse.entity.mobs.job.FoundJob;
import necesse.entity.mobs.job.JobSequence;
import necesse.entity.mobs.job.JobTypeHandler;
import net.bytebuddy.asm.Advice;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

@ModConstructorPatch(target = HumanMob.class, arguments = {int.class, int.class, String.class})
public class HumanMobConstructorPatch {

//    public static final BiPredicate<JobTypeHandler.SubHandler<TalkToNPCLevelJob>, EntityJobWorker> canPerformSupplier = (subHandler, worker) -> true;
//    public static Function<FoundJob<TalkToNPCLevelJob>, JobSequence> getTalkToJobSequenceFunction(HumanMob mob) {
//        return foundJob -> TalkToNPCLevelJob.getJobSequence(mob, foundJob);
//    }

    @Advice.OnMethodExit
    static <T extends HumanMob> void onExit(@Advice.This T mob) {
        // Nah
//        if (LevelJobRegistry.instance.isClosed())
//        {
//            mob.jobTypeHandler.setJobHandler(TalkToNPCLevelJob.class, 0, 0, 0, 0,
//                    canPerformSupplier,
//                    getTalkToJobSequenceFunction(mob)
//            );
//            mob.jobTypeHandler.getJobHandler(TalkToNPCLevelJob.class).searchInLevelJobData = false;
//            mob.jobTypeHandler.getJobHandler(TalkToNPCLevelJob.class).extraJobStreamer = TalkToNPCLevelJob.getJobStreamer();
//        }
    }
}
