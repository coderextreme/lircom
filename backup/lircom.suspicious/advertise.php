<?php
$file = fopen("servers.txt", "r");
$servers = array();
while ($line = fgets($file)) {
	$values = explode("|", $line);
	$host = $values[0];
	$port = $values[1];
	$nick = $values[2];
	$key = implode("|", array($nick, $host, $port));
	$servers[$key] = $line;
}
fclose($file);

if ($_GET['nick']) {
	$nick = $_GET['nick'];
}
if ($_GET['port']) {
	$port = $_GET['port'];
}

$host = $_ENV['REMOTE_HOST'];
if (!$host) {
	$host = $_ENV['REMOTE_ADDR'];
}

if ($nick && $port) {
	$key = implode("|", array($nick, $host, $port));
	$tm = array();
	$tm = localtime(time(), true);

	$date = ($tm[tm_year]+1900)."-".($tm[tm_mon]+1)."-$tm[tm_mday] $tm[tm_hour]:$tm[tm_min]:$tm[tm_sec]";
	$servers[$key] = $host."|".$port."|".$nick."|".$date."\n";
}

$file = fopen("servers.txt", "w");
foreach ($servers AS $key => $value) {
	fwrite($file, $value);
	echo $value;
}
fclose($file);
?>
