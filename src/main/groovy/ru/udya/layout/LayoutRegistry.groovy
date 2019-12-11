package ru.udya.layout

import ru.udya.layout.impl.HardwareLayout
import ru.udya.layout.impl.actionmacos.EnUsCommandLayout
import ru.udya.layout.impl.actionmacos.ProgrammingDvorakCommandLayout
import ru.udya.layout.impl.ergoemacs.ErgoEmacsNavigationLayout
import ru.udya.layout.impl.language.EnUsLayout
import ru.udya.layout.impl.language.EnUsShiftLayout
import ru.udya.layout.impl.language.ProgrammingDvorakShiftLayout
import ru.udya.layout.impl.language.ProgrammingDvorakLayout

class LayoutRegistry {

    protected Map<Class, Layout> registryByClass = [:]
    protected Map<String, Layout> registryByName = [:]

    static LayoutRegistry defaultRegistry() {
        def layoutRegistry = new LayoutRegistry()

        layoutRegistry.register(new HardwareLayout())
        layoutRegistry.register(new EnUsLayout())
        layoutRegistry.register(new EnUsShiftLayout())
        layoutRegistry.register(new ProgrammingDvorakLayout())
        layoutRegistry.register(new ProgrammingDvorakShiftLayout())
        layoutRegistry.register(new EnUsCommandLayout())
        layoutRegistry.register(new ProgrammingDvorakCommandLayout())
        layoutRegistry.register(new ErgoEmacsNavigationLayout())
    }

    LayoutRegistry register(Layout layout) {
        registryByClass.put(layout.getClass(), layout)
        registryByName.put(layout.name, layout)

        return this
    }

    Layout findLayoutByClass(Class clazz) {
        return registryByClass.get(clazz)
    }

    Layout findLayoutByName(String name) {
        return registryByName.get(name)
    }
}
