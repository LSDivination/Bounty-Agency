package com.arkflame.bountyagency.listeners;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.arkflame.bountyagency.Bounty;
import com.arkflame.bountyagency.BountyAgency;
import com.arkflame.bountyagency.BountySign;

public class SignClickListener implements Listener {

    private static BountyAgency main;

    public SignClickListener(BountyAgency main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        handlePlayerInteract(event);

    }

    //Server operator creation interaction
    private void handlePlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (main.getBountySign(block) == null) {
            return;
        }
        BountySign sign = main.getBountySign(block);
        final int case0 = BountySign.ASSIGN_CONTRACT;
        final int case1 = BountySign.LIST_IDLE;
        final int case2 = BountySign.LIST_ACTIVE;
        final int case3 = BountySign.LIST_ACTIVE | BountySign.LIST_OPEN;
        final int case4 = BountySign.LIST_COMPLETE | BountySign.LIST_OPEN;
        final int case5 = BountySign.LIST_COMPLETE;
        List<Bounty> bounties;
        switch (sign.getType()) {
            case case0: //Contracting sign
                //TODO: Open chest inventory of player list
                return;
            case case1: //Idle bounty list (Only closed bounties can be idle)
                bounties = main.getIdleBounties();
                if (bounties.isEmpty()) {
                    //TODO: Send message, No idle bounties, yet.
                    return;
                }
                for (Bounty b : bounties) {
                    String victim = b.getTarget();
                    String rewards = b.listRewards();
                    //TODO: Send message, victim + rewards
                }
                return;
            case case2: // Active closed bounty list
                bounties = main.getActiveClosedBounties();
                if (bounties.isEmpty()) {
                    //TODO: Send message, No idle bounties, yet.
                    return;
                }
                for (Bounty b : bounties) {
                    String victim = b.getTarget();
                    String rewards = b.listRewards();
                    //TODO: Send message, victim + rewards
                }
                return;
            case case3: // Active open bounty list
                bounties = main.getActiveOpenBounties();
                if (bounties.isEmpty()) {
                    //TODO: Send message, No idle bounties, yet.
                    return;
                }
                for (Bounty b : bounties) {
                    String victim = b.getTarget();
                    String rewards = b.listRewards();
                    //TODO: Send message, victim + rewards
                }
                return;
            case case4: // Completed open bounty list
                bounties = main.getCompletedOpenBounties();
                if (bounties.isEmpty()) {
                    //TODO: Send message, No completed open bounties, yet.
                    return;
                }
                for (Bounty b : bounties) {
                    String victim = b.getTarget();
                    String rewards = b.listRewards();
                    //TODO: Send message, victim + rewards
                }
                return;
            case case5: // Completed closed bounty list
                bounties = main.getCompletedClosedBounties();
                if (bounties.isEmpty()) {
                    //TODO: Send message, No completed closed bounties, yet.
                    return;
                }
                for (Bounty b : bounties) {
                    String victim = b.getTarget();
                    String hitman = b.getHitman();
                    String rewards = b.listRewards();
                    //TODO: Send message, victim + rewards
                }
                return;
            default:
                //Send message, what did you do?
                return;
        }
    }
}
