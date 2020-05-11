package net.logandark.fabricconsole.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import net.logandark.fabricconsole.TextToAnsi;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.text.Text;
import net.minecraft.util.UserCache;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.net.Proxy;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MixinMinecraftDedicatedServer extends MinecraftServer {
	@Shadow
	@Final
	private static Logger LOGGER;

	public MixinMinecraftDedicatedServer(File gameDir, Proxy proxy, DataFixer dataFixer, CommandManager commandManager, YggdrasilAuthenticationService authService, MinecraftSessionService sessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, String levelName) {
		super(gameDir, proxy, dataFixer, commandManager, authService, sessionService, gameProfileRepository, userCache, worldGenerationProgressListenerFactory, levelName);
	}

	@Override
	public void sendMessage(Text message) {
		LOGGER.info(TextToAnsi.INSTANCE.textToAnsi(message));
	}
}
