<?php

header('Content-Type: text/plain');

$dir = "/tmp/conversation/";
$handle = opendir("$dir");
while (false !== ($current = readdir($handle))) {
	if ($current != "." && $current != "..") {
		$handle2 = opendir("$dir$current");
		while (false !== ($file = readdir($handle2))) {
			if ($file != "." && $file != "..") {
				unlink("$dir$current/$file");
				print "$dir$current/$file\n";
			}
		}
		closedir($handle2);
		rmdir("$dir$current");
		print "$dir$current\n";
	}
}
closedir($handle);
?>
