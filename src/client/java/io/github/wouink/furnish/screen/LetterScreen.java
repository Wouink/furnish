package io.github.wouink.furnish.screen;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.network.UpdateLetterC2S;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class LetterScreen extends Screen {
    private static final Component SCREEN_NAME = Component.translatable("item.furnish.letter");
    private static final Component SIGN_LETTER = Component.translatable("book.signButton");
    private static final Identifier LETTER_BACKGROUND = Identifier.fromNamespaceAndPath(Furnish.MOD_ID, "textures/gui/letter.png");

    // Max length is 16 lines of 18 characters
    private static final int LETTER_MAX_LENGTH = 288;

    private final Player playerEntity;
    private boolean editable = false;
    private String letterText;
    private TextFieldHelper letterEdit = null;
    private int frameTick = 0;
    private final int slot;

    public LetterScreen(ItemStack letter, Player playerEntity, int slot) {
        super(SCREEN_NAME);
        this.playerEntity = playerEntity;
        this.slot = slot;
        letterText = letter.has(FurnishContents.LETTER_TEXT) ? letter.get(FurnishContents.LETTER_TEXT) : "";
        editable = Letter.canEdit(letter);
        if(editable) {
            letterEdit = new TextFieldHelper(this::getText, this::setText, this::getClipboard, this::setClipboard, (s) -> s.length() < LETTER_MAX_LENGTH);
            Furnish.LOGGER.debug("Letter is editable");
        } else Furnish.LOGGER.debug("Letter is not editable");
    }

    private void setClipboard(String s) {
        if (this.minecraft != null) {
            TextFieldHelper.setClipboardContents(this.minecraft, s);
        }
    }

    private String getClipboard() {
        return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
    }

    private String getText() {
        return letterText;
    }

    private void setText(String s) {
        letterText = s;
    }

    private void sendUpdate(Optional<String> author) {
        Furnish.LOGGER.debug("Sending letter update to server");
        ClientPlayNetworking.send(new UpdateLetterC2S(slot, getText(), author));
    }

    @Override
    protected void init() {
        super.init();
        if(editable) {
            this.addRenderableWidget(Button.builder(SIGN_LETTER, (button) -> {
                sendUpdate(Optional.of(playerEntity.getGameProfile().name()));
                this.minecraft.gui.setScreen(null);
            }).bounds(this.width / 2 - 102, 196, 100, 20).build());
            this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
                sendUpdate(Optional.empty());
                this.minecraft.gui.setScreen(null);
            }).bounds(this.width / 2 + 2, 196, 100, 20).build());
        } else {
            this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
                this.minecraft.gui.setScreen(null);
            }).bounds(this.width / 2 - 50, 196, 100, 20).build());
        }
    }

    @Override
    public boolean charTyped(CharacterEvent characterEvent) {
        if(editable && characterEvent.isAllowedChatCharacter() && letterText.length() < LETTER_MAX_LENGTH) {
            letterEdit.insertText(Character.toString(characterEvent.codepoint()));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.key();
        if(keyEvent.isEscape() && shouldCloseOnEsc()) {
            onClose();
            return true;
        }
        if(editable) {
            if(keyEvent.isSelectAll()) {
                letterEdit.selectAll();
                return true;
            } else if(keyEvent.isCopy()) {
                letterEdit.copy();
                return true;
            } else if(keyEvent.isCut()) {
                letterEdit.cut();
                return true;
            } else if(keyEvent.isPaste()) {
                letterEdit.paste();
                return true;
            } else if(keyEvent.isConfirmation()) { // return key (both keyboard and numpad)
                letterEdit.insertText("\n");
                return true;
            } else if(key == 259) { // backspace
                letterEdit.removeCharsFromCursor(-1);
                return true;
            }
            return true;
        }
        return false;
    }

    // TODO use MultiLineEditBox as in BookEditScreen

    /*
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.setFocused(null);
        // int startX = (this.width - 192) / 2;

        visitText(guiGraphics.textRenderer(GuiGraphics.HoveredTextEffects.TOOLTIP_AND_CURSOR));

        /*
        // text and cursor rendering
        if(editable) {
            if(frameTick / 6 % 2 == 0) {
                guiGraphics.drawWordWrap(font, Component.literal(letterText).append("_").setStyle(Style.EMPTY.withColor(ChatFormatting.BLACK)), startX + 36, 20, 108, 0);
            } else {
                guiGraphics.drawWordWrap(font, Component.literal(letterText).append(" ").setStyle(Style.EMPTY.withColor(ChatFormatting.BLACK)), startX + 36, 20, 108, 0);
            }
        } else {
            guiGraphics.drawWordWrap(font, Component.literal(letterText).setStyle(Style.EMPTY.withColor(ChatFormatting.BLACK)), startX + 36, 20, 108, 0);
        }
    }

    private static final Style TEXT_STYLE = Style.EMPTY.withColor(ChatFormatting.BLACK).withoutShadow();
    private List<FormattedCharSequence> cachedPageComponents;

    private void visitText(ActiveTextCollector activeTextCollector) {
        Component displayedText = Component.literal(letterText);
        if(editable) {
            if(frameTick / 6 % 2 == 0) displayedText = Component.literal(letterText).append("_");
            else displayedText = Component.literal(letterText).append(" ");
        }

        FormattedText formattedText = ComponentUtils.mergeStyles(displayedText, TEXT_STYLE);
        this.cachedPageComponents = this.font.split(formattedText, 114);

        int startX = (this.width - 192) / 2 - 4;
        int startY = -4;

        Objects.requireNonNull(this.font);
        int k = Math.min(128 / 9, this.cachedPageComponents.size());

        for(int l = 0; l < k; ++l) {
            FormattedCharSequence formattedCharSequence = this.cachedPageComponents.get(l);
            int x = startX + 36;
            int y = startY + 30;
            Objects.requireNonNull(this.font);
            activeTextCollector.accept(x, y + l * 9, formattedCharSequence);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        this.renderTransparentBackground(guiGraphics);
        int startX = (this.width - 192) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LETTER_BACKGROUND, startX, 2, 0f, 0f, 192, 192, 256, 256);
    }

    @Override
    public void tick() {
        super.tick();
        if(editable) ++frameTick;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
    }
    */
}
