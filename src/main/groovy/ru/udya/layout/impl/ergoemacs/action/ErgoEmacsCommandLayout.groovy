package ru.udya.layout.impl.ergoemacs.action

import ru.udya.layout.Layout
import ru.udya.layout.LayoutModifiers

class ErgoEmacsCommandLayout implements Layout {

    static final KEYMAP =
            ['f1': '', 'f2': '', 'f3': '', 'f4': '', 'f5': '', 'f6': '', 'f7': '', 'f8': '', 'f9': '', 'f10': '', 'f11': '', 'f12': '',
             '§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
             '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
             '⌘' : '', 'a': '', 's': '', 'd': 'recent_tab_files', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    @Override
    String getName() {
        return 'ergoEmacsCommand'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    LayoutModifiers getModifiers() {
        return [LayoutModifiers.Modifier.LEFT_COMMAND]
    }

}
