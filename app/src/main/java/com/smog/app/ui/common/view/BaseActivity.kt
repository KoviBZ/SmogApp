package com.smog.app.ui.common.view

import android.view.View
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.smog.app.network.Resource
import com.smog.app.network.Status
import com.smog.app.utils.AppError
import com.smog.app.view.ErrorView

abstract class BaseActivity: AppCompatActivity() {

    protected lateinit var errorView: ErrorView
    protected lateinit var progressBar: ProgressBar

    abstract fun showErrorView(error: AppError)

    protected fun <T> initObserver(success: (T) -> Unit): Observer<Resource<T>> {
        return Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    hideErrorView()
                    hideProgress()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                }
            }

            response.data?.let { success(it) }
            response.error?.let { showErrorView(it) }
        }
    }

    protected fun initErrorView(@StringRes message: Int, onClickListener: View.OnClickListener) {
        errorView.apply {
            enableRetryButton()
            setErrorMessage(getString(message))
            setRetryAction(onClickListener)
            visibility = View.VISIBLE
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun hideErrorView() {
        errorView.visibility = View.GONE
    }
}