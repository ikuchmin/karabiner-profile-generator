package ru.udya.layout.impl.intellij

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers

class IntelliJCommandShiftLayout implements Layout {

    static final KEYMAP =
            ['f1': '', 'f2': '', 'f3': '', 'f4': '', 'f5': '', 'f6': '', 'f7': '', 'f8': '', 'f9': '', 'f10': '', 'f11': '', 'f12': '',
             '§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
             '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': 'open_file', 'p': '', '[': '', ']': '', '↩': '',
             '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    @Override
    String getName() {
        return 'intelliJCommand'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    LayoutModifiers getModifiers() {
        return [mandatory: [LayoutModifiers.Modifier.LEFT_COMMAND, LayoutModifiers.Modifier.SHIFT]] as LayoutModifiers
    }


    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.INTELLIJ_LAYOUT_CONDITION]
    }
}
