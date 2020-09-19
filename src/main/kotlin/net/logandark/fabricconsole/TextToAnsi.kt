package net.logandark.fabricconsole

import net.minecraft.text.Style
import net.minecraft.text.Text

object TextToAnsi {
	private fun combine(vararg codes: Int) =
		if (codes.isEmpty()) "" else "\u001B[${codes.joinToString(";")}m"

	private fun colorTransition(from: Int, to: Int, trueColor: Boolean): String {
		if (from == to)
			return ""

		val r = to shr 16 and 0xff
		val g = to shr 8 and 0xff
		val b = to and 0xff

		return if (trueColor) {
			combine(38, 2, r, g, b)
		} else {
			"" // TODO
		}
	}

	private fun transition(from: Style, to: Style, trueColor: Boolean): String {
		val escapes = mutableListOf<Int>()

		// First strip styles away

		if (from.isBold && !to.isBold)
			escapes.add(22) // Normal intensity

		if (from.isStrikethrough && !to.isStrikethrough)
			escapes.add(29) // Not crossed out

		if (from.isUnderlined && !to.isUnderlined)
			escapes.add(24) // Underline off

		if (from.isItalic && !to.isItalic)
			escapes.add(23) // Not italic, not Fraktur

		val pre = combine(*escapes.toIntArray())
		escapes.clear()

		// Then change the color

		val fromColor = from.color?.rgb ?: 0xffffff
		val toColor = to.color?.rgb ?: 0xffffff

		val transition = colorTransition(fromColor, toColor, trueColor)

		// Then add new styles

		if (!from.isBold && to.isBold)
			escapes.add(1) // Bold or increased intensity

		if (!from.isStrikethrough && to.isStrikethrough)
			escapes.add(9) // Crossed-out

		if (!from.isUnderlined && to.isUnderlined)
			escapes.add(4) // Underline

		if (!from.isItalic && to.isItalic)
			escapes.add(3) // Italic

		return pre + transition + combine(*escapes.toIntArray())
	}

	fun textToAnsi(message: Text): String {
		val builder = StringBuilder()

		var lastStyle = Style.EMPTY

		message.visit(lastStyle) { text, style ->
			builder.append(transition(lastStyle, style, true))
			builder.append(text)

			lastStyle = style
		}

		builder.append(transition(lastStyle, Style.EMPTY, true)) // reset

		return builder.toString()
	}
}
