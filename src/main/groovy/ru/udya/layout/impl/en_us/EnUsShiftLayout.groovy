package ru.udya.layout.impl.en_us

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers

import static ru.udya.layout.LayoutModifiers.Modifier.SHIFT

class EnUsShiftLayout implements Layout {

    static final KEYMAP =
            ['§' : '~', '1': '!', '2': '@', '3': '#', '4': '$', '5': '%', '6': '^', '7': '&', '8': '*', '9': '(', '0': ')', '-': '_', '=': '+', '⌫': '',
             '⇥' : '', 'q': 'Q', 'w': 'W', 'e': 'E', 'r': 'R', 't': 'T', 'y': 'Y', 'u': 'U', 'i': 'I', 'o': 'O', 'p': 'P', '[': '{', ']': '}', '↩': '',
             '⌘' : '', 'a': 'A', 's': 'S', 'd': 'D', 'f': 'F', 'g': 'G', 'h': 'H', 'j': 'J', 'k': 'K', 'l': 'L', ';': ':', '\'': '"', '\\': '|',
             '⇧' : '', '⎋': '', 'z': 'Z', 'x': 'X', 'c': 'C', 'v': 'V', 'b': 'B', 'n': 'N', 'm': 'M', ',': '<', '.': '>', '/': '?',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'left_select', '↑': 'up_select', '↓': 'down_select', '→': 'right_select']

    @Override
    String getName() {
        return 'enUsShift'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    LayoutModifiers getModifiers() {
        return [mandatory: [SHIFT]] as LayoutModifiers
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.EN_US_LAYOUT_CONDITION]
    }
}
