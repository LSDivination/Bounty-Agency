package com.arkflame.modernlib.config;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.arkflame.modernlib.utils.ChatColors;

public class ConfigWrapper {
    private ConfigurationSection config = null;
    private String path = null;

    // This will store all text that contains color
    private Map<String, String> colorTextMap = new HashMap<>();

    public ConfigWrapper(ConfigurationSection config) {
        this.config = config;
    }

    public void load(String path) {
        this.path = path;
        load();
    }

    public void load() {
        if (path == null) return;
        try {
            this.config = YamlConfiguration.loadConfiguration(new File(path));
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        if (path == null) {
            return;
        }
        if (config instanceof YamlConfiguration) {
            try {
                ((YamlConfiguration) config).save(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public boolean isLoaded() {
        return config != null;
    }

    public String getText(String key) {
        if (colorTextMap.containsKey(key)) {
            return colorTextMap.get(key);
        } else {
            String text = ChatColors.color(getString(key));

            colorTextMap.put(key, text);

            return text;
        }
    }

    public String getString(String key) {
        if (!isLoaded())
            return "undefined";
        return config.getString(key);
    }

    public int getInt(String key) {
        if (!isLoaded())
            return 0;
        return config.getInt(key);
    }

    public ConfigWrapper getSection(String key) {
        if (!isLoaded())
            return null;
        ConfigurationSection section = config.getConfigurationSection(key);
        if (section == null)
            return null;
        return new ConfigWrapper(section);
    }

    public Set<String> getKeys() {
        if (!isLoaded())
            return Collections.emptySet();
        return config.getKeys(false);
    }
}
