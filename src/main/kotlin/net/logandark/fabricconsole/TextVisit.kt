package net.logandark.fabricconsole

import net.minecraft.text.Style
import net.minecraft.text.Text

fun <R> Text.visit(parentStyle: Style, block: (Text, Style) -> R?): R? {
	val style = style.withParent(parentStyle)

	block(this, style)?.let { return it }

	for (sibling in siblings)
		sibling.visit(style, block)?.let { return it }

	return null
}
