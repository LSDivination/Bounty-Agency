package dev._2lstudios.example.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ModernArguments {
    private String label;
    private String[] args;

    public ModernArguments(String label, String[] args) {
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }

    public String getText(int index) {
        if (args.length >= index) return null;

        return args[index];
    }
    
    public int getNumber(int index) {
        String text = getText(index);

        if (text == null) return 0;

        try {
            return Integer.parseInt(args[index]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Player getPlayer(int index) {
        String text = getText(index);

        if (text == null) return null;

        return Bukkit.getPlayer(text);
    }
}
