package net.logandark.fabricconsole.mixin;

import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextColor.class)
public interface MixinTextColor {
	@Accessor
	int getRgb();
}
