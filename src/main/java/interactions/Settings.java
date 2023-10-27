package interactions;

import necesse.engine.modLoader.ModSettings;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class Settings extends ModSettings {
    public int SpeechChance;
    public int MaxSpeechDistance;
    public int NegativeResponseChance;
    public long InteractionCoolDown;
    public boolean DebugMode;

    public Settings() {
        SpeechChance = 10;
        MaxSpeechDistance = 100;
        NegativeResponseChance = 10;
        InteractionCoolDown = 100000L;
        DebugMode = false;
    }

    @Override
    public void addSaveData(SaveData saveData) {
        saveData.addInt("SpeechChance", SpeechChance);
        saveData.addInt("MaxSpeechDistance", MaxSpeechDistance);
        saveData.addInt("NegativeResponseChance", NegativeResponseChance);
        saveData.addLong("InteractionCoolDown", InteractionCoolDown);
        saveData.addBoolean("DebugMode", DebugMode);
    }

    @Override
    public void applyLoadData(LoadData loadData) {
        SpeechChance = loadData.getInt("SpeechChance");
        MaxSpeechDistance = loadData.getInt("MaxSpeechDistance");
        NegativeResponseChance = loadData.getInt("NegativeResponseChance");
        InteractionCoolDown = loadData.getLong("InteractionCoolDown");
        DebugMode = loadData.getBoolean("DebugMode");
    }
}
