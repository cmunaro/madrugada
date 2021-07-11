package it.cmunaro.madrugada

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

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
        val viewModelProperty: KProperty1<out T, *>? =
            (binding::class.memberProperties as? ArrayList<KProperty1<out T, *>>?)
                ?.firstOrNull { property ->
                    property.returnType.isSubtypeOf(ViewModel::class.starProjectedType)
                }
        val viewModelSetter = (viewModelProperty as? KMutableProperty<*>)?.setter
        viewModelSetter?.isAccessible = true
        viewModelSetter?.call(binding, viewModel)
        viewModelSetter?.isAccessible = false
    }
}