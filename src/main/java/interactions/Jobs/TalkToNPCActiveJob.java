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
        this.talkEndTime = 50000 + GameRandom.globalRandom.nextInt(100000);
        this.talkCoolDown = 1;
        this.talkStartTime = 0;
        this.animSpeed = animationSpeed;
    }

    @Override
    public boolean isJobValid(boolean b) {
        return (this.job.reservable.isAvailable(this.worker.getMobWorker()) && !this.worker.getMobWorker().isInCombat() && !this.target.isInCombat()) || InteractionsMod.DebugEnabled;
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
        return this.hasLOS() && this.worker.getMobWorker().getDistance(this.job.target) < InteractionsMod.MaxInteractionDistance;
    }

    @Override
    public ActiveJobResult perform() {
        // TalkStartTime + TalkEndTime = the maximum duration of the talk session
        if (this.talkStartTime != 0) { // Are we just starting?
            // No, have we been talking for long enough?
            if (this.lastTalkTime > this.talkStartTime + this.talkEndTime) {
                // Yes, lets give a fair well
                return ActiveJobResult.FINISHED;
            }
        } else {
            // Yes, lets set the time we started talking
            this.talkStartTime = this.worker.getMobWorker().getWorldTime();
            // Let's also do a greeting
        }
        // Can't talk to someone who doesn't exist or is in combat very well
        if (!this.target.removed() && !this.target.isInCombat()) {
            HumanMob worker = (HumanMob) this.worker.getMobWorker();
            // No shouting please
            if (!this.isAtTarget())
                return ActiveJobResult.MOVE_TO;
            // Has it been a moment since we have said anything?
            if (worker.getWorldTime() - this.lastTalkTime > this.talkCoolDown) {
                // Yes, lets say something else
                this.lastTalkTime = worker.getWorldTime();
                this.worker.showWorkAnimation(this.target.getX(), this.target.getY(), null, this.animSpeed);
                SettlerDialogue.HandleSettlerConversation(worker, this.target);
            }
            return ActiveJobResult.PERFORMING;
        }
        // Something went wrong, either the target died to went into combat
        return ActiveJobResult.FAILED;
    }
}
