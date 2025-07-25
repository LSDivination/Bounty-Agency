package com.arkflame.bountyagency;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//import net.md_5.bungee.api.chat.hover.content.Item;
public class Database {

    private final String url;
    private final ExecutorService databaseExecutor;
    private static final String DB_USERNAME = "sa"; // Default H2 username
    private static final String DB_PASSWORD = ""; // Default H2 password
    private static final String DELIMITER = ";;;"; // Can't appear in base64 and won't be in YAML

    public Database(String pluginPath) {
        String databasePath = pluginPath + "/BountyAgency.db";
        this.url = "jdbc:h2:file:" + databasePath;
        this.databaseExecutor = Executors.newCachedThreadPool();

        File databaseDir = new File(databasePath);
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }

        createTables();
    }

    private void createTables() {
        String createBountiesTableSQL = "CREATE TABLE IF NOT EXISTS BOUNTIES ("
                + "target VARCHAR NOT NULL, "
                + "hitman VARCHAR NOT NULL, "
                + "rewards CLOB, "
                + "itemmeta VARCHAR NOT NULL, "
                + "mode SMALLINT NOT NULL, "
                + "id SMALLINT PRIMARY KEY AUTO_INCREMENT"
                + ");";
        String createSignsTableSQL = "CREATE TABLE IF NOT EXISTS SIGNS ("
                + "x INT NOT NULL, "
                + "y INT NOT NULL, "
                + "z INT NOT NULL, "
                + "type SMALLINT NOT NULL,"
                + "PRIMARY KEY (x, z, y)"
                + ");";

        try (Connection connection = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD); Statement stmt = connection.createStatement()) {
            stmt.execute(createBountiesTableSQL);
            stmt.execute(createSignsTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBounty(Bounty bounty) {
        String target = bounty.getTarget();
        String hitman = bounty.getHitman();
        ItemStack[] rewards = bounty.getRewards();
        int mode = bounty.getMode();
        int id = bounty.getID();
        try (Connection connection = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD)) {
            ArrayList<String> baseItemStrings = new ArrayList<String>(rewards.length);
            ArrayList<String> baseMetaStrings = new ArrayList<String>(rewards.length);

            //Build base64 strings for individual ItemStack objects and ItemMeta data
            for (ItemStack reward : rewards) {
                YamlConfiguration config = new YamlConfiguration();
                config.set("items", reward);
                String yaml = config.saveToString();
                byte[] bytes = yaml.getBytes(StandardCharsets.UTF_8);
                baseItemStrings.add(Base64Coder.encodeLines(bytes));
                if (reward.hasItemMeta()) {
                    config.set("itemmeta", reward.getItemMeta());
                    yaml = config.saveToString();
                    bytes = yaml.getBytes();
                    baseMetaStrings.add(Base64Coder.encodeLines(bytes));
                } else {
                    baseMetaStrings.add(" ");
                }
            }
            // IT'S CLOBBIN' TIME!
            try (PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO BOUNTIES (target, hitman, rewards, itemmeta, mode, id) VALUES (?, ?, ?, ?, ?, ?)"
            )) {
                //Joins item strings for individual ItemStack objects and ItemMeta data with ; (semi-colon) delimiter
                String joinedItem = String.join(DELIMITER, baseItemStrings);
                String joinedMeta = String.join(DELIMITER, baseMetaStrings);

                insertStatement.setString(1, target);
                insertStatement.setString(2, hitman);
                insertStatement.setClob(3, new StringReader(joinedItem));
                insertStatement.setClob(4, new StringReader(joinedMeta));
                insertStatement.setInt(5, mode);
                insertStatement.setInt(6, id);
                insertStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSign(BountySign sign) {
        ResultSet resultSet;
        int x = sign.getX();
        int y = sign.getY();
        int z = sign.getZ();
        int type = sign.getType();
        String key = "PRIMARY KEY ";
        String insertString = "SELECT * FROM SIGNS";
        try (Connection connection = DriverManager.getConnection(url); Statement stmt = connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            resultSet = stmt.executeQuery(insertString);
            resultSet.moveToInsertRow();
            resultSet.updateInt("x", x);
            resultSet.updateInt("z", z);
            resultSet.updateInt("y", y);
            resultSet.updateInt("type", type);

            resultSet.insertRow();
            resultSet.beforeFirst();
        } catch (SQLException e) {

        }
    }

    //Fetch bounty on server start
    public List<Bounty> fetchBounties() {
        List<Bounty> bounties = new ArrayList<Bounty>();
        try (Connection connection = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD); PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BOUNTIES")) {
            ResultSet resultSet = stmt.executeQuery();

            int results = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    results++;
                }
            } else {
                return null;
            }
            while (resultSet.next()) {
                //TODO: getOfflinePlayers
                Player target = Bukkit.getPlayer(resultSet.getString("target"));
                Player hitman = Bukkit.getPlayer(resultSet.getString("hitman"));
                int mode = resultSet.getInt("mode");
                int id = resultSet.getInt("id");
                ItemStack[] items = deserialize(id);
                Rewards rewards = new Rewards(items);
                Bounty bounty = new Bounty(target, hitman, rewards, mode, id);
                bounties.add(bounty);
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return bounties;
    }

    //TODO: Fetch bounty sign on interact
    public ResultSet fetchBounty(BountySign sign) {
        return null;
    }

    //Fetch bounty signs on server start
    public HashMap<Block, BountySign> fetchSigns() {
        HashMap<Block, BountySign> signs = new HashMap<Block, BountySign>();
        int type = 0;
        int x = 0;
        int y = 0;
        int z = 0;
        try (Connection connection = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD); PreparedStatement stmt = connection.prepareStatement("SELECT * FROM SIGNS")) {
            ResultSet resultSet;
            resultSet = stmt.executeQuery();
            int results = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
                    results++;
                }
            } else {
                return null;
            }
            while (resultSet.next()) {
                type = resultSet.getInt("type");
                x = resultSet.getInt("x");
                y = resultSet.getInt("y");
                z = resultSet.getInt("z");
                BountySign sign = new BountySign(type, x, y, z);
                World world = Bukkit.getWorlds().get(0);
                Block block = world.getBlockAt(x, y, z);
                signs.put(block, sign);
            }

        } catch (SQLException e) {
        }

        return signs;
    }

    //TODO: Fetch bounty signs on interact
    public ResultSet fetchSign(BountySign sign) {
        return null;
    }

    private ItemStack[] deserialize(int id) {
        ItemStack[] item = new ItemStack[0];
        ItemMeta[] meta;
        //TODO: Remove looted rows from database
        try (Connection connection = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT rewards, itemmeta FROM BOUNTIES WHERE id = ?"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Clob itemClob;
            Clob metaClob;
            while (resultSet.next()) {
                itemClob = resultSet.getClob("rewards");
                StringBuilder sbItem = new StringBuilder();
                try (Reader reader = itemClob.getCharacterStream()) {
                    char[] buffer = new char[2048];
                    int bytesRead;
                    while ((bytesRead = reader.read(buffer)) != -1) {
                        sbItem.append(buffer, 0, bytesRead);
                    }

                    ArrayList<String> stringItemList = new ArrayList<String>(Arrays.asList(sbItem.toString().split(DELIMITER)));
                    item = new ItemStack[stringItemList.size()];
                    for (int i = 0; i < stringItemList.size(); i++) {
                        //Deserialize the ItemStack at this index
                        YamlConfiguration config = new YamlConfiguration();
                        String yaml = Base64Coder.decodeString(stringItemList.get(i));
                        config.loadFromString(yaml);
                        item[i] = config.getItemStack("items");

                    }
                } catch (IOException | InvalidConfigurationException ex) {
                    item = new ItemStack[0];
                }
                metaClob = resultSet.getClob("itemmeta");
                StringBuilder sbMeta = new StringBuilder();

                try (Reader reader = metaClob.getCharacterStream()) {
                    char[] buffer = new char[2048];
                    int bytesRead;
                    while ((bytesRead = reader.read(buffer)) != -1) {
                        sbMeta.append(buffer, 0, bytesRead);
                    }
                    ArrayList<String> stringMetaList = new ArrayList<String>(Arrays.asList(sbMeta.toString().split(DELIMITER)));
                    meta = new ItemMeta[stringMetaList.size()];
                    for (int i = 0; i < stringMetaList.size(); i++) {
                        //Get the ItemMeta at this index
                        String s = stringMetaList.get(i);
                        //Check if there is a placeholder for ItemMeta
                        if (!s.equals(" ")) {
                            //Decode ItemMeta and assign to ItemStack
                            String yaml = Base64Coder.decodeString(s);
                            YamlConfiguration config = new YamlConfiguration();
                            config.loadFromString(yaml);
                            meta[i] = (ItemMeta) config.get("itemmeta");
                            item[i].setItemMeta(meta[i]);
                        }
                    }
                } catch (IOException | InvalidConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //TODO: fix return statement possibly not being initialized
        return item;
    }

}
