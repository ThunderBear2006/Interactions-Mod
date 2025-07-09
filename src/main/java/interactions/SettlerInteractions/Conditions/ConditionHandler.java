package interactions.SettlerInteractions.Conditions;

import interactions.Util.Logger;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.friendly.human.HumanMob;

import java.util.HashMap;

public class ConditionHandler {
    private static final HashMap<String, Object> MOB_MAP = new HashMap<>();

    public static boolean Eval(int property, String operator, int standard) {
        boolean result = ((operator.contains("=") && property == standard) || (operator.contains(">") && property > standard)) != operator.contains("!");
        Logger.printDebug("Evaluating condition: {0} {1} {2} is {3}", property, operator, standard, result);
        return result;
    }

    public static <X extends Mob> boolean MobConditionMet(X mob, String prop, String operator, String stan) {
        HashMap<String, Object> mobMap = MapMobData(mob);

        return Eval((Integer) mobMap.get(prop), operator, Integer.parseInt(stan));
    }

    public static <T extends Mob> HashMap<String, Object> MapMobData(T mob) {
        MOB_MAP.clear();

        MOB_MAP.put("health", mob.getHealth());
        MOB_MAP.put("happiness", ((HumanMob)mob).getSettlerHappiness());
        MOB_MAP.put("armor", mob.getArmor());
        MOB_MAP.put("hunger", ((HumanMob) mob).hungerLevel * 100);
        MOB_MAP.put("raining", mob.getLevel().rainingLayer.isRaining() ? 1 : 0);
        MOB_MAP.put("inCombat", mob.isInCombat() ? 1 : 0);
        MOB_MAP.put("isAsleep", mob.getWorldEntity().isNight() ? 1 : 0);
        MOB_MAP.put("lastFoodHappiness", ((HumanMob) mob).lastFoodEaten != null ? ((HumanMob) mob).lastFoodEaten.quality.happinessIncrease : 0);

        return MOB_MAP;
    }
}