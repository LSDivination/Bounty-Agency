package com.arkflame.bountyagency.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.arkflame.bountyagency.BountyAgency;

public class SignClickListener implements Listener {

    private static BountyAgency main;

    public void onPlayerInteract(PlayerInteractEvent event) {
        handlePlayerInteract(event);

    }

    //Server operator creation interaction
    private void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        //TODO: Handle permissions properly.
        if (!player.isOp()) {
            //TODO: Send message, You can't do that.
            return;
        } else {
            Block block = event.getClickedBlock();
            if (!(block.getBlockData() instanceof Sign)) {
                //TODO: Send message, Please click a sign.
                return;
            } else {
                //TODO: Send create() command
            }
        }
    }
}
