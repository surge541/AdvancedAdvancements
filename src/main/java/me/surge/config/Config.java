package me.surge.config;

import me.surge.animation.Easing;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author surge
 * @since 02/07/2023
 */
public class Config {

    private static List<Entry<?>> entries = new ArrayList<>();

    public static Entry<Integer> FADE_IN = new Entry<>("FadeIn", 1000);
    public static Entry<Easing> FADE_IN_EASING = new Entry<>("FadeInEasing", Easing.EXPO_IN_OUT);

    public static Entry<Integer> FADE_OUT = new Entry<>("FadeOut", 1500);
    public static Entry<Easing> FADE_OUT_EASING = new Entry<>("FadeOutEasing", Easing.EXPO_IN_OUT);

    public static Entry<Integer> HOLD = new Entry<>("Hold", 2000);

    public static Entry<String> TASK = new Entry<>("TaskColour", "#55FF55");
    public static Entry<String> GOAL = new Entry<>("GoalColour", "#55FF55");
    public static Entry<String> CHALLENGE = new Entry<>("ChallengeColour", "#AA00AA");

    public static Entry<Float> SCALE = new Entry<>("Scale", 1f);

    public static void save() {
        JSONObject json = new JSONObject();

        entries.forEach(entry -> json.put(entry.getName(), entry.getValue()));

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
                if (entry.getValue() instanceof Integer) {
                    ((Entry<Integer>) entry).setValue(json.getInt(entry.getName()));
                } else if (entry.getValue() instanceof Float) {
                    ((Entry<Float>) entry).setValue(json.getFloat(entry.getName()));
                } else if (entry.getValue() instanceof String) {
                    ((Entry<String>) entry).setValue(json.getString(entry.getName()));
                } else if (entry.getValue() instanceof Enum<?>) {
                    ((Entry<Enum<?>>) entry).setValue(Enum.valueOf(((Enum) entry.getValue()).getDeclaringClass(), json.getString(entry.getName())));
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

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

    }

}
