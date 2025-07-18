package com.arkflame.bountyagency.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.arkflame.bountyagency.BountyAgency;

public class DeathListener implements Listener {

    private static BountyAgency main;
    private static Player victim;

    public void onDeath(PlayerDeathEvent event) {
        handlePLayerDeath(event);
    }

    private static void handlePLayerDeath(PlayerDeathEvent event) {
        victim = event.getEntity();
        if (main.getIncompleteBounties(victim) == null) { // Check for bounty
            return;
        }
        if (victim.getKiller() == null) { // Check for killer
            return;
        }
        if (!(victim.getKiller() instanceof Player)) { // Check if killer is a player
            return;
        }
        
    }
}
