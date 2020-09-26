package com.fif.fpay.android.fsend

import android.app.Activity
import android.content.DialogInterface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class CustomAlertDialog(activity: Activity) : AlertDialog(activity) {
    private var mActivity = activity

    /**
     * Devuelve un alert dialog seteando los valores title, message, positive button, negative button y neutral button customizados
     */
    fun setBasicProperties(message: String, positive: Int?, positiveListener: DialogInterface.OnClickListener?,
                           negative: Int?, negativeListener: DialogInterface.OnClickListener?,
                           neutral: Int?, neutralListener: DialogInterface.OnClickListener?) : AlertDialog {
        val generalServiceError = View.inflate(mActivity, R.layout.error_dialog, null)
        val errorMsg: TextView = generalServiceError.findViewById(R.id.error_description)
        errorMsg.setText(message)

        val icon = generalServiceError.findViewById<ImageView>(R.id.error_icon)
        icon.setImageResource(R.drawable.ic_error)

        return mActivity.let {
            val builder = Builder(mActivity)
            builder.setView(generalServiceError)
            positive?.let {
                builder.setPositiveButton(positive, positiveListener)
            }
            negative?.let {
                builder.setNegativeButton(negative, negativeListener)
            }
            neutral?.let {
                builder.setNeutralButton(neutral, neutralListener)
            }
            builder.create()
        }
    }
}