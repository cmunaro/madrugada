package com.cmunaro.madrugada.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cmunaro.madrugada.BR
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewDataBinding>(
    bindingClass: Class<T>,
    private val fragment: Fragment,
    private val viewModel: MadrugadaViewModel<*>? = null
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null
    private val bindMethod = bindingClass.getMethod("bind", View::class.java)

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (binding != null && binding?.root != thisRef.view) {
            binding = null
        }
        binding?.let { return it }

        @Suppress("UNCHECKED_CAST")
        binding = bindMethod.invoke(null, thisRef.requireView()) as T
        binding?.lifecycleOwner = fragment.viewLifecycleOwner
        viewModel?.let { injectViewModel(it, binding) }
        return binding!!
    }

    private fun injectViewModel(viewModel: MadrugadaViewModel<*>, binding: T?) {
        binding ?: return
        val setVariableMethod = binding::class.java
            .methods
            .firstOrNull { it.name == "setVariable" }?: return
        setVariableMethod(binding, BR.viewModel, viewModel)
    }
}