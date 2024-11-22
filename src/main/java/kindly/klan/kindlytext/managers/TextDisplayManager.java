package kindly.klan.kindlytext.managers;

import kindly.klan.kindlytext.CountdownDisplay;
import kindly.klan.kindlytext.Kindlytext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = Kindlytext.MODID, value = Dist.CLIENT)
public class TextDisplayManager {
    private static final List<TextDisplay> textDisplays = new ArrayList<>();

    public static void addTextDisplay(int x, int y, String text, int ticks) {
        textDisplays.add(new TextDisplay(x, y, text, ticks));
    }

    public static void addCountdownDisplay(int x, int y, String text, int minutes, int seconds) {
        textDisplays.add(new CountdownDisplay(x, y, text, minutes, seconds));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Font customFont = CustomFontManager.getCustomFont();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Iterator<TextDisplay> iterator = textDisplays.iterator();
        while (iterator.hasNext()) {
            TextDisplay display = iterator.next();
            if (display.shouldRemove()) {
                iterator.remove();
            } else {
                display.render(mc, guiGraphics, customFont);
            }
        }
    }

    public static class TextDisplay {
        protected final int x, y;
        protected String text;
        protected int ticksRemaining;
        protected int fadeOutTicks;
        protected int textLength;

        public TextDisplay(int x, int y, String text, int ticks) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.ticksRemaining = ticks * 20; // Convert to Minecraft ticks
            this.fadeOutTicks = 1000; // 5 seconds fade out
            this.textLength = 0;
        }

        @OnlyIn(Dist.CLIENT)
        public void render(Minecraft mc, GuiGraphics guiGraphics, Font customFont) {
            if (ticksRemaining > 0) {
                ticksRemaining--;
                if (textLength < text.length()) {
                    textLength++;
                }
            } else if (fadeOutTicks > 0) {
                fadeOutTicks--;
            }

            int alpha = (int) (255 * ((float) fadeOutTicks / 100));
            int color = (alpha << 24) | 0xFFFFFF;
            String displayText = text.substring(0, Math.min(textLength, text.length()));
            guiGraphics.drawString(customFont, displayText, x, y, color);
        }

        public boolean shouldRemove() {
            return fadeOutTicks <= 0;
        }
    }
}