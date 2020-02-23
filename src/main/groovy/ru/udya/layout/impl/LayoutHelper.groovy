package ru.udya.layout.impl

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition

class LayoutHelper {


    /**
     * Makes new layout based on another
     *
     * @param newNameLayout
     * @param sourceLayout
     * @param newBaseLayout
     * @param conditions
     * @return
     */
    static Layout remapLayoutOnNewBase(String newNameLayout,
                                       Layout sourceLayout, Layout newBaseLayout,
                                       List<LayoutCondition> conditions) {

        Map newKeymap = remapKeymapOnNewBase(sourceLayout.keymap, newBaseLayout.keymap)

        return [name: newNameLayout, modifiers: newBaseLayout.modifiers,
                keymap: newKeymap, conditions: conditions] as DefaultLayout
    }

    /**
     * Makes keymap which map actions in {@param sourceKeymap}
     * on {@param targetKeymap}. For example, in US keymap
     * action "copy" is mapped on "ctrl+c", but in Dvorak
     * the action is mapped on "ctrl+i" (just to try pressing on keyboard).
     * It is default behaviour for macOS.
     *
     * The method realizes that "copy" is the key "c" in
     * sourceKeymap, and "c" is the key "i" in {@param targetKeymap}.
     * The result of generation is [i: "copy"]
     *
     * @param sourceKeymap
     * @param baseKeymap
     * @return
     */
    static Map remapKeymapOnNewBase(Map sourceKeymap, Map baseKeymap) {

        Map genKeymap = [:]

        for (tkv in baseKeymap) {

            def targetValue = sourceKeymap[tkv.value] ?: ''

            genKeymap.put(tkv.key, targetValue)
        }
//        def doTheSame, difference = doNotKeymapsHaveTheSameActions(sourceKeymap, genKeymap)
//
//        if ()
//            logger.warning('Generator finds some source actions which can not be mapped on target. Source key: {}', skv)

        genKeymap
    }

    protected static Map doNotKeymapsHaveTheSameActions(Map sourceKeymap, Map genKeymap) {
        def sourceNotEmpty = sourceKeymap.findAll { kv -> ! kv.value.empty}
                .sort { kv1, kv2 ->  kv}
        def genNotEmpty = genKeymap.findAll { kv -> ! kv.value.empty}
    }
}
