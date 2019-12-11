#! /usr/bin/env groovy
package ru.udya

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import java.util.logging.Logger

@SuppressWarnings("GrMethodMayBeStatic")
class Main /*extends Script*/ {


    enum Modifier {
        SHIFT, COMMAND, OPTION, CONTROL, FN,
        CAPS_LOCK, LEFT_COMMAND, LEFT_CONTROL, LEFT_OPTION,
        LEFT_SHIFT, RIGHT_COMMAND, RIGHT_CONTROL, RIGHT_OPTION,
        RIGHT_SHIFT

        @Override
        String toString() {
            return super.toString().toLowerCase()
        }
    }

    Logger logger = Logger.getLogger(Main.getName())

    List controlKeys = ['⌫', '⇥', '↩', '⌘', '⇧', '⎋', 'fn', 'f16', '⌃', '⌥', '**␣**', '↩', '⌥', '←', '↑', '↓', '→']

    Map commandMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                              '⇥' : 'switch_apps', 'q': 'quit', 'w': 'close', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': 'open', 'p': 'print', '[': '', ']': '', '↩': '',
                              '⌘' : '', 'a': 'select_all', 's': 'save', 'd': '', 'f': 'search', 'g': 'search_forward', 'h': 'hide', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                              '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': 'new', 'm': 'minimize', ',': 'preferences', '.': '', '/': '',
                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': 'spotlight', '←': 'home', '↑': 'begin_document', '↓': 'end_document', '→': 'end']

    Map commandShiftMacOsKeymap = ['§' : 'switch_windows', '1': '', '2': '', '3': 'screenshot', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                   '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': 'search_backward', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                   '⇧' : '', '⎋': '', 'z': 'redo', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                   'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'home_select', '↑': 'begin_document_select', '↓': 'end_document_select', '→': 'end_select']

    Map optionMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': 'remove_previous_word',
                             '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                             '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                             '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                             'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'previous_word', '↑': 'begin_paragraph', '↓': 'end_paragraph', '→': 'next_word']

    Map optionShiftMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                  '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                  '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                  '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                  'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'previous_word_select', '↑': 'previous_paragraph_select', '↓': 'next_paragraph_select', '→': 'next_word_select']

    Map optionCommandMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                    '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                    '⌘' : '', 'a': '', 's': '', 'd': '', 'f': 'replace', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                    '⇧' : '', '⎋': 'force_quit', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                    'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map shiftOptionCommandMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                         '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                         '⌘' : '', 'a': 'search_action', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                         '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                         'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map fnMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': 'remove_next_symbol',
                         '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                         '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                         '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                         'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map fnShiftMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                              '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                              '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                              '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'begin_document_select', '↑': 'previous_page_select', '↓': 'next_page_select', '→': 'end_document_select']

    Map fnOptionMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': 'remove_next_word',
                               '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                               '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                               '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': 'previous_page', '↓': 'next_page', '→': '']

    Map controlEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                              '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'control_u', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                              '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                              '⇧' : '', '⎋': '', 'z': '', 'x': 'control_x', 'c': 'control_c', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map spectacleKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                           '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'previous_third', 'i': 'top_half', 'o': 'next_third', 'p': '', '[': '', ']': '', '↩': '',
                           '⌘' : '', 'a': '', 's': '', 'd': '', 'f': 'fullscreen', 'g': '', 'h': '', 'j': 'left_half', 'k': 'bottom_half', 'l': 'right_half', ';': '', '\'': '', '\\': '',
                           '⇧' : '', '⎋': '', 'z': 'undo', 'x': '', 'c': 'center', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                           'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map spectacleShiftKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'upper_left', 'i': 'make_larger', 'o': 'upper_right', 'p': '', '[': '', ']': '', '↩': '',
                                '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': 'lower_left', 'k': 'make_smaller', 'l': 'lower_right', ';': '', '\'': '', '\\': '',
                                '⇧' : '', '⎋': '', 'z': 'redo', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map prDvorakKeymap = ['§' : '$', '1': '&', '2': '[', '3': '{', '4': '}', '5': '(', '6': '=', '7': '*', '8': ')', '9': '+', '0': ']', '-': '!', '=': '#', '⌫': '',
                          '⇥' : '', 'q': ';', 'w': ',', 'e': '.', 'r': 'p', 't': 'y', 'y': 'f', 'u': 'g', 'i': 'c', 'o': 'r', 'p': 'l', '[': '/', ']': '@', '↩': '',
                          '⌘' : '', 'a': 'a', 's': 'o', 'd': 'e', 'f': 'u', 'g': 'i', 'h': 'd', 'j': 'h', 'k': 't', 'l': 'n', ';': 's', '\'': '-', '\\': '\\',
                          '⇧' : '', '⎋': 'escape', 'z': '\'', 'x': 'q', 'c': 'j', 'v': 'k', 'b': 'x', 'n': 'b', 'm': 'm', ',': 'w', '.': 'v', '/': 'z',
                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']

    Map prDvorakShiftKeymap = ['§': '~', '1': '%', '2': '7', '3': '5', '4': '3', '5': '1', '6': '9', '7': '0', '8': '2', '9': '4', '0': '6', '-': '8', '=': '`', '⌫': '',
                               '⇥':'', 'q': ':', 'w': '<', 'e': '>', 'r': 'P', 't': 'Y', 'y': 'F', 'u': 'G', 'i': 'C', 'o': 'R', 'p': 'L', '[': '?', ']': '^', '↩': '',
                               '⌘':'', 'a': 'A', 's': 'O', 'd': 'E', 'f': 'U', 'g': 'I', 'h': 'D', 'j': 'H', 'k': 'T', 'l': 'N', ';': 'S', '\'': '_', '\\': '|',
                               '⇧':'', '⎋': '', 'z': '"', 'x': 'Q', 'c': 'J', 'v': 'K', 'b': 'X', 'n': 'B', 'm': 'M', ',': 'W', '.': 'V', '/': 'Z',
                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']

    Map navigationErgoEmacsKeymap = ['§':'', '1':'', '2':'close_pane', '3':'close_other_pane', '4':'split_vertically', '5':'replace', '6':'select_block', '7':'select_line', '8':'select_region', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                  '⇥':'', 'q':'fill_unfill_paragraph', 'w':'white', 'e':'remove_previous_word', 'r':'remove_next_word', 't':'compl', 'y':'search_forward', 'u':'previous_word', 'i':'up_arrow', 'o':'next_word', 'p':'recenter', '[':'activate_selection', ']':'deactivate_selection', '↩':'',
                                  '⌘':'', 'a':'search_action', 's':'next_pane', 'd':'remove_previous_symbol', 'f':'remove_next_symbol', 'g':'remove_line_to_end', 'h':'home', 'j':'left_arrow', 'k':'down_arrow', 'l':'right_arrow', ';':'', '\'':'', '\\':'',
                                  '⇧':'', '⎋':'escape', 'z':'undo', 'x':'cut', 'c':'copy', 'v':'paste', 'b':'', 'n':'begin_document', 'm':'', ',':'', '.':'', '/':'toggle_case',
                                  'fn':'', 'f16':'', '⌃':'activate_selection', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map navigationShiftErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': 'split_horizontally', '5': 'replace_regexp', '6': '', '7': '', '8': 'select_quote', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                          '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': 'linter_check', 'y': 'search_backward', 'u': 'begin_paragraph', 'i': 'previous_page', 'o': 'end_paragraph', 'p': '', '[': '', ']': '', '↩': '',
                                          '⌘' : '', 'a': 'shell_command', 's': 'previous_pane', 'd': '', 'f': '', 'g': 'remove_line_to_home', 'h': 'end', 'j': 'previous_bracket', 'k': 'next_page', 'l': 'next_bracket', ';': '', '\'': '', '\\': '',
                                          '⇧' : '', '⎋': '', 'z': 'redo', 'x': 'cut_all', 'c': 'copy_all', 'v': 'paste_buffer', 'b': '', 'n': 'end_document', 'm': '', ',': '', '.': '', '/': 'toggle_camel',
                                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map snappingWindowsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                 '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'previous_third', 'i': 'top_half', 'o': 'next_third', 'p': '', '[': '', ']': '', '↩': '',
                                 '⌘' : '', 'a': '', 's': '', 'd': '', 'f': 'fullscreen', 'g': '', 'h': '', 'j': 'left_half', 'k': 'bottom_half', 'l': 'right_half', ';': '', '\'': '', '\\': '',
                                 '⇧' : '', '⎋': '', 'z': 'undo', 'x': '', 'c': 'center', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                 'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map snappingShiftWindowsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                      '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'upper_left', 'i': 'make_larger', 'o': 'upper_right', 'p': '', '[': '', ']': '', '↩': '',
                                      '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': 'lower_left', 'k': 'make_smaller', 'l': 'lower_right', ';': '', '\'': '', '\\': '',
                                      '⇧' : '', '⎋': '', 'z': 'redo', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                      'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map actionErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                 '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_forward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                 '⌘' : '', 'a': '', 's': 'open', 'd': 'control_x', 'f': 'control_c', 'g': 'control_u', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                 '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                 'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map actionShiftErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                      '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_backward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                      '⌘' : '', 'a': '', 's': 'os_open', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                      '⇧' : '', '⎋': '', 'z': 'redo', 'x': 'cut_all', 'c': 'copy_all', 'v': 'paste_menu', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                      'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map selectedErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'previous_word_select', 'i': 'up_select', 'o': 'next_word_select', 'p': '', '[': '', ']': '', '↩': '',
                                   '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': 'home_select', 'j': 'left_select', 'k': 'down_select', 'l': 'right_select', ';': '', '\'': '', '\\': '',
                                   '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': 'begin_document_select', 'm': '', ',': '', '.': '', '/': '',
                                   'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map selectedShiftErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                        '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'previous_paragraph_select', 'i': 'previous_page_select', 'o': 'next_paragraph_select', 'p': '', '[': '', ']': '', '↩': '',
                                        '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': 'end_select', 'j': '', 'k': 'next_page_select', 'l': '', ';': '', '\'': '', '\\': '',
                                        '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': 'end_document_select', 'm': '', ',': '', '.': '', '/': '',
                                        'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map searchPrErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': 'return_search',
                                   '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                   '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                   'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map searchActionErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                       '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_forward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                       '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                       '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                       'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map searchActionShiftErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                            '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_backward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                            '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                            '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                            'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105CommandKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                          '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                          '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': 'j-mode', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                          '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105CommandShiftKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                               '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                               '⌘' : '', 'a': 'search_action', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                               '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105CommandOptionKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                                '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': 'previous_bracket', ']': 'next_bracket', '↩': '',
                                                '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': 'navigate-back', 'k': '', 'l': 'navigate-forward', ';': '', '\'': '', '\\': '',
                                                '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                                'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105FnKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                     '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                     '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                     '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                     'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': 'previous_page', '↓': 'next_page', '→': '']

    Map jetBrainsMacOs105OptionKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': 'split_vertically', '5': 'replace', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                         '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_forward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                         '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                         '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                         'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105OptionShiftKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': 'split_horizontally', '5': 'replace_regexp', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                              '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_backward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                              '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                              '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105JModeKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                        '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                        '⌘' : '', 'a': '', 's': '', 'd': '', 'f': 'declaration', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                        '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                        'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105JModeCommandKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                               '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                               '⌘' : '', 'a': '', 's': '', 'd': '', 'f': 'quick-documentation', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                               '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map emptyKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                       '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                       '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                       '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                       'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    // conditions
    def enUsLayoutCondition = [type: 'input_source_if',
                               input_sources: [[language: 'en', input_source_id: 'com.apple.keylayout.US']]]
    def prDvorakEnLayoutCondition = [type         : 'input_source_if',
                                     input_sources: [[language: 'en',
                                                      input_source_id: 'com.apple.keyboardlayout.Programmer Dvorak']]]

    def selectedModeCondition = [type: 'variable_if', name: 'selected_mode', value: 1]
    def searchModeCondition = [type: 'variable_if', name: 'search_mode', value: 1]
    def intelliJIdeaAppCondition = [type: 'frontmost_application_if', bundle_identifiers: ['^com\\.jetbrains\\.intellij$']]
    def excludedApplicationCondition = []

    // actions
    def disableSelectedModeAction = [set_variable: [name: 'selected_mode', value: 0]]
    def enableSelectedModeAction = [set_variable: [name: 'selected_mode', value: 1]]

    def enableSearchModeAction = [set_variable: [name: 'search_mode', value: 1]]
    def disableSearchModeAction = [set_variable: [name: 'search_mode', value: 0]]

    Map prErgoEmacsSymbols = [escape: [disableSelectedModeAction, disableSearchModeAction, [key_code: 'escape', modifiers: []]]]

    Map navigationErgoEmacsSymbols = [activate_selection    : [enableSelectedModeAction],
                                      deactivate_selection  : [disableSelectedModeAction],
                                      remove_previous_word  : inherit('remove_previous_word', [disableSelectedModeAction]),
                                      remove_next_word      : inherit('remove_next_word', [disableSelectedModeAction]),
                                      remove_previous_symbol: inherit('remove_previous_symbol', [disableSelectedModeAction]),
                                      remove_next_symbol    : inherit('remove_next_symbol', [disableSelectedModeAction]),
                                      cut                   : inherit('cut', [disableSelectedModeAction]),
                                      copy                  : inherit('copy', [disableSelectedModeAction]),
                                      paste                 : inherit('paste', [disableSelectedModeAction]),
                                      search_forward        : [[key_code: 'f', modifiers: [COMMAND]], enableSearchModeAction],
                                      search_backward       : [[key_code: 'f', modifiers: [COMMAND]], enableSearchModeAction],
    ]

    Map optionShiftErgoEmacsSymbols = [os_open: [[key_code: 'o', modifiers: [LEFT_COMMAND, SHIFT]]]]

    Map prSearchErgoEmacsSymbols = [return_search: [disableSearchModeAction, [key_code: 'escape', modifiers: []]]]

    Map layouts =
            [empty                         : [keymap: emptyKeymap, symbols: { [:] }],

             hardware                      : [keymap: hardwareKeymap, symbols: { [:] }],
             shiftHardware                 : [keymap: hardwareKeymap, symbols: withModifiers([SHIFT])],
             us                            : [keymap: usKeymap, symbols: { [:] }],
             shiftUs                       : [keymap: shiftUsKeymap, symbols: withModifiers([SHIFT])],
             commandMacOs                  : [keymap: commandMacOsKeymap, symbols: withModifiers([LEFT_COMMAND])],
             shiftCommandMacOs             : [keymap: commandShiftMacOsKeymap, symbols: withModifiers([LEFT_COMMAND, SHIFT])],
             optionMacOs                   : [keymap: optionMacOsKeymap, symbols: withModifiers([LEFT_OPTION])],
             shiftOptionMacOs              : [keymap: optionShiftMacOsKeymap, symbols: withModifiers([LEFT_OPTION, SHIFT])],
             optionCommandMacOs            : [keymap: optionCommandMacOsKeymap, symbols: withModifiers([LEFT_OPTION, LEFT_COMMAND])],
             shiftOptionCommandMacOs       : [keymap: shiftOptionCommandMacOsKeymap, symbols: withModifiers([LEFT_OPTION, LEFT_COMMAND, SHIFT])],
             fnMacOs                       : [keymap: fnMacOsKeymap, symbols: withModifiers([FN])],
             fnShiftMacOs                  : [keymap: fnShiftMacOsKeymap, symbols: withModifiers([FN, SHIFT])],
             fnOptionMacOs                 : [keymap: fnOptionMacOsKeymap, symbols: withModifiers([FN, LEFT_OPTION])],
             controlMacOs                  : [keymap: controlEmacsKeymap, symbols: withModifiers([LEFT_CONTROL])],

             spectacle                     : [keymap: spectacleKeymap, symbols: withModifiers([RIGHT_COMMAND, RIGHT_OPTION, RIGHT_CONTROL])],
             spectacleShift                : [keymap: spectacleShiftKeymap, symbols: withModifiers([RIGHT_COMMAND, RIGHT_OPTION, RIGHT_CONTROL, SHIFT])],

             prDvorak                      : [keymap: prDvorakKeymap, symbols: { [:] }],
             prDvorakShift                 : [keymap: prDvorakShiftKeymap, symbols: withModifiers([SHIFT])],
             prDvorakCommandMacOs          : [keymap: supportNewBaseKeymap(commandMacOsKeymap, prDvorakKeymap), symbols: withModifiers([LEFT_COMMAND])],
             prDvorakShiftCommandMacOs     : [keymap: supportNewBaseKeymap(commandShiftMacOsKeymap, prDvorakKeymap), symbols: withModifiers([LEFT_COMMAND, SHIFT])],
             prDvorakOptionMacOs           : [keymap: supportNewBaseKeymap(optionMacOsKeymap, prDvorakKeymap), symbols: withModifiers([LEFT_OPTION])],
             prDvorakShiftOptionMacOs      : [keymap: supportNewBaseKeymap(optionShiftMacOsKeymap, prDvorakKeymap), symbols: withModifiers([LEFT_OPTION, SHIFT])],

             navigationErgoEmacs           : [keymap: navigationErgoEmacsKeymap, symbols: withModifiers([LEFT_OPTION])],
//combine('commandMacOs', 'optionCommandMacOs', 'shiftOptionCommandMacOs', 'optionMacOs', 'fnMacOs', 'fnOptionMacOs', 'us', 'hardware', prErgoEmacsSymbols, navigationErgoEmacsSymbols)],
             navigationShiftErgoEmacs      : [keymap: navigationShiftErgoEmacsKeymap, symbols: withModifiers([LEFT_OPTION, SHIFT])],
//combine('commandMacOs', 'shiftCommandMacOs', 'optionMacOs', 'shiftOptionMacOs', 'fnOptionMacOs', 'shiftHardware', prErgoEmacsSymbols, navigationErgoEmacsSymbols)],

             // snappingWindow                : [keymap: snappingWindowsKeymap, symbols: combine('spectacle')],
             //snappingShiftWindow           : [keymap: snappingShiftWindowsKeymap, symbols: combine('spectacleShift')],

             actionErgoEmacs               : [keymap: actionErgoEmacsKeymap, symbols: withModifiers([LEFT_CONTROL])],
//combine('commandMacOs', 'controlMacOs', navigationErgoEmacsSymbols)], actionShiftErgoEmacs          : [keymap : actionShiftErgoEmacsKeymap,
//                                              symbols: combine(optionShiftErgoEmacsSymbols)],
//             selectedErgoEmacs             : [keymap : selectedErgoEmacsKeymap,
//                                              symbols: combine('shiftUs', 'shiftCommandMacOs', 'shiftOptionMacOs')],
//             selectedShiftErgoEmacs        : [keymap : selectedShiftErgoEmacsKeymap,
//                                              symbols: combine('shiftUs', 'shiftCommandMacOs', 'shiftOptionMacOs', 'fnShiftMacOs')],
//             searchPrErgoEmacs             : [keymap: searchPrErgoEmacsKeymap, symbols: combine(prSearchErgoEmacsSymbols)],
//             searchActionErgoEmacs         : [keymap : searchActionErgoEmacsKeymap,
//                                              symbols: combine('commandMacOs')],
//             searchActionShiftErgoEmacs    : [keymap : searchActionShiftErgoEmacsKeymap,
//                                              symbols: combine('shiftCommandMacOs')],

             jetBrainsMacOs105Command      : [keymap: jetBrainsMacOs105CommandKeymap, symbols: withModifiers([LEFT_COMMAND])],
             jetBrainsMacOs105CommandShift : [keymap: jetBrainsMacOs105CommandShiftKeymap, symbols: withModifiers([LEFT_COMMAND, SHIFT])],
             jetBrainsMacOs105CommandOption: [keymap: jetBrainsMacOs105CommandOptionKeymap, symbols: withModifiers([LEFT_COMMAND, LEFT_OPTION])],
             jetBrainsMacOs105Option       : [keymap: jetBrainsMacOs105OptionKeymap, symbols: withModifiers([LEFT_OPTION])],
             jetBrainsMacOs105OptionShift  : [keymap: jetBrainsMacOs105OptionShiftKeymap, symbols: withModifiers([LEFT_OPTION, SHIFT])],
             jetBrainsMacOs105Fn           : [keymap: jetBrainsMacOs105FnKeymap, symbols: withModifiers([FN])]
             //jetBrainsNavigation           : [keymap : navigationErgoEmacsKeymap, symbols:

//combine('jetBrainsMacOs105CommandShift', 'jetBrainsMacOs105Command', 'jetBrainsMacOs105Option')],
             //jetBrainsNavigationShift      : [keymap : navigationShiftErgoEmacsKeymap, symbols:
//combine('jetBrainsMacOs105Fn', 'jetBrainsMacOs105CommandOption', 'jetBrainsMacOs105OptionShift')]
            ]

             List mappingRules = [
                     [desc      : 'Remap keys to use Programming Dvorak Layout is covered by enUs',
                      layout    : layouts.prDvorak,
                      baseLayout: layouts.us,
                      conditions: [enUsLayoutCondition]],

                     [desc      : 'Remap keys to use Programming Dvorak Layout is covered by enUs [shift]',
                      layout    : layouts.prDvorakShift,
                      baseLayout: layouts.shiftUs,
                      conditions: [enUsLayoutCondition]],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [jetbrains+navigation]',
//                                       layout    : layouts.jetBrainsNavigation,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [LEFT_OPTION], optional: []],
//                                       conditions: [intelliJIdeaAppCondition]],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [jetbrains+navigation+shift]',
//                                       layout    : layouts.jetBrainsNavigationShift,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [LEFT_OPTION, SHIFT], optional: []],
//                                       conditions: [intelliJIdeaAppCondition]],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [selected_mode]',
//                                       layout    : layouts.selectedErgoEmacs,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [LEFT_OPTION], optional: []],
//                                       conditions: [selectedModeCondition]],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [selected_mode+shift]',
//                                       layout    : layouts.selectedShiftErgoEmacs,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [LEFT_OPTION, SHIFT], optional: []],
//                                       conditions: [selectedModeCondition]],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [search_mode]',
//                                       layout    : layouts.searchPrErgoEmacs,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [], optional: []],
//                                       conditions: [searchModeCondition]],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [search_mode+action]',
//                                       layout    : layouts.searchActionErgoEmacs,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [LEFT_OPTION], optional: []],
//                                       conditions: [searchModeCondition]],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [search_mode+action+shift]',
//                                       layout    : layouts.searchActionShiftErgoEmacs,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [LEFT_OPTION, SHIFT], optional: []],
//                                       conditions: [searchModeCondition]],
//
                                  [desc      : 'Remap keys to use ErgoEmacs Layout is covered by Programming Dvorak [navigation]',
                                       layout    : layouts.navigationErgoEmacs,
                                       baseLayout: layouts.prDvorakOptionMacOs + layouts.prDvorakCommandMacOs,
                                       conditions: [prDvorakEnLayoutCondition]],

                                  [desc      : 'Remap keys to use ErgoEmacs Layout is covered by Programming Dvorak [navigation+shift]',
                                       layout    : layouts.navigationShiftErgoEmacs,
                                       baseLayout: layouts.prDvorakShiftOptionMacOs + layouts.prDvorakShiftCommandMacOs,
                                       conditions: [prDvorakEnLayoutCondition]],

                                  [desc      : 'Remap keys to use ErgoEmacs Layout is covered by Programming Dvorak [action]',
                                       layout    : layouts.actionErgoEmacs,
                                       baseLayout: layouts.prDvorakCommandMacOs,
                                       conditions: [prDvorakEnLayoutCondition]],

//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [action+shift]',
//                                       layout    : layouts.actionShiftErgoEmacs,
//                                       baseLayout: layouts.shiftCommandMacOs,
//                                       modifiers : [mandatory: [LEFT_COMMAND, SHIFT], optional: []],
//                                       conditions: []],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [snapping]',
//                                       layout    : layouts.snappingWindow,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [RIGHT_OPTION], optional: []],
//                                       conditions: []],
//
//                                  [desc      : 'Remap keys to use ErgoEmacs Layout [snapping+shift]',
//                                       layout    : layouts.snappingShiftWindow,
//                                       baseLayout: layouts.empty,
//                                       modifiers : [mandatory: [RIGHT_OPTION, SHIFT], optional: []],
//                                       conditions: []
]

    def run() {

        List rules = []

        for (def rule : mappingRules) {

            logger.info("Mapping description: $rule.desc")

            def baseSymbols = evaluateSymbols(rule.baseLayout) //mappingLayoutOnSymbols(mappingLayout.baseLayout.keymap, mappingLayout.baseLayout.symbols)
            def layoutKeymap = rule.layout.keymap //mappingLayoutOnSymbols(mappingLayout.layout.keymap, mappingLayout.layout.symbols)
            def layoutSymbols = evaluateSymbols(rule.layout)

            // generate manipulators
            def manipulators = []
            for (def targetKey in layoutKeymap) {

                // filter keys with empty target key sequence
                if (targetKey.value.empty) {
                    logger.fine("Key sequence ($targetKey) in the target layout is empty")
                    continue
                }

                // filter keys which have the same sequence in base
//                if (baseLayout[layoutKey.key] == layoutKey.value) {
//                    logger.info("Key sequence in target and base layouts are equal (base: ${baseLayout[layoutKey.key]}, target: $layoutKey.value")
//                    continue
//                }

                def fromKeySymbol = layoutSymbols[targetKey.value]
                def toKeySymbol = baseSymbols[targetKey.value]

                if (toKeySymbol == null) {
                    logger.warning("Key sequence ($targetKey) doesn't exist in base keymap")
                    continue
                }

                def fromKarabinerStructure = createFromKarabinerStructure(escapeHardware(fromKeySymbol))
                def toKarabinerStructure = createToKarabinerStructure(escapeHardware(toKeySymbol))
                manipulators << createManipulator(fromKarabinerStructure, toKarabinerStructure, rule.conditions)
            }

            rules << [description: rule.desc, manipulators: manipulators]
        }

        def karabinerComplexLayout =
                [title: 'Dvorak Keyboards', rules: rules]

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
        mapper.writeValue(System.out, karabinerComplexLayout)
    }


    /**
     * Makes keymap which map actions in {@param sourceKeymap}
     * on {@param targetKeymap}. For example, in US keymap
     * action "copy" is mapped on "ctrl+c", but in Dvorak
     * the action is mapped on "ctrl+i". It is default
     * behaviour for macOS.
     *
     * The method realizes that "copy" is the key "c" in
     * sourceKeymap, and "c" is the key "i" in {@param targetKeymap}.
     * The result of generation is [i: "copy"]
     *
     * @param sourceKeymap
     * @param targetKeymap
     * @return
     */
    Map supportNewBaseKeymap(Map sourceKeymap, Map targetKeymap) {

        Map genKeymap = [:]

        for (tkv in targetKeymap) {

            def targetValue = sourceKeymap[tkv.value] ?: ''

            genKeymap.put(tkv.key, targetValue)
        }
//        def doTheSame, difference = doNotKeymapsHaveTheSameActions(sourceKeymap, genKeymap)
//
//        if ()
//            logger.warning('Generator finds some source actions which can not be mapped on target. Source key: {}', skv)

        genKeymap
    }

    Map doNotKeymapsHaveTheSameActions(Map sourceKeymap, Map genKeymap) {
        def sourceNotEmpty = sourceKeymap.findAll { kv -> ! kv.value.empty}
                .sort { kv1, kv2 ->  kv}
        def genNotEmpty = genKeymap.findAll { kv -> ! kv.value.empty}
    }


    @SuppressWarnings("GroovyAssignabilityCheck")
    def withModifiers(List mandatoryModifiers) {
        return { keymap ->

            Map symbols = [:]
            for (key in keymap) {

                if (key.value.empty) {
                    continue
                }

                symbols << [(key.value): [key_code: key.key,
                                          modifiers: mandatoryModifiers]]
            }

            return symbols
        }
    }

    def evaluateSymbols(def layout) {
        return layout.symbols(layout.keymap)
    }

    /**
     * There is bad smell code
     * @param symbol
     * @return
     */
    def escapeHardware(def symbol) {
        symbol['key_code'] = hardwareKeymap[symbol['key_code']]

        return symbol
    }
    /**
     * Create symbols by params
     *
     * @param params list of params
     * @return symbols
     */
    Map defaultSymbolsComposer(Map params) {
        Map layout = [:]

        for (key in params.keymap) {
            if (key.value.empty) {
                continue
            }

            def keySeq = params.generator(key, params.symbols())
            if (keySeq != null) {
                layout << [(key.value): keySeq + params.actions]
            }
        }

        layout
    }

    Map sg(Map params) {
        [keymap: params.keymap, symbols: { -> defaultSymbolsComposer(params) }]
    }

    Map sgh(Map params) {
        sg([keymap : params.keymap, symbols: { hardwareKeymap },
            actions: [], generator: this.&rawKeyGenerator.rcurry(params.modifiers)])
    }

    List rawKeyGenerator(def key, Map baseLayout, List modifiers) {
        def bk = baseLayout[key.key]
        if (bk != null && (!bk.empty)) {
            return [[key_code: bk, modifiers: modifiers]]
        }

        return null
    }

    /**
     * Makes meta layout. The method generate combination of
     * args layout
     *
     * @param args
     * @return
     */
    Closure combine(Object... args) {
        { ->
            Map symbols = [:]
            for (Object arg : args) {
                switch (arg) {
                    case String:
                        symbols.putAll(layouts.get(arg)?.symbols() ?: [:])
                        break
                    case Map:
                        symbols.putAll(resolveSymbols(symbols, arg))
                        break
                    default:
                        throw new IllegalArgumentException('Unknown argument')
                }
            }

            symbols
        }
    }

    Map resolveSymbols(Map availableSymbols, Map symbols) {
        Map result = [:]
        for (s in ((Map) symbols)) {
            switch (s.value) {
                case List:
                    result.put(s.key, s.value)
                    break
                case Closure:
                    result.put(s.key, s.value(availableSymbols))
                    break
            }
        }

        result
    }

    Closure inherit(String name, List actions) {
        { Map from ->
            List acts = from.get(name)

            if (acts != null) {
                return acts + actions
            } else {
                return null
            }
        }
    }

    // standard deep copy implementation
    def deepcopy(Object orig) {
        bos = new ByteArrayOutputStream()
        oos = new ObjectOutputStream(bos)
        oos.writeObject(orig); oos.flush()
        bin = new ByteArrayInputStream(bos.toByteArray())
        ois = new ObjectInputStream(bin)
        return ois.readObject()
    }

    def createBasicSymbolDesc(Object symbol, List modifiers) {
        return [key_code: symbol, modifiers: modifiers]
    }

    def createManipulator(def srcDesc, def dtsSequence, def conditions) {
        return [type: 'basic', from: srcDesc, to: dtsSequence, conditions: conditions]
    }

    def createFromKeySeq(Object key, List mandatoryModifiers, List optionalModifiers) {
        def keySeq = createBasicSymbolDesc(key, mandatoryModifiers)

        return [key_code : keySeq.key_code,
                modifiers: [mandatory: keySeq.modifiers, optional: optionalModifiers]]
    }

    def createFromKarabinerStructure(def symbol) {
        return [key_code : symbol.key_code,
                modifiers: [mandatory: symbol.modifiers]]
    }

    def createToKarabinerStructure(def symbol) {
        return symbol
    }

    def mappingLayoutOnSymbols(Map layout, Closure symbols) {
        Map result = [:]

        def evaluatedSymbols = symbols()
        for (def layoutKey in layout) {
            def keySeq = evaluatedSymbols[layoutKey.value] ?: []
            if ((!layoutKey.value.empty) && keySeq == []) {
                logger.warning("Symbol ($layoutKey) has not been found in symbols")
            }


            result << [(layoutKey.key): keySeq]
        }

        result
    }
}
