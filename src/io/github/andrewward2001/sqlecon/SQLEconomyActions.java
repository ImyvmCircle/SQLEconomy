package io.github.andrewward2001.sqlecon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import io.github.andrewward2001.sqlecon.util.Account;
import org.bukkit.OfflinePlayer;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.bukkit.command.CommandException;

import static io.github.andrewward2001.sqlecon.SQLEconomy.S;

public class SQLEconomyActions {

	private static Connection c = SQLEconomy.c;
	private static String table = SQLEconomy.getTable();
	
	private static double taxRate = S.taxRate;

	private static boolean caching = SQLEconomy.caching;

	public synchronized static boolean playerDataContainsPlayer(UUID uid) {
		try {
			Statement sql = c.createStatement();
			ResultSet resultSet = sql.executeQuery(
					"SELECT * FROM `" + table + "` WHERE `player_uuid` = '" + uid + "';");
			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized static boolean playerDataContainsPlayer(String player) {
		try {
			Statement sql = c.createStatement();
			ResultSet resultSet = sql.executeQuery(
					"SELECT * FROM `" + table + "` WHERE `player` = '" + player + "';");
			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void createTable() {
		try {
			Statement tableCreate = c.createStatement();
			tableCreate.execute("CREATE TABLE IF NOT EXISTS `" + table
					+ "` (`player_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, `player` varchar(255) NOT NULL, `player_uuid` varchar(255) NOT NULL, `money` int(20) NOT NULL, `active` int(1) NOT NULL DEFAULT '1') ENGINE=InnoDB DEFAULT CHARSET=latin1;");

			System.out.println("[SQLEconomy] Created/checked the database table");
			tableCreate.close();
		} catch (MySQLSyntaxErrorException e) {
			e.printStackTrace();
			System.out.println(
					"[SQLEconomy] There was a snag initializing the database. Please send the ENTIRE stack trace above.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(
					"[SQLEconomy] There was a snag initializing the database. Make sure you set up the config!");
		}
	}

	public static boolean giveMoney(UUID uid, int amount, boolean tax) {
		if(amount > 0) {
            if (tax)
                amount -= amount * taxRate;
            if (caching) {
                try {
                    int accountPos = S.getCache().getAccountIndex(uid);
                    S.getCache().stored.get(accountPos).bal += amount;
                } catch (CommandException e) {
                    System.out.println("[SQLEconomy] Error: Couldn't find user");
                    return false;
                }
            } else
                try {
                    PreparedStatement giveMoney = c
                            .prepareStatement("UPDATE `" + table + "` SET money = money + ? WHERE player_uuid=?;");
                    giveMoney.setInt(1, amount);
                    giveMoney.setString(2, uid.toString());
                    giveMoney.executeUpdate();

                    giveMoney.close();

                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

		return false;
	}

	public static boolean giveMoney(String name, int amount, boolean tax) {
		if(amount > 0) {
            if (tax)
                amount -= amount * taxRate;
            if (caching) {
                try {
                    int accountPos = S.getCache().getAccountIndex(name);
                    S.getCache().stored.get(accountPos).bal += amount;
                } catch (CommandException e) {
                    System.out.println("[SQLEconomy] Error: Couldn't find user");
                    return false;
                }
            } else
                try {
                    PreparedStatement giveMoney = c
                            .prepareStatement("UPDATE `" + table + "` SET money = money + ? WHERE player=?;");
                    giveMoney.setInt(1, amount);
                    giveMoney.setString(2, name);
                    giveMoney.executeUpdate();

                    giveMoney.close();

                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }

		return false;
	}

	public static boolean removeMoney(UUID uid, int amount, boolean tax) {
		if(amount > 0) {
            if (tax)
                amount += amount * taxRate;
            if (caching) {
                try {
                    int accountPos = S.getCache().getAccountIndex(uid);
                    S.getCache().stored.get(accountPos).bal -= amount;
                } catch (CommandException e) {
                    System.out.println("[SQLEconomy] Error: Couldn't find user");
                    return false;
                }
            } else
                try {
                    PreparedStatement removeMoney = c
                            .prepareStatement("UPDATE `" + table + "` SET money = money - ? WHERE player_uuid=?;");
                    removeMoney.setInt(1, amount);
                    removeMoney.setString(2, uid.toString());
                    removeMoney.executeUpdate();

                    removeMoney.close();

                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }

		return false;
	}

	public static boolean removeMoney(String name, int amount, boolean tax) {
		if(amount > 0) {
            if (tax)
                amount += amount * taxRate;
            if (caching) {
                try {
                    int accountPos = S.getCache().getAccountIndex(name);
                    S.getCache().stored.get(accountPos).bal -= amount;
                } catch (CommandException e) {
                    System.out.println("[SQLEconomy] Error: Couldn't find user");
                    return false;
                }
            } else
                try {
                    PreparedStatement getBal = c
                            .prepareStatement("UPDATE `" + table + "` SET money = money - ? WHERE player=?;");
                    getBal.setInt(1, amount);
                    getBal.setString(2, name);
                    getBal.executeUpdate();

                    getBal.close();

                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

		return false;
	}

	public static int getMoney(UUID uid) {

        if (caching) {
            try {
                int accountPos = S.getCache().getAccountIndex(uid);
                return S.getCache().stored.get(accountPos).bal;
            } catch (CommandException e) {
                System.out.println("[SQLEconomy] Error: Couldn't find user");
                return -1;
            }
        }

		try {
			Statement getMoney = c.createStatement();
			ResultSet res = getMoney
					.executeQuery("SELECT money FROM `" + table + "` WHERE player_uuid = '" + uid.toString() + "';");
			while(res.next()) {
				int money = 0;
				if (res.getString("money") != null)
					money = res.getInt("money");
	
				getMoney.close();
				res.close();
	
				return money;
			}
			
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;

	}

	public static int getMoney(String name) {

        if (caching) {
            try {
                int accountPos = SQLEconomy.S.getCache().getAccountIndex(name);
                return SQLEconomy.S.getCache().stored.get(accountPos).bal;
            } catch (CommandException e) {
                System.out.println("[SQLEconomy] Error: Couldn't find user");
                return -1;
            }
        }

		try {
			PreparedStatement getMoney = c.prepareStatement("SELECT money FROM `" + table + "` WHERE player = ?;");
			getMoney.setString(1, name);
			ResultSet res = getMoney.executeQuery();
			while(res.next()) {
				int money = 0;
				if (res.getString("money") != null)
					money = res.getInt("money");
	
				getMoney.close();
				res.close();
	
				return money;
			}
			
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;

	}
	
	public static boolean createAccount(OfflinePlayer player) {
		try {
			PreparedStatement econRegister = c.prepareStatement(
					"INSERT INTO `" + table + "` (player, player_uuid, money, active) VALUES (?, ?, ?, ?);");
			econRegister.setString(1, player.getName());
			econRegister.setString(2, player.getUniqueId().toString());
			econRegister.setString(3, SQLEconomy.getDefaultMoney());
			econRegister.setLong(4, 1);
			econRegister.executeUpdate();
			econRegister.close();
			
			System.out.println("[SQLEconomy] Added user " + player.getName() + " to the economy database.");
			if(caching)
                S.getCache().createCache();
		} catch (SQLException e) {
			System.out.println("[SQLEconomy] Error creating user!");
		}
		
		return false;
	}
	
	public static boolean createAccount(String name) {
		try {
			PreparedStatement econRegister = c.prepareStatement(
					"INSERT INTO `" + table + "` (player, player_uuid, money, active) VALUES (?, ?, ?, ?);");
			econRegister.setString(1, name);
			econRegister.setString(2, UUID.randomUUID().toString());
			econRegister.setString(3, SQLEconomy.getDefaultMoney());
			econRegister.setLong(4, 1);
			econRegister.executeUpdate();
			econRegister.close();
			
			System.out.println("[SQLEconomy] Added user " + name + " to the economy database.");
			if(caching)
                S.getCache().createCache();
		} catch (SQLException e) {
			System.out.println("[SQLEconomy] Error creating user!");
		}
		
		return false;
	}
	
	public static boolean hasEnough(UUID uid, int amount) {
		int bal = getMoney(uid);

		return bal >= (int) amount;
	}
	
	public static boolean hasEnough(String name, int amount) {
		int bal = getMoney(name);

        return bal >= (int) amount;
	}
	
	public static boolean transferMoney(String name, UUID uid, int amount) {
		return hasEnough(uid, amount) && giveMoney(name, amount, false) && removeMoney(uid, amount, false);
	}

}
