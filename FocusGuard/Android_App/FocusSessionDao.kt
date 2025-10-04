<html>
<head>
<title>FocusSessionDao.kt</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.s0 { color: #cf8e6d;}
.s1 { color: #bcbec4;}
.s2 { color: #bcbec4;}
.s3 { color: #7a7e85;}
.s4 { color: #6aab73;}
</style>
</head>
<body bgcolor="#1e1f22">
<table CELLSPACING=0 CELLPADDING=5 COLS=1 WIDTH="100%" BGCOLOR="#606060" >
<tr><td><center>
<font face="Arial, Helvetica" color="#000000">
FocusSessionDao.kt</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">iot</span><span class="s2">.</span><span class="s1">focusguardapp</span>

<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">Dao</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">Insert</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">Query</span>

<span class="s1">@Dao</span>
<span class="s0">interface </span><span class="s1">FocusSessionDao </span><span class="s2">{</span>
    <span class="s1">@Insert</span>
    <span class="s1">suspend </span><span class="s0">fun </span><span class="s1">insert</span><span class="s2">(</span><span class="s1">session: FocusSession</span><span class="s2">)</span>

    <span class="s3">// Query to get total focus time for a specific day</span>
    <span class="s1">@Query</span><span class="s2">(</span><span class="s4">&quot;SELECT SUM(durationSeconds) FROM focus_sessions WHERE type = 'FOCUS' AND date(timestamp / 1000, 'unixepoch') = date('now', :dayModifier)&quot;</span><span class="s2">)</span>
    <span class="s1">suspend </span><span class="s0">fun </span><span class="s1">getTotalFocusTimeForDay</span><span class="s2">(</span><span class="s1">dayModifier: String</span><span class="s2">)</span><span class="s1">: Int?</span>

    <span class="s3">// Query to get total focus time for a specific month</span>
    <span class="s1">@Query</span><span class="s2">(</span><span class="s4">&quot;SELECT SUM(durationSeconds) FROM focus_sessions WHERE type = 'FOCUS' AND strftime('%Y-%m', datetime(timestamp / 1000, 'unixepoch')) = :yearMonth&quot;</span><span class="s2">)</span>
    <span class="s1">suspend </span><span class="s0">fun </span><span class="s1">getTotalFocusTimeForMonth</span><span class="s2">(</span><span class="s1">yearMonth: String</span><span class="s2">)</span><span class="s1">: Int?</span>
<span class="s2">}</span></pre>
</body>
</html>