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

@SuppressWarnings("GrMethodMayBeStatic")
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

    Map hardwareKeymap = ['§': 'grave_accent_and_tilde', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': 'hyphen', '=': 'equal_sign', '⌫': 'delete_or_backspace',
                          '⇥': 'tab', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': 'open_bracket', ']': 'close_bracket', '↩': 'return_or_enter',
                          '⌘': 'command', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': 'semicolon', '\'': 'quote', '\\': 'backslash',
                          '⇧': 'shift', '⎋': 'escape', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': 'comma', '.': 'period', '/': 'slash', '⇧': 'shift',
                          'fn': 'fn', 'f16': 'f16', '⌃': 'control', '⌥': 'option', '**␣**': 'spacebar', '↩': 'return_or_enter', '⌥': 'option', '←': 'left_arrow', '↑': 'up_arrow', '↓': 'down_arrow', '→': 'right_arrow']

    Map usKeymap = ['§' : '`', '1': '1', '2': '2', '3': '3', '4': '4', '5': '5', '6': '6', '7': '7', '8': '8', '9': '9', '0': '0', '-': '-', '=': '=', '⌫': '',
                    '⇥' : '', 'q': 'q', 'w': 'w', 'e': 'e', 'r': 'r', 't': 't', 'y': 'y', 'u': 'u', 'i': 'i', 'o': 'o', 'p': 'p', '[': '[', ']': ']', '↩': '',
                    '⌘' : '', 'a': 'a', 's': 's', 'd': 'd', 'f': 'f', 'g': 'g', 'h': 'h', 'j': 'j', 'k': 'k', 'l': 'l', ';': ';', '\'': '\'', '\\': '\\',
                    '⇧' : '', '⎋': '', 'z': 'z', 'x': 'x', 'c': 'c', 'v': 'v', 'b': 'b', 'n': 'n', 'm': 'm', ',': ',', '.': '.', '/': '/',
                    'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map shiftUsKeymap = ['§' : '~', '1': '!', '2': '@', '3': '#', '4': '$', '5': '%', '6': '^', '7': '&', '8': '*', '9': '(', '0': ')', '-': '_', '=': '+', '⌫': '',
                         '⇥' : '', 'q': 'Q', 'w': 'W', 'e': 'E', 'r': 'R', 't': 'T', 'y': 'Y', 'u': 'U', 'i': 'I', 'o': 'O', 'p': 'P', '[': '{', ']': '}', '↩': '',
                         '⌘' : '', 'a': 'A', 's': 'S', 'd': 'D', 'f': 'F', 'g': 'G', 'h': 'H', 'j': 'J', 'k': 'K', 'l': 'L', ';': ':', '\'': '"', '\\': '|',
                         '⇧' : '', '⎋': '', 'z': 'Z', 'x': 'X', 'c': 'C', 'v': 'V', 'b': 'B', 'n': 'N', 'm': 'M', ',': '<', '.': '>', '/': '?',
                         'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map commandMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                              '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                              '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                              '⇧' : '', '⎋': '', 'z': 'undo', 'x': 'cut', 'c': 'copy', 'v': 'paste', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                              'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map shiftCommandMacOsKeymap = ['§' : '', '1': '', '2': '', '3': '', '4': '', '5': '', '6': '', '7': '', '8': '', '9': '', '0': '', '-': '', '=': '', '⌫': '',
                                   '⇥' : '', 'q': '', 'w': '', 'e': '', 'r': '', 't': '', 'y': '', 'u': '', 'i': '', 'o': '', 'p': '', '[': '', ']': '', '↩': '',
                                   '⌘' : '', 'a': '', 's': '', 'd': '', 'f': '', 'g': '', 'h': '', 'j': '', 'k': '', 'l': '', ';': '', '\'': '', '\\': '',
                                   '⇧' : '', '⎋': '', 'z': 'redo', 'x': '', 'c': '', 'v': '', 'b': '', 'n': '', 'm': '', ',': '', '.': '', '/': '',
                                   'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '←': '', '↑': '', '↓': '', '→': '']

    Map prDvorakKeymap = ['§' : '$', '1': '&', '2': '[', '3': '{', '4': '}', '5': '(', '6': '=', '7': '*', '8': ')', '9': '+', '0': ']', '-': '!', '=': '#', '⌫': '',
                          '⇥' : '', 'q': ';', 'w': ',', 'e': '.', 'r': 'p', 't': 'y', 'y': 'f', 'u': 'g', 'i': 'c', 'o': 'r', 'p': 'l', '[': '/', ']': '@', '↩': '',
                          '⌘' : '', 'a': 'a', 's': 'o', 'd': 'e', 'f': 'u', 'g': 'i', 'h': 'd', 'j': 'h', 'k': 't', 'l': 'n', ';': 's', '\'': '-', '\\': '\\',
                          '⇧' : '', '⎋': '', 'z': '\'', 'x': 'q', 'c': 'j', 'v': 'k', 'b': 'x', 'n': 'b', 'm': 'm', ',': 'w', '.': 'v', '/': 'z',
                          'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']

    Map shiftPrDvorakKeymap = ['§': '~', '1': '%', '2': '7', '3': '5', '4': '3', '5': '1', '6': '9', '7': '0', '8': '2', '9': '4', '0': '6', '-': '8', '=': '`', '⌫': '',
                               '⇥':'', 'q': ':', 'w': '<', 'e': '>', 'r': 'P', 't': 'Y', 'y': 'F', 'u': 'G', 'i': 'C', 'o': 'R', 'p': 'L', '[': '?', ']': '^', '↩': '',
                               '⌘':'', 'a': 'A', 's': 'O', 'd': 'E', 'f': 'U', 'g': 'I', 'h': 'D', 'j': 'H', 'k': 'T', 'l': 'N', ';': 'S', '\'': '_', '\\': '|',
                               '⇧':'', '⎋': '', 'z': '"', 'x': 'Q', 'c': 'J', 'v': 'K', 'b': 'X', 'n': 'B', 'm': 'M', ',': 'W', '.': 'V', '/': 'Z',
                               'fn': '', 'f16': '', '⌃': '', '⌥': '', '**␣**': '', '↩': '', '⌥': '', '←': '', '↑': '', '↓': '', '→': '']

    Map optionErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                 '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'up_arrow', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                 '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'home', 'j':'left_arrow', 'k':'down_arrow', 'l':'right_arrow', ';':'', '\'':'', '\\':'',
                                 '⇧':'', '⎋':'', 'z':'undo', 'x':'cut', 'c':'copy', 'v':'paste', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                 'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map optionShiftErgoEmacsKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'',
                                      '⇥':'', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'',
                                      '⌘':'', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'end', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                      '⇧':'', '⎋':'', 'z':'redo', 'x':'cut', 'c':'copy', 'v':'paste', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                      'fn':'', 'f16':'', '⌃':'', '⌥':'', '**␣**':'', '←':'', '↑':'', '↓':'', '→':'']

    Map commandErgoEmacsLayout = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'delete_or_backspace',
                                  '⇥':'tab', 'q':'', 'w':'', 'e':'', 'r':'', 't':'', 'y':'', 'u':'', 'i':'', 'o':'', 'p':'', '[':'', ']':'', '↩':'return_or_enter',
                                  '⌘':'command', 'a':'', 's':'', 'd':'', 'f':'', 'g':'', 'h':'', 'j':'', 'k':'', 'l':'', ';':'', '\'':'', '\\':'',
                                  '⇧':'shift', '⎋':'escape', 'z':'', 'x':'', 'c':'', 'v':'', 'b':'', 'n':'', 'm':'', ',':'', '.':'', '/':'',
                                  'fn':'fn', 'f16':'f16', '⌃':'control', '⌥':'option', '**␣**':'spacebar', '←':'left_arrow', '↑':'up_arrow', '↓':'down_arrow', '→':'right_arrow']

    Map emptyKeymap = ['§':'', '1':'', '2':'', '3':'', '4':'', '5':'', '6':'', '7':'', '8':'', '9':'', '0':'', '-':'', '=':'', '⌫':'delete_or_backspace',
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

    Map optionErgoEmacsSymbols = ['cut'  : [[key_code: 'x', modifiers: ['command']], disableSelectedModeAction],
                                  'copy' : [[key_code: 'c', modifiers: ['command']], [key_code: 'escape'], disableSelectedModeAction],
                                  'paste': [[key_code: 'v', modifiers: ['command']], disableSelectedModeAction]]

    Map optionShiftErgoEmacsSymbols = ['cut'  : [[key_code: 'x', modifiers: ['command']], disableSelectedModeAction],
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

    Map symbols = [hardware       : lg([desc      : 'Hardware',
                                        keymap    : hardwareKeymap,
                                        baseLayout: { hardwareKeymap },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: []]] : null
                                        }]),

                   shiftHardware  : lg([desc      : 'Hardware [shift]',
                                        keymap    : hardwareKeymap,
                                        baseLayout: { hardwareKeymap },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: [SHIFT]]] : null
                                        }]),

                   us             : lg([desc      : 'U.S.',
                                        keymap    : usKeymap,
                                        baseLayout: { hardwareKeymap },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: []]] : null
                                        }]),

                   shiftUs        : lg([desc      : 'U.S. [shift]',
                                        keymap    : shiftUsKeymap,
                                        baseLayout: { hardwareKeymap },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: [SHIFT]]] : null
                                        }]),

                   commandMacOs   : lg([desc      : 'macOs [command]',
                                        keymap    : commandMacOsKeymap,
                                        baseLayout: { usKeymap },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: [COMMAND]]] : null
                                        }]),

                   shiftCommandMacOs   : lg([desc      : 'macOs [command+shift]',
                                        keymap    : shiftCommandMacOsKeymap,
                                        baseLayout: { usKeymap },
                                        symbols   : [:],
                                        actions   : [],
                                        generator : { key, baseLayout ->
                                            baseLayout[key.key] != null ? [[key_code : baseLayout[key.key],
                                                                            modifiers: [COMMAND, SHIFT]]] : null
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
                                        baseLayout: { symbols.commandMacOs() + symbols.hardware() },
                                        symbols   : optionErgoEmacsSymbols,
                                        actions   : [],
                                        generator : { key, baseLayout -> baseLayout[key.value] }]),

                   optionShiftErgoEmacs: lg([desc      : 'ErgoEmacs [option]',
                                        keymap    : optionShiftErgoEmacsKeymap,
                                        baseLayout: { symbols.shiftCommandMacOs() + symbols.shiftHardware() },
                                        symbols   : optionShiftErgoEmacsSymbols,
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
                                       baseLayout: [layout: emptyKeymap, symbols: { [:] }],
                                       modifiers : [mandatory: [OPTION], optional: []],
                                       conditions: []],

                                      [desc      : 'Remap keys to use Programming Dvorak Layout [option+shift]',
                                       layout    : [layout: optionShiftErgoEmacsKeymap, symbols: symbols.optionShiftErgoEmacs],
                                       baseLayout: [layout: emptyKeymap, symbols: { [:] }],
                                       modifiers : [mandatory: [OPTION, SHIFT], optional: []],
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