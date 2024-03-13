package dev._2lstudios.example.commands;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class ModernCommand extends Command {
    public ModernCommand(String name) {
        super(name);
    }

    public void register() {
        // Command register example
        Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            Object commandMap = bukkitCommandMap.get(Bukkit.getServer());

            commandMap.getClass().getMethod("register", String.class, Command.class).invoke(commandMap, getName(), this);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        onCommand(sender, new ModernArguments(label, args));
        return true;
    }

    public abstract void onCommand(CommandSender sender, ModernArguments args);
}
