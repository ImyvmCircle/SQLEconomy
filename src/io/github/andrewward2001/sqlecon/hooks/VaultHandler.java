package io.github.andrewward2001.sqlecon.hooks;

import org.bukkit.plugin.Plugin;

public class VaultHandler {

    private final Plugin plugin;

    public VaultHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean enabled() {
        return plugin !=null && plugin.isEnabled();
    }

    public boolean exists() {
        return plugin!=null;
    }

}
