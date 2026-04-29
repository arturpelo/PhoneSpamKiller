package com.phonespamkiller.utils

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

object ContactsHelper {

    /**
     * Returns true if [phoneNumber] has a match in the device contacts book.
     * Uses the optimised PhoneLookup URI which handles number normalisation.
     * Must be called on a background thread.
     */
    fun isNumberInContacts(context: Context, phoneNumber: String): Boolean {
        val lookupUri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val cursor = context.contentResolver.query(
            lookupUri,
            arrayOf(ContactsContract.PhoneLookup._ID),
            null,
            null,
            null
        )
        return cursor?.use { it.count > 0 } ?: false
    }
}
