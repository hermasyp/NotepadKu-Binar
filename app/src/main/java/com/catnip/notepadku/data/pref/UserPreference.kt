package com.catnip.notepadku.data.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class UserPreference(context: Context) {
    //1 : declare the preference
    private val preference: SharedPreferences = context.getSharedPreferences(NAME, MODE)

    companion object {
        private const val NAME = "notepadku_pref"
        private const val MODE = Context.MODE_PRIVATE
    }

    //3 : define a variable for set and get the preference
    var isSkipIntro: Boolean
        get() = preference.getBoolean(
            PreferenceKey.PREF_IS_SKIP_INTRO.first,
            PreferenceKey.PREF_IS_SKIP_INTRO.second
        )
        set(value) = preference.edit {
            this.putBoolean(PreferenceKey.PREF_IS_SKIP_INTRO.first, value)
        }

}

object PreferenceKey {
    //2 : define key and default value for sf, key = PREF_USER_APP_KEY, default = null
    val PREF_IS_SKIP_INTRO = Pair("PREF_IS_SKIP_INTRO", false)
}