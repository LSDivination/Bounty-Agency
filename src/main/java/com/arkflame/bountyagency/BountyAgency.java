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
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

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
    private List<BountySign> signs = new ArrayList<BountySign>();

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
        //Register database
        database = new Database(this.getDataFolder().getAbsolutePath());

        //Setup logger
        log = new PluginLogger(this);

        //Setup H2 Database
        ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class);
        for (Driver driver : drivers) {
            try {
                DriverManager.registerDriver(driver);
            } catch (SQLException ex) {
                log.log(new LogRecord(Level.SEVERE, ex.getMessage()));
            }
        }
        List<Bounty> check = database.fetchBounties();
        if (check != null || !check.isEmpty()) {
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

    public List<Bounty> getIncompleteBounties(Player victim) {
        if (bounties.containsKey(victim)) {
            List<Bounty> incompBounties = new ArrayList<Bounty>();
            //TODO: shit
        }
        return null;
    }

}
