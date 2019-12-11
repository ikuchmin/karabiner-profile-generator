package ru.udya.layout.impl.actionmacos

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers
import ru.udya.layout.impl.language.ProgrammingDvorakLayout

import static ru.udya.layout.LayoutModifiers.Modifier.LEFT_COMMAND
import static ru.udya.layout.impl.LayoutHelper.remapKeymapOnNewBase

class ProgrammingDvorakCommandLayout implements Layout {

    @Override
    String getName() {
        return 'commandProgrammingDvorak'
    }

    @Override
    Map getKeymap() {
        return remapKeymapOnNewBase(EnUsCommandLayout.KEYMAP,
                ProgrammingDvorakLayout.KEYMAP)
    }

    @Override
    LayoutModifiers getModifiers() {
        return [mandatory: [LEFT_COMMAND], optional: []] as LayoutModifiers
    }

    @Override
    List<LayoutCondition> getLayoutCondition() {
        return [LayoutCondition.PROGRAMMING_DVORAK_LAYOUT_CONDITION]
    }
}
