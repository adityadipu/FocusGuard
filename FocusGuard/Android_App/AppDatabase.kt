<html>
<head>
<title>AppDatabase.kt</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.s0 { color: #cf8e6d;}
.s1 { color: #bcbec4;}
.s2 { color: #bcbec4;}
.s3 { color: #2aacb8;}
.s4 { color: #6aab73;}
</style>
</head>
<body bgcolor="#1e1f22">
<table CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#606060" >
<tr><td><center>
<font face="Arial, Helvetica" color="#000000">
AppDatabase.kt</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">iot</span><span class="s2">.</span><span class="s1">focusguardapp</span>

<span class="s1">import android</span><span class="s2">.</span><span class="s1">content</span><span class="s2">.</span><span class="s1">Context</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">Database</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">Room</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">RoomDatabase</span>

<span class="s1">@Database</span><span class="s2">(</span><span class="s1">entities </span><span class="s2">= [</span><span class="s1">FocusSession::</span><span class="s0">class</span><span class="s2">], </span><span class="s1">version </span><span class="s2">= </span><span class="s3">1</span><span class="s2">, </span><span class="s1">exportSchema </span><span class="s2">= </span><span class="s0">false</span><span class="s2">)</span>
<span class="s1">abstract </span><span class="s0">class </span><span class="s1">AppDatabase : RoomDatabase</span><span class="s2">() {</span>
    <span class="s1">abstract </span><span class="s0">fun </span><span class="s1">focusSessionDao</span><span class="s2">()</span><span class="s1">: FocusSessionDao</span>

    <span class="s1">companion </span><span class="s0">object </span><span class="s2">{</span>
        <span class="s1">@Volatile</span>
        <span class="s1">private </span><span class="s0">var </span><span class="s1">INSTANCE: AppDatabase? </span><span class="s2">= </span><span class="s0">null</span>

        <span class="s0">fun </span><span class="s1">getDatabase</span><span class="s2">(</span><span class="s1">context: Context</span><span class="s2">)</span><span class="s1">: AppDatabase </span><span class="s2">{</span>
            <span class="s0">return </span><span class="s1">INSTANCE ?: synchronized</span><span class="s2">(</span><span class="s0">this</span><span class="s2">) {</span>
                <span class="s0">val </span><span class="s1">instance </span><span class="s2">= </span><span class="s1">Room</span><span class="s2">.</span><span class="s1">databaseBuilder</span><span class="s2">(</span>
                    <span class="s1">context</span><span class="s2">.</span><span class="s1">applicationContext</span><span class="s2">,</span>
                    <span class="s1">AppDatabase::</span><span class="s0">class</span><span class="s2">.</span><span class="s1">java</span><span class="s2">,</span>
                    <span class="s4">&quot;focus_guard_database&quot;</span>
                <span class="s2">).</span><span class="s1">build</span><span class="s2">()</span>
                <span class="s1">INSTANCE </span><span class="s2">= </span><span class="s1">instance</span>
                <span class="s1">instance</span>
            <span class="s2">}</span>
        <span class="s2">}</span>
    <span class="s2">}</span>
<span class="s2">}</span></pre>
</body>
</html>