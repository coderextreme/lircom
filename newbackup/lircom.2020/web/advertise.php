<?php
	$filename = "advertisements.txt";
	$nick = $_GET['nick'];
	$port = $_GET['port'];

	if ($_SERVER['REMOTE_HOST']) {
		$host = $_SERVER['REMOTE_HOST'];
	} else {
		$host = $_SERVER['REMOTE_ADDR'];
	}
	$date = date("r");

	if ($nick && $port) {
		$handle = fopen($filename, "a+");
		if ($handle) {
			fwrite($handle, "$host|$port|$nick|$date\n");
			fclose($handle);
		}
	}
	$handle = fopen($filename, "r");
	if ($handle && filesize($filename) > 0) {
		$contents = fread($handle, filesize($filename));
		echo $contents;
		fclose($handle);
	}
?>
