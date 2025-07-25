package com.arkflame.bountyagency;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Bounty {

    BountyAgency main;
    String target;
    String hitman;
    String killer;
    ItemStack[] rewards;
    ItemMeta[] rewardsMeta;
    int mode;
    int id;

    public static final int IS_ACTIVE = 1;
    public static final int IS_OPEN = 2;
    public static final int IS_COMPLETE = 4;
    public static final int IS_PAID_OUT = 8;

    public Bounty(Player target, Player hitman, Rewards rewards, int mode, int id) {
        this.target = target.getUniqueId().toString();
        this.hitman = hitman.getUniqueId().toString();
        this.mode = mode;
        this.rewards = rewards.getRewards();
        this.id = id;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return Bukkit.getPlayer(target).getUniqueId().toString();
    }

    public void setHitman(String hitman) {
        this.hitman = hitman;
    }

    public String getHitman() {
        return Bukkit.getPlayer(hitman).getUniqueId().toString();
    }

    public ItemStack[] getRewards() {

        return rewards;

    }

    public ItemMeta[] getMeta(ItemStack[] rewards) {
        for (int i = 0; i < rewards.length; i++) {
            rewardsMeta[i] = rewards[i].getItemMeta();
        }
        return rewardsMeta;
    }

    public void closeBounty() {
        mode = mode & ~IS_ACTIVE;
        mode |= IS_COMPLETE;
    }

    public int getMode() {
        return mode;
    }

    public boolean hasFlags(int flags){
        return (mode & flags) == flags;
    }

    public boolean missingFlags(int flags){
        return (mode & flags) == 0;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public String listRewards(){
        String rewardsList = null;
        for(ItemStack item : rewards){
            //TODO: Build string of rewards list
        }
        return rewardsList;
    }

    public void setKiller(String killer){
        this.killer = killer;
    }

    public String getKiller(){
        return killer;
    }
}
