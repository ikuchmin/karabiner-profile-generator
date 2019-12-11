package ru.udya.layout

import groovy.transform.Canonical

trait Layout {

    abstract String getName()

    abstract Map getKeymap()

    LayoutModifiers getModifiers() { [:] as LayoutModifiers}

    List<LayoutCondition> getLayoutCondition() { [] }

    Map<String, List<LayoutSymbol>> getSymbols() {

        Map symbols = [:]

        for (def key in getKeymap()) {

            if (key.value.empty) {
                continue
            }

            symbols << [(key.value): [[keyCode   : key.key,
                                       modifiers : getModifiers(),
                                       conditions: getLayoutCondition()] as LayoutSymbol]]
        }

        symbols
    }

}

@Canonical
class LayoutModifiers {

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

    List<Modifier> mandatory = []

    List<Modifier> optional = []
}

class LayoutCondition implements Map {

    static final LayoutCondition EN_US_LAYOUT_CONDITION =
            [condition: [type         : 'input_source_if',
                         input_sources: [[language       : 'en',
                                          input_source_id: 'com.apple.keylayout.US']]]] as LayoutCondition

    static final LayoutCondition PROGRAMMING_DVORAK_LAYOUT_CONDITION =
            [condition: [type         : 'input_source_if',
                         input_sources: [[language       : 'en',
                                          input_source_id: 'com.apple.keyboardlayout.Programmer Dvorak']]]] as LayoutCondition

    @Delegate
    Map condition
}


//@Canonical
class LayoutSymbol {

    String keyCode

    LayoutModifiers modifiers = []

    List<LayoutCondition> conditions = []
}