package io.github.andrewward2001.sqlecon;

import java.sql.SQLException;

import io.github.andrewward2001.sqlecon.util.Cache;
import io.github.andrewward2001.sqlecon.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.andrewward2001.sqlecon.hooks.VaultConnector;
import io.github.andrewward2001.sqlecon.cmd.Config;
import io.github.andrewward2001.sqlecon.cmd.Money;
import io.github.andrewward2001.sqlecon.hooks.Dependency;
import io.github.andrewward2001.sqlecon.mysql.*;
import net.milkbowl.vault.economy.Economy;

import java.util.Timer;
import java.sql.Connection;
import java.util.TimerTask;

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
	private static String defMoney;
    public static boolean caching;
    private int cacheRate;
    private static Cache cache;
    private static Timer updateCache;

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
        caching = dbConf.getBoolean("Caching");
        cacheRate = dbConf.getInt("CacheRate");

		defMoney = conf.getString("DefaultMoney");
		moneyUnit = conf.getString("MoneyUnit");
		taxRate = conf.getInt("TaxRate")/100.0;

		MySQL = new MySQL(host, port, database, user, pass);
		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SQLEconomyActions.createTable();

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

		this.getCommand("money").setExecutor(new Money(this));
		this.getCommand("m").setExecutor(new Money(this));
		this.getCommand("sqle-config").setExecutor(new Config(this, getConfig()));
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
