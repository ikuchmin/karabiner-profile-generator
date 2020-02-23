package ru.udya.layout.impl.en_us.action

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers

import static ru.udya.layout.LayoutModifiers.Modifier.COMMAND
import static ru.udya.layout.LayoutModifiers.Modifier.LEFT_COMMAND
import static ru.udya.layout.LayoutModifiers.Modifier.SHIFT

class EnUsCommandShiftLayout implements Layout {

    static final KEYMAP =
            ['§' : 'switch_windows', '1': '', '2': '', '3': 'screenshot', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
             '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
             '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': 'search_backward', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': '', 'z': 'redo', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'home_select', '↑': 'begin_document_select', '↓': 'end_document_select', '→': 'end_select']

    @Override
    String getName() {
        return 'enUsCommandShift'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    LayoutModifiers getModifiers() {
        return [mandatory: [LEFT_COMMAND, SHIFT]] as LayoutModifiers
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.EN_US_LAYOUT_CONDITION]
    }
}
