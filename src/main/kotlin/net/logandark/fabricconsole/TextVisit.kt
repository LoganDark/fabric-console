package net.logandark.fabricconsole

import net.logandark.fabricconsole.mixin.MixinTranslatableText
import net.minecraft.text.StringVisitable
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.util.Optional

fun Text.visit(parentStyle: Style, block: (String, Style) -> Unit) {
	val style = style.withParent(parentStyle)

	if (this is TranslatableText) {
		val accessor = (this as MixinTranslatableText)

		accessor.callUpdateTranslations()

		for (translation in accessor.translations) {
			(translation as? Text)?.visit(style, block)
				?: block(translation.string, style)
		}
	} else {
		val sb = StringBuilder()
		this.visitSelf(StringVisitable.Visitor<Unit> {
			sb.append(it)
			Optional.empty()
		})

		block(sb.toString(), style)
	}

	siblings.forEach { sibling ->
		sibling.visit(style, block)
	}
}
