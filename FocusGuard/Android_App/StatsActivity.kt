<html>
<head>
<title>StatsActivity.kt</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.s0 { color: #cf8e6d;}
.s1 { color: #bcbec4;}
.s2 { color: #bcbec4;}
.s3 { color: #7a7e85;}
.s4 { color: #6aab73;}
.s5 { color: #2aacb8;}
</style>
</head>
<body bgcolor="#1e1f22">
<table CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#606060" >
<tr><td><center>
<font face="Arial, Helvetica" color="#000000">
StatsActivity.kt</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">iot</span><span class="s2">.</span><span class="s1">focusguardapp</span>

<span class="s1">import android</span><span class="s2">.</span><span class="s1">os</span><span class="s2">.</span><span class="s1">Bundle</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">appcompat</span><span class="s2">.</span><span class="s1">app</span><span class="s2">.</span><span class="s1">AppCompatActivity</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">lifecycle</span><span class="s2">.</span><span class="s1">lifecycleScope</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">github</span><span class="s2">.</span><span class="s1">mikephil</span><span class="s2">.</span><span class="s1">charting</span><span class="s2">.</span><span class="s1">charts</span><span class="s2">.</span><span class="s1">BarChart</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">github</span><span class="s2">.</span><span class="s1">mikephil</span><span class="s2">.</span><span class="s1">charting</span><span class="s2">.</span><span class="s1">components</span><span class="s2">.</span><span class="s1">XAxis</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">github</span><span class="s2">.</span><span class="s1">mikephil</span><span class="s2">.</span><span class="s1">charting</span><span class="s2">.</span><span class="s1">data</span><span class="s2">.</span><span class="s1">BarData</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">github</span><span class="s2">.</span><span class="s1">mikephil</span><span class="s2">.</span><span class="s1">charting</span><span class="s2">.</span><span class="s1">data</span><span class="s2">.</span><span class="s1">BarDataSet</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">github</span><span class="s2">.</span><span class="s1">mikephil</span><span class="s2">.</span><span class="s1">charting</span><span class="s2">.</span><span class="s1">data</span><span class="s2">.</span><span class="s1">BarEntry</span>
<span class="s1">import com</span><span class="s2">.</span><span class="s1">github</span><span class="s2">.</span><span class="s1">mikephil</span><span class="s2">.</span><span class="s1">charting</span><span class="s2">.</span><span class="s1">formatter</span><span class="s2">.</span><span class="s1">IndexAxisValueFormatter</span>
<span class="s1">import kotlinx</span><span class="s2">.</span><span class="s1">coroutines</span><span class="s2">.</span><span class="s1">launch</span>
<span class="s1">import java</span><span class="s2">.</span><span class="s1">text</span><span class="s2">.</span><span class="s1">SimpleDateFormat</span>
<span class="s1">import java</span><span class="s2">.</span><span class="s1">util</span><span class="s2">.*</span>

<span class="s0">class </span><span class="s1">StatsActivity : AppCompatActivity</span><span class="s2">() {</span>

    <span class="s1">private lateinit </span><span class="s0">var </span><span class="s1">dailyChart: BarChart</span>
    <span class="s1">private </span><span class="s0">val </span><span class="s1">dao by lazy </span><span class="s2">{ </span><span class="s1">AppDatabase</span><span class="s2">.</span><span class="s1">getDatabase</span><span class="s2">(</span><span class="s0">this</span><span class="s2">).</span><span class="s1">focusSessionDao</span><span class="s2">() }</span>

    <span class="s1">override </span><span class="s0">fun </span><span class="s1">onCreate</span><span class="s2">(</span><span class="s1">savedInstanceState: Bundle?</span><span class="s2">) {</span>
        <span class="s0">super</span><span class="s2">.</span><span class="s1">onCreate</span><span class="s2">(</span><span class="s1">savedInstanceState</span><span class="s2">)</span>
        <span class="s1">setContentView</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">layout</span><span class="s2">.</span><span class="s1">activity_stats</span><span class="s2">)</span>

        <span class="s3">// Set the title for the activity</span>
        <span class="s1">title </span><span class="s2">= </span><span class="s4">&quot;Focus Statistics&quot;</span>

        <span class="s1">dailyChart </span><span class="s2">= </span><span class="s1">findViewById</span><span class="s2">(</span><span class="s1">R</span><span class="s2">.</span><span class="s1">id</span><span class="s2">.</span><span class="s1">dailyChart</span><span class="s2">)</span>
        <span class="s1">loadDailyData</span><span class="s2">()</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">loadDailyData</span><span class="s2">() {</span>
        <span class="s1">lifecycleScope</span><span class="s2">.</span><span class="s1">launch </span><span class="s2">{</span>
            <span class="s0">val </span><span class="s1">entries </span><span class="s2">= </span><span class="s1">ArrayList</span><span class="s2">&lt;</span><span class="s1">BarEntry</span><span class="s2">&gt;()</span>
            <span class="s0">val </span><span class="s1">labels </span><span class="s2">= </span><span class="s1">ArrayList</span><span class="s2">&lt;</span><span class="s1">String</span><span class="s2">&gt;()</span>
            <span class="s0">val </span><span class="s1">calendar </span><span class="s2">= </span><span class="s1">Calendar</span><span class="s2">.</span><span class="s1">getInstance</span><span class="s2">()</span>
            <span class="s0">val </span><span class="s1">dateFormat </span><span class="s2">= </span><span class="s1">SimpleDateFormat</span><span class="s2">(</span><span class="s4">&quot;EEE&quot;</span><span class="s2">, </span><span class="s1">Locale</span><span class="s2">.</span><span class="s1">getDefault</span><span class="s2">()) </span><span class="s3">// &quot;Mon&quot;, &quot;Tue&quot;, etc.</span>

            <span class="s0">for </span><span class="s2">(</span><span class="s1">i </span><span class="s0">in </span><span class="s5">6 </span><span class="s1">downTo </span><span class="s5">0</span><span class="s2">) {</span>
                <span class="s0">val </span><span class="s1">tempCal </span><span class="s2">= </span><span class="s1">calendar</span><span class="s2">.</span><span class="s1">clone</span><span class="s2">() </span><span class="s0">as </span><span class="s1">Calendar</span>
                <span class="s1">tempCal</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">Calendar</span><span class="s2">.</span><span class="s1">DAY_OF_YEAR</span><span class="s2">, -</span><span class="s1">i</span><span class="s2">)</span>
                <span class="s0">val </span><span class="s1">dayModifier </span><span class="s2">= </span><span class="s4">&quot;-</span><span class="s0">$</span><span class="s1">i </span><span class="s4">days&quot;</span>

                <span class="s0">val </span><span class="s1">totalSeconds </span><span class="s2">= </span><span class="s1">dao</span><span class="s2">.</span><span class="s1">getTotalFocusTimeForDay</span><span class="s2">(</span><span class="s1">dayModifier</span><span class="s2">) </span><span class="s1">?: </span><span class="s5">0</span>
                <span class="s0">val </span><span class="s1">totalMinutes </span><span class="s2">= </span><span class="s1">totalSeconds </span><span class="s2">/ </span><span class="s5">60f</span>

                <span class="s1">entries</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">BarEntry</span><span class="s2">( (</span><span class="s5">6 </span><span class="s2">- </span><span class="s1">i</span><span class="s2">).</span><span class="s1">toFloat</span><span class="s2">(), </span><span class="s1">totalMinutes</span><span class="s2">))</span>
                <span class="s1">labels</span><span class="s2">.</span><span class="s1">add</span><span class="s2">(</span><span class="s1">dateFormat</span><span class="s2">.</span><span class="s1">format</span><span class="s2">(</span><span class="s1">tempCal</span><span class="s2">.</span><span class="s1">time</span><span class="s2">))</span>
            <span class="s2">}</span>
            <span class="s1">setupChart</span><span class="s2">(</span><span class="s1">entries</span><span class="s2">, </span><span class="s1">labels</span><span class="s2">)</span>
        <span class="s2">}</span>
    <span class="s2">}</span>

    <span class="s1">private </span><span class="s0">fun </span><span class="s1">setupChart</span><span class="s2">(</span><span class="s1">entries: List</span><span class="s2">&lt;</span><span class="s1">BarEntry</span><span class="s2">&gt;, </span><span class="s1">labels: List</span><span class="s2">&lt;</span><span class="s1">String</span><span class="s2">&gt;) {</span>
        <span class="s3">// --- NEW: Get the correct text color from the current theme ---</span>
        <span class="s0">val </span><span class="s1">typedValue </span><span class="s2">= </span><span class="s1">android</span><span class="s2">.</span><span class="s1">util</span><span class="s2">.</span><span class="s1">TypedValue</span><span class="s2">()</span>
        <span class="s1">theme</span><span class="s2">.</span><span class="s1">resolveAttribute</span><span class="s2">(</span><span class="s1">com</span><span class="s2">.</span><span class="s1">google</span><span class="s2">.</span><span class="s1">android</span><span class="s2">.</span><span class="s1">material</span><span class="s2">.</span><span class="s1">R</span><span class="s2">.</span><span class="s1">attr</span><span class="s2">.</span><span class="s1">colorOnSurface</span><span class="s2">, </span><span class="s1">typedValue</span><span class="s2">, </span><span class="s0">true</span><span class="s2">)</span>
        <span class="s0">val </span><span class="s1">textColor </span><span class="s2">= </span><span class="s1">typedValue</span><span class="s2">.</span><span class="s1">data</span>
        <span class="s1">theme</span><span class="s2">.</span><span class="s1">resolveAttribute</span><span class="s2">(</span><span class="s1">com</span><span class="s2">.</span><span class="s1">google</span><span class="s2">.</span><span class="s1">android</span><span class="s2">.</span><span class="s1">material</span><span class="s2">.</span><span class="s1">R</span><span class="s2">.</span><span class="s1">attr</span><span class="s2">.</span><span class="s1">colorPrimary</span><span class="s2">, </span><span class="s1">typedValue</span><span class="s2">, </span><span class="s0">true</span><span class="s2">)</span>
        <span class="s0">val </span><span class="s1">barColor </span><span class="s2">= </span><span class="s1">typedValue</span><span class="s2">.</span><span class="s1">data</span>

        <span class="s0">val </span><span class="s1">dataSet </span><span class="s2">= </span><span class="s1">BarDataSet</span><span class="s2">(</span><span class="s1">entries</span><span class="s2">, </span><span class="s4">&quot;Focus Time (Minutes)&quot;</span><span class="s2">)</span>
        <span class="s1">dataSet</span><span class="s2">.</span><span class="s1">color </span><span class="s2">= </span><span class="s1">barColor </span><span class="s3">// Use theme's primary color</span>
        <span class="s1">dataSet</span><span class="s2">.</span><span class="s1">valueTextColor </span><span class="s2">= </span><span class="s1">textColor </span><span class="s3">// Apply theme text color</span>
        <span class="s1">dataSet</span><span class="s2">.</span><span class="s1">valueTextSize </span><span class="s2">= </span><span class="s5">12f</span>

        <span class="s0">val </span><span class="s1">barData </span><span class="s2">= </span><span class="s1">BarData</span><span class="s2">(</span><span class="s1">dataSet</span><span class="s2">)</span>
        <span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">data </span><span class="s2">= </span><span class="s1">barData</span>

        <span class="s3">// --- Apply theme text color to all chart elements ---</span>
        <span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">legend</span><span class="s2">.</span><span class="s1">textColor </span><span class="s2">= </span><span class="s1">textColor</span>

        <span class="s0">val </span><span class="s1">xAxis </span><span class="s2">= </span><span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">xAxis</span>
        <span class="s1">xAxis</span><span class="s2">.</span><span class="s1">textColor </span><span class="s2">= </span><span class="s1">textColor</span>
        <span class="s1">xAxis</span><span class="s2">.</span><span class="s1">valueFormatter </span><span class="s2">= </span><span class="s1">IndexAxisValueFormatter</span><span class="s2">(</span><span class="s1">labels</span><span class="s2">)</span>
        <span class="s1">xAxis</span><span class="s2">.</span><span class="s1">position </span><span class="s2">= </span><span class="s1">XAxis</span><span class="s2">.</span><span class="s1">XAxisPosition</span><span class="s2">.</span><span class="s1">BOTTOM</span>
        <span class="s1">xAxis</span><span class="s2">.</span><span class="s1">granularity </span><span class="s2">= </span><span class="s5">1f</span>
        <span class="s1">xAxis</span><span class="s2">.</span><span class="s1">setDrawGridLines</span><span class="s2">(</span><span class="s0">false</span><span class="s2">)</span>

        <span class="s0">val </span><span class="s1">yAxisLeft </span><span class="s2">= </span><span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">axisLeft</span>
        <span class="s1">yAxisLeft</span><span class="s2">.</span><span class="s1">textColor </span><span class="s2">= </span><span class="s1">textColor</span>
        <span class="s1">yAxisLeft</span><span class="s2">.</span><span class="s1">axisMinimum </span><span class="s2">= </span><span class="s5">0f </span><span class="s3">// Start Y-axis at 0</span>
        <span class="s1">yAxisLeft</span><span class="s2">.</span><span class="s1">setDrawGridLines</span><span class="s2">(</span><span class="s0">false</span><span class="s2">)</span>

        <span class="s0">val </span><span class="s1">yAxisRight </span><span class="s2">= </span><span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">axisRight</span>
        <span class="s1">yAxisRight</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s0">false </span><span class="s3">// Disable right Y-axis</span>

        <span class="s3">// General Chart Customization</span>
        <span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">description</span><span class="s2">.</span><span class="s1">isEnabled </span><span class="s2">= </span><span class="s0">false</span>
        <span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">animateY</span><span class="s2">(</span><span class="s5">1000</span><span class="s2">)</span>
        <span class="s1">dailyChart</span><span class="s2">.</span><span class="s1">invalidate</span><span class="s2">() </span><span class="s3">// Refresh the chart</span>
    <span class="s2">}</span>
<span class="s2">}</span></pre>
</body>
</html>