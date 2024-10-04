package dev.kokiriglade.popcorn.text;

import java.util.HashMap;
import java.util.Map;

//
// Copy of SmallFont
//

/**
 * A utility class for converting text to a 𝓯𝓻𝓮𝓪𝓴𝔂 font representation using a character mapping.
 *
 * @since 3.3.5
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class FreakyFont {

    /**
     * A map that holds the character mapping for converting normal characters to small font characters.
     *
     * @since 3.3.5
     */
    private static final Map<Character, String> smallFontMap = new HashMap<>();

    // initializing the character mapping
    static {
        smallFontMap.put('a', "\uD835\uDCEA");
        smallFontMap.put('b', "\uD835\uDCEB");
        smallFontMap.put('c', "\uD835\uDCEC");
        smallFontMap.put('d', "\uD835\uDCED");
        smallFontMap.put('e', "\uD835\uDCEE");
        smallFontMap.put('f', "\uD835\uDCEF");
        smallFontMap.put('g', "\uD835\uDCF0");
        smallFontMap.put('h', "\uD835\uDCF1");
        smallFontMap.put('i', "\uD835\uDCF2");
        smallFontMap.put('j', "\uD835\uDCF3");
        smallFontMap.put('k', "\uD835\uDCF4");
        smallFontMap.put('l', "\uD835\uDCF5");
        smallFontMap.put('m', "\uD835\uDCF6");
        smallFontMap.put('n', "\uD835\uDCF7");
        smallFontMap.put('o', "\uD835\uDCF8");
        smallFontMap.put('p', "\uD835\uDCF9");
        smallFontMap.put('q', "\uD835\uDCFA");
        smallFontMap.put('r', "\uD835\uDCFB");
        smallFontMap.put('s', "\uD835\uDCFC");
        smallFontMap.put('t', "\uD835\uDCFD");
        smallFontMap.put('u', "\uD835\uDCFE");
        smallFontMap.put('v', "\uD835\uDCFF");
        smallFontMap.put('w', "\uD835\uDD00");
        smallFontMap.put('x', "\uD835\uDD01");
        smallFontMap.put('y', "\uD835\uDD02");
        smallFontMap.put('z', "\uD835\uDD03");

        smallFontMap.put('A', "\uD835\uDCD0");
        smallFontMap.put('B', "\uD835\uDCD1");
        smallFontMap.put('C', "\uD835\uDCD2");
        smallFontMap.put('D', "\uD835\uDCD3");
        smallFontMap.put('E', "\uD835\uDCD4");
        smallFontMap.put('F', "\uD835\uDCD5");
        smallFontMap.put('G', "\uD835\uDCD6");
        smallFontMap.put('H', "\uD835\uDCD7");
        smallFontMap.put('I', "\uD835\uDCD8");
        smallFontMap.put('J', "\uD835\uDCD9");
        smallFontMap.put('K', "\uD835\uDCDA");
        smallFontMap.put('L', "\uD835\uDCDB");
        smallFontMap.put('M', "\uD835\uDCDC");
        smallFontMap.put('N', "\uD835\uDCDD");
        smallFontMap.put('O', "\uD835\uDCDE");
        smallFontMap.put('P', "\uD835\uDCDF");
        smallFontMap.put('Q', "\uD835\uDCE0");
        smallFontMap.put('R', "\uD835\uDCE1");
        smallFontMap.put('S', "\uD835\uDCE2");
        smallFontMap.put('T', "\uD835\uDCE3");
        smallFontMap.put('U', "\uD835\uDCE4");
        smallFontMap.put('V', "\uD835\uDCE5");
        smallFontMap.put('W', "\uD835\uDCE6");
        smallFontMap.put('X', "\uD835\uDCE7");
        smallFontMap.put('Y', "\uD835\uDCE8");
        smallFontMap.put('Z', "\uD835\uDCE9");
    }

    /**
     * Converts a given text to its small font representation.
     *
     * @param text The input text to be converted.
     * @return The converted text in small font representation.
     * @since 3.3.5
     */
    public static String convert(final String text) {
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

