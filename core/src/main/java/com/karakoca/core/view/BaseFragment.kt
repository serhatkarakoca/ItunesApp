package com.karakoca.core.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.karakoca.core.extension.observe
import com.karakoca.core.viewmodel.BaseViewModel

abstract class BaseFragment<B : ViewDataBinding, V : BaseViewModel> constructor(private val layoutResId: Int) :
    Fragment() {

    private var _binding: B? = null
    val binding
        get() = _binding ?: throw NullPointerException("Binding null pointer exception")

    private var viewModel: V? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = DataBindingUtil.inflate(inflater, layoutResId, null, false)
            viewModel?.let {
                observe(it.baseLiveData, ::onStateChanged)
            }
            init()
        }

        _binding?.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun onStateChanged(state: BaseViewModel.State) {
        when (state) {
            is BaseViewModel.State.ShowLoading -> (requireActivity() as BaseActivity<*, *>).showProgress(
                state.requestType
            )

            is BaseViewModel.State.ShowContent -> (requireActivity() as BaseActivity<*, *>).dismissProgress()

            is BaseViewModel.State.ShowError -> {
                (requireActivity() as BaseActivity<*, *>).showError(state.throwable)
                viewModel?.let {
                    it.baseLiveData.value = BaseViewModel.State.Empty
                }
            }
            else -> Unit
        }
    }

    open fun init() {}
}