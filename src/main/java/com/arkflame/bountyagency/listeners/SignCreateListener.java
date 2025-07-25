package com.arkflame.bountyagency.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.arkflame.bountyagency.BountyAgency;
import com.arkflame.bountyagency.BountySign;

public class SignCreateListener implements Listener {

    BountyAgency main;
    String[] args;

    public SignCreateListener(BountyAgency main, String[] args) {
        this.main = main;
        this.args = args;
    }

    @EventHandler
    public void onCreate(PlayerInteractEvent event) {
        handleSignCreate(event);
    }

    private void handleSignCreate(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        //TODO: Handle permissions properly.
        if (!player.isOp()) {
            //TODO: Send message, You can't do that.
            return;
        } else if (!(block.getBlockData() instanceof Sign)) {
            //TODO: Send message, Please click a sign.
            return;
        } else if (args.length < 2) {
            //TODO: Send message, please specify a type of sign
            return;
        } else if (args[1].toLowerCase().equals("list") && args.length < 3) {
            //TODO: Send message, please specify a type of list
            return;
        } else if (args[1].toLowerCase().equals("contracting")) {
            BountySign sign = new BountySign(block, BountySign.ASSIGN_CONTRACT);
            main.addBountySign(block, sign);
            return;
        } else if (args[1].toLowerCase().equals("list")) {
            BountySign sign;
            int hasFlags;
            switch (args[2].toLowerCase()) {
                case "idle":
                    hasFlags = BountySign.LIST_IDLE;
                    sign = new BountySign(block, hasFlags);
                    main.addBountySign(block, sign);
                    return;
                case "active":
                    switch (args[3].toLowerCase()){
                        case "open":
                            hasFlags = BountySign.LIST_ACTIVE | BountySign.LIST_OPEN;
                            sign = new BountySign(block, hasFlags);
                            main.addBountySign(block, sign);
                            return;
                        case "closed":
                            hasFlags = BountySign.LIST_ACTIVE;
                            sign = new BountySign(block, hasFlags);
                            main.addBountySign(block, sign);
                            return;
                        default:
                            //TODO: Send message, that type of active list does not exist
                    }
                case "completed":
                    switch(args[3].toLowerCase()){
                        case "open":
                            hasFlags = BountySign.LIST_OPEN | BountySign.LIST_COMPLETE;
                            sign = new BountySign(block, hasFlags);
                            main.addBountySign(block, sign);
                            return;
                        case "closed":
                            sign = new BountySign(block, BountySign.LIST_COMPLETE);
                            main.addBountySign(block, sign);
                            return;
                        default:
                            //TODO: Send message, that type of completed list does not exist
                            return;
                    }
                default:
                    //TODO: Send message, that type of list does not exist.
                    return;
            }
        }
    }
}
