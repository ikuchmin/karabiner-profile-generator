package ru.udya

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.transform.Canonical
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutRegistry
import ru.udya.layout.LayoutSymbol
import ru.udya.layout.impl.actionmacos.EnUsCommandLayout
import ru.udya.layout.impl.actionmacos.ProgrammingDvorakCommandLayout
import ru.udya.layout.impl.ergoemacs.ErgoEmacsNavigationLayout
import ru.udya.layout.impl.language.EnUsLayout
import ru.udya.layout.impl.language.EnUsShiftLayout
import ru.udya.layout.impl.language.ProgrammingDvorakShiftLayout
import ru.udya.layout.impl.language.ProgrammingDvorakLayout

@Canonical
class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application);


    static List MAPPING_RULES = [
            [desc        : 'Remap keys to use Programming Dvorak Layout is covered by enUs',
             targetLayout: ProgrammingDvorakLayout,
             baseLayouts : [EnUsLayout]],

            [desc        : 'Remap keys to use Programming Dvorak Layout is covered by enUs [shift]',
             targetLayout: ProgrammingDvorakShiftLayout,
             baseLayouts : [EnUsShiftLayout]],

//            [desc        : 'Remap keys to use ErgoEmacs Layout is covered by Programming Dvorak [action]',
//             targetLayout: EnUsCommandLayout,
//             baseLayouts : [ProgrammingDvorakCommandLayout]],

            [desc      : 'Remap keys to use ErgoEmacs Layout is covered by Programming Dvorak [navigation]',
             targetLayout    : ErgoEmacsNavigationLayout,
             baseLayouts: [ProgrammingDvorakCommandLayout]]]

    LayoutRegistry layoutRegistry;

    static void main(String[] args) {
        LayoutRegistry layoutRegistry = LayoutRegistry.defaultRegistry()

        new Application(layoutRegistry).run()
    }

    void run() {
        List rules = []

        for (Map rule : MAPPING_RULES) {

            logger.info('Mapping description: {}', rule.desc)

            Map<String, List<LayoutSymbol>> baseSymbols = rule.baseLayouts
                    .collect { layoutRegistry.findLayoutByClass(rule.baseLayouts).getSymbols() }
                    .inject { acc, val -> acc + val }

            Layout targetLayout = layoutRegistry.findLayoutByClass(rule.targetLayout)
            Map<String, List<LayoutSymbol>> targetSymbols = targetLayout.getSymbols()

            List manipulators = targetLayout.keymap
                    .findAll { !it.value.empty }
                    .collect { createManipulator(it, targetSymbols, baseSymbols) }
                    .findAll { it != Collections.emptyMap() }

            rules << [description: rule.desc, manipulators: manipulators]
        }

        def karabinerComplexLayout = [title: 'Dvorak Keyboards', rules: rules]

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
        mapper.writeValue(System.out, karabinerComplexLayout)
    }

    Map createManipulator(def targetKey,
                          Map<String, List<LayoutSymbol>> targetSymbols,
                          Map<String, List<LayoutSymbol>> baseSymbols) {


        def fromKeySymbol = targetSymbols[targetKey.value]

        if (fromKeySymbol == null) {
            logger.error('Something is going wrong, because the app does not find targetKey ({}) in targetSymbols ({})', targetKey, targetSymbols)
            return [:]
        }

        if (fromKeySymbol.empty || fromKeySymbol.size > 1) {
            logger.error('From key symbol ({}) must not be empty or more then one key code', targetKey)
            return [:]
        }

        def toKeySymbol = baseSymbols[targetKey.value]

        if (toKeySymbol == null) {
            logger.warn('Key sequence ({}) does not exist in bases layouts', targetKey)
            return [:]
        }

        if (toKeySymbol.empty) {
            logger.warn('To key symbol ({}) must not be empty', targetKey)
            return [:]
        }

        def fromKarabinerStructure = createFromKarabinerStructure(fromKeySymbol)
        def toKarabinerStructure = createToKarabinerStructure(toKeySymbol)

        return createManipulatorStructure(fromKarabinerStructure,
                toKarabinerStructure, toKeySymbol.conditions)
    }

    def createFromKarabinerStructure(List<LayoutSymbol> symbol) {
        return [key_code : escapeHardware(symbol[0].keyCode),
                modifiers: [mandatory: symbol[0].modifiers.mandatory,
                            optional: symbol[0].modifiers.optional]]
    }

    /**
     * The method supports only one symbol in row. For
     * the future the method can support more the one
     * symbol in a row
     *
     * @param symbol
     * @return
     */
    def createToKarabinerStructure(List<LayoutSymbol> symbol) {
        return [[key_code : escapeHardware(symbol[0].keyCode),
                 modifiers: (symbol[0].modifiers.mandatory + symbol[0].modifiers.optional)]]
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    def createManipulatorStructure(def fromKarabinerStructure,
                                   def toKarabinerStructure,
                                   def conditions) {
        return [type: 'basic', from: fromKarabinerStructure, to: toKarabinerStructure, conditions: conditions[0]]
    }

    def escapeHardware(def keyCode) {
        def hardwareLayout = layoutRegistry.findLayoutByName('hardware')
        return hardwareLayout.keymap[keyCode]
    }
}