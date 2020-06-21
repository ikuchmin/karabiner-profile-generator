package ru.udya

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.inject.Guice
import com.google.inject.Injector
import groovy.transform.Canonical
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.udya.layout.Layout
import ru.udya.layout.LayoutModule

import ru.udya.layout.LayoutSymbol
import ru.udya.layout.impl.HardwareLayout
import ru.udya.layout.impl.en_us.EnUsLayout
import ru.udya.layout.impl.en_us.EnUsShiftLayout
import ru.udya.layout.impl.ergoemacs.action.ErgoEmacsCommandLayout
import ru.udya.layout.impl.ergoemacs.action.ErgoEmacsOptionLayout
import ru.udya.layout.impl.firefox.FirefoxLayout
import ru.udya.layout.impl.intellij.IntelliJCommandLayout
import ru.udya.layout.impl.programming_dvorak.ProgrammingDvorakShiftLayout
import ru.udya.layout.impl.programming_dvorak.ProgrammingDvorakLayout
import ru.udya.layout.impl.programming_dvorak.action.ProgrammingDvorakCommandLayout
import ru.udya.layout.mixin.DisableSelectionLayoutMixin
import ru.udya.layout.mixin.LayoutMixin

import javax.inject.Inject

@Canonical
class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application);

    static List MAPPING_RULES = [
            [desc        : 'Remap keys to use Programming Dvorak Layout is covered by enUs',
             targetLayout: ProgrammingDvorakLayout,
             baseLayouts : [EnUsLayout, EnUsShiftLayout],
             mixins      : [DisableSelectionLayoutMixin]],

            [desc        : 'Remap keys to use Programming Dvorak Layout is covered by enUs [shift]',
             targetLayout: ProgrammingDvorakShiftLayout,
             baseLayouts : [EnUsLayout, EnUsShiftLayout]],

            [desc        : 'Remap keys to use ErgoEmacs Layout for Firefox',
             targetLayout: ErgoEmacsCommandLayout,
             baseLayouts : [FirefoxLayout]],

            [desc        : 'Remap keys to use ErgoEmacs Layout for IntelliJ IDEA',
             targetLayout: ErgoEmacsCommandLayout,
             baseLayouts : [IntelliJCommandLayout]],

//            [desc        : 'Remap keys to use ErgoEmacs Layout is covered by Programming Dvorak [action]',
//             targetLayout: EnUsCommandLayout,
//             baseLayouts : [ProgrammingDvorakCommandLayout]],

            [desc        : 'Remap keys to use ErgoEmacs Layout is covered by Programming Dvorak [navigation]',
             targetLayout: ErgoEmacsOptionLayout,
             baseLayouts : [ProgrammingDvorakCommandLayout]]]

    Injector injector

    @Inject
    Application(Injector injector) {
        this.injector = injector
    }

    static void main(String[] args) {

        Injector injector = Guice.createInjector(new LayoutModule())

        injector.getInstance(Application).run()
    }

    void run() {
        List rules = []

        for (Map rule : MAPPING_RULES) {

            logger.info('Mapping description: {}', rule.desc)

            Map<String, List<LayoutSymbol>> baseSymbols = rule.baseLayouts
                    .collect { injector.getInstance(it).getSymbols() }
                    .inject { acc, val -> acc + val }

            Layout targetLayout = injector.getInstance(rule.targetLayout)
            Map<String, List<LayoutSymbol>> targetSymbols = targetLayout.getSymbols()

            List manipulators = targetLayout.keymap
                    .findAll { !it.value.empty }
                    .collect { createManipulator(it, targetSymbols, baseSymbols, rule.mixins ?: []) }
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
                          Map<String, List<LayoutSymbol>> baseSymbols,
                          List<LayoutMixin> layoutMixins) {


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
        def conditionKarabinerStructure = toKeySymbol.conditions

        def karabinerStructure = createManipulatorStructure(fromKarabinerStructure,
                toKarabinerStructure, conditionKarabinerStructure)

        if (! layoutMixins.empty) {

            layoutMixins.each { m ->
                def instance = injector.getInstance(m)
                karabinerStructure = instance.modify(karabinerStructure)
            }
        }

        return karabinerStructure
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
        def hardwareLayout = injector.getInstance(HardwareLayout)
        return hardwareLayout.keymap[keyCode]
    }
}