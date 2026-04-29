package com.phonespamkiller

import android.Manifest
import android.app.role.RoleManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.phonespamkiller.data.AppDatabase
import com.phonespamkiller.databinding.ActivityMainBinding
import com.phonespamkiller.ui.BlockedCallsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private lateinit var adapter: BlockedCallsAdapter

    // ── Permission launcher ──────────────────────────────────────────────────
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            requestScreeningRole()
        } else {
            Toast.makeText(
                this,
                "Uprawnienie do kontaktów jest wymagane do blokowania nieznanych połączeń.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // ── Role launcher (Android 10+) ──────────────────────────────────────────
    private val roleRequestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            updateStatusUi()
            Toast.makeText(this, "Ochrona przed spamem jest teraz aktywna!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Uprawnienie odrzucone – ochrona nieaktywna.", Toast.LENGTH_SHORT).show()
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        db = AppDatabase.getDatabase(this)
        adapter = BlockedCallsAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe blocked calls list
        db.blockedCallDao().getAllCalls().observe(this) { calls ->
            adapter.submitList(calls)
            binding.tvEmpty.visibility = if (calls.isEmpty()) View.VISIBLE else View.GONE
        }

        // Observe total count
        db.blockedCallDao().getTotalCount().observe(this) { count ->
            binding.tvBlockedCount.text = "Zablokowanych połączeń: $count"
        }

        binding.btnEnable.setOnClickListener {
            checkContactsPermissionThenRequestRole()
        }

        binding.btnClear.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Wyczyść historię")
                .setMessage("Usunąć całą historię zablokowanych połączeń?")
                .setPositiveButton("Tak") { _, _ ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.blockedCallDao().deleteAll()
                    }
                }
                .setNegativeButton("Anuluj", null)
                .show()
        }

        updateStatusUi()
    }

    override fun onResume() {
        super.onResume()
        updateStatusUi()
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private fun checkContactsPermissionThenRequestRole() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestScreeningRole()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                AlertDialog.Builder(this)
                    .setTitle("Wymagane uprawnienie")
                    .setMessage(
                        "PhoneSpamKiller potrzebuje dostępu do kontaktów, " +
                                "aby rozpoznawać znane numery i blokować nieznane połączenia."
                    )
                    .setPositiveButton("Zezwól") { _, _ ->
                        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    .setNegativeButton("Anuluj", null)
                    .show()
            }

            else -> permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun requestScreeningRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            when {
                !roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING) -> {
                    Toast.makeText(
                        this,
                        "Filtrowanie połączeń jest niedostępne na tym urządzeniu.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING) -> {
                    Toast.makeText(this, "Ochrona jest już aktywna!", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    roleRequestLauncher.launch(
                        roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                    )
                }
            }
        } else {
            // Android 8/9: service is declared in the manifest; full blocking requires
            // the app to also be set as the default Phone app (system setting).
            Toast.makeText(
                this,
                "Android < 10: usługa jest zarejestrowana. Aby blokowanie działało w pełni, " +
                        "ustaw tę aplikację jako domyślny telefon w ustawieniach systemu.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateStatusUi() {
        val isActive = isScreeningActive()

        binding.tvStatus.text = if (isActive) "● Ochrona AKTYWNA" else "○ Ochrona NIEAKTYWNA"
        binding.tvStatus.setTextColor(
            if (isActive) getColor(R.color.statusActive) else getColor(R.color.statusInactive)
        )
        binding.btnEnable.text = if (isActive) "Ochrona aktywna ✓" else "Włącz ochronę"
        binding.btnEnable.isEnabled = !isActive
    }

    private fun isScreeningActive(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            return roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
        }
        // On Android 8/9 we optimistically show "active" once the permission is granted
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
}
