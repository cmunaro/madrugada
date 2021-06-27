package com.cmunaro.madrugada.base

/*
* By Mavericks
* */

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.cmunaro.madrugada.BR
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewDataBinding>(
    bindingClass: Class<T>,
    private val fragment: Fragment,
    private val viewModel: MadrugadaViewModel? = null
) : ReadOnlyProperty<Fragment, T> {
    private val clearBindingHandler by lazy(LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }
    private var binding: T? = null

    private val bindMethod = bindingClass.getMethod("bind", View::class.java)

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() { clearBindingHandler.post { binding = null } }
            })
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (binding != null && binding?.root !== thisRef.view) {
            binding = null
        }
        binding?.let { return it }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Cannot access view bindings. View lifecycle is ${lifecycle.currentState}!")
        }

        @Suppress("UNCHECKED_CAST")
        binding = bindMethod.invoke(null, thisRef.requireView()) as T
        viewModel?.let { injectViewModel(it, binding) }
        binding?.lifecycleOwner = fragment.viewLifecycleOwner
        return binding!!
    }

    private fun injectViewModel(viewModel: MadrugadaViewModel, binding: T?) {
        binding ?: return
        val setVariableMethod = binding::class.java
            .methods
            .firstOrNull { it.name == "setVariable" }?: return
        setVariableMethod(binding, BR.viewModel, viewModel)
    }
}