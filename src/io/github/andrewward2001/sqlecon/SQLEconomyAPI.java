package io.github.andrewward2001.sqlecon;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

public class SQLEconomyAPI {

	public SQLEconomyAPI() {
		System.out.println("[SQLEconomy] API is in use.");
	}

	public String currencyName() {
		return SQLEconomy.moneyUnit;
	}

	public boolean accountExists(String name) {
		// System.out.println("Checked for account " + name);
		return SQLEconomyActions.playerDataContainsPlayer(name);
	}

	public boolean accountExists(UUID uid) {
		// System.out.println("Checked for account " + uid.toString());
		return SQLEconomyActions.playerDataContainsPlayer(uid);
	}

	public double getBalance(String name) {
		// System.out.println("Checked bal " + name + ", got " +
		// SQLEconomyActions.getMoney(name));
		return SQLEconomyActions.getMoney(name);
	}

	public double getBalance(UUID uid) {
		// System.out.println("Checked bal " + uid.toString() + ", got " +
		// SQLEconomyActions.getMoney(uid));
		return SQLEconomyActions.getMoney(uid);
	}

	public boolean hasEnough(String name, double amount) {
		// System.out.println("Checked enough");
		return SQLEconomyActions.hasEnough(name, amount);
	}

	public boolean hasEnough(UUID uid, double amount) {
		// System.out.println("Checked enough");
		return SQLEconomyActions.hasEnough(uid, amount);
	}

	public boolean withdraw(String name, double amount) {
		// System.out.println("Withdrew " + name + " " + amount);
		return SQLEconomyActions.removeMoney(name, amount, true);
	}

	public boolean withdraw(UUID uid, double amount) {
		// System.out.println("Withdrew " + uid.toString() + " " + amount);
		return SQLEconomyActions.removeMoney(uid, amount, true);
	}

	public boolean give(String name, double amount) {
		return SQLEconomyActions.giveMoney(name, amount, true);
	}

	public boolean give(UUID uid, double amount) {
		return SQLEconomyActions.giveMoney(uid, amount, true);
	}

	public boolean createAccount(OfflinePlayer player) {
		// System.out.println("Created account");
		return SQLEconomyActions.createAccount(player);
	}

	public boolean createAccount(String player) {
		// System.out.println("Created account");
		return SQLEconomyActions.createAccount(player);
	}

}
