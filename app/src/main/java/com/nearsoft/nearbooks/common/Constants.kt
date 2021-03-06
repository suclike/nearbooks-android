package com.nearsoft.nearbooks.common

import com.nearsoft.nearbooks.BuildConfig

/**
 * Constants.
 * Created by epool on 1/19/16.
 */
interface Constants {

    companion object {
        const val NEARBOOKS_MANAGER_PASSWORD = "admin"
        const val REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE"
        const val CONTENT_AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"
    }

}
