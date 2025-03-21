package de.mc.cobblemon.rusty.bottlecaps.items;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
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
            final PlayerEntity source,
            final LivingEntity target,
            final Hand hand
    ) {
        // Ensure we don't use rusty bottle caps only on the client.
        // This is to prevent desync.
        if (source.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        if (!(target instanceof PokemonEntity pokemonEntity)) {
            source.sendMessage(Text.translatable("Target is not a Pokemon!"));
            return ActionResult.FAIL;
        }

        if (!isPokemonOwned(pokemonEntity, source)) {
            source.sendMessage(Text.translatable("The targeted Pokemon is not owned by you!"));
            return ActionResult.FAIL;
        }

        pokemonEntity.getPokemon().getIvs().set(this.stat, 0);
        ItemStack heldStack = source.getStackInHand(hand);
        heldStack.decrement(1);

        source.sendMessage(Text.translatable(getSuccessfullMessage(this.stat.getShowdownId(), pokemonEntity)));
        return ActionResult.PASS;
    }

    private boolean isPokemonOwned(final PokemonEntity target, final PlayerEntity playerEntity) {
        var storeCoordinates = target.getPokemon().getStoreCoordinates().get();
        return storeCoordinates != null && storeCoordinates.getStore().getUuid() == playerEntity.getUuid();
    }

    private String getSuccessfullMessage(final String showdownId, final PokemonEntity pokemonEntity) {
        return "%s of %s set to 0".formatted(showdownIdToIvName(showdownId), formatPokemonName(pokemonEntity.getPokemon()));
    }

    private String showdownIdToIvName(final String showdownId) {
        return switch (showdownId) {
            case "hp" -> "Health Points";
            case "spe" -> "Speed";
            case "atk" -> "Attack";
            case "def" -> "Defense";
            case "spa" -> "Special Attack";
            case "spd" -> "Special Defense";
            default -> "IV";
        };
    }

    private String formatPokemonName(final Pokemon pokemon) {
        if (pokemon.getNickname() != null && !pokemon.getNickname().toString().isBlank()) {
            // TODO: refactor qick and dirty solution
            return pokemon.getNickname().getContent().toString().replace("literal{", "").replace("}", "");
        }
        final String displayName = pokemon.getDisplayName().toString().split("\\.")[2];
        return displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
    }
}
