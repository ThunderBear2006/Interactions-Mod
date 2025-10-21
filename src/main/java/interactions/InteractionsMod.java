package interactions;

import interactions.SettlerInteractions.DialogueRegistry;
import interactions.Util.Logger;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.modLoader.annotations.ModEntry;

import java.io.IOException;

@ModEntry
public class InteractionsMod {
    public static Settings settings;

    public void init() {
        Logger.init("Interaction Mod");

        Logger.print("Initializing! If any issues arise with the mod then please contact me (ThunderBear) on the Necesse discord server and I will try my best to help", "INFO");

        try {
            DialogueRegistry.Init();
        } catch (IOException e) {
            Logger.printError("Failed to initialize dialogue registry because {0}\n{1}", e.getMessage(), e.getStackTrace());
        }

        Logger.print("Initialized!");
    }

    public ModSettings initSettings() {
        settings = new Settings();

        return settings;
    }
}
