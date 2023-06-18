package com.karakoca.core.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.karakoca.core.R
import com.karakoca.core.extension.observe
import com.karakoca.core.viewmodel.BaseViewModel

abstract class BaseActivity<B: ViewDataBinding, V: BaseViewModel> constructor(private val layoutResId: Int): AppCompatActivity(){

    private var _binding: B? = null
    val binding
        get() = _binding ?: throw NullPointerException("Binding null pointer exception")

    private var viewModel: V? = null

    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResId)
        viewModel?.let {
            observe(it.baseLiveData, ::onStateChanged)
        }
        init()
    }

    private fun onStateChanged(state: BaseViewModel.State) {
        when (state) {
            is BaseViewModel.State.ShowLoading -> showProgress(state.requestType)
            is BaseViewModel.State.ShowContent -> dismissProgress()
            is BaseViewModel.State.ShowError -> {
                showError(
                    state.throwable
                )
                viewModel?.let {
                    it.baseLiveData.value = BaseViewModel.State.Empty
                }
            }

            else -> Unit
        }
    }

    fun showError(throwable: Throwable?) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error))
            .setMessage(throwable?.message ?: getString(R.string.unexpected_error))
            .setPositiveButton(getString(android.R.string.ok)){ dialog, _ ->
                dialog.dismiss()
            }
    }

    fun dismissProgress() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    fun showProgress(requestType: BaseViewModel.RequestType) {
        if (progressDialog == null && requestType == BaseViewModel.RequestType.PROGRESS) {
            progressDialog = Dialog(this).apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCancelable(false)
                setContentView(R.layout.view_progress)
            }
        }
        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }


    open fun init(){}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel = null
    }
}