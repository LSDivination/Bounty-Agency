package com.arkflame.bountyagency.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.arkflame.bountyagency.BountyAgency;
import com.arkflame.bountyagency.listeners.SignClickListener;
public class BountyAgencyCommand implements CommandExecutor{
    //BountyAgency main;

    // All commands requiring OP: /bountyagency create | /bountyagency list | /bountyagency target
    @Override
    public boolean onCommand(CommandSender sender, Command c, String alias, String[] args){
        //TODO: Replace isOp() with permissions
        if(!sender.isOp()){
            //TODO: Send message, no permissions.
            return true;
        }
        if(args.length < 2){
            //TODO: send message, invalid syntax
            return true;
        }
        String[] subArgs = new String[args.length-1];
        String subCMD = args[0].toLowerCase();

        switch (subCMD) {
            case "create":
                create(subArgs);
                break;
            case "list":
                list();
                break;
            case "target":
                target();
                break;
            default:
                throw new AssertionError();
        }

        return true;
    }

    private void create(String[] args){
        BountyAgency.getInstance().getServer().getPluginManager().registerEvents(new SignClickListener(), BountyAgency.getInstance());
    }

    private void list(){

    }

    private void target(){

    }
}