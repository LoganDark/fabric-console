package net.logandark.fabricconsole

import net.minecraft.text.StringVisitable
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.util.Optional

fun Text.visit(parentStyle: Style, block: (String, Style) -> Unit) {
	val style = style.withParent(parentStyle)

	val sb = StringBuilder()
	this.visitSelf(StringVisitable.Visitor<Unit> {
		sb.append(it)
		Optional.empty()
	})

	block(sb.toString(), style)

	siblings.forEach { sibling ->
		sibling.visit(style, block)
	}
}
