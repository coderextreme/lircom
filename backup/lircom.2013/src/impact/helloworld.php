<?php

header('Content-Type: text/plain');

$dir = "/tmp/conversation/";
if (!file_exists($dir)) {
	mkdir($dir);
}
$current = $_GET['nick'];
if (preg_match("/^[A-Za-z0-9]+$/", $current) == 0) {
	echo "pick a different nickname than $current\n";
	return;
}
if (!file_exists("$dir$current")) {
	mkdir("$dir$current");
}

$output = "";
#while ($output == "") {
	$handle = opendir("$dir$current");
	if ($handle) {
		while (false !== ($file = readdir($handle))) {
			if ($file != "." && $file != "..") {
				$fn = "$dir$current/$file"; 
				$fh = fopen($fn, "r+");
				if ($fh && filesize($fn) > 0) {
					flock($fh, LOCK_EX);
					$output .= fread($fh, filesize("$fn"));
					ftruncate($fh, 0);
					flock($fh, LOCK_UN);
					fclose($fh);
				}
			}
		}
		closedir($handle);
	}
	echo $output;
#}
if ($_GET['text'] != "") {
	$line = $_GET['text']."\n";
	$handle = opendir($dir);
	while (false !== ($file = readdir($handle))) {
		if ($file != "." && $file != ".." && $file != $current) {
			$fh = fopen("$dir$file/$current", 'a');
			if ($fh) {
				fwrite($fh, $line);
				fclose($fh);
			}
		}
	}
	closedir($handle);
}
if ($_GET['remove'] != "") {
	$handle = opendir("$dir$current");
	while (false !== ($file = readdir($handle))) {
		if ($file != "." && $file != "..") {
			unlink("$dir$current/$file");
		}
    	}
	if (rmdir("$dir$current")) {
		echo "removed $dir$current";
	}
}
?>
