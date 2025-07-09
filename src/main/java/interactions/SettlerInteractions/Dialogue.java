package interactions.SettlerInteractions;

import interactions.SettlerInteractions.Conditions.ConditionHandler;
import interactions.Util.Logger;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.friendly.human.HumanMob;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Dialogue {
    public final String StringID;

    public final List<String> Text;

    public final List<String> Conditions;

    public byte HappinessMod = 0;

    /** A list that represents responses that the receiving npc may say */
    public final List<String> Responses = new ArrayList<>();

    public Dialogue(String id, List<String> text, List<String> conditions) {
        this.StringID = id;
        this.Conditions = conditions;
        this.Text = text.stream().map(s -> s.replace("<COMMA>", ",")).collect(Collectors.toList());;
    }

    public Dialogue(String id, List<String> text, byte mod, List<String> conditions) {
        this(id, text, conditions);
        this.HappinessMod = mod;
    }

    /**
     * Use to add a response to this interaction.
     * A response is just another interaction object's ID
     */
    public void AddResponse(String key){
        this.Responses.add(key);
    }

    public String GetRandomMessage(){
        return this.Text.get(GameRandom.globalRandom.nextInt(this.Text.size()));
    }

    public <T extends HumanMob> boolean CheckConditions(T mob, T target) {
        if (this.Conditions.size() == 0)
            return true;

        // The number of conditions satisfied
        int i = 0;

        try{
            while (i < Conditions.size()) {
                String[] parts = Conditions.get(i).split(" ");

                String property = parts[0];
                boolean isTargetingReceiver = property.split("_")[0].equals("target");
                String operator = parts[1];
                String standard = parts[2];

                // Do any of these condition checks fail?
                if (!ConditionHandler.MobConditionMet(isTargetingReceiver ? target : mob, property, operator, standard))
                    break; // Yes, lets stop counting

                // No? Then lets add to the number of conditions satisfied, represented as i
                i++;
            }
        } catch (Exception e) {
            Logger.printError("Invalid condition check: {0}", e.getStackTrace(), Conditions.get(i));
        }

        // Return true if the number of satisfied conditions is equal to the amount of conditions
        // E.g. all conditions are fulfilled
        return i >= Conditions.size();
    }

    /**
     * Iterates over each possible response this Interaction has
     * @param mob Any mob that extends HumanMob.class, generally settlers or village NPCs but never raiders or pirates
     * @return The first Interaction that has their predicate return true
     */
    public <T extends HumanMob> String GetBestResponse(T mob, T receiver){
        for (String key : Responses){
            if (DialogueRegistry.REGISTRY.get("response").get(key).CheckConditions(receiver, mob))
                return key;
        }
        return null;
    }
}
