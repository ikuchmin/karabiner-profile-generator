package ru.udya.layout.impl.language

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition

class ProgrammingDvorakLayout implements Layout {

    static final KEYMAP =
            ['§' : '$', '1': '&', '2': '[', '3': '{', '4': '}', '5': '(', '6': '=', '7': '*', '8': ')', '9': '+', '0': ']', '-': '!', '=': '#', '⌫': '',
             '⇥' : '', 'q': ';', 'w': ',', 'e': '.', 'r': 'p', 't': 'y', 'y': 'f', 'u': 'g', 'i': 'c', 'o': 'r', 'p': 'l', '[': '/', ']': '@', '↩': '',
             '⌘' : '', 'a': 'a', 's': 'o', 'd': 'e', 'f': 'u', 'g': 'i', 'h': 'd', 'j': 'h', 'k': 't', 'l': 'n', ';': 's', '\'': '-', '\\': '\\',
             '⇧' : '', '⎋': 'escape', 'z': '\'', 'x': 'q', 'c': 'j', 'v': 'k', 'b': 'x', 'n': 'b', 'm': 'm', ',': 'w', '.': 'v', '/': 'z',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']

    @Override
    String getName() {
        return 'programmingDvorak'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.PROGRAMMING_DVORAK_LAYOUT_CONDITION]
    }
}
