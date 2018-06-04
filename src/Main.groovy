#! /usr/bin/env groovy
@groovy.lang.Grapes([
        @Grab(group='com.fasterxml.jackson.core', module='jackson-core', version='2.9.5'),
        @Grab(group='com.fasterxml.jackson.core', module='jackson-databind', version='2.9.5')
])

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import java.util.logging.Logger

import static Main.Modifier.COMMAND
import static Main.Modifier.LEFT_COMMAND
import static Main.Modifier.LEFT_OPTION
import static Main.Modifier.LEFT_SHIFT
import static Main.Modifier.OPTION
import static Main.Modifier.SHIFT
import static Main.Modifier.FN

@SuppressWarnings("GrMethodMayBeStatic")
class Main extends Script {

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

    List controlKeys = ['⌫','⇥','↩', '⌘','⇧', '⎋', 'fn', 'f16', '⌃', '⌥', '**␣**', '↩', '⌥', '←', '↑', '↓', '→']

    Map hardwareKeymap = ['§': 'grave_accent_and_tilde', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': 'hyphen', '=': 'equal_sign', '⌫': 'delete_or_backspace',
                          '⇥': 'tab', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': 'open_bracket', ']': 'close_bracket', '↩': 'return_or_enter',
                          '⌘': 'left_command', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': 'semicolon', '\'': 'quote', '\\': 'backslash',
                          '⇧': 'left_shift', '⎋': 'escape', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': 'comma', '.': 'period', '/': 'slash', '⇧': 'shift',
                          'fn': 'fn', 'f16': 'f16', '⌃': 'left_control', '⌥': 'left_option', '**␣**': 'spacebar', '↩': 'return_or_enter', '⌥': 'option', '←': 'left_arrow', '↑': 'up_arrow', '↓': 'down_arrow', '→': 'right_arrow']

    Map usKeymap = ['§' : '`', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': '-', '=': '=', '⌫': 'remove_previous_symbol',
                    '⇥' : '', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': '[', ']': ']', '↩': '',
                    '⌘' : '', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': ';', '\'': '\'', '\\': '\\',
                    '⇧' : '', '⎋': '', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': ',', '.': '.', '/': '/',
                    'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map shiftUsKeymap = ['§' : '~', '1': '!', '2': '@', '3': '#', '4': '$', '5': '%', '6': '^', '7': '&', '8': '*', '9': '(', '0': ')', '-': '_', '=': '+', '⌫': '',
                         '⇥' : '', 'q': 'Q', 'w': 'W', 'e': 'E', 'r': 'R', 't': 'T', 'y': 'Y', 'u': 'U', 'i': 'I', 'o': 'O', 'p': 'P', '[': '{', ']': '}', '↩': '',
                         '⌘' : '', 'a': 'A', 's': 'S', 'd': 'D', 'f': 'F', 'g': 'G', 'h': 'H', 'j': 'J', 'k': 'K', 'l': 'L', ';': ':', '\'': '"', '\\': '|',
                         '⇧' : '', '⎋': '', 'z': 'Z', 'x': 'X', 'c': 'C', 'v': 'V', 'b': 'B', 'n': 'N', 'm': 'M', ',': '<', '.': '>', '/': '?',
                         'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'left_select', '↑': 'up_select', '↓': 'down_select', '→': 'right_select']

    Map commandMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                              '⇥' : 'switch_apps', 'q': 'quit', 'w': 'close', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': 'open', 'p': 'print', '[': '', ']': '', '↩': '',
                              '⌘' : '', 'a': 'select_all', 's': 'save', 'd': '', 'f': 'search', 'g': 'search_forward', 'h': 'hide', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                              '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': 'new', 'm': 'minimize', ',': 'preferences', '.': '', '/': '',
                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': 'spotlight', '←': 'home', '↑': 'begin_document', '↓': 'end_document', '→': 'end']

    Map commandShiftMacOsKeymap = ['§': 'switch_windows', '1': '', '2': '', '3': 'screenshot', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥': '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                   '⌘': '', 'a': '', 's': '', 'd': '', 'f': '', 'g': 'search_backward', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                   '⇧': '', '⎋': '', 'z': 'redo', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                   'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': 'home_select', '↑': 'begin_document_select', '↓': 'end_document_select', '→': 'end_select']

    Map optionMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'remove_previous_word',
                             '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                             '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                             '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                             'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'previous_word', '↑':'begin_paragraph', '↓':'end_paragraph', '→':'next_word']

    Map optionShiftMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                  '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                  '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                  '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                  'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'previous_word_select', '↑':'previous_paragraph_select', '↓':'next_paragraph_select', '→':'next_word_select']

    Map optionCommandMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                    '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                    '⌘':'', 'a':'', 's':'', 'd':'', 'f':'replace', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                    '⇧':'', '⎋':'force_quit', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                    'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map shiftOptionCommandMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                         '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                         '⌘':'', 'a':'search_action', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                         '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                         'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map fnMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'remove_next_symbol',
                              '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                              '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                              '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                              'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map fnShiftMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                         '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                         '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                         '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                         'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'begin_document_select', '↑':'previous_page_select', '↓':'next_page_select', '→':'end_document_select']

    Map fnOptionMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'remove_next_word',
                               '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                               '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                               '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                               'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'previous_page', '↓':'next_page', '→':'']

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

    Map navigationShiftErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'split_horizontally', '5':'replace_regexp', '6':'', '7':'', '8':'select_quote', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                       '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'linter_check', 'y':'search_backward', 'u':'begin_paragraph', 'i':'previous_page', 'o':'end_paragraph', 'p':'', '[':'', ']':'', '↩':'',
                                       '⌘':'', 'a':'shell_command', 's':'previous_pane', 'd':'', 'f':'', 'g':'remove_line_to_home', 'h':'end', 'j':'previous_bracket', 'k':'next_page', 'l':'next_bracket', ';':'', '\'':'', '\\':'',
                                       '⇧':'', '⎋':'', 'z':'redo', 'x':'cut_all', 'c':'copy_all', 'v':'paste_buffer', 'b':'', 'n':'end_document', 'm':'', ',':'', '.':'', '/':'toggle_camel',
                                       'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']
    Map actionErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                 '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                 '⌘':'', 'a':'', 's':'open', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                 '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                 'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map actionShiftErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                      '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                      '⌘':'', 'a':'', 's':'os_open', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                      '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                      'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map selectedErgoEmacsKeymap = ['§': '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥': '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'previous_word_select', 'i': 'up_select', 'o': 'next_word_select', 'p': '', '[': '', ']': '', '↩': '',
                                   '⌘': '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': 'home_select', 'j': 'left_select', 'k': 'down_select', 'l': 'right_select', ';': '', '\'': '', '\\': '',
                                   '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': 'begin_document_select', 'm': '', ',': '', '.': '', '/': '',
                                   'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map selectedShiftErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                        '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'previous_paragraph_select', 'i':'previous_page_select', 'o':'next_paragraph_select', 'p':'', '[':'', ']':'', '↩':'',
                                        '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'end_select', 'j':'', 'k':'next_page_select', 'l':'', ';':'', '\'':'', '\\':'',
                                        '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'end_document_select', 'm':'', ',':'', '.':'', '/':'',
                                        'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map searchPrErgoEmacsKeymap = ['§': '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥': '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': 'return_search',
                                   '⌘': '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                   '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                   'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map searchActionErgoEmacsKeymap = ['§': '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                       '⇥': '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_forward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                       '⌘': '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                       '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                       'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map searchActionShiftErgoEmacsKeymap = ['§': '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                            '⇥': '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': 'search_backward', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                            '⌘': '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                            '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                            'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105CommandKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                          '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                          '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                          '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105CommandShiftKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                               '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                               '⌘' : '', 'a': 'search_action', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                               '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map jetBrainsMacOs105CommandOptionKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                       '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': 'previous_bracket', ']': 'next_bracket', '↩': '',
                       '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
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

    Map emptyKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                       '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                       '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                       '⇧' : '', '⎋': '', 'z': '', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                       'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    // conditions
    def enLayoutCondition = [type: 'input_source_if', input_sources: [[language: 'en']]]
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

    Map optionShiftErgoEmacsSymbols = [os_open: [[key_code: 'o', modifiers: [LEFT_COMMAND, LEFT_SHIFT]]]]

    Map prSearchErgoEmacsSymbols = [return_search: [disableSearchModeAction, [key_code: 'escape', modifiers: []]]]

    Map layouts =
            [empty                         : [keymap: emptyKeymap, symbols: { [:] }],

             hardware                      : sgh([keymap: hardwareKeymap, modifiers: []]),
             shiftHardware                 : sgh([keymap: hardwareKeymap, modifiers: [SHIFT]]),
             us                            : sgh([keymap: usKeymap, modifiers: []]),
             shiftUs                       : sgh([keymap: shiftUsKeymap, modifiers: [SHIFT]]),
             commandMacOs                  : sgh([keymap: commandMacOsKeymap, modifiers: [COMMAND]]),
             shiftCommandMacOs             : sgh([keymap: commandShiftMacOsKeymap, modifiers: [COMMAND, SHIFT]]),
             optionMacOs                   : sgh([keymap: optionMacOsKeymap, modifiers: [OPTION]]),
             shiftOptionMacOs              : sgh([keymap: optionShiftMacOsKeymap, modifiers: [OPTION, SHIFT]]),
             optionCommandMacOs            : sgh([keymap: optionCommandMacOsKeymap, modifiers: [OPTION, COMMAND]]),
             shiftOptionCommandMacOs       : sgh([keymap: shiftOptionCommandMacOsKeymap, modifiers: [OPTION, COMMAND, SHIFT]]),
             fnMacOs                       : sgh([keymap: fnMacOsKeymap, modifiers: [FN]]),
             fnShiftMacOs                  : sgh([keymap: fnShiftMacOsKeymap, modifiers: [FN, SHIFT]]),
             fnOptionMacOs                 : sgh([keymap: fnOptionMacOsKeymap, modifiers: [FN, OPTION]]),

             prDvorak                      : sg([keymap   : prDvorakKeymap,
                                                 symbols  : combine('us', 'shiftUs', prErgoEmacsSymbols),
                                                 actions  : [disableSelectedModeAction],
                                                 generator: { key, baseLayout -> baseLayout[key.value] }]),
             prDvorakShift                 : sg([keymap   : prDvorakShiftKeymap,
                                                 symbols  : combine('us', 'shiftUs', prErgoEmacsSymbols),
                                                 actions  : [disableSelectedModeAction],
                                                 generator: { key, baseLayout -> baseLayout[key.value] }]),
             navigationErgoEmacs           : [keymap : navigationErgoEmacsKeymap,
                                              symbols: combine('commandMacOs', 'optionCommandMacOs',
                                                      'shiftOptionCommandMacOs', 'optionMacOs', 'fnMacOs', 'fnOptionMacOs',
                                                      'us', 'hardware', prErgoEmacsSymbols, navigationErgoEmacsSymbols)],

             navigationShiftErgoEmacs      : [keymap : navigationShiftErgoEmacsKeymap,
                                              symbols: combine('commandMacOs', 'shiftCommandMacOs',
                                                      'optionMacOs', 'shiftOptionMacOs', 'fnOptionMacOs', 'shiftHardware',
                                                      prErgoEmacsSymbols, navigationErgoEmacsSymbols)],

             actionErgoEmacs               : [keymap : actionErgoEmacsKeymap,
                                              symbols: combine('commandMacOs')],
             actionShiftErgoEmacs          : [keymap : actionShiftErgoEmacsKeymap,
                                              symbols: combine(optionShiftErgoEmacsSymbols)],
             selectedErgoEmacs             : [keymap : selectedErgoEmacsKeymap,
                                              symbols: combine('shiftUs', 'shiftCommandMacOs', 'shiftOptionMacOs')],
             selectedShiftErgoEmacs        : [keymap : selectedShiftErgoEmacsKeymap,
                                              symbols: combine('shiftUs', 'shiftCommandMacOs', 'shiftOptionMacOs', 'fnShiftMacOs')],
             searchPrErgoEmacs             : [keymap: searchPrErgoEmacsKeymap, symbols: combine(prSearchErgoEmacsSymbols)],
             searchActionErgoEmacs         : [keymap : searchActionErgoEmacsKeymap,
                                              symbols: combine('commandMacOs')],
             searchActionShiftErgoEmacs    : [keymap : searchActionShiftErgoEmacsKeymap,
                                              symbols: combine('shiftCommandMacOs')],
             jetBrainsMacOs105Command      : sgh([keymap   : jetBrainsMacOs105CommandKeymap,
                                                  modifiers: [LEFT_COMMAND]]),
             jetBrainsMacOs105CommandShift : sgh([keymap   : jetBrainsMacOs105CommandShiftKeymap,
                                                  modifiers: [LEFT_COMMAND, LEFT_SHIFT]]),
             jetBrainsMacOs105CommandOption: sgh([keymap   : jetBrainsMacOs105CommandOptionKeymap,
                                                  modifiers: [LEFT_COMMAND, LEFT_OPTION]]),
             jetBrainsMacOs105Option       : sgh([keymap   : jetBrainsMacOs105OptionKeymap,
                                                  modifiers: [LEFT_OPTION]]),
             jetBrainsMacOs105OptionShift  : sgh([keymap   : jetBrainsMacOs105OptionShiftKeymap,
                                                  modifiers: [LEFT_OPTION, LEFT_SHIFT]]),
             jetBrainsMacOs105Fn           : sgh([keymap: jetBrainsMacOs105FnKeymap, modifiers: [FN]]),
             jetBrainsNavigation           : [keymap : navigationErgoEmacsKeymap,
                                              symbols: combine('jetBrainsMacOs105CommandShift', 'jetBrainsMacOs105Command',
                                                      'jetBrainsMacOs105Option')],
             jetBrainsNavigationShift      : [keymap : navigationShiftErgoEmacsKeymap,
                                              symbols: combine('jetBrainsMacOs105Fn', 'jetBrainsMacOs105CommandOption',
                                                      'jetBrainsMacOs105OptionShift')]
            ]

    List mappingLayoutsOnModifiers = [[desc      : 'Remap keys to use Programming Dvorak Layout',
                                       layout    : layouts.prDvorak,
                                       baseLayout: layouts.us,
                                       modifiers : [mandatory: [], optional: []],
                                       conditions: [enLayoutCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [shift]',
                                       layout    : layouts.prDvorakShift,
                                       baseLayout: layouts.shiftUs,
                                       modifiers : [mandatory: [SHIFT], optional: []],
                                       conditions: [enLayoutCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [jetbrains+navigation]',
                                       layout    : layouts.jetBrainsNavigation,
                                       baseLayout: layouts.empty,
                                       modifiers : [mandatory: [OPTION], optional: []],
                                       conditions: [intelliJIdeaAppCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [jetbrains+navigation+shift]',
                                       layout    : layouts.jetBrainsNavigationShift,
                                       baseLayout: layouts.empty,
                                       modifiers : [mandatory: [OPTION, SHIFT], optional: []],
                                       conditions: [intelliJIdeaAppCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [selected_mode]',
                                       layout    : layouts.selectedErgoEmacs,
                                       baseLayout: layouts.empty,
                                       modifiers : [mandatory: [OPTION], optional: []],
                                       conditions: [selectedModeCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [selected_mode+shift]',
                                       layout    : layouts.selectedShiftErgoEmacs,
                                       baseLayout: layouts.empty,
                                       modifiers : [mandatory: [OPTION, SHIFT], optional: []],
                                       conditions: [selectedModeCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [search_mode]',
                                       layout    : layouts.searchPrErgoEmacs,
                                       baseLayout: layouts.empty,
                                       modifiers : [mandatory: [], optional: []],
                                       conditions: [searchModeCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [search_mode+action]',
                                       layout    : layouts.searchActionErgoEmacs,
                                       baseLayout: layouts.empty,
                                       modifiers : [mandatory: [OPTION], optional: []],
                                       conditions: [searchModeCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [search_mode+action+shift]',
                                       layout    : layouts.searchActionShiftErgoEmacs,
                                       baseLayout: layouts.empty,
                                       modifiers : [mandatory: [OPTION, SHIFT], optional: []],
                                       conditions: [searchModeCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [navigation]',
                                       layout    : layouts.navigationErgoEmacs,
                                       baseLayout: layouts.optionMacOs,
                                       modifiers : [mandatory: [OPTION], optional: []],
                                       conditions: []],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [navigation+shift]',
                                       layout    : layouts.navigationShiftErgoEmacs,
                                       baseLayout: layouts.shiftOptionMacOs,
                                       modifiers : [mandatory: [OPTION, SHIFT], optional: []],
                                       conditions: []],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [action]',
                                       layout    : layouts.actionErgoEmacs,
                                       baseLayout: layouts.commandMacOs,
                                       modifiers : [mandatory: [COMMAND], optional: []],
                                       conditions: []],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [action+shift]',
                                       layout    : layouts.actionShiftErgoEmacs,
                                       baseLayout: layouts.shiftCommandMacOs,
                                       modifiers : [mandatory: [COMMAND, SHIFT], optional: []],
                                       conditions: []]
    ]

    def run() {

        List rules = []

        for (def mappingLayout : mappingLayoutsOnModifiers) {

            logger.info("Mapping description: $mappingLayout.desc")

            def baseLayout = mappingLayoutOnSymbols(mappingLayout.baseLayout.keymap, mappingLayout.baseLayout.symbols)
            def targetLayout = mappingLayoutOnSymbols(mappingLayout.layout.keymap, mappingLayout.layout.symbols)

            // generate manipulators
            def manipulators = []
            for (def layoutKey in targetLayout) {

                // filter keys with empty target key sequence
                if (layoutKey.value.empty) {
                    logger.fine("Key sequence ($layoutKey) in the target layout is empty")
                    continue
                }

                // filter keys which have the same sequence in base
                if (baseLayout[layoutKey.key] == layoutKey.value) {
                    logger.info("Key sequence in target and base layouts are equal (base: ${baseLayout[layoutKey.key]}, target: $layoutKey.value")
                    continue
                }

                def fromKeySeq = createFromKeySeq(hardwareKeymap[layoutKey.key],
                        mappingLayout.modifiers.mandatory, mappingLayout.modifiers.optional)
                def toKeySeq = layoutKey.value

                manipulators << createManipulator(fromKeySeq, toKeySeq, mappingLayout.conditions)
            }

            rules << [description: mappingLayout.desc, manipulators: manipulators]
        }

        def karabinerComplexLayout =
                [title: 'Dvorak Keyboards', rules: rules]

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
        mapper.writeValue(System.out, karabinerComplexLayout)
    }



    Map defaultSymbolsComposer(Map params) {
        Map layout = [:]

        for(key in params.keymap) {
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
        sg([keymap: params.keymap, symbols: { hardwareKeymap },
            actions: [], generator: this.&rawKeyGenerator.rcurry(params.modifiers)])
    }

    List rawKeyGenerator(def key, Map baseLayout, List modifiers) {
        def bk = baseLayout[key.key]
        if (bk != null && (! bk.empty)) {
            return [[key_code: bk, modifiers: modifiers]]
        }

        return null
    }

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

    def createManipulator(Object srcDesc, List dtsSequence, List conditions) {
        return [type: 'basic', from: srcDesc, to: dtsSequence, conditions: conditions]
    }

    def createFromKeySeq(Object key, List mandatoryModifiers, List optionalModifiers) {
        def keySeq = createBasicSymbolDesc(key, mandatoryModifiers)

        return [key_code : keySeq.key_code,
                modifiers: [mandatory: keySeq.modifiers, optional: optionalModifiers]]
    }

    def mappingLayoutOnSymbols(Map layout, Closure symbols) {
        Map result = [:]

        def evaluatedSymbols = symbols()
        for (def layoutKey in layout) {
            def keySeq = evaluatedSymbols[layoutKey.value] ?: []
            if ((! layoutKey.value.empty) && keySeq == []) {
                logger.warning("Symbol ($layoutKey) has not been found in symbols")
            }


            result << [(layoutKey.key): keySeq]
        }

        result
    }
}
