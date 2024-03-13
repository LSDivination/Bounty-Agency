package dev._2lstudios.example.commands;

import org.bukkit.command.CommandSender;

public class ExampleCommand extends ModernCommand {
    public ExampleCommand() {
        super("example");
    }

    @Override
    public void onCommand(CommandSender sender, ModernArguments args) {
        String arg1 = args.getText(0);

        if (arg1 != null) {
            sender.sendMessage("You wrote: " + arg1);
        } else {
            sender.sendMessage("This is the Modern Bukkit Plugin example command");
        }
    }
}
