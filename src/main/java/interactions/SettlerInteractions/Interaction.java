package interactions.SettlerInteractions;

import interactions.InteractionsMod;
import interactions.Util.Tuple;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.friendly.human.HumanMob;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Interaction{
    /** The base message key in locale file */
    public final String MessageKeyBase;

    /** Max number of local entries this interaction has. Must be set to the maximum number of localization entries this interaction has in lang file*/
    public final int Iterations;

    /** Returns true if supplied conditions are met */
    public final Predicate<Tuple<HumanMob,HumanMob>> Predicate;

    /** A list that represents responses that the receiving npc may say */
    private final List<Interaction> PossibleResponses = new ArrayList<>();

    private long LastTimeUsed = -1L;

    public Interaction(String keyBase, int keyIterations, Predicate<Tuple<HumanMob,HumanMob>> predicate) {
        this.MessageKeyBase = keyBase;
        this.Iterations = keyIterations;
        this.Predicate = predicate;
    }

    /**
     * Use to add a response to this interaction.
     * Note a response is just another interaction object, and may be supplied its own conditions.
     * Multiple interactions can be added to an interaction as a response,
     * sub responses (e.g. a response within a response) will never be used
     * @return Itself
     */
    public Interaction AddResponse(Interaction response){
        PossibleResponses.add(response);
        return this;
    }

    /**
     * Use to get a random message.
     * Appends a random number (between 0 and this interaction's number of Iterations) at the end of MessageBaseKey
     * @return A new localized String
     */
    public String GetRandomMessage(long timeUsed){
        if (InteractionsMod.DebugEnabled && this.LastTimeUsed != -1L)
            System.out.println(this.LastTimeUsed + " " + timeUsed);

        this.LastTimeUsed = timeUsed;

        return Localization.translate(
                "mobmsg",
                MessageKeyBase + "_" + GameRandom.getIntBetween(GameRandom.globalRandom, 0, Iterations - 1)
        );
    }

    public boolean WasUsedRecently(long currentTime){
        if (InteractionsMod.DebugEnabled && this.LastTimeUsed != -1L)
            System.out.println("DEBUG: time last used = " + this.LastTimeUsed + " current time: " + currentTime + " delta: " + (currentTime - this.LastTimeUsed));
        return this.LastTimeUsed != -1L && currentTime - this.LastTimeUsed < InteractionsMod.InteractionCoolDown;
    }

    /**
     * Iterates over each possible response this Interaction has
     * @param mob Any mob that extends HumanMob.class, generally settlers or village NPCs but never raiders or pirates
     * @return The first Interaction that has their predicate return true
     */
    public <T extends HumanMob> Interaction GetBestResponse(T mob, T reciever){
        for (Interaction i : PossibleResponses){
            if (i.Predicate.test(new Tuple<>(mob, reciever)))
                return i;
        }
        return null;
    }
}
