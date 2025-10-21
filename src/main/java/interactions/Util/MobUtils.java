package interactions.Util;

import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.packet.PacketMobChat;
import necesse.engine.network.server.Server;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.util.TargetFinderDistance;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.gfx.GameColor;
import necesse.level.maps.hudManager.floatText.ChatBubbleText;

import java.awt.geom.Line2D;
import java.util.Optional;

public class MobUtils {
    private static final TargetFinderDistance<Mob> targetFinder = new TargetFinderDistance<>(0);

    /**
     * Tells clients to display a dialogue popup with translated text above the supplied mob
     * @param mob Anything extending Mob.class
     * @param message The message to display
     */
    public static <T extends Mob> void SendMobMessage(T mob, String message){
        if (mob.getLevel().isClient())
            return;

        Server server = mob.getServer();

        if (server == null)
            return;

        mob.getLevel().getServer().network.sendToClientsWithRegion(
                new PacketMobChat(mob.getUniqueID(), message),
                mob.getLevel(),
                mob.getLevel().regionManager.getRegionCoordByTile(mob.getTileX()), mob.getLevel().regionManager.getRegionCoordByTile(mob.getTileY())
        );
    }

    public static <T extends HumanMob> HumanMob GetNearbyHumanMob(T t, int distance, boolean lineOfSight){
        if (distance > targetFinder.searchDistance)
            targetFinder.searchDistance = distance;

        Optional<Mob> result = targetFinder.streamMobsInRange(t.getPositionPoint(), t).filter(
                mob -> mob instanceof HumanMob && mob.getDistance(t) <= distance && (!lineOfSight || HasLOS(t, (HumanMob)mob)) /* Work around for weird distance checking */
        ).findFirst();

        return (HumanMob) result.orElse(null);
    }

    public static  <T extends HumanMob> boolean HasLOS(T mob, T target) {
        Line2D line = new Line2D.Float(mob.x, mob.y, target.x, target.y);
        return !mob.getLevel().collides(line, mob.getLevelCollisionFilter());
    }
}