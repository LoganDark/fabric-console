package net.logandark.fabricconsole.mixin;

import net.logandark.fabricconsole.TextToAnsi;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = MinecraftServer.class)
public class MixinMinecraftServer {
	@Shadow
	public CommandManager getCommandManager() {
		return null;
	}

	@Redirect(
		method = "sendSystemMessage",
		at = @At(
			value = "INVOKE",
			target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V",
			remap = false
		)
	)
	private void fabric_console_onSystemMessage(Logger instance, String s, Text message, UUID senderUuid) {
		instance.info(TextToAnsi.INSTANCE.textToAnsi(message));
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void fabric_console_onInit(CallbackInfo ci) {
		getCommandManager().getDispatcher().register(CommandManager.literal("tellrawc")
			.requires(src -> src.hasPermissionLevel(2))
			.then(CommandManager.argument("message", TextArgumentType.text())
				.executes(ctx -> {
					ctx.getSource().getMinecraftServer().sendSystemMessage(TextArgumentType.getTextArgument(ctx, "message"), UUID.randomUUID());
					return 1;
				})
			)
		);
	}
}
