package com.fif.fpay.android.fsend.conection

import android.app.Application
import androidx.annotation.StringRes
import javax.inject.Singleton
/**
 * To access the resources that are in strings.xml and that can return a string from an ID.
 * Does this class receive an Application object, instead of a Context? To be able to use this
 * StringProvider as a Singleton, and that does not depend on the Context of an Activity or a Fragment.
 */
@Singleton
class StringsProvider (val application: Application) {
    fun getString(@StringRes id: Int): String = application.getString(id) }