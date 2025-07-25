package com.arkflame.bountyagency;

import org.bukkit.block.Block;

public class BountySign {

    private static BountyAgency main;
    public static final int ASSIGN_CONTRACT = 0;
    public static final int LIST_IDLE = 1; //Implied closed
    public static final int LIST_ACTIVE = 2;
    public static final int LIST_OPEN = 4;
    public static final int LIST_COMPLETE = 8;
    private static int type;
    private static int x;
    private static int y;
    private static int z;

    public BountySign(Block sign, int type) {
        this.type = type;
        x = sign.getX();
        y = sign.getY();
        z = sign.getZ();
    }

    public BountySign(int type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void create(int type) {

    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setType(int flags){
        type = type | flags;
    }

    public boolean hasFlags(int flags){
        return (type & flags) == flags;
    }

    public boolean missingFlags(int flags){
        return (type & flags) == 0;
    }
}
