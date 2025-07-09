package interactions;

import interactions.Jobs.TalkToNPCLevelJob;
import interactions.SettlerInteractions.DialogueRegistry;
import interactions.Util.Logger;
import interactions.Util.Utils;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.JobTypeRegistry;
import necesse.engine.registries.LevelJobRegistry;
import necesse.entity.mobs.job.JobType;

import java.io.IOException;
import java.util.ArrayList;

@ModEntry
public class InteractionsMod {
    public static Settings settings;

    //TODO: Remove
//    public static Dialogue LegendBadResponse = new Dialogue("legend_bad_response", 4, mobs -> mobs.First().getSettlerHappiness() < 30 || GameRandom.globalRandom.nextInt(100) < NegativeResponseBaseChance);
//    public static Dialogue InsultResponse = new Dialogue("insult_response", 7, -5,mobs -> mobs.First().getSettlerHappiness() < 30 || GameRandom.globalRandom.nextInt(100) < NegativeResponseBaseChance);

    public void init() throws IOException {
        Logger.init("Interaction Mod");

        Logger.print("Initializing! If any issues arise with the mod then please contact me (ThunderBear) on the Necesse discord server and I will try my best to help", "INFO");

        DialogueRegistry.Init();

        // # Add interactions with their according responses #

        //TODO: Remove
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("compliment_query", 4, 5,mobs -> mobs.First().getSettlerHappiness() > 30)
//                .AddResponse(new Dialogue("compliment_response", 4, mobs -> mobs.First().getSettlerHappiness() > 30))
//                .AddResponse(InsultResponse)
//        );
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("insult_query", 4, -5, mobs -> mobs.First().getSettlerHappiness() < 30)
//                .AddResponse(InsultResponse)
//        );
//
//        SettlerDialogue.AddNewDialogue("greeting", new Dialogue("generic_greeting", 4, mobs -> true));
//        SettlerDialogue.AddNewDialogue("farewell", new Dialogue("generic_bye", 4, mobs -> true));
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("generic_story_query", 5, mobs -> true)
//                .AddResponse(new Dialogue("generic_story_response", 4, mobs -> true))
//        );
//
//        // World
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("happy_day_query", 3, 3,mobs -> mobs.First().getSettlerHappiness() > 30)
//                .AddResponse(new Dialogue("happy_day_response", 3, 3, mobs -> mobs.First().getSettlerHappiness() > 30))
//                .AddResponse(new Dialogue("unhappy_day_response", 3, -3, mobs -> mobs.First().getSettlerHappiness() <= 30))
//        );
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("unhappy_day_query", 3, -3, mobs -> mobs.First().getSettlerHappiness() > 30)
//                .AddResponse(new Dialogue("happy_day_response", 3, 3, mobs -> mobs.First().getSettlerHappiness() > 30))
//                .AddResponse(new Dialogue("unhappy_day_response", 3, -3, mobs -> mobs.First().getSettlerHappiness() <= 30))
//        );
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("weather_sunny_query", 3, mobs -> !mobs.First().getLevel().rainingLayer.isRaining())
//                .AddResponse(new Dialogue("weather_sunny_response", 3, mobs -> true))
//        );
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("weather_rain_query", 3, mobs -> mobs.First().getLevel().rainingLayer.isRaining())
//                .AddResponse(new Dialogue("weather_rain_response", 3, mobs -> true))
//        );
//
//        // Food
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("hungry_query", 4, mobs -> mobs.First().hungerLevel <= 0.2F)
//                .AddResponse(new Dialogue("hungry_response", 4, mobs -> mobs.First().hungerLevel <= 0.2F))
//        );
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("food_good_query", 2, mobs -> mobs.First().lastFoodEaten != null && mobs.First().lastFoodEaten.quality.happinessIncrease > 10)
//                .AddResponse(new Dialogue("food_good_response", 2, mobs -> mobs.First().lastFoodEaten != null && mobs.First().lastFoodEaten.quality.happinessIncrease > 10))
//        );
//
//        // Professions
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("profession_guard_armor_good", 4,
//                mobs -> mobs.First() instanceof GuardHumanMob && mobs.First().getArmor() >= 19
//        ));
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("profession_guard_armor_bad", 3,
//                mobs -> mobs.First() instanceof GuardHumanMob && mobs.First().getArmor() < 19
//        ));
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("profession_elder", 7, mobs -> mobs.First() instanceof ElderHumanMob)
//                .AddResponse(InsultResponse));
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("profession_guard", 4, mobs -> mobs.First() instanceof GuardHumanMob)
//                .AddResponse(InsultResponse));
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("profession_blacksmith", 4, mobs -> mobs.First() instanceof BlacksmithHumanMob)
//                .AddResponse(InsultResponse));
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("profession_blacksmith_shop_query", 2, mobs -> mobs.First() instanceof BlacksmithHumanMob)
//                .AddResponse(new Dialogue("profession_blacksmith_shop_response", 3, mobss -> true)));
//
//        // Specific/rumors
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("legend_wizard_query", 4, mobss -> true)
//                .AddResponse(new Dialogue("legend_wizard_response", 3, mobss -> true))
//                .AddResponse(LegendBadResponse)
//        );
//
//        SettlerDialogue.AddNewDialogue("dialogue", new Dialogue("legend_dragon_query", 3, mobss -> true)
//                .AddResponse(new Dialogue("legend_dragon_response", 4, mobss -> true))
//                .AddResponse(LegendBadResponse)
//        );

//        Nah
//        JobTypeRegistry.registerType("socialize", new JobType(
//                InteractionsMod.settings.DebugMode,
//                false,
//                level -> level.getWorldSettings().jobSearchRange,
//                new LocalMessage("jobType", "socialJobType"),
//                new LocalMessage("jobTypeDescription", "socialJobType"))
//        );
//
//        LevelJobRegistry.registerJob("talk", TalkToNPCLevelJob.class, "socialize");

        Logger.print("Initialized!");
    }

    public void postInit() {
    }

    public ModSettings initSettings() {
        settings = new Settings();

        return settings;
    }
}
