package me.surge.config;

import me.surge.animation.Easing;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author surge
 * @since 02/07/2023
 */
public class Config {

    public static final List<Entry<?>> entries = new ArrayList<>();

    public static final Entry<Boolean> ADVANCEMENTS = new Entry<>("Advancements", true);
    public static final Entry<Boolean> RECIPES = new Entry<>("Recipes", true);

    public static final Entry<Integer> FADE_IN = new Entry<>("FadeIn", 1000);
    public static final Entry<Easing> FADE_IN_EASING = new Entry<>("FadeInEasing", Easing.EXPO_IN_OUT);

    public static final Entry<Integer> FADE_OUT = new Entry<>("FadeOut", 1500);
    public static final Entry<Easing> FADE_OUT_EASING = new Entry<>("FadeOutEasing", Easing.EXPO_IN_OUT);

    public static final Entry<Integer> HOLD = new Entry<>("Hold", 2000);

    public static final Entry<String> TASK = new Entry<>("TaskColour", "#55FF55");
    public static final Entry<String> GOAL = new Entry<>("GoalColour", "#55FF55");
    public static final Entry<String> CHALLENGE = new Entry<>("ChallengeColour", "#AA00AA");
    public static final Entry<String> RECIPES_COLOUR = new Entry<>("RecipesColour", "#FFA500");
    public static final Entry<String> MESSAGE = new Entry<>("MessageColour", "#808080");

    public static final Entry<Float> SCALE = new Entry<>("Scale", 1f);
    public static final Entry<EntryAnimation> ENTRY_ANIMATION = new Entry<>("EntryAnimation", EntryAnimation.FLASH);

    public static final Entry<Integer> Y_OFFSET = new Entry<>("YOffset", 50);

    public static final Entry<Boolean> MUTE = new Entry<>("Mute", false);

    public static void save() {
        JSONObject json = new JSONObject();

        entries.forEach(entry -> json.put(entry.getName(), entry.get()));

        try {
            File dir = new File("config");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, "advanced-advancements.json");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter("config/advanced-advancements.json");

            writer.write(json.toString(4));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        File file = new File("config/advanced-advancements.json");

        if (!file.exists()) {
            return;
        }

        try {
            JSONObject json = new JSONObject(new JSONTokener(new FileInputStream("config/advanced-advancements.json")));

            entries.forEach(entry -> {
                if (entry.get() instanceof Integer) {
                    ((Entry<Integer>) entry).set(json.getInt(entry.getName()));
                } else if (entry.get() instanceof Float) {
                    ((Entry<Float>) entry).set(json.getFloat(entry.getName()));
                } else if (entry.get() instanceof String) {
                    ((Entry<String>) entry).set(json.getString(entry.getName()));
                } else if (entry.get() instanceof Enum<?>) {
                    ((Entry<Enum<?>>) entry).set(Enum.valueOf(((Enum) entry.get()).getDeclaringClass(), json.getString(entry.getName())));
                } else if (entry.get() instanceof Boolean) {
                    ((Entry<Boolean>) entry).set(json.getBoolean(entry.getName()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Entry<T> {

        private final String name;
        private T value;

        public Entry(String name, T value) {
            this.name = name;
            this.value = value;

            entries.add(this);
        }

        public String getName() {
            return name;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

    }

}
