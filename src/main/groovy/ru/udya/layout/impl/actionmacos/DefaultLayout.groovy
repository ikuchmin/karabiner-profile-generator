package ru.udya.layout.impl.actionmacos

import groovy.transform.Canonical
import ru.udya.layout.Layout
import ru.udya.layout.LayoutCondition
import ru.udya.layout.LayoutModifiers

@Canonical
class DefaultLayout implements Layout {

    String name

    Map keymap

    LayoutModifiers modifiers

    List<LayoutCondition> conditions
}
