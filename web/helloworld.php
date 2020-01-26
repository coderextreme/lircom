<?php

header('Content-Type: text/plain');

$dir = "/tmp/conversation/";
if (!file_exists($dir)) {
	mkdir($dir);
}
$current = $_POST['nick'];
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
				$fh = fopen($fn, "r");
				if ($fh && filesize($fn) > 0) {
					$output .= fread($fh, filesize("$fn"));
					fclose($fh);
				}
				$fh = fopen("$fn", "w");
				if ($fh) {
					fclose($fh);
				}
			}
		}
		closedir($handle);
	}
	echo $output;
#}
if ($_POST['remove'] == 'true') {
	$handle = opendir("$dir$current");
	while (false !== ($file = readdir($handle))) {
		if ($file != "." && $file != "..") {
			unlink("$dir$current/$file");
		}
    	}
	rmdir("$dir$current");
} else if ($_POST['text'] != "") {
	$line = "<$current> ".$_POST['text'];
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
?>
