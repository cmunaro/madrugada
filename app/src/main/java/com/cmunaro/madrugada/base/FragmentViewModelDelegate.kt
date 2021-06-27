package com.cmunaro.madrugada.base

import androidx.fragment.app.Fragment
import org.koin.core.context.GlobalContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

class FragmentViewModelDelegate<T : MadrugadaViewModel> : ReadOnlyProperty<Fragment, T> {
    private var viewModel: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        viewModel?.let { return it }
        val clazz: KClass<T> = property.getter.returnType.jvmErasure as KClass<T>
        viewModel = GlobalContext.get().get(clazz = clazz)
        return viewModel!!
    }
}