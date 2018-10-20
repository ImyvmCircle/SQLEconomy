package io.github.andrewward2001.sqlecon.hooks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

import io.github.andrewward2001.sqlecon.SQLEconomy;
import io.github.andrewward2001.sqlecon.SQLEconomyAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultConnector implements Economy {

	private final String name = "SQLEconomy";
	private SQLEconomyAPI api = SQLEconomy.S.getAPI();

	@Override
	public EconomyResponse bankBalance(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Deprecated
	public EconomyResponse createBank(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Deprecated
	public boolean createPlayerAccount(String arg0) {
		return api.createAccount(arg0);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		return api.createAccount(arg0);
	}

	@Deprecated
	public boolean createPlayerAccount(String arg0, String arg1) {
		return api.createAccount(arg0);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		return api.createAccount(arg0);
	}

	@Override
	public String currencyNamePlural() {
		return api.currencyName();
	}

	@Override
	public String currencyNameSingular() {
		return api.currencyName();
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	/**
	 * @param arg0
	 *            玩家名
	 * @param arg1
	 *            执行完转账后的余额
	 */
	@Deprecated
	public EconomyResponse depositPlayer(String arg0, double arg1) {
		boolean deposit = api.give(arg0, arg1);

		if (deposit) {
			return new EconomyResponse(arg1, getBalance(arg0) + arg1, ResponseType.SUCCESS, null);
		}

		return new EconomyResponse(0, getBalance(arg0), ResponseType.FAILURE, "Error with deposit (is it a positive number?)");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {
		boolean deposit = api.give(arg0.getUniqueId(), arg1);

		if (deposit) {
			return new EconomyResponse(arg1, getBalance(arg0) + arg1, ResponseType.SUCCESS, null);
		}

		return new EconomyResponse(0, getBalance(arg0), ResponseType.FAILURE, "Error with deposit (is it a positive number?)");
	}

	@Deprecated
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		return depositPlayer(arg0, arg2);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		return depositPlayer(arg0, arg2);
	}

	@Override
	public String format(double arg0) {
		return String.format("%d %s", (int) arg0, api.currencyName());
	}

	@Override
	public int fractionalDigits() {
		return 0;
	}

	@Deprecated
	public double getBalance(String arg0) {
		return api.getBalance(arg0);
	}

	@Override
	public double getBalance(OfflinePlayer arg0) {
		return api.getBalance(arg0.getUniqueId());
	}

	@Deprecated
	public double getBalance(String arg0, String arg1) {
		return api.getBalance(arg0);
	}

	@Override
	public double getBalance(OfflinePlayer arg0, String arg1) {
		return api.getBalance(arg0.getUniqueId());
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<String>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Deprecated
	public boolean has(String arg0, double arg1) {
		return api.hasEnough(arg0, arg1);
	}

	@Override
	public boolean has(OfflinePlayer arg0, double arg1) {
		return api.hasEnough(arg0.getUniqueId(), arg1);
	}

	@Deprecated
	public boolean has(String arg0, String arg1, double arg2) {
		return api.hasEnough(arg0, arg2);
	}

	@Override
	public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
		return api.hasEnough(arg0.getUniqueId(), arg2);
	}

	@Deprecated
	public boolean hasAccount(String arg0) {
		return api.accountExists(arg0);
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		return api.accountExists(arg0.getUniqueId());
	}

	@Deprecated
	public boolean hasAccount(String arg0, String arg1) {
		return api.accountExists(arg0);
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		return api.accountExists(arg0.getUniqueId());
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Deprecated
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Deprecated
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts!");
	}

	@Override
	public boolean isEnabled() {
		return SQLEconomy.S != null && SQLEconomy.S.isEnabled();
	}

	@Deprecated
	public EconomyResponse withdrawPlayer(String arg0, double arg1) {
		boolean withdraw = api.withdraw(arg0, arg1);

		if (withdraw) {
			return new EconomyResponse(arg1, getBalance(arg0) - arg1, ResponseType.SUCCESS, null);
		}

		return new EconomyResponse(0, getBalance(arg0), ResponseType.FAILURE, "Not enough money.");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
		boolean withdraw = api.withdraw(arg0.getUniqueId(), arg1);

		if (withdraw) {
			return new EconomyResponse(arg1, getBalance(arg0) - arg1, ResponseType.SUCCESS, null);
		}

		return new EconomyResponse(0, getBalance(arg0), ResponseType.FAILURE, "Not enough money.");
	}

	@Deprecated
	public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
		return withdrawPlayer(arg0, arg2);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		return withdrawPlayer(arg0, arg2);
	}

}
