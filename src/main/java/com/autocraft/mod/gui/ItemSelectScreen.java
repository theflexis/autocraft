package com.autocraft.mod.gui;

import com.autocraft.mod.AutoCraftClient;
import com.autocraft.mod.AutoCraftConfig;
import com.autocraft.mod.AutoCraftLogic;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemSelectScreen extends Screen {
    private final Screen parent;
    private final AutoCraftConfig cfg = AutoCraftClient.CONFIG;
    private TextFieldWidget input;

    public ItemSelectScreen(Screen parent) {
        super(Text.literal("Item Seç"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = 40;

        input = new TextFieldWidget(this.textRenderer, x, y, 150, 20,
                Text.literal("örn: minecraft:stick"));
        this.addDrawableChild(input);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Ekle"), b -> {
            String id = input.getText().trim();
            if (!id.isEmpty() && !cfg.selectedItems.contains(id)) {
                cfg.selectedItems.add(id);
                AutoCraftLogic.clearCache();
                this.clearAndInit();
            }
        }).dimensions(x + 155, y, 45, 20).build());

        int listY = y + 30;
        List<String> items = new ArrayList<>(cfg.selectedItems);
        for (int i = 0; i < items.size(); i++) {
            String id = items.get(i);
            int rowY = listY + i * 22;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(id + "  [Kaldır]"), b -> {
                cfg.selectedItems.remove(id);
                AutoCraftLogic.clearCache();
                this.clearAndInit();
            }).dimensions(x, rowY, 200, 20).build());
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Geri"), b -> {
            cfg.save();
            this.client.setScreen(parent);
        }).dimensions(x, this.height - 30, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        cfg.save();
        this.client.setScreen(parent);
    }
}
