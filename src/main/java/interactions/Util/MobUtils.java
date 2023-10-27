package interactions.Util;

import necesse.engine.network.packet.PacketMobChat;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.util.TargetFinderDistance;
import necesse.entity.mobs.friendly.human.HumanMob;

import java.util.Optional;

public class MobUtils {
    private static final TargetFinderDistance<Mob> targetFinder = new TargetFinderDistance<>(0);

    /**
     * Tells clients to display a dialogue popup with translated text above the supplied mob
     * @param mob Anything extending Mob.class
     * @param message The message to display
     */
    public static <T extends Mob> void SendMobMessage(T mob, String message){
        if (!mob.getLevel().isServerLevel())
            return;

        mob.getLevel().getServer().network.sendToClientsAt(new PacketMobChat(mob.getUniqueID(), message), mob.getLevel());
    }

    public static <T extends HumanMob> HumanMob GetNearbyHumanMob(T t, int distance){
        if (distance > targetFinder.searchDistance)
            targetFinder.searchDistance = distance;

        Optional<Mob> result = targetFinder.streamMobsInRange(t.getPositionPoint(), t).filter(
                mob -> mob instanceof HumanMob && mob.getDistance(t) <= distance /* Work around for weird distance checking */
        ).findFirst();

        return (HumanMob) result.orElse(null);
    }
}