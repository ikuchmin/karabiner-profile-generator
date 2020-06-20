package ru.udya.layout.mixin

import ru.udya.layout.LayoutCondition

class DisableSelectionLayoutMixin implements LayoutMixin {

    @Override
    Map modify(Map karabinerStructure) {
        return karabinerStructure
    }
}
