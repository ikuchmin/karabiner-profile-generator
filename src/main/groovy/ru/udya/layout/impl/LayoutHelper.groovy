package ru.udya.layout.impl

class LayoutHelper {

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
    static Map remapKeymapOnNewBase(Map sourceKeymap, Map targetKeymap) {

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

    protected static Map doNotKeymapsHaveTheSameActions(Map sourceKeymap, Map genKeymap) {
        def sourceNotEmpty = sourceKeymap.findAll { kv -> ! kv.value.empty}
                .sort { kv1, kv2 ->  kv}
        def genNotEmpty = genKeymap.findAll { kv -> ! kv.value.empty}
    }
}
