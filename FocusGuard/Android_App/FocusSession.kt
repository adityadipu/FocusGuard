<html>
<head>
<title>FocusSession.kt</title>
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
FocusSession.kt</font>
</center></td></tr></table>
<pre><span class="s0">package </span><span class="s1">com</span><span class="s2">.</span><span class="s1">iot</span><span class="s2">.</span><span class="s1">focusguardapp</span>

<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">Entity</span>
<span class="s1">import androidx</span><span class="s2">.</span><span class="s1">room</span><span class="s2">.</span><span class="s1">PrimaryKey</span>

<span class="s1">@Entity</span><span class="s2">(</span><span class="s1">tableName </span><span class="s2">= </span><span class="s3">&quot;focus_sessions&quot;</span><span class="s2">)</span>
<span class="s1">data </span><span class="s0">class </span><span class="s1">FocusSession</span><span class="s2">(</span>
    <span class="s1">@PrimaryKey</span><span class="s2">(</span><span class="s1">autoGenerate </span><span class="s2">= </span><span class="s0">true</span><span class="s2">)</span>
    <span class="s0">val </span><span class="s1">id: Int </span><span class="s2">= </span><span class="s4">0</span><span class="s2">,</span>
    <span class="s0">val </span><span class="s1">timestamp: Long</span><span class="s2">,</span>
    <span class="s0">val </span><span class="s1">durationSeconds: Int</span><span class="s2">,</span>
    <span class="s0">val </span><span class="s1">type: String </span><span class="s5">// &quot;FOCUS&quot; or &quot;BREAK&quot;</span>
<span class="s2">)</span></pre>
</body>
</html>