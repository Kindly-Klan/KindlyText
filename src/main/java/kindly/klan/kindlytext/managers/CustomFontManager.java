package kindly.klan.kindlytext.managers;

import kindly.klan.kindlytext.Kindlytext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CustomFontManager {
    private static Font customFont;
    private static final ResourceLocation CUSTOM_FONT_LOCATION = new ResourceLocation(Kindlytext.MODID, "textures/font/custom_font.png");

    @OnlyIn(Dist.CLIENT)
    public static Font getCustomFont() {
        if (customFont == null) {
            Minecraft mc = Minecraft.getInstance();
            customFont = mc.font;
            // Todavía no lo he implementado, pero aquí es donde cargarías la fuente custom
            mc.getTextureManager().bindForSetup(CUSTOM_FONT_LOCATION);
        }
        return customFont;
    }
}