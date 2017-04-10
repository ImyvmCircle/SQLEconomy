package io.github.andrewward2001.sqlecon.util;

import com.google.common.io.ByteStreams;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class Configuration {

    public static File loadResource(Plugin plugin, String res) {
        File folder = plugin.getDataFolder();
        if(!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, res);
        try {
            if(!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(res);
                    OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

}
