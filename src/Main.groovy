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

class Main extends Script {

    enum Modifier {
        SHIFT, COMMAND, OPTION, CONTROL, FN, F16

        @Override
        String toString() {
            return super.toString().toLowerCase()
        }
    }

    Logger logger = Logger.getLogger(Main.getName())

    List controlKeys = ['⌫','⇥','↩', '⌘','⇧', '⎋', 'fn', 'f16', '⌃', '⌥', '**␣**', '↩', '⌥', '←', '↑', '↓', '→']

    Map keyboardLayout = ['§': 'grave_accent_and_tilde', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': 'hyphen', '=': 'equal_sign', '⌫': 'delete_or_backspace',
                          '⇥': 'tab', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': 'open_bracket', ']': 'close_bracket', '↩': 'return_or_enter',
                          '⌘': 'command', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': 'semicolon', '\'': 'quote', '\\': 'backslash',
                          '⇧': 'shift', '⎋': 'escape', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': 'comma', '.': 'period', '/': 'slash', '⇧': 'shift',
                          'fn': 'fn', 'f16': 'f16', '⌃': 'control', '⌥': 'option', '**␣**': 'spacebar', '↩': 'return_or_enter', '⌥': 'option', '←': 'left_arrow', '↑': 'up_arrow', '↓': 'down_arrow', '→': 'right_arrow']


    Map usKeymap = ['§' : '`', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': '-', '=': '=', '⌫': '',
                    '⇥' : '', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': '[', ']': ']', '↩': '',
                    '⌘' : '', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': ';', '\'': '\'', '\\': '\\',
                    '⇧' : '', '⎋': '', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': ',', '.': '.', '/': '/',
                    'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']
    Map usLayoutSymbols = usKeymap.collect { k, v -> [(v): [[key_code: keyboardLayout[k], modifiers: []]]]}.inject([:]) { v, acc -> acc << v}

    Map shiftUsKeymap = ['§' : '~', '1': '!', '2': '@', '3': '#', '4': '$', '5': '%', '6': '^', '7': '&', '8': '*', '9': '(', '0': ')', '-': '_', '=': '+', '⌫': '',
                         '⇥' : '', 'q': 'Q', 'w': 'W', 'e': 'E', 'r': 'R', 't': 'T', 'y': 'Y', 'u': 'U', 'i': 'I', 'o': 'O', 'p': 'P', '[': '{', ']': '}', '↩': '',
                         '⌘' : '', 'a': 'A', 's': 'S', 'd': 'D', 'f': 'F', 'g': 'G', 'h': 'H', 'j': 'J', 'k': 'K', 'l': 'L', ';': ':', '\'': '"', '\\': '|',
                         '⇧' : '', '⎋': '', 'z': 'Z', 'x': 'X', 'c': 'C', 'v': 'V', 'b': 'B', 'n': 'N', 'm': 'M', ',': '<', '.': '>', '/': '?',
                         'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']
    Map shiftUsLayoutSymbols = shiftUsKeymap.collect { k, v -> [(v): [[key_code: keyboardLayout[k], modifiers: [SHIFT]]]]}.inject([:]) { v, acc -> acc << v}

    Map allUsLayoutSymbols = usLayoutSymbols + shiftUsLayoutSymbols

    Map commandMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                              '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                              '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                              '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']
    Map commandDefaultLayoutSymbols = commandMacOsKeymap.findAll { k, v -> ! v.empty}
            .collect {k, v -> [(v): [[key_code: keyboardLayout[k], modifiers: [COMMAND]]]]}.inject([:]) { v, acc -> acc << v}

    Map prDvorakKeymap = ['§' : '$', '1': '&', '2': '[', '3': '{', '4': '}', '5': '(', '6': '=', '7': '*', '8': ')', '9': '+', '0': ']', '-': '!', '=': '#', '⌫': '',
                          '⇥' : '', 'q': ';', 'w': ',', 'e': '.', 'r': 'p', 't': 'y', 'y': 'f', 'u': 'g', 'i': 'c', 'o': 'r', 'p': 'l', '[': '/', ']': '@', '↩': '',
                          '⌘' : '', 'a': 'a', 's': 'o', 'd': 'e', 'f': 'u', 'g': 'i', 'h': 'd', 'j': 'h', 'k': 't', 'l': 'n', ';': 's', '\'': '-', '\\': '\\',
                          '⇧' : '', '⎋': '', 'z': '\'', 'x': 'q', 'c': 'j', 'v': 'k', 'b': 'x', 'n': 'b', 'm': 'm', ',': 'w', '.': 'v', '/': 'z',
                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']
    Map prDvLayoutSymbols = prDvorakKeymap.findAll { k, v -> ! v.empty }
            .collect {k, v -> [(v): allUsLayoutSymbols[v]]}.inject([:]) {v, acc -> acc << v}

    Map shiftPrDvorakKeymap = ['§': '~', '1': '%', '2': '7', '3': '5', '4': '3', '5': '1', '6': '9', '7': '0', '8': '2', '9': '4', '0': '6', '-': '8', '=': '`', '⌫': '',
                               '⇥':'', 'q': ':', 'w': '<', 'e': '>', 'r': 'P', 't': 'Y', 'y': 'F', 'u': 'G', 'i': 'C', 'o': 'R', 'p': 'L', '[': '?', ']': '^', '↩': '',
                               '⌘':'', 'a': 'A', 's': 'O', 'd': 'E', 'f': 'U', 'g': 'I', 'h': 'D', 'j': 'H', 'k': 'T', 'l': 'N', ';': 'S', '\'': '_', '\\': '|',
                               '⇧':'', '⎋': '', 'z': '"', 'x': 'Q', 'c': 'J', 'v': 'K', 'b': 'X', 'n': 'B', 'm': 'M', ',': 'W', '.': 'V', '/': 'Z',
                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']
    Map shiftPrDvLayoutSymbols = shiftUsKeymap.findAll { k, v -> ! v.empty }
            .collect {k, v -> [(v): allUsLayoutSymbols[v]]}.inject([:]) {v, acc -> acc << v}

    Map optionErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                 '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'up_arrow', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                 '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'home', 'j':'left_arrow', 'k':'down_arrow', 'l':'right_arrow', ';':'', '\'':'', '\\':'',
                                 '⇧':'', '⎋':'', 'z':'undo', 'x':'cut', 'c':'copy', 'v':'paste', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                 'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map optionShiftCommonLayout = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                   '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                   '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'end', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                   '⇧':'', '⎋':'', 'z':'undo', 'x':'cut', 'c':'copy', 'v':'paste', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                   'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map commandCommonLayout = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'delete_or_backspace',
                               '⇥':'tab', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'return_or_enter',
                               '⌘':'command', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                               '⇧':'shift', '⎋':'escape', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                               'fn':'fn', 'f16':'f16', '⌃':'control', '⌥':'option', '**␣**':'spacebar', '←':'left_arrow', '↑':'up_arrow', '↓':'down_arrow', '→':'right_arrow']


    Map emptyLayout = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'delete_or_backspace',
                       '⇥':'tab', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'return_or_enter',
                       '⌘':'command', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                       '⇧':'shift', '⎋':'escape', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                       'fn':'fn', 'f16':'f16', '⌃':'control', '⌥':'option', '**␣**':'spacebar', '←':'left_arrow', '↑':'up_arrow', '↓':'down_arrow', '→':'right_arrow']

    // conditions
    def enLayoutCondition = [type: 'input_source_if', input_sources: [[language: 'en']]]
    def excludedApplicationCondition = []

    // actions
    def disableSelectedModeAction = [set_variable: [name: 'selected_mode', value: 0]]
    def enableSelectedModeAction = [set_variable: [name: 'selected_mode', value: 1]]

    Map prDvSymbols = ['$' : [[key_code: keyboardLayout.'4', modifiers: [SHIFT]]],
                       '&' : [[key_code: keyboardLayout.'7', modifiers: [SHIFT]]],
                       '[' : [[key_code: 'open_bracket', modifiers: []]],
                       '{' : [[key_code: 'open_bracket', modifiers: [SHIFT]]],
                       '}' : [[key_code: 'close_bracket', modifiers: [SHIFT]]],
                       '(' : [[key_code: '9', modifiers: [SHIFT]]],
                       '=' : [[key_code: 'equal_sign', modifiers: []]],
                       '*' : [[key_code: '8', modifiers: [SHIFT]]],
                       ')' : [[key_code: '0', modifiers: [SHIFT]]],
                       '+' : [[key_code: 'equal_sign', modifiers: [SHIFT]]],
                       ']' : [[key_code: 'close_bracket', modifiers: []]],
                       '!' : [[key_code: '1', modifiers: [SHIFT]]],
                       '#' : [[key_code: '3', modifiers: [SHIFT]]],
                       ';' : [[key_code: 'semicolon', modifiers: []]],
                       ',' : [[key_code: 'comma', modifiers: []]],
                       '.' : [[key_code: 'period', modifiers: []]],
                       '/' : [[key_code: 'slash', modifiers: []]],
                       '@' : [[key_code: '2', modifiers: [SHIFT]]],
                       '-' : [[key_code: 'hyphen', modifiers: []]],
                       '\\': [[key_code: 'backslash', modifiers: []]],
                       '\'': [[key_code: 'quote', modifiers: []]],
                       '~' : [[key_code: 'grave_accent_and_tilde', modifiers: [SHIFT]]],
                       '%' : [[key_code: '5', modifiers: [SHIFT]]],
                       '`' : [[key_code: 'grave_accent_and_tilde', modifiers: []]],
                       ':' : [[key_code: 'semicolon', modifiers: [SHIFT]]],
                       '<' : [[key_code: 'comma', modifiers: [SHIFT]]],
                       '>' : [[key_code: 'period', modifiers: [SHIFT]]],
                       '?' : [[key_code: 'slash', modifiers: [SHIFT]]],
                       '^' : [[key_code: '6', modifiers: [SHIFT]]],
                       '_' : [[key_code: 'hyphen', modifiers: [SHIFT]]],
                       '|' : [[key_code: 'backslash', modifiers: [SHIFT]]],
                       '"' : [[key_code: 'quote', modifiers: [SHIFT]]]]

    Map prDvLowerCharacters = ('a'..'z').collect { key -> [(key.toLowerCase()): [[key_code: key, modifiers: []]]]}
            .inject([:]) { v, acc -> acc << v}

    Map prDvUpperCharacters = ('a'..'z').collect { key -> [(key.toUpperCase()): [[key_code: key, modifiers: [SHIFT]]]]}
            .inject([:]) { v, acc -> acc << v}

    Map prDvNumbers = (0..9).collect(String.&valueOf)
            .collect { key -> [(key): [[key_code: key, modifiers: []]]]}
            .inject([:], { v, acc -> acc << v})

    Map prDvActions = ['undo' : [[key_code: 'z', modifiers: ['command']], disableSelectedModeAction],
                       'cut'  : [[key_code: 'x', modifiers: ['command']], disableSelectedModeAction],
                       'copy' : [[key_code: 'c', modifiers: ['command']], [key_code: 'escape'], disableSelectedModeAction],
                       'paste': [[key_code: 'p', modifiers: ['command']], disableSelectedModeAction]]

    Map signs = prDvSymbols + prDvLowerCharacters + prDvUpperCharacters + prDvNumbers + prDvActions

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

    Map symbols = [us             : lg([desc      : 'U.S.',
                                        keymap    : usKeymap,
                                        baseLayout: { keyboardLayout },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: []]] : null
                                        }]),

                   shiftUs        : lg([desc      : 'U.S. [shift]',
                                        keymap    : shiftUsKeymap,
                                        baseLayout: { keyboardLayout },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: [SHIFT]]] : null
                                        }]),

                   commandMacOs   : lg([desc      : 'macOs [command]',
                                        keymap    : commandMacOsKeymap,
                                        baseLayout: { symbols.us() },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: [COMMAND]]] : null
                                        }]),

                   prDvorak       : lg([desc      : 'Programming Dvorak',
                                        keymap    : prDvorakKeymap,
                                        baseLayout: { symbols.us() + symbols.shiftUs() },
                                        symbols   : [:],
                                        actions   : [disableSelectedModeAction],
                                        generator : { key, baseLayout -> baseLayout[key.value] }]),

                   shiftPrDvorak  : lg([desc      : 'Programming Dvorak [shift]',
                                        keymap    : shiftPrDvorakKeymap,
                                        baseLayout: { symbols.us() + symbols.shiftUs() },
                                        symbols   : [:],
                                        actions   : [disableSelectedModeAction],
                                        generator : { key, baseLayout -> baseLayout[key.value] }]),

                   optionErgoEmacs: lg([desc      : 'ErgoEmacs [option]',
                                        keymap    : optionErgoEmacsKeymap,
                                        baseLayout: { symbols.commandMacOs() },
                                        symbols   : prDvActions,
                                        actions   : [],
                                        generator : { key, baseLayout -> baseLayout[key.value] }])]

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

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [option]',
                                       layout    : [layout: optionErgoEmacsKeymap, symbols: symbols.optionErgoEmacs],
                                       baseLayout: [layout: commandMacOsKeymap, symbols: symbols.commandMacOs],
                                       modifiers : [mandatory: [OPTION], optional: []],
                                       conditions: []]
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

            result << [(layoutKey.key): keySeq]
        }

        result
    }

    def run() {

        List rules = []

        for (def mappingLayout : mappingLayoutsOnModifiers) {

            def baseLayout = mappingLayoutOnSymbols(mappingLayout.baseLayout.layout, mappingLayout.baseLayout.symbols)
            def targetLayout = mappingLayoutOnSymbols(mappingLayout.layout.layout, mappingLayout.layout.symbols)

            // generate manipulators
            def manipulators = []
            for (def layoutKey in targetLayout) {

                // filter keys with empty target key sequence
                if (layoutKey.value.empty) {
                    logger.warning("Key sequence ($layoutKey) in the target layout is empty")
                    continue
                }

                // filter keys which have the same sequence in base
                if (baseLayout[layoutKey.key] == layoutKey.value) {
                    logger.info("Key sequence in target and base layouts are equal (base: ${baseLayout[layoutKey.key]}, target: $layoutKey.value")
                    continue
                }

                def fromKeySeq = createFromKeySeq(keyboardLayout[layoutKey.key],
                        mappingLayout.modifiers.mandatory, mappingLayout.modifiers.optional)
                def toKeySeq = layoutKey.value

                manipulators << createManipulator(fromKeySeq, toKeySeq, mappingLayout.conditions)
            }

            rules << [description: mappingLayout.desc, manipulators: manipulators]
        }

//
//        def basicPrDvManipulators = []
//
//        // create basic Programming Dvorak Layout
//        for (int i = 0; i < keyboardLayout.size(); i++) {
//            def usLayoutSeq = createFromKeySeq(keyboardLayout[i], [], [])
//            def prDvLayoutSeq = createToKeySeq(prDvSymbols.get(prDvorakKeymap[i], prDvorakKeymap[i]), [])
//
//            basicPrDvManipulators << createManipulator(usLayoutSeq, [prDvLayoutSeq, disableSelectedModeAction], [enLayoutCondition])
//        }
//
//        // create basic Shift Programming Dvorak Layout
//        for (int i = 0; i < keyboardLayout.size(); i++) {
//            def usLayoutSeq = createFromKeySeq(keyboardLayout[i], ['shift'], [])
//            def shiftPrDvLayoutSeq = createToKeySeq(prDvSymbols.get(shiftPrDvorakKeymap[i], shiftPrDvorakKeymap[i]), ['shift'])
//
//            basicPrDvManipulators << createManipulator(
//                    usLayoutSeq, [shiftPrDvLayoutSeq, disableSelectedModeAction], [enLayoutCondition])
//        }
//
//        // create Option Programming Dvorak Layout
//        for (int i = 0; i < keyboardLayout.size(); i++) {
//
//            if (optionPrDvLayout[i].empty) {
//                continue
//            }
//
//            def usLayoutSeq = createFromKeySeq(keyboardLayout[i], ['option'], [])
//            def optionPrDvLayoutSeq = createToKeySeq(prDvSymbols.get(optionPrDvLayout[i], optionPrDvLayout[i]), ['option'])
//
//            basicPrDvManipulators << createManipulator(
//                    usLayoutSeq, [optionPrDvLayoutSeq, disableSelectedModeAction], [])
//        }

        def karabinerComplexLayout =
                [title: 'Dvorak Keyboards', rules: rules]

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
        mapper.writeValue(System.out, karabinerComplexLayout)
//        println JsonOutput.prettyPrint(JsonOutput.toJson(karabinerComplexLayout))
    }
}