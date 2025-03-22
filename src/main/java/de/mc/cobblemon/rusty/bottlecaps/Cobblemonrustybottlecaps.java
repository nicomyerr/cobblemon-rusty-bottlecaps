package de.mc.cobblemon.rusty.bottlecaps;

import de.mc.cobblemon.rusty.bottlecaps.items.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobblemonrustybottlecaps implements ModInitializer {

    public static final String MOD_ID = "cobblemon-rusty-bottlecaps";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.initialize();
        LOGGER.info("Items of cobblemon-rusty-bottlecaps successfully initialized!");
    }
}