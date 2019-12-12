package ru.udya.layout.impl.actionmacos

import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.impl.language.ProgrammingDvorakLayout

import javax.inject.Inject

import static ru.udya.layout.impl.LayoutHelper.remapLayoutOnNewBase

class ProgrammingDvorakCommandLayout implements Layout {

    @Delegate
    Layout programmingDvorakCommandLayout

    @Inject
    ProgrammingDvorakCommandLayout(EnUsCommandLayout enUsCommandLayout,
                                   ProgrammingDvorakLayout programmingDvorakLayout) {

        this.programmingDvorakCommandLayout =
                remapLayoutOnNewBase('programmingDvorakCommand',
                        enUsCommandLayout, programmingDvorakLayout,
                        [LayoutCondition.PROGRAMMING_DVORAK_LAYOUT_CONDITION])

    }
}
