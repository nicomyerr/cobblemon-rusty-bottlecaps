package de.mc.cobblemon.rusty.bottlecaps.items;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class RustyBottleCap extends Item {

    private final Stat stat;

    public RustyBottleCap(final Settings settings, final Stat stat) {
        super(settings);
        this.stat = stat;
    }

    @Override
    public ActionResult useOnEntity(
            final ItemStack itemStack,
            final PlayerEntity player,
            final LivingEntity target,
            final Hand hand
    ) {
        // Ensure we don't use rusty bottle caps only on the client.
        // This is to prevent desync.
        if (player.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        if (!(target instanceof PokemonEntity pokemon)) {
            player.sendMessage(Text.translatable("Target is not a Pokemon!"));
            return ActionResult.FAIL;
        }

        if (!isPokemonOwned(pokemon, player)) {
            player.sendMessage(Text.translatable("The targeted Pokemon is not owned by you!"));
            return ActionResult.FAIL;
        }

        pokemon.getPokemon().getIvs().set(this.stat, 0);
        ItemStack heldStack = player.getStackInHand(hand);
        heldStack.decrement(1);

        // TODO: make pokemon name safe (and use nickname?)
        // TODO: Map name of shown id e.g. spd -> Speed
        String text = "%s of %s set to 0".formatted(this.stat.getShowdownId(), pokemon.getPokemon().getDisplayName().toString().split("\\.")[2].toUpperCase());

        player.sendMessage(Text.translatable(text));
        return ActionResult.PASS;
    }

    private boolean isPokemonOwned(final PokemonEntity target, final PlayerEntity player) {
        var storeCoordinates = target.getPokemon().getStoreCoordinates().get();
        return storeCoordinates != null && storeCoordinates.getStore().getUuid() == player.getUuid();
    }
}
