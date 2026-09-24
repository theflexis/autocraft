package com.autocraft.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Crafting ekrani (workbench veya 2x2 envanter) acikken, secilen itemlar icin
 * periyodik olarak vanilla "recipe book craft" istegini sunucuya yollar.
 * Bu paket, tarif defterinde bir tarife tiklamis (ve istenirse shift-click / "hepsini craft et")
 * yapmis gibi sunucu tarafinda islenir; hile/anti-cheat atlatma icermez.
 */
public class AutoCraftLogic {
    private static int tickCounter = 0;
    private static final Map<Item, Identifier> RECIPE_CACHE = new HashMap<>();

    public static void tick(MinecraftClient client) {
        AutoCraftConfig cfg = AutoCraftClient.CONFIG;
        if (!cfg.active || client.player == null || client.world == null) {
            return;
        }

        if (!(client.currentScreen instanceof HandledScreen<?> handledScreen)) {
            return;
        }

        ScreenHandler handler = handledScreen.getScreenHandler();
        boolean isCraftingScreen = handler instanceof CraftingScreenHandler
                || handler instanceof PlayerScreenHandler;
        if (!isCraftingScreen) {
            return;
        }

        tickCounter++;
        if (tickCounter < Math.max(1, cfg.intervalTicks)) {
            return;
        }
        tickCounter = 0;

        if (cfg.selectedItems.isEmpty()) {
            return;
        }

        RecipeManager recipeManager = client.world.getRecipeManager();

        for (String itemId : cfg.selectedItems) {
            Identifier id = Identifier.tryParse(itemId);
            if (id == null) {
                continue;
            }
            Item item = Registries.ITEM.get(id);
            if (item == null) {
                continue;
            }

            Identifier recipeId = RECIPE_CACHE.computeIfAbsent(item,
                    i -> findRecipeFor(recipeManager, client, i));
            if (recipeId == null) {
                continue;
            }

            client.player.networkHandler.sendPacket(
                    new CraftRequestC2SPacket(handler.syncId, recipeId, cfg.craftAll)
            );

            if (cfg.chatFeedback) {
                client.player.sendMessage(
                        Text.literal("[AutoCraft] " + item.getName().getString() + " craft istendi"),
                        true
                );
            }
        }
    }

    private static Identifier findRecipeFor(RecipeManager recipeManager, MinecraftClient client, Item item) {
        for (Recipe<?> recipe : recipeManager.values()) {
            if (!(recipe instanceof CraftingRecipe craftingRecipe)) {
                continue;
            }
            ItemStack output = craftingRecipe.getOutput(client.world.getRegistryManager());
            if (output.getItem() == item) {
                return craftingRecipe.getId();
            }
        }
        return null;
    }

    /** Item listesi degistiginde cache'i temizlemek icin */
    public static void clearCache() {
        RECIPE_CACHE.clear();
    }
}
