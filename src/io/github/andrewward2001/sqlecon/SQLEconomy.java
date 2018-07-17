package io.github.andrewward2001.sqlecon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.andrewward2001.sqlecon.hooks.Dependency;
import io.github.andrewward2001.sqlecon.hooks.VaultConnector;
import io.github.andrewward2001.sqlecon.mysql.MySQL;
import io.github.andrewward2001.sqlecon.util.Cache;
import io.github.andrewward2001.sqlecon.util.Configuration;
import net.milkbowl.vault.economy.Economy;

public class SQLEconomy extends JavaPlugin implements Listener {

    private Plugin plugin;

    public static SQLEconomy S;

    // Making variables for SQL connection static, escaping errors
    private static String host;
    private static String port;
    private static String database;
    private static String table;
    private static String user;
    private static String pass;
    private static boolean useSSL;
    private static boolean trustSSL;
    private static String defMoney;
    public static boolean caching;
    private int cacheRate;
    private static Cache cache;
    private static Timer updateCache;
    private static String servername;
    private static String logtable;

    public static String moneyUnit;

    public static double taxRate;

    private static MySQL MySQL;
    static Connection c;

    public void onDisable() {
    }

    public void onEnable() {
        FileConfiguration conf = getConfig("config.yml");
        FileConfiguration dbConf = getConfig("db.yml");

        host = dbConf.getString("HostIP");
        port = dbConf.getString("Port");
        database = dbConf.getString("Name");
        table = dbConf.getString("Table");
        user = dbConf.getString("Username");
        pass = dbConf.getString("Password");
        useSSL = dbConf.getBoolean("UseSSL");
        trustSSL = dbConf.getBoolean("TrustSSL");
        caching = dbConf.getBoolean("Caching");
        cacheRate = dbConf.getInt("CacheRate");

        defMoney = conf.getString("DefaultMoney");
        moneyUnit = conf.getString("MoneyUnit");
        taxRate = conf.getInt("TaxRate")/100.0;
        servername = conf.getString("servername");
        logtable = conf.getString("table");

        MySQL = new MySQL(host, port, database, user, pass, useSSL, trustSSL);
        try {
            c = MySQL.openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            getLogger().info("Found an exception connecting to the database. Make sure you've set your db.yml file up.");
        }

        SQLEconomyActions.createTable();
        SQLEconomyActions.createlogTable();
        /**
         * @author Holeyness
         * @description 防数据库连接丢失，每天查询一次数据库
         */
        Timer timer = new Timer();
        //每6小时查询一次数据库时间
        timer.schedule(new TimerTask() {
                public void run() {
                    if(c != null){
                    	String sql = "select sysDate();";
                    	try {
                    		Statement statement = c.createStatement();
							ResultSet resultSet = statement.executeQuery(sql);
							if(resultSet.next()){
								System.out.println(resultSet.getDate("sysDate()"));
							}
							
							statement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
                    }
                }
        }, 2000, 1000*3600*6);

        if(caching) {
            cache = new Cache(c, table);
            cache.createCache();
            updateCache = new Timer();
            updateCache.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    cache.updateCache();
                }
            }, cacheRate, cacheRate);
        }

        /**
         * @date 2017年6月18日 23点02分
         * @author Holeyness
         * @description 删除指令响应
         */
        /*this.getCommand("money").setExecutor(new Money(this));
        this.getCommand("m").setExecutor(new Money(this));
        this.getCommand("sqle-config").setExecutor(new Config(this, getConfig()));*/
        Bukkit.getServer().getPluginManager().registerEvents(new SQLEconomyListener(table, defMoney, c), this);

        registerEconomy();
    }

    public static String getTable() {
        return table;
    }

    public static String getDefaultMoney() {
        return defMoney;
    }

    public static SQLEconomyAPI getAPI() {
        return new SQLEconomyAPI();
    }

    public static Cache getCache() {
        return cache;
    }

    public static String getServername(){
        return servername;
    }

    public static String getLogtable(){
        return logtable;
    }

    // Vault hook based on implementation found at https://github.com/MinecraftWars/Gringotts
    private void registerEconomy() {
        if (Dependency.DEP.vault.exists()) {
            final ServicesManager sm = getServer().getServicesManager();
            sm.register(Economy.class, new VaultConnector(), this, ServicePriority.Highest);
            getLogger().info("Registered Vault interface.");
        } else {
            getLogger().info("[SQLEconomy] Vault not found. Other plugins may not be able to access SQLEconomy accounts.");
        }
    }

    public FileConfiguration getConfig(String name) {
        return YamlConfiguration.loadConfiguration(Configuration.loadResource(this, name));
    }

    public boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

}
