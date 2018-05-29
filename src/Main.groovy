#! /usr/bin/env groovy
@groovy.lang.Grapes([
        @Grab(group='com.fasterxml.jackson.core', module='jackson-core', version='2.9.5'),
        @Grab(group='com.fasterxml.jackson.core', module='jackson-databind', version='2.9.5')
])

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import java.util.logging.Logger

import static Main.Modifier.COMMAND
import static Main.Modifier.OPTION
import static Main.Modifier.SHIFT
import static Main.Modifier.FN

@SuppressWarnings("GrMethodMayBeStatic")
class Main extends Script {

    enum Modifier {
        SHIFT, COMMAND, OPTION, CONTROL, FN, F16,
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
                              '⌘' : '', 'a': 'select_all', 's': 'save', 'd': '', 'f': 'search_forward', 'g': 'find_again', 'h': 'hide', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                              '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': 'new', 'm': 'minimize', ',': 'preferences', '.': '', '/': '',
                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': 'spotlight', '←': 'home', '↑': 'begin_document', '↓': 'end_document', '→': 'end']

    Map commandShiftMacOsKeymap = ['§': 'switch_windows', '1': '', '2': '', '3': 'screenshot', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥': '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                   '⌘': '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
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
                                    '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                    '⇧':'', '⎋':'force_quit', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                    'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map shiftOptionCommandMacOsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                         '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                         '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
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
                          '⇧' : '', '⎋': 'escape_deactivate_selection', 'z': '\'', 'x': 'q', 'c': 'j', 'v': 'k', 'b': 'x', 'n': 'b', 'm': 'm', ',': 'w', '.': 'v', '/': 'z',
                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']

    Map shiftPrDvorakKeymap = ['§': '~', '1': '%', '2': '7', '3': '5', '4': '3', '5': '1', '6': '9', '7': '0', '8': '2', '9': '4', '0': '6', '-': '8', '=': '`', '⌫': '',
                               '⇥':'', 'q': ':', 'w': '<', 'e': '>', 'r': 'P', 't': 'Y', 'y': 'F', 'u': 'G', 'i': 'C', 'o': 'R', 'p': 'L', '[': '?', ']': '^', '↩': '',
                               '⌘':'', 'a': 'A', 's': 'O', 'd': 'E', 'f': 'U', 'g': 'I', 'h': 'D', 'j': 'H', 'k': 'T', 'l': 'N', ';': 'S', '\'': '_', '\\': '|',
                               '⇧':'', '⎋': '', 'z': '"', 'x': 'Q', 'c': 'J', 'v': 'K', 'b': 'X', 'n': 'B', 'm': 'M', ',': 'W', '.': 'V', '/': 'Z',
                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']

    Map commandErgoEmacsKeymap = ['§':'', '1':'', '2':'close_pane', '3':'close_other_pane', '4':'split_vertical', '5':'replace', '6':'select_block', '7':'select_line', '8':'select_region', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                  '⇥':'', 'q':'fill_unfill_paragraph', 'w':'white', 'e':'remove_previous_word', 'r':'remove_next_word', 't':'compl', 'y':'search_forward', 'u':'previous_word', 'i':'up_arrow', 'o':'next_word', 'p':'recenter', '[':'activate_selection', ']':'deactivate_selection', '↩':'',
                                  '⌘':'', 'a':'search_action', 's':'next_pane', 'd':'remove_previous_symbol', 'f':'remove_next_symbol', 'g':'remove_line_to_end', 'h':'home', 'j':'left_arrow', 'k':'down_arrow', 'l':'right_arrow', ';':'', '\'':'', '\\':'',
                                  '⇧':'', '⎋':'', 'z':'undo', 'x':'cut', 'c':'copy', 'v':'paste', 'b':'', 'n':'begin_document', 'm':'', ',':'', '.':'', '/':'toggle_case',
                                  'fn':'', 'f16':'', '⌃':'activate_selection', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map commandShiftErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'split_horizontally', '5':'replace_regexp', '6':'', '7':'', '8':'select_quote', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                       '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'linter_check', 'y':'search_backward', 'u':'begin_paragraph', 'i':'previous_page', 'o':'end_paragraph', 'p':'', '[':'', ']':'', '↩':'',
                                       '⌘':'', 'a':'shell_command', 's':'previous_pane', 'd':'', 'f':'', 'g':'remove_line_to_home', 'h':'end', 'j':'previous_bracket', 'k':'next_page', 'l':'next_bracket', ';':'', '\'':'', '\\':'',
                                       '⇧':'', '⎋':'', 'z':'redo', 'x':'cut_all', 'c':'copy_all', 'v':'paste_buffer', 'b':'', 'n':'end_document', 'm':'', ',':'', '.':'', '/':'toggle_camel',
                                       'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map commandSelectedErgoEmacsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                          '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': 'previous_word_select', 'i': 'up_select', 'o': 'next_word_select', 'p': '', '[': '', ']': '', '↩': '',
                                          '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': 'home_select', 'j': 'left_select', 'k': 'down_select', 'l': 'right_select', ';': '', '\'': '', '\\': '',
                                          '⇧' : '', '⎋': '', 'z': '', 'x': 'cut_select', 'c': 'copy_select', 'v': 'paste_select', 'b': '', 'n': 'begin_document_select', 'm': '', ',': '', '.': '', '/': '',
                                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map commandShiftSelectedErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                       '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'previous_paragraph_select', 'i':'previous_page_select', 'o':'next_paragraph_select', 'p':'', '[':'', ']':'', '↩':'',
                                       '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'end_select', 'j':'', 'k':'next_page_select', 'l':'', ';':'', '\'':'', '\\':'',
                                       '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'end_document_select', 'm':'', ',':'', '.':'', '/':'',
                                       'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map emptyKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                       '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                       '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                       '⇧':'', '⎋':'', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                       'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    // conditions
    def enLayoutCondition = [type: 'input_source_if', input_sources: [[language: 'en']]]
    def selectedModeCondition = [type: "variable_if", name: "selected_mode", value: 1]
    def excludedApplicationCondition = []

    // actions
    def disableSelectedModeAction = [set_variable: [name: 'selected_mode', value: 0]]
    def enableSelectedModeAction = [set_variable: [name: 'selected_mode', value: 1]]

    Map commandErgoEmacsSymbols = [activate_selection  : [enableSelectedModeAction]]
    Map commandSelectedErgoEmacsSymbols = [cut_select          : [[key_code: 'x', modifiers: ['command']], disableSelectedModeAction],
                                           copy_select         : [[key_code: 'c', modifiers: ['command']], disableSelectedModeAction],
                                           paste_select        : [[key_code: 'v', modifiers: ['command']], disableSelectedModeAction],
                                           activate_selection  : [[key_code: 'escape', modifiers: []], enableSelectedModeAction],
                                           deactivate_selection: [[key_code: 'escape', modifiers: []], disableSelectedModeAction]]

    Map commandShiftErgoEmacsSymbols = ['cut'  : [[key_code: 'x', modifiers: ['command']], disableSelectedModeAction],
                                        'copy' : [[key_code: 'c', modifiers: ['command']], [key_code: 'escape'], disableSelectedModeAction],
                                        'paste': [[key_code: 'v', modifiers: ['command']], disableSelectedModeAction]]

    Map defaultSymbolsComposer(Map params) {
        Map layout = [:]

        for(key in params.keymap) {
            if (key.value.empty) {
                continue
            }

            def keySeq = params.generator(key, params.baseLayout() + params.symbols)
            if (keySeq != null) {
                layout << [(key.value): keySeq + params.actions]
            }
        }

        layout
    }

    Closure lg(Map params) {
        { -> defaultSymbolsComposer(params) }
    }

    List rawKeyGenerator(def key, Map baseLayout, List modifiers) {
        def bk = baseLayout[key.key]
        if (bk != null && (! bk.empty)) {
            return [[key_code: bk, modifiers: modifiers]]
        }

        return null
    }

    Map<String, Closure> symbols =
            [hardware                     : lg([desc      : 'Hardware',
                                                keymap    : hardwareKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([])]),

             shiftHardware                : lg([desc      : 'Hardware [shift]',
                                                keymap    : hardwareKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([SHIFT])]),

             us                           : lg([desc      : 'U.S.',
                                                keymap    : usKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([])]),

             shiftUs                      : lg([desc      : 'U.S. [shift]',
                                                keymap    : shiftUsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([SHIFT])]),

             commandMacOs                 : lg([desc      : 'macOs [command]',
                                                keymap    : commandMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([COMMAND])]),

             shiftCommandMacOs            : lg([desc      : 'macOs [command+shift]',
                                                keymap    : commandShiftMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([COMMAND, SHIFT])]),

             optionMacOs                  : lg([desc      : 'macOs [option]',
                                                keymap    : optionMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([OPTION])]),

             shiftOptionMacOs             : lg([desc      : 'macOs [option+shift]',
                                                keymap    : optionShiftMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([OPTION, SHIFT])]),

             optionCommandMacOs           : lg([desc      : 'macOs [command+option]',
                                                keymap    : optionCommandMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([OPTION, COMMAND])]),

             shiftOptionCommandMacOs      : lg([desc      : 'macOs [command+option+shift]',
                                                keymap    : shiftOptionCommandMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([OPTION, COMMAND, SHIFT])]),

             fnMacOs                      : lg([desc      : 'macOs [fn]',
                                                keymap    : fnMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([FN])]),

             fnShiftMacOs                      : lg([desc      : 'macOs [fn+shift]',
                                                keymap    : fnShiftMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([FN, SHIFT])]),

             fnOptionMacOs                : lg([desc      : 'macOs [fn+option]',
                                                keymap    : fnOptionMacOsKeymap,
                                                baseLayout: { hardwareKeymap },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : this.&rawKeyGenerator.rcurry([FN, OPTION])]),

             prDvorak                     : lg([desc      : 'Programming Dvorak',
                                                keymap    : prDvorakKeymap,
                                                baseLayout: { symbols.us() + symbols.shiftUs() },
                                                symbols   : [:],
                                                actions   : [disableSelectedModeAction],
                                                generator : { key, baseLayout -> baseLayout[key.value] }]),

             shiftPrDvorak                : lg([desc      : 'Programming Dvorak [shift]',
                                                keymap    : shiftPrDvorakKeymap,
                                                baseLayout: { symbols.us() + symbols.shiftUs() },
                                                symbols   : [:],
                                                actions   : [disableSelectedModeAction],
                                                generator : { key, baseLayout -> baseLayout[key.value] }]),

             commandErgoEmacs             : lg([desc      : 'ErgoEmacs [command]',
                                                keymap    : commandErgoEmacsKeymap,
                                                baseLayout: {
                                                    symbols.commandMacOs() + symbols.optionMacOs() +
                                                            symbols.fnMacOs() + symbols.fnOptionMacOs() +
                                                            symbols.us() + symbols.hardware()
                                                },
                                                symbols   : commandErgoEmacsSymbols,
                                                actions   : [],
                                                generator : { key, baseLayout -> baseLayout[key.value] }]),

             commandShiftErgoEmacs        : lg([desc      : 'ErgoEmacs [command + shift]',
                                                keymap    : commandShiftErgoEmacsKeymap,
                                                baseLayout: {
                                                    symbols.commandMacOs() + symbols.shiftCommandMacOs() +
                                                            symbols.optionMacOs() + symbols.shiftOptionMacOs() +
                                                            symbols.fnOptionMacOs() + symbols.shiftHardware()
                                                },
                                                symbols   : commandShiftErgoEmacsSymbols,
                                                actions   : [],
                                                generator : { key, baseLayout -> baseLayout[key.value] }]),
             commandSelectedErgoEmacs     : lg([desc      : 'ErgoEmacs [command + selected]',
                                                keymap    : commandSelectedErgoEmacsKeymap,
                                                baseLayout: {
                                                    symbols.shiftUs() + symbols.shiftCommandMacOs() +
                                                            symbols.shiftOptionMacOs()
                                                },
                                                symbols   : commandSelectedErgoEmacsSymbols,
                                                actions   : [],
                                                generator : { key, baseLayout -> baseLayout[key.value] }]),
             commandShiftSelectedErgoEmacs: lg([desc      : 'ErgoEmacs [command + shift + selected]',
                                                keymap    : commandShiftSelectedErgoEmacsKeymap,
                                                baseLayout: {
                                                    symbols.shiftUs() + symbols.shiftCommandMacOs() +
                                                            symbols.shiftOptionMacOs() + symbols.fnShiftMacOs()
                                                },
                                                symbols   : [:],
                                                actions   : [],
                                                generator : { key, baseLayout -> baseLayout[key.value] }])
            ]

    List mappingLayoutsOnModifiers = [[desc      : 'Remap keys to use Programming Dvorak Layout',
                                       layout    : [layout: prDvorakKeymap, symbols: symbols.prDvorak],
                                       baseLayout: [layout: usKeymap, symbols: symbols.us],
                                       modifiers : [mandatory: [], optional: []],
                                       conditions: [enLayoutCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [shift]',
                                       layout    : [layout: shiftPrDvorakKeymap, symbols: symbols.shiftPrDvorak],
                                       baseLayout: [layout: shiftUsKeymap, symbols: symbols.shiftUs],
                                       modifiers : [mandatory: [SHIFT], optional: []],
                                       conditions: [enLayoutCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [command]',
                                       layout    : [layout: commandErgoEmacsKeymap, symbols: symbols.commandErgoEmacs],
                                       baseLayout: [layout: commandMacOsKeymap, symbols: symbols.commandMacOs],
                                       modifiers : [mandatory: [COMMAND], optional: []],
                                       conditions: []],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [command+shift]',
                                       layout    : [layout: commandShiftErgoEmacsKeymap, symbols: symbols.commandShiftErgoEmacs],
                                       baseLayout: [layout: commandShiftMacOsKeymap, symbols: symbols.shiftCommandMacOs],
                                       modifiers : [mandatory: [COMMAND, SHIFT], optional: []],
                                       conditions: []],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [command+selected_mode]',
                                       layout    : [layout: commandSelectedErgoEmacsKeymap, symbols: symbols.commandSelectedErgoEmacs],
                                       baseLayout: [layout: emptyKeymap, symbols: { -> [:]}],
                                       modifiers : [mandatory: [COMMAND], optional: []],
                                       conditions: [selectedModeCondition]],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [command+shift+selected_mode]',
                                       layout    : [layout: commandShiftSelectedErgoEmacsKeymap, symbols: symbols.commandShiftSelectedErgoEmacs],
                                       baseLayout: [layout: emptyKeymap, symbols: { -> [:]}],
                                       modifiers : [mandatory: [COMMAND, SHIFT], optional: []],
                                       conditions: [selectedModeCondition]],
    ]

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

    def run() {

        List rules = []

        for (def mappingLayout : mappingLayoutsOnModifiers) {

            logger.info("Mapping description: $mappingLayout.desc")

            def baseLayout = mappingLayoutOnSymbols(mappingLayout.baseLayout.layout, mappingLayout.baseLayout.symbols)
            def targetLayout = mappingLayoutOnSymbols(mappingLayout.layout.layout, mappingLayout.layout.symbols)

//            println mappingLayout.layout.layout
//            println mappingLayout.layout.symbols()
//            println(symbols.shiftCommandMacOs() + symbols.shiftOptionMacOs() + symbols.shiftHardware())
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
}
