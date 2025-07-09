package interactions;

import necesse.engine.modLoader.ModSettings;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class Settings extends ModSettings {
    public int MaxSpeechDistance;
    public int NegativeResponseChance;
    public int InteractionChance;
    public long InteractionCoolDown;
    public boolean DebugMode;
    public boolean GenerateDefaultDialogue;

    public Settings() {
        InteractionChance = 10;
        MaxSpeechDistance = 100;
        NegativeResponseChance = 10;
        InteractionCoolDown = 100000L;
        DebugMode = false;
        GenerateDefaultDialogue = true;
    }

    @Override
    public void addSaveData(SaveData saveData) {
        saveData.addInt("MaxSpeechDistance", MaxSpeechDistance, "The distance a mob must be within to be able to talk to another mob");
        saveData.addInt("NegativeResponseChance", NegativeResponseChance, "The chance that a mob will reply with a negative response despite being happy");
        saveData.addLong("InteractionCoolDown", InteractionCoolDown, "Minimum time a mob must wait before they can talk again");
        saveData.addFloat("InteractionChance", InteractionChance, "The chance an interaction will occur between two settlers");
        saveData.addBoolean("GenerateDefaultDialogue", GenerateDefaultDialogue, "Should the default dialogue files for the mod be generated in the config folder? Note: This should only be disabled if you have custom dialogue and don't want the default ones generated. Disabling this without having any dialogue files will make the mod not work properly");
        saveData.addBoolean("DebugMode", DebugMode, "Only enable this if the mod author tells you to, this setting can make the mod act (intentionally) weird");
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        MaxSpeechDistance = loadData.getInt("MaxSpeechDistance");
        NegativeResponseChance = loadData.getInt("NegativeResponseChance");
        InteractionCoolDown = loadData.getLong("InteractionCoolDown");
        DebugMode = loadData.getBoolean("DebugMode");
        GenerateDefaultDialogue = loadData.getBoolean("GenerateDefaultDialogue");
    }
}
