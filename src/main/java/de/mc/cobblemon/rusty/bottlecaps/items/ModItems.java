package de.mc.cobblemon.rusty.bottlecaps.items;

import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import de.mc.cobblemon.rusty.bottlecaps.Cobblemonrustybottlecaps;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    private final List<Stats> stats;
    private final List<String> ids;

    public ModItems() {
        stats = List.of(
                Stats.HP,
                Stats.SPEED,
                Stats.ATTACK,
                Stats.DEFENCE,
                Stats.SPECIAL_ATTACK,
                Stats.SPECIAL_DEFENCE
        );
        ids = List.of(
                "rusty_bottle_cap_hp",
                "rusty_bottle_cap_speed",
                "rusty_bottle_cap_attack",
                "rusty_bottle_cap_defence",
                "rusty_bottle_cap_special_attack",
                "rusty_bottle_cap_special_defence"
        );
    }

    public static void initialize() {
        ModItems modItems = new ModItems();
        List<Item> items = modItems.registerRustyBottleCaps();

        items.forEach(item -> {
                    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL) // TODO: change item group
                            .register(fabricItemGroupEntries -> fabricItemGroupEntries.add(item));
                }
        );
    }

    private List<Item> registerRustyBottleCaps() {
        // TODO: refactor
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < stats.size(); i++) {
            items.add(registerRustyBottleCap(stats.get(i), ids.get(i)));
        }
        return items;
    }

    private Item registerRustyBottleCap(final Stats stat, final String id) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(Cobblemonrustybottlecaps.MOD_ID, id),
                new RustyBottleCap(new Item.Settings().maxCount(16), stat)
        );
    }
}
