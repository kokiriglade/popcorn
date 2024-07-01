package dev.kokiriglade.popcorn.text;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for converting text to a small font representation using a character mapping.
 *
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public final class SmallFont {

    /**
     * A map that holds the character mapping for converting normal characters to small font characters.
     *
     * @since 3.0.0
     */
    private static final Map<Character, Character> smallFontMap = new HashMap<>();

    // initializing the character mapping
    static {
        smallFontMap.put('a', 'ᴀ');
        smallFontMap.put('b', 'ʙ');
        smallFontMap.put('c', 'ᴄ');
        smallFontMap.put('d', 'ᴅ');
        smallFontMap.put('e', 'ᴇ');
        smallFontMap.put('f', 'ꜰ');
        smallFontMap.put('g', 'ɢ');
        smallFontMap.put('h', 'ʜ');
        smallFontMap.put('i', 'ɪ');
        smallFontMap.put('j', 'ᴊ');
        smallFontMap.put('k', 'ᴋ');
        smallFontMap.put('l', 'ʟ');
        smallFontMap.put('m', 'ᴍ');
        smallFontMap.put('n', 'ɴ');
        smallFontMap.put('o', 'ᴏ');
        smallFontMap.put('p', 'ᴘ');
        smallFontMap.put('q', 'q');
        smallFontMap.put('r', 'ʀ');
        smallFontMap.put('s', 'ꜱ');
        smallFontMap.put('t', 'ᴛ');
        smallFontMap.put('u', 'ᴜ');
        smallFontMap.put('v', 'ᴠ');
        smallFontMap.put('w', 'ᴡ');
        smallFontMap.put('x', 'x');
        smallFontMap.put('y', 'ʏ');
        smallFontMap.put('z', 'ᴢ');
        smallFontMap.put('~', '˜');
    }

    /**
     * Converts a given text to its small font representation.
     *
     * @param text The input text to be converted.
     * @return The converted text in small font representation.
     * @since 3.0.0
     */
    public static String convert(String text) {
        text = text.toLowerCase();
        final StringBuilder result = new StringBuilder();

        for (final char character : text.toCharArray()) {
            if (smallFontMap.containsKey(character)) {
                result.append(smallFontMap.get(character));
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

}

