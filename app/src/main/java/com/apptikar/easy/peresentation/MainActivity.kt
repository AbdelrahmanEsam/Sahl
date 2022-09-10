package com.apptikar.easy.peresentation

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.apptikar.dribbox.utils.rememberWindowSizeDp
import com.apptikar.easy.R
import com.apptikar.easy.common.utils.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver
    private var nfcAdapter: NfcAdapter? = null
    var tag = MutableStateFlow<Tag?>(null)

    var connectivityStatus: ConnectivityObserver.Status =  ConnectivityObserver.Status.Lost

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityObserver(connectivityObserver)

        val devicePosture = WindowInfoTracker
            .getOrCreate(this)
            .windowLayoutInfo(this)
            .stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Eagerly,
                initialValue = WindowLayoutInfo(emptyList())
            )
        initNfc()


        setContent {
            val windowSizeDp = rememberWindowSizeDp(this)

            EasyModal(
                devicePosture = devicePosture,
                windowSizeDp = windowSizeDp,
            )
        }
    }

    private fun connectivityObserver(connectivityObserver: ConnectivityObserver) {
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest { status ->
                connectivityStatus = status
                Log.d("status20",connectivityStatus.name)
                if (status == ConnectivityObserver.Status.Lost) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.connectivity_lost),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun initNfc() {
        // Check if NFC is supported and enabled
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        Log.e("NFC supported", (nfcAdapter != null).toString())
        Log.e("NFC enabled", (nfcAdapter?.isEnabled).toString())
        nfcPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }, PendingIntent.FLAG_MUTABLE)

        if (intent != null) {
            Log.e("Found intent in onCreate", intent.action.toString())
            processIntent(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null);
        // Alternative: only get specific HTTP NDEF intent
        //nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, nfcIntentFilters, null);
    }

    override fun onPause() {
        super.onPause()
        // Disable foreground dispatch, as this activity is no longer in the foreground
        nfcAdapter?.disableForegroundDispatch(this);
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("Found intent in onNewIntent", intent?.action.toString())
        if (intent != null) processIntent(intent)
    }


    fun processIntent(checkIntent: Intent) {


        if (checkIntent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            tag = checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!!
            val techList = tag.value?.techList
            Log.e("tag techs : ", techList.toString())
        }

    }
}




