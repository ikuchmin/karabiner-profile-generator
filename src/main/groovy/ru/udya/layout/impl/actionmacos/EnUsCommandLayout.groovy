package ru.udya.layout.impl.actionmacos

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition

class EnUsCommandLayout implements Layout {

    static final KEYMAP =
            ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
             '⇥' : 'switch_apps', 'q': 'quit', 'w': 'close', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': 'open', 'p': 'print', '[': '', ']': '', '↩': '',
             '⌘' : '', 'a': 'select_all', 's': 'save', 'd': '', 'f': 'search', 'g': 'search_forward', 'h': 'hide', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': 'new', 'm': 'minimize', ',': 'preferences', '.': '', '/': '',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': 'spotlight', '←': 'home', '↑': 'begin_document', '↓': 'end_document', '→': 'end']

    @Override
    String getName() {
        return 'commandMacOs'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.EN_US_LAYOUT_CONDITION]
    }
}
