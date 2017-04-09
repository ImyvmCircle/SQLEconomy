package io.github.andrewward2001.sqlecon.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cache {

    public static List<Account> stored;

    private Connection c;
    private String table;

    public Cache(Connection c, String table) {
        this.c = c;
        this.table = table;

        stored = new ArrayList<>();
    }

    public boolean createCache() {
        try {
            Statement getMoney = c.createStatement();
            ResultSet res = getMoney
                    .executeQuery("SELECT * FROM `" + table + "`;");
            while(res.next()) {
                stored.add(new Account(res.getString("player"), UUID.fromString(res.getString("player_uuid")), res.getInt("money")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean updateCache() {
        for(Account a: stored) {
            try {
                PreparedStatement updateCache = c.prepareStatement("UPDATE `" + table + "` SET `player` = ?, `player_uuid` = ?, `money` = ? WHERE `player_uuid` = ?;");
                updateCache.setString(1, a.name);
                updateCache.setString(2, a.uid.toString());
                updateCache.setInt(3, a.bal);
                updateCache.setString(4, a.uid.toString());

                updateCache.executeUpdate();
                updateCache.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        System.out.println("[SQLEconomy] Updated database from cache.");

        return true;
    }

    /*
    Mirrors ArrayList.indexOf which can be found at http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/ArrayList.java#228
     */
    public static int getAccountIndex(String s) {
        for (int i = 0; i < stored.size(); i++) {
            if(stored.get(i).name.equalsIgnoreCase(s)) {
                return i;
            }
        }
        return -1;
    }

    public static int getAccountIndex(UUID uid) {
        for (int i = 0; i < stored.size(); i++) {
            if(stored.get(i).uid.equals(uid))
                return i;
        }
        return -1;
    }

}
