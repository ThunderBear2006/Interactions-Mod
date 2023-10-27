package interactions.Jobs;

import interactions.InteractionsMod;
import necesse.engine.GameTileRange;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.save.LoadData;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.entity.mobs.job.*;
import necesse.level.maps.levelData.jobs.EntityLevelJob;
import necesse.level.maps.levelData.settlementData.ZoneTester;

import java.awt.*;

public class TalkToNPCLevelJob extends EntityLevelJob<HumanMob> {

    public TalkToNPCLevelJob(HumanMob mob) {
        super(mob);
    }

    public TalkToNPCLevelJob(LoadData save) {
        super(save);
    }

    public boolean shouldSave() {
        return false;
    }

    public static <T extends TalkToNPCLevelJob> JobSequence getJobSequence(EntityJobWorker worker, FoundJob<T> foundJob) {
        GameMessage activityDescription = new LocalMessage("activities", "talking_to", "<target>", foundJob.job.target.getDisplayName());
        GameLinkedListJobSequence sequence = new GameLinkedListJobSequence(activityDescription);
        sequence.add(foundJob.job.getActiveJob(worker, foundJob.priority, foundJob.handler.tileRange.apply(worker.getLevel()), 500));
        return sequence;
    }

    public static JobTypeHandler.JobStreamSupplier<? extends TalkToNPCLevelJob> getJobStreamer() {
        return (worker, handler) -> {
            Mob mob = worker.getMobWorker();
            ZoneTester restrictZone = worker.getJobRestrictZone();
            Point base = worker.getJobSearchTile();
            GameTileRange tileRange = handler.tileRange.apply(worker.getLevel());
            return mob.getLevel().entityManager.mobs.streamInRegionsShape(tileRange.getRangeBounds(base), 0).filter((m) -> {
                return !m.removed() && mob.isSamePlace(m) && m.isHuman && !m.isHostile;
            }).filter((m) -> {
                return restrictZone.containsTile(m.getTileX(), m.getTileY());
            }).filter((m) -> {
                return tileRange.isWithinRange(base, m.getTileX(), m.getTileY());
            }).map((m) -> {
                return new TalkToNPCLevelJob((HumanMob) m);
            });
        };
    }

    public TalkToNPCActiveJob getActiveJob(EntityJobWorker worker, JobTypeHandler.TypePriority priority, GameTileRange range, int animSpeed) {
        return new TalkToNPCActiveJob(worker, priority, range, this, animSpeed);
    }
}
