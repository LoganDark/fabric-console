package net.logandark.fabricconsole

import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*
import java.util.regex.Pattern

object TextToAnsi {
	object FormattingToAnsi {
		private const val ESC = "\u001b"
		private val CODES = mapOf(
			Formatting.BLACK to "30",
			Formatting.DARK_BLUE to "34",
			Formatting.DARK_GREEN to "32",
			Formatting.DARK_AQUA to "36",
			Formatting.DARK_RED to "31",
			Formatting.DARK_PURPLE to "35",
			Formatting.GOLD to "33",
			Formatting.GRAY to "37",
			Formatting.DARK_GRAY to "90",
			Formatting.BLUE to "94",
			Formatting.GREEN to "92",
			Formatting.AQUA to "96",
			Formatting.RED to "91",
			Formatting.LIGHT_PURPLE to "95",
			Formatting.YELLOW to "93",
			Formatting.WHITE to "97",
			Formatting.OBFUSCATED to "",
			Formatting.BOLD to "1",
			Formatting.STRIKETHROUGH to "9",
			Formatting.UNDERLINE to "4",
			Formatting.ITALIC to "3",
			Formatting.RESET to "0"
		)

		private fun combine(vararg strings: String) =
			"$ESC[${strings.filterNot(String::isEmpty).joinToString(";")}m"

		private fun forCode(code: Formatting) = CODES[code] ?: ""

		fun toAnsi(code: Formatting) =
			if (code.isColor) {
				combine(forCode(Formatting.RESET), forCode(code))
			} else {
				combine(forCode(code))
			}
	}

	private val formattingCodePattern: Pattern = Pattern.compile("§[0-9a-fk-or]", Pattern.CASE_INSENSITIVE)

	private fun formattingFromChar(char: Char): Formatting? {
		val normalized = char.toString().toLowerCase(Locale.ROOT)[0]
		return Formatting.values().find { it.toString()[1] == normalized }
	}

	fun textToAnsi(message: Text): String {
		return message.asFormattedString()
			.replace(formattingCodePattern.toRegex()) {
				FormattingToAnsi.toAnsi(formattingFromChar(it.value[1])!!)
			} + FormattingToAnsi.toAnsi(Formatting.RESET)
	}
}