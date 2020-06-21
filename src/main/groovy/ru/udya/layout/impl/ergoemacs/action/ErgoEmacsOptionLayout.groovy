package ru.udya.layout.impl.ergoemacs.action

import ru.udya.layout.Layout
import ru.udya.layout.LayoutModifiers

class ErgoEmacsOptionLayout implements Layout {

    static final KEYMAP =
            ['§' : '', '1': '', '2': 'close_pane', '3': 'close_other_pane', '4': 'split_vertically', '5': 'replace', '6': 'select_block', '7': 'select_line', '8': 'select_region', '9': '', '0': '', '-': '', '=': '', '⌫': '',
             '⇥' : '', 'q': 'fill_unfill_paragraph', 'w': 'white', 'e': 'remove_previous_word', 'r': 'remove_next_word', 't': 'compl', 'y': 'search_forward', 'u': 'previous_word', 'i': 'up_arrow', 'o': 'next_word', 'p': 'recenter', '[': 'activate_selection', ']': 'deactivate_selection', '↩': '',
             '⌘' : '', 'a': 'search_action', 's': 'next_pane', 'd': 'remove_previous_symbol', 'f': 'remove_next_symbol', 'g': 'remove_line_to_end', 'h': 'home', 'j': 'left_arrow', 'k': 'down_arrow', 'l': 'right_arrow', ';': '', '\'': '', '\\': '',
             '⇧' : '', '⎋': 'escape', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': 'begin_document', 'm': '', ',': '', '.': '', '/': 'toggle_case',
             'fn': '', 'f16': '', '⌃': 'activate_selection', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    @Override
    String getName() {
        return 'ergoEmacsOption'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }

    @Override
    LayoutModifiers getModifiers() {
        return [LayoutModifiers.Modifier.LEFT_OPTION]
    }
}
