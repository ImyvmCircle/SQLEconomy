package io.github.andrewward2001.sqlecon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.earth2me.essentials.api.Economy;

public class SQLEconomyListener implements Listener {

    private String table;
    private String defMoney;
    // private static String servername = SQLEconomy.getServername();

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
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    //
                }
                PreparedStatement econRegister = c.prepareStatement(
                        "INSERT INTO `" + table + "` (player, player_uuid, money, active) VALUES (?, ?, ?, ?);");
                if (Economy.playerExists(player.getName())) {
                    try {
                        defMoney = Economy.getMoneyExact(player.getName()).toString();
                    } catch (Exception e) {
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

//    @EventHandler
//    public void UserBalUpdate(UserBalanceUpdateEvent event) {
//        try {
//            Date dt = new Date();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            PreparedStatement logInfo = c.prepareStatement("INSERT INTO `eventlog` (uuid, newevent," +
//                    "oldevent,eventtime,playername,servercode) VALUES (?, ?, ?, ?, ?, ?);");
//            logInfo.setString(1, event.getPlayer().getUniqueId().toString());
//            logInfo.setString(2, event.getNewBalance().toString());
//            logInfo.setString(3, event.getOldBalance().toString());
//            logInfo.setString(4, df.format(dt));
//            logInfo.setString(5, event.getPlayer().getName());
//            logInfo.setString(6, servername);
//            logInfo.executeUpdate();
//            logInfo.close();
//        } catch (SQLException e) {
//            System.out.println("Error creating moneylog!");
//        }
//    }

}
