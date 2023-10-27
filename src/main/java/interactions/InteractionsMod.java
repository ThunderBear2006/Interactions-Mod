package interactions;

import interactions.Jobs.TalkToNPCLevelJob;
import interactions.SettlerInteractions.Interaction;
import interactions.SettlerInteractions.SettlerDialogue;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.JobTypeRegistry;
import necesse.engine.registries.LevelJobRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.friendly.human.ElderHumanMob;
import necesse.entity.mobs.friendly.human.GuardHumanMob;
import necesse.entity.mobs.friendly.human.humanShop.BlacksmithHumanMob;
import necesse.entity.mobs.job.JobType;

@ModEntry
public class InteractionsMod {
    private static Settings InteractionSettings;

    public static int SpeechChance;
    public static int MaxInteractionDistance;
    public static int NegativeResponseBaseChance;

    public static long InteractionCoolDown;
    public static boolean DebugEnabled;

    public static Interaction LegendBadResponse = new Interaction("legend_bad_response", 4, mobs -> mobs.First().getSettlerHappiness() < 30 || GameRandom.globalRandom.nextInt(100) < NegativeResponseBaseChance);
    public static Interaction InsultResponse = new Interaction("insult_response", 7, mobs -> mobs.First().getSettlerHappiness() < 30 || GameRandom.globalRandom.nextInt(100) < NegativeResponseBaseChance);

    public void init() {
        System.out.println("[Interaction Mod] Initializing!");

        SpeechChance = InteractionSettings.SpeechChance;
        MaxInteractionDistance = InteractionSettings.MaxSpeechDistance;
        NegativeResponseBaseChance = InteractionSettings.NegativeResponseChance;
        InteractionCoolDown = InteractionSettings.InteractionCoolDown;
        DebugEnabled = InteractionSettings.DebugMode;

        // # Add interactions with their according responses #

        // Simple
        SettlerDialogue.AddNewInteraction(new Interaction("compliment_query", 4, mobs -> mobs.First().getSettlerHappiness() > 30)
                .AddResponse(new Interaction("compliment_response", 4, mobs -> mobs.First().getSettlerHappiness() > 30))
                .AddResponse(InsultResponse)
        );

        SettlerDialogue.AddNewInteraction(new Interaction("insult_query", 4, mobs -> mobs.First().getSettlerHappiness() < 30)
                .AddResponse(InsultResponse)
        );

        SettlerDialogue.AddNewInteraction(new Interaction("generic_greeting", 4, mobs -> true));
        SettlerDialogue.AddNewInteraction(new Interaction("generic_bye", 4, mobs -> true));
        SettlerDialogue.AddNewInteraction(new Interaction("generic_story_query", 5, mobs -> true)
                .AddResponse(new Interaction("generic_story_response", 4, mobs -> true))
        );

        // World
        SettlerDialogue.AddNewInteraction(new Interaction("happy_day_query", 3, mobs -> mobs.First().getSettlerHappiness() > 30)
                .AddResponse(new Interaction("happy_day_response", 3, mobs -> mobs.First().getSettlerHappiness() > 30))
                .AddResponse(new Interaction("unhappy_day_response", 3, mobs -> mobs.First().getSettlerHappiness() <= 30))
        );

        SettlerDialogue.AddNewInteraction(new Interaction("unhappy_day_query", 3, mobs -> mobs.First().getSettlerHappiness() > 30)
                .AddResponse(new Interaction("happy_day_response", 3, mobs -> mobs.First().getSettlerHappiness() > 30))
                .AddResponse(new Interaction("unhappy_day_response", 3, mobs -> mobs.First().getSettlerHappiness() <= 30))
        );

        SettlerDialogue.AddNewInteraction(new Interaction("weather_sunny_query", 3, mobs -> !mobs.First().getLevel().rainingLayer.isRaining())
                .AddResponse(new Interaction("weather_sunny_response", 3, mobs -> true))
        );

        SettlerDialogue.AddNewInteraction(new Interaction("weather_rain_query", 3, mobs -> mobs.First().getLevel().rainingLayer.isRaining())
                .AddResponse(new Interaction("weather_rain_response", 3, mobs -> true))
        );

        // Food
        SettlerDialogue.AddNewInteraction(new Interaction("hungry_query", 4, mobs -> mobs.First().hungerLevel <= 0.2F)
                .AddResponse(new Interaction("hungry_response", 4, mobs -> mobs.First().hungerLevel <= 0.2F))
        );

        SettlerDialogue.AddNewInteraction(new Interaction("food_good_query", 2, mobs -> mobs.First().lastFoodEaten != null && mobs.First().lastFoodEaten.quality.happinessIncrease > 10)
                .AddResponse(new Interaction("food_good_response", 2, mobs -> mobs.First().lastFoodEaten != null && mobs.First().lastFoodEaten.quality.happinessIncrease > 10))
        );

        // Professions

        SettlerDialogue.AddNewInteraction(new Interaction("profession_guard_armor_good", 4,
                mobs -> mobs.First() instanceof GuardHumanMob && mobs.First().getArmor() >= 19
        ));

        SettlerDialogue.AddNewInteraction(new Interaction("profession_guard_armor_bad", 3,
                mobs -> mobs.First() instanceof GuardHumanMob && mobs.First().getArmor() < 19
        ));

        SettlerDialogue.AddNewInteraction(new Interaction("profession_elder", 7, mobs -> mobs.First() instanceof ElderHumanMob)
                .AddResponse(InsultResponse));
        SettlerDialogue.AddNewInteraction(new Interaction("profession_guard", 4, mobs -> mobs.First() instanceof GuardHumanMob)
                .AddResponse(InsultResponse));
        SettlerDialogue.AddNewInteraction(new Interaction("profession_blacksmith", 4, mobs -> mobs.First() instanceof BlacksmithHumanMob)
                .AddResponse(InsultResponse));
        SettlerDialogue.AddNewInteraction(new Interaction("profession_blacksmith_shop_query", 2, mobs -> mobs.First() instanceof BlacksmithHumanMob)
                .AddResponse(new Interaction("profession_blacksmith_shop_response", 3, mobss -> true)));

        // Specific/rumors

        SettlerDialogue.AddNewInteraction(new Interaction("legend_wizard_query", 4, mobss -> true)
                .AddResponse(new Interaction("legend_wizard_response", 3, mobss -> true))
                .AddResponse(LegendBadResponse)
        );

        SettlerDialogue.AddNewInteraction(new Interaction("legend_dragon_query", 3, mobss -> true)
                .AddResponse(new Interaction("legend_dragon_response", 4, mobss -> true))
                .AddResponse(LegendBadResponse)
        );

        JobTypeRegistry.registerType("socialize", new JobType(
                InteractionsMod.DebugEnabled,
                false,
                level -> level.getWorldSettings().jobSearchRange,
                new LocalMessage("jobType", "socialJobType"),
                new LocalMessage("jobTypeDescription", "socialJobType"))
        );

        LevelJobRegistry.registerJob("talk", TalkToNPCLevelJob.class, "socialize");

        System.out.println("[Interactions Mod] Initialized!");
    }

    public void postInit() {
    }

    public ModSettings initSettings() {
        InteractionSettings = new Settings();

        return InteractionSettings;
    }
}
