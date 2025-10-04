<html>
<head>
<title>MainActivity.kt</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.s0 { color: #cf8e6d;}
.s1 { color: #bcbec4;}
.s2 { color: #bcbec4;}
.s3 { color: #6aab73;}
.s4 { color: #2aacb8;}
.s5 { color: #7a7e85;}
</style>
</head>
<body bgcolor="#1e1f22">
<table CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#606060" >
<tr><td><center>
<font face="Arial, Helvetica" color="#000000">
MainActivity.kt</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">iot</span><span class="s2">.</span><span class="s1">focusguardapp</span>

<span class="s1">import android</span><span class="s2">.</span><span class="s1">content</span><span class="s2">.</span><span class="s1">BroadcastReceiver</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">content</span><span class="s2">.</span><span class="s1">Context</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">content</span><span class="s2">.</span><span class="s1">Intent</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">content</span><span class="s2">.</span><span class="s1">IntentFilter</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">app</span><span class="s2">.</span><span class="s1">NotificationManager</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">os</span><span class="s2">.</span><span class="s1">Bundle</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">provider</span><span class="s2">.</span><span class="s1">Settings</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">widget</span><span class="s2">.</span><span class="s1">Button</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">widget</span><span class="s2">.</span><span class="s1">TextView</span>
<span class="s1">import android</span><span class="s2">.</span><span class="s1">widget</span><span class="s2">.</span><span class="s1">Toast</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">appcompat</span><span class="s2">.</span><span class="s1">app</span><span class="s2">.</span><span class="s1">AppCompatActivity</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">lifecycle</span><span class="s2">.</span><span class="s1">lifecycleScope</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">android</span><span class="s2">.</span><span class="s1">volley</span><span class="s2">.</span><span class="s1">Request</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">android</span><span class="s2">.</span><span class="s1">volley</span><span class="s2">.</span><span class="s1">toolbox</span><span class="s2">.</span><span class="s1">StringRequest</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">android</span><span class="s2">.</span><span class="s1">volley</span><span class="s2">.</span><span class="s1">toolbox</span><span class="s2">.</span><span class="s1">Volley</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">google</span><span class="s2">.</span><span class="s1">android</span><span class="s2">.</span><span class="s1">material</span><span class="s2">.</span><span class="s1">progressindicator</span><span class="s2">.</span><span class="s1">CircularProgressIndicator</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">google</span><span class="s2">.</span><span class="s1">android</span><span class="s2">.</span><span class="s1">material</span><span class="s2">.</span><span class="s1">textfield</span><span class="s2">.</span><span class="s1">TextInputEditText</span>
<span class="s1">import kotlinx</span><span class="s2">.</span><span class="s1">coroutines</span><span class="s2">.</span><span class="s1">launch</span>
<span class="s1">import org</span><span class="s2">.</span><span class="s1">json</span><span class="s2">.</span><span class="s1">JSONObject</span>

<span class="s0">class </span><span class="s1">MainActivity : AppCompatActivity</span><span class="s2">() {</span>

    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">etDeviceIp: TextInputEditText</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">etFocusTime: TextInputEditText</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">etBreakTime: TextInputEditText</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">tvStatus: TextView</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">tvTimer: TextView</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">btnRefresh: Button</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">btnStart: Button</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">btnPause: Button</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">btnReset: Button</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">btnSetTimes: Button</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">progressIndicator: CircularProgressIndicator</span>
    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">btnStats: Button</span>

    <span class="s1">private </span><span class="s0">var </span><span class="s1">deviceIp </span><span class="s2">= </span><span class="s3">&quot;&quot;</span>
    <span class="s1">private </span><span class="s0">var </span><span class="s1">isConnected </span><span class="s2">= </span><span class="s0">false</span>
    <span class="s1">private </span><span class="s0">var </span><span class="s1">previousState: Int </span><span class="s2">= </span><span class="s4">0</span>
    <span class="s1">private </span><span class="s0">val </span><span class="s1">dao by lazy </span><span class="s2">{ </span><span class="s1">AppDatabase</span><span class="s2">.</span><span class="s1">getDatabase</span><span class="s2">(</span><span class="s0">this</span><span class="s2">).</span><span class="s1">focusSessionDao</span><span class="s2">() }</span>

    <span class="s1">companion </span><span class="s0">object </span><span class="s2">{</span>
        <span class="s1">const </span><span class="s0">val </span><span class="s1">PREFS_NAME </span><span class="s2">= </span><span class="s3">&quot;FocusGuardPrefs&quot;</span>
        <span class="s1">const </span><span class="s0">val </span><span class="s1">KEY_IP_ADDRESS </span><span class="s2">= </span><span class="s3">&quot;ipAddress&quot;</span>
    <span class="s2">}</span>

    <span class="s5">// Receiver to get updates from the service</span>
    <span class="s1">private </span><span class="s0">val </span><span class="s1">statusUpdateReceiver </span><span class="s2">= </span><span class="s0">object </span><span class="s1">: BroadcastReceiver</span><span class="s2">() {</span>
        <span class="s1">override </span><span class="s0">fun </span><span class="s1">onReceive</span><span class="s2">(</span><span class="s1">context: Context?</span><span class="s2">, </span><span class="s1">intent: Intent?</span><span class="s2">) {</span>
            <span class="s0">if </span><span class="s2">(</span><span class="s1">intent?</span><span class="s2">.</span><span class="s1">action </span><span class="s2">== </span><span class="s1">FocusGuardService</span><span class="s2">.</span><span class="s1">BROADCAST_STATUS_UPDATE</span><span class="s2">) {</span>
                <span class="s0">val </span><span class="s1">response </span><span class="s2">= </span><span class="s1">intent</span><span class="s2">.</span><span class="s1">getStringExtra</span><span class="s2">(</span><span class="s3">&quot;response&quot;</span><span class="s2">)</span>
                <span class="s0">val </span><span class="s1">error </span><span class="s2">= </span><span class="s1">intent</span><span class="s2">.</span><span class="s1">getStringExtra</span><span class="s2">(</span><span class="s3">&quot;error&quot;</span><span class="s2">)</span>

                <span class="s0">if </span><span class="s2">(</span><span class="s1">response </span><span class="s2">!= </span><span class="s0">null</span><span class="s2">) {</span>
                    <span class="s1">parseStatus</span><span class="s2">(</span><span class="s1">response</span><span class="s2">)</span>
                <span class="s2">} </span><span class="s0">else if </span><span class="s2">(</span><span class="s1">error </span><span class="s2">!= </span><span class="s0">null</span><span class="s2">) {</span>
                    <span class="s1">tvStatus</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Disconnected&quot;</span>
                    <span class="s1">isConnected </span><span class="s2">= </span><span class="s0">false</span>
                    <span class="s1">updateButtonStates</span><span class="s2">()</span>
                <span class="s2">}</span>
            <span class="s2">}</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s1">override </span><span class="s0">fun </span><span class="s1">onCreate</span><span class="s2">(</span><span class="s1">savedInstanceState: Bundle?</span><span class="s2">) {</span>
        <span class="s0">super</span><span class="s2">.</span><span class="s1">onCreate</span><span class="s2">(</span><span class="s1">savedInstanceState</span><span class="s2">)</span>
        <span class="s1">setContentView</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">layout</span><span class="s2">.</span><span class="s1">activity_main</span><span class="s2">)</span>

        <span class="s1">etDeviceIp </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">etDeviceIp</span><span class="s2">)</span>
        <span class="s1">etFocusTime </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">etFocusTime</span><span class="s2">)</span>
        <span class="s1">etBreakTime </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">etBreakTime</span><span class="s2">)</span>
        <span class="s1">tvStatus </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">tvStatus</span><span class="s2">)</span>
        <span class="s1">tvTimer </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">tvTimer</span><span class="s2">)</span>
        <span class="s1">btnRefresh </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">btnRefresh</span><span class="s2">)</span>
        <span class="s1">btnStart </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">btnStart</span><span class="s2">)</span>
        <span class="s1">btnPause </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">btnPause</span><span class="s2">)</span>
        <span class="s1">btnReset </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">btnReset</span><span class="s2">)</span>
        <span class="s1">btnSetTimes </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">btnSetTimes</span><span class="s2">)</span>
        <span class="s1">progressIndicator </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">progressIndicator</span><span class="s2">)</span>
        <span class="s1">btnStats </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">btnStats</span><span class="s2">)</span>

        <span class="s1">checkDndPermission</span><span class="s2">()</span>
        <span class="s1">updateButtonStates</span><span class="s2">()</span>
        <span class="s1">loadIpAddress</span><span class="s2">()</span>

        <span class="s1">btnRefresh</span><span class="s2">.</span><span class="s1">setOnClickListener </span><span class="s2">{</span>
            <span class="s1">deviceIp </span><span class="s2">= </span><span class="s1">etDeviceIp</span><span class="s2">.</span><span class="s1">text</span><span class="s2">.</span><span class="s1">toString</span><span class="s2">().</span><span class="s1">trim</span><span class="s2">()</span>
            <span class="s0">if </span><span class="s2">(</span><span class="s1">deviceIp</span><span class="s2">.</span><span class="s1">isNotEmpty</span><span class="s2">()) {</span>
                <span class="s1">saveIpAddress</span><span class="s2">(</span><span class="s1">deviceIp</span><span class="s2">)</span>
                <span class="s5">// Start the background service</span>
                <span class="s0">val </span><span class="s1">serviceIntent </span><span class="s2">= </span><span class="s1">Intent</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s1">FocusGuardService::</span><span class="s0">class</span><span class="s2">.</span><span class="s1">java</span><span class="s2">).</span><span class="s1">apply </span><span class="s2">{</span>
                    <span class="s1">action </span><span class="s2">= </span><span class="s1">FocusGuardService</span><span class="s2">.</span><span class="s1">ACTION_START_SERVICE</span>
                    <span class="s1">putExtra</span><span class="s2">(</span><span class="s3">&quot;IP_ADDRESS&quot;</span><span class="s2">, </span><span class="s1">deviceIp</span><span class="s2">)</span>
                <span class="s2">}</span>
                <span class="s1">startForegroundService</span><span class="s2">(</span><span class="s1">serviceIntent</span><span class="s2">)</span>
                <span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Connecting...&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">()</span>
            <span class="s2">} </span><span class="s0">else </span><span class="s2">{</span>
                <span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Please enter an IP address&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">()</span>
            <span class="s2">}</span>
        <span class="s2">}</span>

        <span class="s1">btnStart</span><span class="s2">.</span><span class="s1">setOnClickListener </span><span class="s2">{</span>
            <span class="s0">if </span><span class="s2">(</span><span class="s1">previousState </span><span class="s2">== </span><span class="s4">0</span><span class="s2">) { </span><span class="s5">// If Idle, start the timer</span>
                <span class="s1">sendCommand</span><span class="s2">(</span><span class="s3">&quot;start&quot;</span><span class="s2">)</span>
            <span class="s2">} </span><span class="s0">else </span><span class="s2">{ </span><span class="s5">// If running, paused, or on break, reset</span>
                <span class="s1">sendCommand</span><span class="s2">(</span><span class="s3">&quot;reset&quot;</span><span class="s2">)</span>
            <span class="s2">}</span>
        <span class="s2">}</span>
        <span class="s1">btnPause</span><span class="s2">.</span><span class="s1">setOnClickListener </span><span class="s2">{ </span><span class="s1">sendCommand</span><span class="s2">(</span><span class="s3">&quot;pause&quot;</span><span class="s2">) }</span>
        <span class="s1">btnReset</span><span class="s2">.</span><span class="s1">setOnClickListener </span><span class="s2">{ </span><span class="s1">sendCommand</span><span class="s2">(</span><span class="s3">&quot;reset&quot;</span><span class="s2">) }</span>
        <span class="s1">btnStats</span><span class="s2">.</span><span class="s1">setOnClickListener </span><span class="s2">{</span>
            <span class="s1">startActivity</span><span class="s2">(</span><span class="s1">Intent</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s1">StatsActivity::</span><span class="s0">class</span><span class="s2">.</span><span class="s1">java</span><span class="s2">))</span>
        <span class="s2">}</span>

        <span class="s1">btnSetTimes</span><span class="s2">.</span><span class="s1">setOnClickListener </span><span class="s2">{</span>
            <span class="s0">val </span><span class="s1">focus </span><span class="s2">= </span><span class="s1">etFocusTime</span><span class="s2">.</span><span class="s1">text</span><span class="s2">.</span><span class="s1">toString</span><span class="s2">()</span>
            <span class="s0">val </span><span class="s1">breakT </span><span class="s2">= </span><span class="s1">etBreakTime</span><span class="s2">.</span><span class="s1">text</span><span class="s2">.</span><span class="s1">toString</span><span class="s2">()</span>
            <span class="s0">if </span><span class="s2">(</span><span class="s1">focus</span><span class="s2">.</span><span class="s1">isNotEmpty</span><span class="s2">() &amp;&amp; </span><span class="s1">breakT</span><span class="s2">.</span><span class="s1">isNotEmpty</span><span class="s2">()) {</span>
                <span class="s1">sendCommand</span><span class="s2">(</span><span class="s3">&quot;set?focus=</span><span class="s0">$</span><span class="s1">focus</span><span class="s3">&amp;break=</span><span class="s0">$</span><span class="s1">breakT</span><span class="s3">&quot;</span><span class="s2">)</span>
            <span class="s2">} </span><span class="s0">else </span><span class="s2">{</span>
                <span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Please enter both times&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">()</span>
            <span class="s2">}</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s1">override </span><span class="s0">fun </span><span class="s1">onResume</span><span class="s2">() {</span>
        <span class="s0">super</span><span class="s2">.</span><span class="s1">onResume</span><span class="s2">()</span>
        <span class="s1">registerReceiver</span><span class="s2">(</span><span class="s1">statusUpdateReceiver</span><span class="s2">, </span><span class="s1">IntentFilter</span><span class="s2">(</span><span class="s1">FocusGuardService</span><span class="s2">.</span><span class="s1">BROADCAST_STATUS_UPDATE</span><span class="s2">), </span><span class="s1">RECEIVER_NOT_EXPORTED</span><span class="s2">)</span>
    <span class="s2">}</span>

    <span class="s1">override </span><span class="s0">fun </span><span class="s1">onPause</span><span class="s2">() {</span>
        <span class="s0">super</span><span class="s2">.</span><span class="s1">onPause</span><span class="s2">()</span>
        <span class="s1">unregisterReceiver</span><span class="s2">(</span><span class="s1">statusUpdateReceiver</span><span class="s2">)</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">saveIpAddress</span><span class="s2">(</span><span class="s1">ip: String</span><span class="s2">) {</span>
        <span class="s0">val </span><span class="s1">sharedPref </span><span class="s2">= </span><span class="s1">getSharedPreferences</span><span class="s2">(</span><span class="s1">PREFS_NAME</span><span class="s2">, </span><span class="s1">Context</span><span class="s2">.</span><span class="s1">MODE_PRIVATE</span><span class="s2">) </span><span class="s1">?: </span><span class="s0">return</span>
        <span class="s1">with</span><span class="s2">(</span><span class="s1">sharedPref</span><span class="s2">.</span><span class="s1">edit</span><span class="s2">()) { </span><span class="s1">putString</span><span class="s2">(</span><span class="s1">KEY_IP_ADDRESS</span><span class="s2">, </span><span class="s1">ip</span><span class="s2">); </span><span class="s1">apply</span><span class="s2">() }</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">loadIpAddress</span><span class="s2">() {</span>
        <span class="s0">val </span><span class="s1">sharedPref </span><span class="s2">= </span><span class="s1">getSharedPreferences</span><span class="s2">(</span><span class="s1">PREFS_NAME</span><span class="s2">, </span><span class="s1">Context</span><span class="s2">.</span><span class="s1">MODE_PRIVATE</span><span class="s2">) </span><span class="s1">?: </span><span class="s0">return</span>
        <span class="s1">etDeviceIp</span><span class="s2">.</span><span class="s1">setText</span><span class="s2">(</span><span class="s1">sharedPref</span><span class="s2">.</span><span class="s1">getString</span><span class="s2">(</span><span class="s1">KEY_IP_ADDRESS</span><span class="s2">, </span><span class="s3">&quot;&quot;</span><span class="s2">))</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">updateButtonStates</span><span class="s2">() {</span>
        <span class="s1">btnStart</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s1">isConnected</span>
        <span class="s1">btnPause</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s1">isConnected</span>
        <span class="s1">btnReset</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s1">isConnected</span>
        <span class="s1">btnSetTimes</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s1">isConnected</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">sendCommand</span><span class="s2">(</span><span class="s1">command: String</span><span class="s2">) {</span>
        <span class="s0">if </span><span class="s2">(!</span><span class="s1">isConnected</span><span class="s2">) {</span>
            <span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Please connect to a device first&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">()</span>
            <span class="s0">return</span>
        <span class="s2">}</span>
        <span class="s0">val </span><span class="s1">queue </span><span class="s2">= </span><span class="s1">Volley</span><span class="s2">.</span><span class="s1">newRequestQueue</span><span class="s2">(</span><span class="s0">this</span><span class="s2">)</span>
        <span class="s0">val </span><span class="s1">url </span><span class="s2">= </span><span class="s3">&quot;http://</span><span class="s0">$</span><span class="s1">deviceIp</span><span class="s3">/</span><span class="s0">$</span><span class="s1">command</span><span class="s3">&quot;</span>
        <span class="s0">val </span><span class="s1">stringRequest </span><span class="s2">= </span><span class="s1">StringRequest</span><span class="s2">(</span><span class="s1">Request</span><span class="s2">.</span><span class="s1">Method</span><span class="s2">.</span><span class="s1">GET</span><span class="s2">, </span><span class="s1">url</span><span class="s2">,</span>
            <span class="s2">{ </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Command Sent&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">() },</span>
            <span class="s2">{ </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Command Failed&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">() }</span>
        <span class="s2">)</span>
        <span class="s1">queue</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">stringRequest</span><span class="s2">)</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">parseStatus</span><span class="s2">(</span><span class="s1">response: String</span><span class="s2">) {</span>
        <span class="s0">if </span><span class="s2">(!</span><span class="s1">isConnected</span><span class="s2">) {</span>
            <span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Connected successfully!&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">()</span>
        <span class="s2">}</span>
        <span class="s1">isConnected </span><span class="s2">= </span><span class="s0">true</span>
        <span class="s1">updateButtonStates</span><span class="s2">()</span>
        <span class="s0">try </span><span class="s2">{</span>
            <span class="s0">val </span><span class="s1">json </span><span class="s2">= </span><span class="s1">JSONObject</span><span class="s2">(</span><span class="s1">response</span><span class="s2">)</span>
            <span class="s0">val </span><span class="s1">currentState </span><span class="s2">= </span><span class="s1">json</span><span class="s2">.</span><span class="s1">getInt</span><span class="s2">(</span><span class="s3">&quot;state&quot;</span><span class="s2">)</span>
            <span class="s0">var </span><span class="s1">remainingSeconds </span><span class="s2">= </span><span class="s1">json</span><span class="s2">.</span><span class="s1">getLong</span><span class="s2">(</span><span class="s3">&quot;remaining_seconds&quot;</span><span class="s2">)</span>
            <span class="s0">val </span><span class="s1">focusDuration </span><span class="s2">= </span><span class="s1">json</span><span class="s2">.</span><span class="s1">getLong</span><span class="s2">(</span><span class="s3">&quot;focus_duration&quot;</span><span class="s2">)</span>
            <span class="s0">val </span><span class="s1">breakDuration </span><span class="s2">= </span><span class="s1">json</span><span class="s2">.</span><span class="s1">getLong</span><span class="s2">(</span><span class="s3">&quot;break_duration&quot;</span><span class="s2">)</span>

            <span class="s0">if </span><span class="s2">(</span><span class="s1">remainingSeconds </span><span class="s2">&lt; </span><span class="s4">0</span><span class="s2">) </span><span class="s1">remainingSeconds </span><span class="s2">= </span><span class="s4">0</span>

            <span class="s0">if </span><span class="s2">(</span><span class="s1">previousState </span><span class="s2">== </span><span class="s4">1 </span><span class="s2">&amp;&amp; </span><span class="s1">currentState </span><span class="s2">== </span><span class="s4">2</span><span class="s2">) {</span>
                <span class="s1">logSession</span><span class="s2">(</span><span class="s1">focusDuration</span><span class="s2">, </span><span class="s3">&quot;FOCUS&quot;</span><span class="s2">)</span>
            <span class="s2">}</span>
            <span class="s0">if </span><span class="s2">(</span><span class="s1">previousState </span><span class="s2">== </span><span class="s4">2 </span><span class="s2">&amp;&amp; </span><span class="s1">currentState </span><span class="s2">== </span><span class="s4">0</span><span class="s2">) {</span>
                <span class="s1">logSession</span><span class="s2">(</span><span class="s1">breakDuration</span><span class="s2">, </span><span class="s3">&quot;BREAK&quot;</span><span class="s2">)</span>
            <span class="s2">}</span>
            <span class="s1">previousState </span><span class="s2">= </span><span class="s1">currentState</span>

            <span class="s0">val </span><span class="s1">minutes </span><span class="s2">= </span><span class="s1">remainingSeconds </span><span class="s2">/ </span><span class="s4">60L</span>
            <span class="s0">val </span><span class="s1">seconds </span><span class="s2">= </span><span class="s1">remainingSeconds </span><span class="s2">% </span><span class="s4">60L</span>
            <span class="s1">tvTimer</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s1">String</span><span class="s2">.</span><span class="s1">format</span><span class="s2">(</span><span class="s3">&quot;%02d:%02d&quot;</span><span class="s2">, </span><span class="s1">minutes</span><span class="s2">, </span><span class="s1">seconds</span><span class="s2">)</span>
            <span class="s1">etFocusTime</span><span class="s2">.</span><span class="s1">setText</span><span class="s2">((</span><span class="s1">focusDuration </span><span class="s2">/ </span><span class="s4">60L</span><span class="s2">).</span><span class="s1">toString</span><span class="s2">())</span>
            <span class="s1">etBreakTime</span><span class="s2">.</span><span class="s1">setText</span><span class="s2">((</span><span class="s1">breakDuration </span><span class="s2">/ </span><span class="s4">60L</span><span class="s2">).</span><span class="s1">toString</span><span class="s2">())</span>

            <span class="s0">var </span><span class="s1">totalDuration </span><span class="s2">= </span><span class="s1">focusDuration</span>
            <span class="s0">if </span><span class="s2">(</span><span class="s1">currentState </span><span class="s2">== </span><span class="s4">2</span><span class="s2">) </span><span class="s1">totalDuration </span><span class="s2">= </span><span class="s1">breakDuration</span>

            <span class="s0">if </span><span class="s2">(</span><span class="s1">totalDuration </span><span class="s2">&gt; </span><span class="s4">0 </span><span class="s2">&amp;&amp; </span><span class="s1">remainingSeconds </span><span class="s2">&lt;= </span><span class="s1">totalDuration</span><span class="s2">) {</span>
                <span class="s0">val </span><span class="s1">progress </span><span class="s2">= ((</span><span class="s1">totalDuration </span><span class="s2">- </span><span class="s1">remainingSeconds</span><span class="s2">) * </span><span class="s4">100 </span><span class="s2">/ </span><span class="s1">totalDuration</span><span class="s2">).</span><span class="s1">toInt</span><span class="s2">()</span>
                <span class="s1">progressIndicator</span><span class="s2">.</span><span class="s1">progress </span><span class="s2">= </span><span class="s1">progress</span>
            <span class="s2">} </span><span class="s0">else </span><span class="s2">{</span>
                <span class="s1">progressIndicator</span><span class="s2">.</span><span class="s1">progress </span><span class="s2">= </span><span class="s4">0</span>
            <span class="s2">}</span>

            <span class="s0">when </span><span class="s2">(</span><span class="s1">currentState</span><span class="s2">) {</span>
                <span class="s4">0 </span><span class="s2">-&gt; { </span><span class="s1">tvStatus</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Idle&quot;</span><span class="s2">; </span><span class="s1">btnStart</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Start&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Pause&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s0">false </span><span class="s2">}</span>
                <span class="s4">1 </span><span class="s2">-&gt; { </span><span class="s1">tvStatus</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Focusing&quot;</span><span class="s2">; </span><span class="s1">btnStart</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Reset&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Pause&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s0">true </span><span class="s2">}</span>
                <span class="s4">2 </span><span class="s2">-&gt; { </span><span class="s1">tvStatus</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;On Break&quot;</span><span class="s2">; </span><span class="s1">btnStart</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Reset&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Pause&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s0">false </span><span class="s2">}</span>
                <span class="s4">3 </span><span class="s2">-&gt; { </span><span class="s1">tvStatus</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Paused&quot;</span><span class="s2">; </span><span class="s1">btnStart</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Reset&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Resume&quot;</span><span class="s2">; </span><span class="s1">btnPause</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s0">true </span><span class="s2">}</span>
            <span class="s2">}</span>
        <span class="s2">} </span><span class="s1">catch </span><span class="s2">(</span><span class="s1">e: Exception</span><span class="s2">) {</span>
            <span class="s1">tvStatus</span><span class="s2">.</span><span class="s1">text </span><span class="s2">= </span><span class="s3">&quot;Invalid Response&quot;</span>
            <span class="s1">isConnected </span><span class="s2">= </span><span class="s0">false</span>
            <span class="s1">updateButtonStates</span><span class="s2">()</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">logSession</span><span class="s2">(</span><span class="s1">durationInSeconds: Long</span><span class="s2">, </span><span class="s1">type: String</span><span class="s2">) {</span>
        <span class="s1">lifecycleScope</span><span class="s2">.</span><span class="s1">launch </span><span class="s2">{</span>
            <span class="s0">val </span><span class="s1">session </span><span class="s2">= </span><span class="s1">FocusSession</span><span class="s2">(</span>
                <span class="s1">timestamp </span><span class="s2">= </span><span class="s1">System</span><span class="s2">.</span><span class="s1">currentTimeMillis</span><span class="s2">(),</span>
                <span class="s1">durationSeconds </span><span class="s2">= </span><span class="s1">durationInSeconds</span><span class="s2">.</span><span class="s1">toInt</span><span class="s2">(),</span>
                <span class="s1">type </span><span class="s2">= </span><span class="s1">type</span>
            <span class="s2">)</span>
            <span class="s1">dao</span><span class="s2">.</span><span class="s1">insert</span><span class="s2">(</span><span class="s1">session</span><span class="s2">)</span>
            <span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s1">@MainActivity</span><span class="s2">, </span><span class="s3">&quot;</span><span class="s0">$</span><span class="s1">type </span><span class="s3">session of </span><span class="s0">${</span><span class="s1">durationInSeconds </span><span class="s2">/ </span><span class="s4">60L</span><span class="s0">} </span><span class="s3">min logged!&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_SHORT</span><span class="s2">).</span><span class="s1">show</span><span class="s2">()</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">checkDndPermission</span><span class="s2">() {</span>
        <span class="s0">val </span><span class="s1">notificationManager </span><span class="s2">= </span><span class="s1">getSystemService</span><span class="s2">(</span><span class="s1">Context</span><span class="s2">.</span><span class="s1">NOTIFICATION_SERVICE</span><span class="s2">) </span><span class="s0">as </span><span class="s1">NotificationManager</span>
        <span class="s0">if </span><span class="s2">(!</span><span class="s1">notificationManager</span><span class="s2">.</span><span class="s1">isNotificationPolicyAccessGranted</span><span class="s2">) {</span>
            <span class="s1">Toast</span><span class="s2">.</span><span class="s1">makeText</span><span class="s2">(</span><span class="s0">this</span><span class="s2">, </span><span class="s3">&quot;Please grant Do Not Disturb permission&quot;</span><span class="s2">, </span><span class="s1">Toast</span><span class="s2">.</span><span class="s1">LENGTH_LONG</span><span class="s2">).</span><span class="s1">show</span><span class="s2">()</span>
            <span class="s1">startActivity</span><span class="s2">(</span><span class="s1">Intent</span><span class="s2">(</span><span class="s1">Settings</span><span class="s2">.</span><span class="s1">ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS</span><span class="s2">))</span>
        <span class="s2">}</span>
    <span class="s2">}</span>
<span class="s2">}</span></pre>
</body>
</html>