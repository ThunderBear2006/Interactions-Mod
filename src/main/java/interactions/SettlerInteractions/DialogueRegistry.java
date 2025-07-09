package interactions.SettlerInteractions;

import interactions.InteractionsMod;
import interactions.Util.Logger;
import interactions.Util.Utils;
import necesse.engine.GlobalData;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DialogueRegistry {
    /*
     Requirements
        id
        type
        text
        happiness modifier
        conditions
        *responses
    */

    public static final List<String> DEFAULT_CONDITIONS = Utils.CreateList("inCombat = 0", "isAsleep = 0");

    public static final HashMap<String, HashMap<String,Dialogue>> REGISTRY = new HashMap<>();

    public static void Init() throws IOException {
        REGISTRY.put("dialogue", new HashMap<>());
        REGISTRY.put("response", new HashMap<>());

        if (InteractionsMod.settings.GenerateDefaultDialogue)
            RegisterDefaultDialogue();

        LoadDialogueData();
    }

    public static void RegisterDefaultDialogue(){
        SaveDialogueToFile(
                "positive_query",
                "dialogue",
                (byte) 5,
                Utils.CreateList("Good seeing you <TARGET_NAME><COMMA> take care","Wow <TARGET_NAME><COMMA> you're looking great today!","You are doing a great job <TARGET_NAME><COMMA> keep at it","I love what you are wearing <TARGET_NAME>!"),
                Utils.CreateList("default", "happiness > 30"),
                Utils.CreateList("compliment_response", "insult_response")
        );

        SaveDialogueToFile(
                "positive_response",
                "response",
                (byte) 5,
                Utils.CreateList("Thank you <TARGET_NAME>!","I needed that<COMMA> thank you <TARGET_NAME>","You are too kind","Likewise!"),
                Utils.CreateList("default", "happiness > 30"),
                new ArrayList<>()
        );

        SaveDialogueToFile(
                "negative_query",
                "dialogue",
                (byte) -10,
                Utils.CreateList("Go dig up some truffles you hog","Go swim in an ocean of sharks","I've seen goblins that look better than you <TARGET_NAME>","Go jump off a tree <TARGET_NAME>"),
                Utils.CreateList("default", "happiness < 30"),
                Utils.CreateList("insult_response")
        );

        SaveDialogueToFile(
                "negative_response",
                "response",
                (byte) -10,
                Utils.CreateList("You should be locked away deep in a dungeon","Yeah<COMMA> right.","Shove off","Go hug a zombie <TARGET_NAME>","Even a goblin would find you ugly","Leave me be","Well you get no maidens <TARGET_NAME>", "*silence*"),
                Utils.CreateList("default", "happiness < 30"),
                new ArrayList<>()
        );

        SaveDialogueToFile(
                "generic_story_query",
                "dialogue",
                (byte) 0,
                Utils.CreateList("I am pretty sure I saw a squirrel fly from one tree to another the other day", "I once went to a desert and saw a snake that had two heads!", "Did you know that you can sometimes find chests dotted around the world?"),
                Utils.CreateList("default"),
                Utils.CreateList("generic_story_response", "negative_story_response")
        );

        SaveDialogueToFile(
                "generic_story_response",
                "response",
                (byte) 0,
                Utils.CreateList("Oh really?", "You don't say?", "Is that so?", "Neat!"),
                Utils.CreateList("default"),
                new ArrayList<>()
        );

        SaveDialogueToFile(
                "negative_story_response",
                "response",
                (byte) -5,
                Utils.CreateList("I don't care", "I'm not interested in your stories", "Your stories bore me", "Please<COMMA> I'm busy"),
                Utils.CreateList("default", "happiness !> 30"),
                new ArrayList<>()
        );

        SaveDialogueToFile(
                "sunny_weather_query",
                "dialogue",
                (byte) 0,
                Utils.CreateList("The weather is really nice today isn't it? Perfect for going on a walk", "On days like these<COMMA> I just want to sit by a tree and read a book", "With weather like this I might just sit by the beach and watch the waves"),
                Utils.CreateList("default", "raining = 0"),
                Utils.CreateList("sunny_weather_response")
        );

        SaveDialogueToFile(
                "sunny_weather_response",
                "response",
                (byte) 0,
                Utils.CreateList("I was just thinking of doing that!", "I'd rather just stay inside all day if I could", "Too bad I won't have time to do anything like that with all the work I have to do"),
                Utils.CreateList("default"),
                new ArrayList<>()
        );

        SaveDialogueToFile(
                "rainy_weather_query",
                "dialogue",
                (byte) 0,
                Utils.CreateList("I wish this rain would clear up already", "I am soaked from all this damn rain", "I just love the sound of rainfall!"),
                Utils.CreateList("default", "raining = 1"),
                Utils.CreateList("rainy_weather_response")
        );

        SaveDialogueToFile(
                "rainy_weather_response",
                "response",
                (byte) 0,
                Utils.CreateList("At least the rain is good for the crops", "Wish I had time to jump in the puddles", "I wonder where rain comes from, perhaps Poseidon?"),
                Utils.CreateList("default"),
                new ArrayList<>()
        );

        SaveDialogueToFile(
                "food_good_query",
                "dialogue",
                (byte) 0,
                Utils.CreateList("Mmm that <LAST_FOOD_EATEN> was delicious!", "I absolutely loved the <LAST_FOOD_EATEN> I ate", "You simply must try some <LAST_FOOD_EATEN> when you get a chance"),
                Utils.CreateList("default", "lastFoodHappiness >= 10", "hunger > 25"),
                Utils.CreateList("food_good_response", "bad_food_response")
        );

        SaveDialogueToFile(
                "good_bad_response",
                "response",
                (byte) 5,
                Utils.CreateList("That was some real gourmet stuff"),
                Utils.CreateList("default", "lastFoodHappiness >= 10", "hunger > 25"),
                new ArrayList<>()
        );

        SaveDialogueToFile(
                "food_bad_response",
                "response",
                (byte) -5,
                Utils.CreateList("Man I am starving! Theres nothing to eat!", "That cow over there is starting to look like a steak", "So uh<COMMA> I hear there are people that eat other people...", "I'm so hungry I could eat an ostrich!"),
                Utils.CreateList("default", "hunger !> 25"),
                new ArrayList<>()
        );

        //TODO: Add these when I'm not feeling lazy
        //"My mother used to make the most delicious butterscotch cinnamon pies"
        //"I dreamt last night I was walking down a long dark corridor, it was really creepy"
    }

    public static List<Dialogue> GetShuffledDialogueList(String type) {
        List<Dialogue> list = new ArrayList<>(REGISTRY.get(type).values());
        Collections.shuffle(list);
        return list;
    }

    public static void Register(String fileName,LoadData data) {
        try {
            List<String> conditions = new ArrayList<>();

            if (data.hasLoadDataByName("conditions")){
                conditions = new ArrayList<>();
                for (String condition : data.getStringList("conditions")) {
                    Logger.printDebug("Condition added to interaction: {0}", condition);
                    if (condition.equals("default")) {
                        conditions.addAll(DEFAULT_CONDITIONS);
                    } else {
                        conditions.add(condition);
                    }
                }
            }

            Dialogue newDialogue = new Dialogue(
                    data.getSafeString("id"),
                    data.getStringList("text"),
                    data.hasLoadDataByName("happinessModifier") ? data.getByte("happinessModifier") : 0,
                    conditions
            );

            if (data.hasLoadDataByName("responses")) {
                for (String response_id : data.getStringList("responses")) {
                    newDialogue.AddResponse(response_id);
                }
            }

            REGISTRY.get(data.getSafeString("type")).put(newDialogue.StringID, newDialogue);
            Logger.print("Dialogue registered: {0}",data.getSafeString("id"));
        }
        catch (Exception e) {
            Logger.printError("Failed to register new dialogue: {0} because {1}", e.getStackTrace(), fileName, e);
        }
    }

    public static void LoadDialogueData() throws IOException {
        String dialogueFolderPath = GlobalData.cfgPath() + "mods\\InteractionsMod\\NPCDialogue";

        if (!Files.exists(Paths.get(dialogueFolderPath))) {
            Files.createDirectory(Paths.get(dialogueFolderPath));
        }
        File[] files = new File(dialogueFolderPath).listFiles();

        if (files == null || files.length == 0){
            Logger.printError("No dialogue files found!! Please ensure that there is a folder with files located at: {0} if there is then please contact the mod author if issues persists", dialogueFolderPath);
            throw new RuntimeException();
        }

        for (File file : files) {
            if (!file.getName().endsWith(".dialogue"))
                continue;
            if (file.isDirectory())
                continue;
            LoadData data = new LoadData(file);
            DialogueRegistry.Register(file.getName(),data);
        }
    }

    public static void SaveDialogueToFile(String id, String type, byte happiness, List<String> text, List<String> conditions, List<String> responses) {
        String dialogueFolderPath = GlobalData.cfgPath() + "mods\\InteractionsMod\\NPCDialogue\\" + id + ".dialogue";

        if (Files.exists(Paths.get(dialogueFolderPath)))
            return;

        if (conditions.remove("default")){
            conditions.addAll(DEFAULT_CONDITIONS);
        }

        SaveData data = new SaveData("");

        data.addSafeString("id", id);
        data.addSafeString("type", type);
        data.addStringList("text", text);
        data.addByte("happinessModifier", happiness);
        data.addStringList("conditions", conditions);
        data.addStringList("responses", responses);

        data.saveScript(new File(dialogueFolderPath));
    }
}
