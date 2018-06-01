package io.github.andrewward2001.sqlecon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.earth2me.essentials.api.Economy;

public class SQLEconomyListener implements Listener {

    private String table;
    private String defMoney;

    private Connection c;

    public SQLEconomyListener(String table, String defMoney, Connection c) {
        this.table = table;
        this.defMoney = defMoney;
        this.c = c;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        try {
            if (!SQLEconomyActions.playerDataContainsPlayer(player.getUniqueId())) {
                PreparedStatement econRegister = c.prepareStatement(
                        "INSERT INTO `" + table + "` (player, player_uuid, money, active) VALUES (?, ?, ?, ?);");
                if (Economy.playerExists(player.getName())){
                    try{
                        defMoney = Economy.getMoneyExact(player.getName()).toString();
                    } catch (Exception e){
                        //Exception handling
                    }
                }
                econRegister.setString(1, player.getName());
                econRegister.setString(2, player.getUniqueId().toString());
                econRegister.setString(3, defMoney);
                econRegister.setLong(4, 1);
                econRegister.executeUpdate();
                econRegister.close();

                System.out.println("[SQLEconomy] Added user " + player.getName() + " to the economy database.");
            } else {
                // make sure the stored player name is kept current

                Statement statement = c.createStatement();
                statement.executeUpdate("UPDATE `" + table + "` SET player = '" + player.getName()
                        + "' WHERE player_uuid = '" + player.getUniqueId().toString() + "';");
                statement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
