package com.cmunaro.madrugada.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

open class MadrugadaViewModel : ViewModel()

abstract class MadrugadaFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    abstract val viewModel: MadrugadaViewModel

    protected inline fun <reified VM : MadrugadaViewModel> MadrugadaFragment.viewModel() =
        FragmentViewModelDelegate<VM>()

    protected inline fun <reified VB : ViewDataBinding> MadrugadaFragment.viewBinding() =
        FragmentViewBindingDelegate(VB::class.java, this)

    protected inline fun <reified VB : ViewDataBinding> MadrugadaFragment.viewBindingWithViewModelReference() =
        FragmentViewBindingDelegate(VB::class.java, this, viewModel)
}
