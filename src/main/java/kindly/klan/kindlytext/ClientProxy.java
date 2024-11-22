
package kindly.klan.kindlytext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy {
    public static void renderCountdownDisplay(CountdownDisplay countdownDisplay, GuiGraphics guiGraphics, Font customFont) {
        Minecraft mc = Minecraft.getInstance();
        countdownDisplay.render(mc, guiGraphics, customFont);
    }
}