package com.arkflame.bountyagency.listeners;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.arkflame.bountyagency.Bounty;
import com.arkflame.bountyagency.BountyAgency;

public class DeathListener implements Listener {

    private static BountyAgency main;
    private static Player victim;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        handlePLayerDeath(event);
    }

    private static void handlePLayerDeath(PlayerDeathEvent event) {
        victim = event.getEntity();
        String victimName = victim.getName();
        Entity killerEntity = victim.getKiller();
        List<Bounty> bounty = main.checkIncompleteBounties(victim);

        if (bounty == null) { // Check for bounty
            return;
        }
        if (killerEntity == null) { // Check for killer
            return;
        }
        if (!(killerEntity instanceof Player)) { // Check if killer is a player
            return;
        }

        List<Bounty> closedBounties = main.getActiveClosedBounties();
        List<Bounty> openBounties = main.getActiveOpenBounties();
        Player killerPlayer = (Player) killerEntity;
        String killer = killerPlayer.getName();

        for (Bounty b : closedBounties) {
            if (victimName.equals(b.getTarget())) { //Check if the victim is the target of each closed bounty
                if (b.getHitman().equals(killer)) { //Check if the killer is the hitman for each closed bounty the victim has
                    b.closeBounty();
                    b.setKiller(killer);
                    //TODO: Probably more handlers for a bounty being completed
                }
            }
        }

        for (Bounty b : openBounties) {
            if (victimName.equals(b.getTarget())) { //Check if th victim is the target of each open bounty
                b.closeBounty();
                b.setKiller(killer);
                //TODO: Probably more handlers for a bounty being completed
            }
        }
    }
}
