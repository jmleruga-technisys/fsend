package com.fif.fpay.android.fsend.fragments

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fif.fpay.android.fsend.R
import com.google.android.material.appbar.MaterialToolbar

interface BaseActivity{
    fun showLoading()
    fun hideLoading()
}

open class BaseFragment: Fragment(){
    private var progressBar: View? = null
    private var navigation: View? = null

    fun setToolbar(toolbar: MaterialToolbar){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_withe)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        (activity as AppCompatActivity?)!!.supportActionBar?.title = requireContext().getString(R.string.personal_data)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if(!findNavController().popBackStack()) {
                            requireActivity().finish()
                        }
                    }
                }
            )
        }

    fun onBackPressed() {
        if(!findNavController().popBackStack()) {
            requireActivity().finish()
        }
    }

    fun setLoading(progressBar: View, navigation: View) {
        this.progressBar = progressBar
        this.navigation = navigation
    }

    fun setProgressBar(progressBar: View) {
        this.progressBar = progressBar
    }

    fun setNavigation(navigation: View) {
        this.navigation = navigation
    }

    fun showLoading(){
        if(activity is BaseActivity){
            (activity as BaseActivity).showLoading()
        }
    }

    fun hideLoading() {
        if(activity is BaseActivity){
            (activity as BaseActivity).hideLoading()
        }
    }
}