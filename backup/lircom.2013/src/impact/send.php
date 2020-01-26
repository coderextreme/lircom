<?php
header('Content-Type: text/plain');

$dir = "/tmp/conversation/";
if (!file_exists($dir)) {
	mkdir($dir);
}
$current = $_GET['nick'];
if (preg_match("/^[A-Za-z0-9]+$/", $current) == 0) {
	echo "pick a simpler nickname\n";
	return;
}
if ($_POST['remove'] == 'true') {
	$handle = opendir("$dir$current");
	while (false !== ($file = readdir($handle))) {
		if ($file != "." && $file != "..") {
			unlink("$dir$current/$file");
		}
    	}
	rmdir("$dir$current");
} else {
	if (!file_exists("$dir$current")) {
		mkdir("$dir$current");
	}

	$line = "<$current> ".$_GET['text'];
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
