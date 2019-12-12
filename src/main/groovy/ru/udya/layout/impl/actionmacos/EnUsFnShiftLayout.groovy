package ru.udya.layout.impl.actionmacos

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers

import static ru.udya.layout.LayoutModifiers.Modifier.FN
import static ru.udya.layout.LayoutModifiers.Modifier.SHIFT

class EnUsFnShiftLayout implements Layout {

    static final KEYMAP =
            ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
             '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
             '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'begin_document_select', '↑': 'previous_page_select', '↓': 'next_page_select', '→': 'end_document_select']

    @Override
    String getName() {
        return 'enUsFnShift'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    LayoutModifiers getModifiers() {
        return [mandatory: [FN, SHIFT]] as LayoutModifiers
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.EN_US_LAYOUT_CONDITION]
    }
}
