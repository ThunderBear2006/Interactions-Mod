package interactions.SettlerInteractions;

import necesse.engine.save.LoadData;

public class CustomInteractionsHandler {

    public static void AddInteraction(LoadData data){
        String name = data.getName();
        System.out.println("[Interactions Mod] Custom interaction loaded: " + name);
    }
}
