package com.arkflame.example;

import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.example.commands.ExampleCommand;
import com.arkflame.example.listeners.PlayerJoinListener;
import com.arkflame.example.tasks.ExampleTask;

public class ExamplePlugin extends JavaPlugin {
    
    @Override
    public void onEnable () {
        // Set static instance
        setInstance(this);

        // Save default config
        this.saveDefaultConfig();
        
        // Register the example listener
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // Register the example task
        new ExampleTask().register();

        // Register example commands
        new ExampleCommand().register();
    }

    private static ExamplePlugin instance;

    public static void setInstance(ExamplePlugin instance) {
        ExamplePlugin.instance = instance;
    }

    public static ExamplePlugin getInstance () {
        return ExamplePlugin.instance;
    }
}