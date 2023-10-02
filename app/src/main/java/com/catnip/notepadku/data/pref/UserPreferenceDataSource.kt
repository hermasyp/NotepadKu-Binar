package com.catnip.notepadku.data.pref

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface UserPreferenceDataSource {
    fun isSkipIntro(): Boolean
    fun setSkipIntro(isSkipIntro: Boolean)
}

class UserPreferenceDataSourceImpl(
    private val userPreference: UserPreference
) : UserPreferenceDataSource {
    override fun isSkipIntro(): Boolean {
        return userPreference.isSkipIntro
    }

    override fun setSkipIntro(isSkipIntro: Boolean) {
        userPreference.isSkipIntro = isSkipIntro
    }

}
