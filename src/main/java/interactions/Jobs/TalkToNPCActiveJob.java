package interactions.Jobs;

import interactions.InteractionsMod;
import interactions.SettlerInteractions.SettlerDialogue;
import necesse.engine.GameTileRange;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.entity.mobs.job.EntityJobWorker;
import necesse.entity.mobs.job.JobTypeHandler;
import necesse.entity.mobs.job.activeJob.ActiveJobResult;
import necesse.entity.mobs.job.activeJob.MobActiveJob;
import necesse.level.maps.levelData.jobs.EntityLevelJob;

@Deprecated
public class TalkToNPCActiveJob extends MobActiveJob<HumanMob> {
    public EntityLevelJob<? extends HumanMob> job;
    public long talkEndTime;
    public long lastTalkTime;
    public long talkCoolDown;
    public long talkStartTime;
    public int animSpeed;

    public TalkToNPCActiveJob(EntityJobWorker worker, JobTypeHandler.TypePriority priority, GameTileRange maxRange, EntityLevelJob<? extends HumanMob> job, int animationSpeed) {
        super(worker, priority, job.target, maxRange);
        this.job = job;
        this.talkEndTime = 5000 + GameRandom.globalRandom.nextInt(10000);
        this.talkCoolDown = 500;
        this.talkStartTime = 0;
        this.animSpeed = animationSpeed;
    }

    @Override
    public boolean isJobValid(boolean b) {
        boolean valid = (this.job.reservable.isAvailable(this.worker.getMobWorker()) && !this.worker.getMobWorker().isInCombat() && !this.target.isInCombat());
        return valid && !SettlerDialogue.HasInteractedRecently(worker.getMobWorker());
    }

    @Override
    public ActiveJobResult performTarget() {
        return ActiveJobResult.FAILED;
    }

    @Override
    public void tick(boolean b, boolean b1) {
        this.job.reservable.reserve(this.worker.getMobWorker());
    }

    @Override
    public boolean isAtTarget() {
        return this.hasLOS() && this.worker.getMobWorker().getDistance(this.job.target) < InteractionsMod.settings.MaxSpeechDistance;
    }

    @Override
    public ActiveJobResult perform() {
        HumanMob worker = (HumanMob) this.worker.getMobWorker();

        // Can't talk to someone who doesn't exist or is in combat very well
        if (this.target.removed() || this.target.isInCombat())
            return ActiveJobResult.FAILED;

        // No shouting please
        if (!this.isAtTarget())
            return ActiveJobResult.MOVE_TO;

        // TalkStartTime + TalkEndTime = the duration of the talk session
        if (this.talkStartTime != 0) { // Are we just starting?
            // No, have we been talking for long enough?
            if (worker.getWorldTime() > this.talkStartTime + this.talkEndTime) {
                // Yes, lets give a fair well
                SettlerDialogue.HandleDialogue(worker, this.target, "farewell");
                return ActiveJobResult.FINISHED;
            }
        } else {
            // Yes, lets set the time we started talking
            this.talkStartTime = this.worker.getMobWorker().getWorldTime();
            // Let's also do a greeting
            SettlerDialogue.HandleDialogue(worker, this.target, "greeting");
        }

        // Has it been a moment since we have said anything?
        if (worker.getWorldTime() - this.lastTalkTime >= this.talkCoolDown) {
            // Yes, lets talk some more
            this.lastTalkTime = worker.getWorldTime();
            SettlerDialogue.HandleDialogue(worker, this.target, "dialogue");
        }

        return ActiveJobResult.PERFORMING;
    }
}
