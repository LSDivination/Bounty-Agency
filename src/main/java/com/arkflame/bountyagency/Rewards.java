package com.arkflame.bountyagency;

import org.bukkit.inventory.ItemStack;


public class Rewards{
    private static ItemStack[] rewards;
    private static BountyAgency main;
    

    public Rewards(ItemStack[] rewards){
        this.rewards = rewards;
    }

    public Rewards(ItemStack rewards){
        this.rewards[0] = rewards;
    }

    public void setRewards(ItemStack rewards){
        
        
    }

    public void setRewards(ItemStack[] rewards){
        
    }
    public ItemStack[] getRewards(){
        

        return rewards;
    }
}