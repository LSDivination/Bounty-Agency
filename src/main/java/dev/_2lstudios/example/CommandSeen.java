package dev._2lstudios.example;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandSeen extends Command {
    public CommandSeen() {
        super("seen", "Allows you to see a player", "/seen <player>", Arrays.asList("saw"));
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        System.out.println(sender.getName() + " has seen " + args[0] + "!");
        return true;
    }

}
