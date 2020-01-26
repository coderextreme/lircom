<?php

header('Content-Type: text/plain');

$dir = "/tmp/conversation/";
$current = $_GET['nick'];
if (preg_match("/^[A-Za-z0-9]+$/", $current) == 0) {
	echo "pick a simpler nickname\n";
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
?>
