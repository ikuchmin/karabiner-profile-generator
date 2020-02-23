package ru.udya.layout.impl.en_us.action

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers

import static ru.udya.layout.LayoutModifiers.Modifier.LEFT_COMMAND
import static ru.udya.layout.LayoutModifiers.Modifier.LEFT_OPTION
import static ru.udya.layout.LayoutModifiers.Modifier.OPTION
import static ru.udya.layout.LayoutModifiers.Modifier.SHIFT

class EnUsOptionLayout implements Layout {


    static final KEYMAP =
            ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': 'remove_previous_word',
             '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
             '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'previous_word', '↑': 'begin_paragraph', '↓': 'end_paragraph', '→': 'next_word']

    @Override
    String getName() {
        return 'enUsOption'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    LayoutModifiers getModifiers() {
        return [mandatory: [LEFT_OPTION]] as LayoutModifiers
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.EN_US_LAYOUT_CONDITION]
    }
}
