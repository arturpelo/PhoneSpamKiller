package com.phonespamkiller;

/**
 * System-bound service that inspects every incoming call.
 * – Numbers present in contacts are allowed through.
 * – All other numbers (including hidden/private) are rejected and stored in the local DB.
 *
 * On Android 10+ the app must hold the ROLE_CALL_SCREENING role (requested from MainActivity).
 * On Android 8/9 the service is registered but requires the app to be the default Phone app.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/phonespamkiller/SpamCallScreeningService;", "Landroid/telecom/CallScreeningService;", "()V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "onDestroy", "", "onScreenCall", "callDetails", "Landroid/telecom/Call$Details;", "Companion", "app_debug"})
public final class SpamCallScreeningService extends android.telecom.CallScreeningService {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "SpamCallScreening";
    @org.jetbrains.annotations.NotNull()
    public static final com.phonespamkiller.SpamCallScreeningService.Companion Companion = null;
    
    public SpamCallScreeningService() {
        super();
    }
    
    @java.lang.Override()
    public void onScreenCall(@org.jetbrains.annotations.NotNull()
    android.telecom.Call.Details callDetails) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/phonespamkiller/SpamCallScreeningService$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}