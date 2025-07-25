package com.arkflame.bountyagency;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.bountyagency.commands.BountyAgencyCommand;
import com.arkflame.bountyagency.listeners.DeathListener;

import com.arkflame.modernlib.config.ConfigWrapper;

/*  TODO
*
*
 */
public class BountyAgency extends JavaPlugin {

    private ConfigWrapper config;
    private ConfigWrapper messages;
    private static BountyAgency main;
    private static Database database;
    private Logger log;

    private HashMap<Bounty, Player> bounties = new HashMap<Bounty, Player>();
    private HashMap<Block, BountySign> signs = new HashMap<Block, BountySign>();

    public ConfigWrapper getCfg() {
        return config;
    }

    public ConfigWrapper getMsg() {
        return messages;
    }

    @Override
    public void onEnable() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Set static instance
        setInstance(this);

        // Save default config
        config = new ConfigWrapper(this, "config.yml").saveDefault().load();
        messages = new ConfigWrapper(this, "messages.yml").saveDefault().load();
        //Register Listeners
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);
        //Reigster commands
        this.getCommand("bountyagency").setExecutor(new BountyAgencyCommand(this));
        //Register database
        database = new Database(this.getDataFolder().getAbsolutePath());

        //Setup logger
        log = new PluginLogger(this);

        List<Bounty> check = database.fetchBounties();
        if (!check.isEmpty()) {
            List<Bounty> tempBounties = new ArrayList<Bounty>();
            tempBounties = database.fetchBounties();
            for (Bounty b : tempBounties) {
                Player target = Bukkit.getPlayer(b.getTarget());
                bounties.put(b, target);
            }
        }
        if (!database.fetchSigns().isEmpty()) {
            signs = database.fetchSigns();
        }
    }

    public static void main(BountyAgency main) {
        BountyAgency.main = main;
    }

    public static BountyAgency getInstance() {
        return BountyAgency.main;
    }

    private void setInstance(BountyAgency main) {
        BountyAgency.main = main;
    }

    public List<Bounty> getAllIncompleteBounties(Player victim) {
        List<Bounty> incompleteBounties = new ArrayList<>();
        for (Bounty b : bounties.keySet()) {
            if (bounties.get(b) == victim) {
                if((b.getMode() & Bounty.IS_COMPLETE) == 0){
                    incompleteBounties.add(b);
                }
            }
        }
        return incompleteBounties;
    }

    public List<Bounty> getIdleBounties() {
        List<Bounty> idleBounties = new ArrayList<>();
        int flags = Bounty.IS_ACTIVE | Bounty.IS_COMPLETE;
        for (Bounty b : bounties.keySet()) {
            if (b.missingFlags(flags)) {
                idleBounties.add(b);
            }
        }
        return idleBounties;
    }

    public List<Bounty> getActiveOpenBounties() {
        List<Bounty> activeOpenBounties = new ArrayList<>();
        int flags = Bounty.IS_ACTIVE | Bounty.IS_OPEN;
        for (Bounty b : bounties.keySet()) {
            if ((b.hasFlags(flags))) {
                activeOpenBounties.add(b);
            }
        }
        return activeOpenBounties;
    }

    public List<Bounty> getActiveClosedBounties() {
        List<Bounty> activeClosedBounties = new ArrayList<>();
        int hasFlags = Bounty.IS_ACTIVE;
        int missingFlags = Bounty.IS_OPEN;
        for (Bounty b : bounties.keySet()) {
            if ((b.hasFlags(hasFlags)) && (b.missingFlags(missingFlags))) {
                activeClosedBounties.add(b);
            }
        }

        return activeClosedBounties;
    }

    public List<Bounty> getCompletedOpenBounties() {
        List<Bounty> completeOpenBounties = new ArrayList<>();
        int flags = Bounty.IS_COMPLETE & Bounty.IS_OPEN;
        for (Bounty b : bounties.keySet()) {
            if (b.hasFlags(flags)) {
                completeOpenBounties.add(b);
            }
        }
        return completeOpenBounties;
    }

    public List<Bounty> getCompletedClosedBounties(){
        List<Bounty> completedClosedBounties = new ArrayList<>();
        int hasFlags = Bounty.IS_COMPLETE;
        int missingFlags = Bounty.IS_OPEN;
        for(Bounty b : bounties.keySet()){
            if((b.hasFlags(hasFlags)) && (b.missingFlags(missingFlags))){
                completedClosedBounties.add(b);
            }
        }
        return completedClosedBounties;
    }

    public void addBountySign(Block block, BountySign sign) {
        signs.put(block, sign);
    }

    public void removeBountySign(Block block) {
        signs.remove(block);
    }

    public BountySign getBountySign(Block block) {
        return signs.get(block);
    }
}
