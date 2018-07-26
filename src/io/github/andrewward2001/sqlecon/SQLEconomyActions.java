package io.github.andrewward2001.sqlecon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.bukkit.command.CommandException;

import static io.github.andrewward2001.sqlecon.SQLEconomy.S;

public class SQLEconomyActions {

	private static Connection c = SQLEconomy.c;
	private static String table = SQLEconomy.getTable();
	private static String servername = SQLEconomy.getServername();

	private static double taxRate = S.taxRate;
	private static String logtable = SQLEconomy.getLogtable();

	private static boolean caching = SQLEconomy.caching;

	public synchronized static boolean playerDataContainsPlayer(UUID uid) {
		try {
			Statement sql = c.createStatement();
			ResultSet resultSet = sql.executeQuery("SELECT * FROM `" + table + "` WHERE `player_uuid` = '" + uid + "';");
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
			ResultSet resultSet = sql.executeQuery("SELECT * FROM `" + table + "` WHERE `player` = '" + player + "';");
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
			
//			tableCreate
//					.execute("CREATE TABLE IF NOT EXISTS `"
//							+ table
//							+ "` (`player_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, `player` varchar(255) NOT NULL, `player_uuid` varchar(255) NOT NULL, `money` int(20) NOT NULL, `active` int(1) NOT NULL DEFAULT '1') ENGINE=InnoDB DEFAULT CHARSET=latin1;");
			/**
			 * @author mononokehimi -> copy for chi3llini
			 */
			tableCreate
			.execute("CREATE TABLE IF NOT EXISTS `"
					+ table
					+ "` (`player_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, `player` varchar(255) NOT NULL," +
					" `player_uuid` varchar(255) NOT NULL, `money` double(20, 2) NOT NULL," +
					" `active` int(1) NOT NULL DEFAULT '1') ENGINE=InnoDB DEFAULT CHARSET=latin1;");

//			System.out.println("[SQLEconomy] Created/checked the database table");
			tableCreate.close();
		} catch (MySQLSyntaxErrorException e) {
			e.printStackTrace();
			System.out.println("[SQLEconomy] There was a snag initializing the database. Please send the ENTIRE stack trace above.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[SQLEconomy] There was a snag initializing the database. Make sure you set up the config!");
		}
	}

	public static void createlogTable() {
		try {
			Statement tableCreate = c.createStatement();

//			tableCreate
//					.execute("CREATE TABLE IF NOT EXISTS `"
//							+ table
//							+ "` (`player_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, `player` varchar(255) NOT NULL, `player_uuid` varchar(255) NOT NULL, `money` int(20) NOT NULL, `active` int(1) NOT NULL DEFAULT '1') ENGINE=InnoDB DEFAULT CHARSET=latin1;");
			/**
			 * @author mononokehimi -> copy for chi3llini
			 */
			tableCreate
					.execute("CREATE TABLE IF NOT EXISTS `"
							+ logtable
							+ "` (`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, `uuid` varchar(255) DEFAULT NULL,\n" +
							"  `oldevent` text COMMENT '操作前数据',\n" +
							"  `newevent` text COMMENT '更改数据',\n" +
							"  `eventtime` datetime DEFAULT NULL COMMENT '操作时间',\n" +
							"  `playername` varchar(255) DEFAULT NULL COMMENT '玩家名称',\n" +
							"  `servercode` varchar(255) DEFAULT NULL COMMENT '服务器编号') ENGINE=InnoDB DEFAULT CHARSET=latin1;");

//			System.out.println("[SQLEconomy] Created/checked the database table");
			tableCreate.close();
		} catch (MySQLSyntaxErrorException e) {
			e.printStackTrace();
			System.out.println("[SQLEconomy] There was a snag initializing the database. Please send the ENTIRE stack trace above.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[SQLEconomy] There was a snag initializing the database. Make sure you set up the config!");
		}
	}

	public static boolean giveMoney(UUID uid, double amount, boolean tax) {
		if (amount > 0) {
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
			} else {
				try {

					String oldmoney = String.valueOf(getMoney(uid));
					String name = Bukkit.getOfflinePlayer(uid).getName();

					PreparedStatement giveMoney = c.prepareStatement("UPDATE `" + table + "` SET money = money + ? WHERE player_uuid=?;");
					giveMoney.setDouble(1, amount);
					giveMoney.setString(2, uid.toString());
					giveMoney.executeUpdate();
					giveMoney.close();

					String newmoney = String.valueOf(getMoney(uid));
					if (moneyLog(oldmoney, newmoney, uid.toString(), name, "give")) {
						return true;
					} else {
						System.out.println("[SQLEconomy] Error: write log false, player:" + name +
								"om:" + oldmoney + "nm:" + newmoney);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public static boolean giveMoney(String name, double amount, boolean tax) {
		if (amount > 0) {
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

					String oldmoney = String.valueOf(getMoney(name));

					PreparedStatement giveMoney = c.prepareStatement("UPDATE `" + table + "` SET money = money + ? WHERE player = ?;");
					giveMoney.setDouble(1, amount);
					giveMoney.setString(2, name);
					giveMoney.executeUpdate();
					giveMoney.close();

					String newmoney = String.valueOf(getMoney(name));
					if (moneyLog(oldmoney, newmoney, getUUID(name), name, "give")) {
						return true;
					} else {
						System.out.println("[SQLEconomy] Error: write log false, player:" + name +
								"om:" + oldmoney + "nm:" + newmoney);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

		}

		return false;
	}

	public static boolean removeMoney(UUID uid, double amount, boolean tax) {
		/**
		 * @author mononokehimi >改为>= bug原因是当前身上余额为0时，return flase，不能执行转入操作
		 */
		if (amount >= 0) {
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

					String oldmoney = String.valueOf(getMoney(uid));
					String name = Bukkit.getOfflinePlayer(uid).getName();

					PreparedStatement removeMoney = c.prepareStatement("UPDATE `" + table + "` SET money = money - ? WHERE player_uuid=?;");
					removeMoney.setDouble(1, amount);
					removeMoney.setString(2, uid.toString());
					removeMoney.executeUpdate();

					removeMoney.close();

					String newmoney = String.valueOf(getMoney(uid));
					if (moneyLog(oldmoney, newmoney, uid.toString(), name, "remove")) {
						return true;
					} else {
						System.out.println("[SQLEconomy] Error: write log false, player:" + name +
								"om:" + oldmoney + "nm:" + newmoney);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

		}

		return false;
	}

	public static boolean removeMoney(String name, double amount, boolean tax) {
		/**
		 * @author mononokehimi >改为>= bug原因是当前身上余额为0时，return flase，不能执行转入操作
		 */
		if (amount >= 0) {
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

					String oldmoney = String.valueOf(getMoney(name));

					PreparedStatement getBal = c.prepareStatement("UPDATE `" + table + "` SET money = money - ? WHERE player=?;");
					getBal.setDouble(1, amount);
					getBal.setString(2, name);
					getBal.executeUpdate();
					getBal.close();

					String newmoney = String.valueOf(getMoney(name));
					if (moneyLog(oldmoney, newmoney, getUUID(name), name, "remove")) {
						return true;
					} else {
						System.out.println("[SQLEconomy] Error: write log false, player:" + name +
								"om:" + oldmoney + "nm:" + newmoney);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		return false;
	}

	private static boolean moneyLog(String oldinfo, String newinfo, String uid, String name, String type) {
		try {
			Date dt = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			if(Objects.equals(type, "remove")) {
				PreparedStatement logInfo = c.prepareStatement("INSERT INTO `eventlog` (uuid, newevent,oldevent,eventtime,playername,servercode) VALUES (?, ?, ?, ?, ?, ?);");
				logInfo.setString(1, uid);
				logInfo.setString(2, newinfo);
				logInfo.setString(3, oldinfo);
				logInfo.setString(4, df.format(dt));
				logInfo.setString(5, name);
				logInfo.setString(6, servername);
				logInfo.executeUpdate();
				logInfo.close();
			}else {
				PreparedStatement logInfo = c.prepareStatement("UPDATE `eventlog` SET newevent=? WHERE uuid=? and eventtime=? and servercode=? and playername=?;");
				logInfo.setString(1, newinfo);
				logInfo.setString(2, uid);
				logInfo.setString(3, df.format(dt));
				logInfo.setString(4, servername);
				logInfo.setString(5, name);
				logInfo.executeUpdate();
				logInfo.close();
			}
			return true;
		} catch (SQLException e) {
			System.out.println("[ItemMail] Error creating user!");
		}

		return false;
	}

	public static double getMoney(UUID uid) {
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
			ResultSet res = getMoney.executeQuery("SELECT money FROM `" + table + "` WHERE player_uuid = '" + uid.toString() + "';");
			while (res.next()) {
				double money = 0;
				if (res.getString("money") != null) {
					money = res.getDouble("money");
				}
				getMoney.close();
				res.close();

				return money;
			}

			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0.0;
	}

	/**
	 * 查询余额接口
	 * 
	 * @param name 玩家名
	 * @return
	 */
	public static double getMoney(String name) {
		if (caching) {
			try {
				int accountPos = SQLEconomy.S.getCache().getAccountIndex(name);
				return SQLEconomy.S.getCache().stored.get(accountPos).bal;
			} catch (CommandException e) {
				return -1;
			}
		}

		try {
			PreparedStatement getMoney = c.prepareStatement("SELECT money FROM `" + table + "` WHERE player = ?;");
			getMoney.setString(1, name);
			ResultSet res = getMoney.executeQuery();
			while (res.next()) {
				double money = 0;
				if (res.getString("money") != null) {
					money = res.getDouble("money");
				}

				getMoney.close();
				res.close();

				return money;
			}

			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0.0;
	}

	/**
	 * 查询UUID接口
	 *
	 * @param name 玩家名
	 * @return
	 */
	private static String getUUID(String name) {
		try {
			PreparedStatement getuid = c.prepareStatement("SELECT player_uuid FROM `" + table + "` WHERE player = ?;");
			getuid.setString(1, name);
			ResultSet res = getuid.executeQuery();
			while (res.next()) {
				String uid = res.getString("player_uuid");
				getuid.close();
				res.close();

				return uid;
			}

			return "";
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static boolean createAccount(OfflinePlayer player) {
		try {
			PreparedStatement econRegister = c.prepareStatement("INSERT INTO `" + table + "` (player, player_uuid, money, active) VALUES (?, ?, ?, ?);");
			econRegister.setString(1, player.getName());
			econRegister.setString(2, player.getUniqueId().toString());
			econRegister.setString(3, SQLEconomy.getDefaultMoney());
			econRegister.setLong(4, 1);
			econRegister.executeUpdate();
			econRegister.close();

//			System.out.println("[SQLEconomy] Added user " + player.getName() + " to the economy database.");
			if (caching)
				S.getCache().updateCache();

			return true;
		} catch (SQLException e) {
			System.out.println("[SQLEconomy] Error creating user!");
		}

		return false;
	}

	public static boolean createAccount(String name) {
		try {
			PreparedStatement econRegister = c.prepareStatement("INSERT INTO `" + table + "` (player, player_uuid, money, active) VALUES (?, ?, ?, ?);");
			econRegister.setString(1, name);
			econRegister.setString(2, UUID.randomUUID().toString());
			econRegister.setString(3, SQLEconomy.getDefaultMoney());
			econRegister.setLong(4, 1);
			econRegister.executeUpdate();
			econRegister.close();

//			System.out.println("[SQLEconomy] Added user " + name + " to the economy database.");
			if (caching)
				S.getCache().updateCache();

			return true;
		} catch (SQLException e) {
			System.out.println("[SQLEconomy] Error creating user!");
		}

		return false;
	}

	public static boolean hasEnough(UUID uid, double amount) {
		double bal = getMoney(uid);
		return bal >= amount;
	}

	public static boolean hasEnough(String name, double amount) {
		double bal = getMoney(name);
		return bal >= amount;
	}

	public static boolean transferMoney(String name, UUID uid, double amount) {
		return hasEnough(uid, amount) && giveMoney(name, amount, false) && removeMoney(uid, amount, false);
	}

}
