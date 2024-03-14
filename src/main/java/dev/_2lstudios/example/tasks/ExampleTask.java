package dev._2lstudios.example.tasks;

import org.bukkit.Bukkit;

import dev._2lstudios.example.ExamplePlugin;

public class ExampleTask extends ModernTask {
    public ExampleTask() {
        super(ExamplePlugin.getInstance(), ExamplePlugin.getInstance().getConfig().getInt("task-repeat-every") * 20L, false);
    }

    @Override
    public void run() {
        final String message = ExamplePlugin.getInstance().getConfig().getString("messages.from-task");
        Bukkit.getServer().broadcastMessage(message);
    }
}