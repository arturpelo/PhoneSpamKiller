package com.phonespamkiller.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u00a8\u0006\t"}, d2 = {"Lcom/phonespamkiller/utils/ContactsHelper;", "", "()V", "isNumberInContacts", "", "context", "Landroid/content/Context;", "phoneNumber", "", "app_debug"})
public final class ContactsHelper {
    @org.jetbrains.annotations.NotNull()
    public static final com.phonespamkiller.utils.ContactsHelper INSTANCE = null;
    
    private ContactsHelper() {
        super();
    }
    
    /**
     * Returns true if [phoneNumber] has a match in the device contacts book.
     * Uses the optimised PhoneLookup URI which handles number normalisation.
     * Must be called on a background thread.
     */
    public final boolean isNumberInContacts(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber) {
        return false;
    }
}