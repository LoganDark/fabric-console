package net.logandark.fabricconsole.mixin;

import net.logandark.fabricconsole.TextToAnsi;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatHud.class)
public class MixinChatHud {
	@Redirect(
		method = "addMessage(Lnet/minecraft/text/Text;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V",
			remap = false
		)
	)
	private void fabricConsole$onAddMessage(Logger instance, String s, Object o, Text message) {
		instance.info(s, TextToAnsi.INSTANCE.textToAnsi(message));
	}
}
