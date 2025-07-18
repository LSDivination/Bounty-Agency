package com.arkflame.bountyagency;

import org.bukkit.block.Block;

public class BountySign {

    private static BountyAgency main;
    private static final int ASSIGN_CONTRACT = 0;
    private static final int LIST_INCOMPLETE_OPEN = 1;
    private static final int LIST_INCOMPLETE_CLOSED = 2;
    private static final int LIST_COMPLETE_OPEN = 3;
    private static final int LIST_COMPLETE_CLOSED = 4;
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

}
