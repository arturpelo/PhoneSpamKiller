package com.phonespamkiller

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import com.phonespamkiller.data.AppDatabase
import com.phonespamkiller.data.BlockedCall
import com.phonespamkiller.utils.ContactsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * System-bound service that inspects every incoming call.
 * – Numbers present in contacts are allowed through.
 * – All other numbers (including hidden/private) are rejected and stored in the local DB.
 *
 * On Android 10+ the app must hold the ROLE_CALL_SCREENING role (requested from MainActivity).
 * On Android 8/9 the service is registered but requires the app to be the default Phone app.
 */
class SpamCallScreeningService : CallScreeningService() {

    // Separate scope for the DB insert so it survives respondToCall() returning
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val rawNumber = callDetails.handle?.schemeSpecificPart
        val displayNumber = rawNumber ?: "Nieznany numer"

        Log.d(TAG, "Screening call from: $displayNumber")

        // ContactsHelper.isNumberInContacts does a ContentProvider query – run on IO thread
        // runBlocking is acceptable here because onScreenCall must call respondToCall()
        // before returning; the contact lookup is typically < 50 ms.
        val isKnown: Boolean = if (rawNumber != null) {
            runBlocking {
                withContext(Dispatchers.IO) {
                    ContactsHelper.isNumberInContacts(this@SpamCallScreeningService, rawNumber)
                }
            }
        } else {
            false // Unknown/private number → treat as spam
        }

        if (isKnown) {
            Log.d(TAG, "Known contact, allowing: $displayNumber")
            respondToCall(
                callDetails,
                CallResponse.Builder()
                    .setDisallowCall(false)
                    .build()
            )
        } else {
            Log.d(TAG, "Unknown number, blocking: $displayNumber")
            respondToCall(
                callDetails,
                CallResponse.Builder()
                    .setDisallowCall(true)
                    .setRejectCall(true)
                    .setSkipCallLog(false)       // still visible in the system call log
                    .setSkipNotification(false)  // show a missed-call style notification
                    .build()
            )

            // Persist to local database
            serviceScope.launch {
                AppDatabase.getDatabase(applicationContext)
                    .blockedCallDao()
                    .insert(BlockedCall(phoneNumber = displayNumber))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        private const val TAG = "SpamCallScreening"
    }
}
