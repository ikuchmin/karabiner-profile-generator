package ru.udya.layout.impl

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers

class HardwareLayout implements Layout {

    static final KEYMAP =
            ['f1': 'f1', 'f2': 'f2', 'f3': 'f3', 'f4': 'f4', 'f5': 'f5', 'f6': 'f6', 'f7': 'f7', 'f8': 'f8', 'f9': 'f9', 'f10': 'f10', 'f11': 'f11', 'f12': 'f12',
             '§' : 'grave_accent_and_tilde', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': 'hyphen', '=': 'equal_sign', '⌫': 'delete_or_backspace',
             '⇥' : 'tab', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': 'open_bracket', ']': 'close_bracket', '↩': 'return_or_enter',
             '⌘' : 'left_command', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': 'semicolon', '\'': 'quote', '\\': 'backslash',
             '⇧' : 'left_shift', '⎋': 'escape', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': 'comma', '.': 'period', '/': 'slash', '⇧': 'shift',
             'fn': 'fn', 'f16': 'f16', '⌃': 'left_control', '⌥': 'left_option', '**␣**': 'spacebar', '↩': 'return_or_enter', '⌥': 'option', '←': 'left_arrow', '↑': 'up_arrow', '↓': 'down_arrow', '→': 'right_arrow']

    @Override
    String getName() {
        return 'hardware'
    }

    @Override
    Map getKeymap() {
        return KEYMAP
    }
}
