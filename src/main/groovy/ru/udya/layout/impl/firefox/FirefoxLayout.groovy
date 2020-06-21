package ru.udya.layout.impl.firefox

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition

class FirefoxLayout implements Layout {

    static final KEYMAP =
            ['f1': 'recent_tab_files', 'f2': '', 'f3': '', 'f4': '', 'f5': '', 'f6': '', 'f7': '', 'f8': '', 'f9': '', 'f10': '', 'f11': '', 'f12': '',
             '§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
             '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
             '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    @Override
    String getName() {
        return 'firefox'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.FIREFOX_LAYOUT_CONDITION]
    }
}

