package net.logandark.fabricconsole.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.logandark.fabricconsole.TextToAnsi;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = MinecraftServer.class)
public class MixinMinecraftServer {

	@Shadow @Final private static Logger LOGGER;

	@Inject(method = "sendSystemMessage", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", remap = false), cancellable = true)
	private void colorConsole(Text message, UUID senderUuid, CallbackInfo ci) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			LOGGER.info(TextToAnsi.INSTANCE.textToAnsi(message));
			ci.cancel();
		}
	}
}
