package com.autocraft.mod.gui;

import com.autocraft.mod.AutoCraftClient;
import com.autocraft.mod.AutoCraftConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

public class AutoCraftScreen extends Screen {
    private static final int PURPLE = 0xFFA020F0;
    private static final int PANEL_WIDTH = 220;

    private final Screen parent;
    private final AutoCraftConfig cfg = AutoCraftClient.CONFIG;

    public AutoCraftScreen(Screen parent) {
        super(Text.literal("Auto Craft"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - PANEL_WIDTH / 2 + 10;
        int y = this.height / 2 - 70;

        this.addDrawableChild(new CheckboxWidget(x, y, 150, 20, Text.literal("Active"), cfg.active) {
            @Override
            public void onPress() {
                super.onPress();
                cfg.active = this.isChecked();
            }
        });

        this.addDrawableChild(new CheckboxWidget(x, y + 24, 150, 20, Text.literal("Craft All"), cfg.craftAll) {
            @Override
            public void onPress() {
                super.onPress();
                cfg.craftAll = this.isChecked();
            }
        });

        this.addDrawableChild(new CheckboxWidget(x, y + 48, 150, 20, Text.literal("Chat Feedback"), cfg.chatFeedback) {
            @Override
            public void onPress() {
                super.onPress();
                cfg.chatFeedback = this.isChecked();
            }
        });

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Items (" + cfg.selectedItems.size() + " seçili)"),
                b -> this.client.setScreen(new ItemSelectScreen(this))
        ).dimensions(x, y + 76, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Tamam"), b -> {
            cfg.save();
            this.client.setScreen(parent);
        }).dimensions(x, y + 106, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        int panelLeft = this.width / 2 - PANEL_WIDTH / 2;
        int panelTop = this.height / 2 - 90;
        context.fill(panelLeft, panelTop, panelLeft + PANEL_WIDTH, panelTop + 18, PURPLE);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                this.width / 2, panelTop + 5, 0xFFFFFF);

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
