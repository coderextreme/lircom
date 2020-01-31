#!/usr/bin/perl

$|=1;
$on = 0;
open (STDIN,"/usr/sbin/tcpdump -lnx -s 1500 port 80 |");
$packet = "";
while (<>) {
	if (/^[0-9]/) {
		# print;
		if (/ P /) {
			$packet =~ s/%([0-9A-F]{2})/chr(hex($1))/eg;
			if ($packet =~ /^(HTTP|GET|POST)/) {
				print "--------------------------------------------------------------------------------";
			}
			print "$packet\n";
			$packet = "";
			$on = 1;
		} else {
			$on = 0;
		}
	}
	if ($on) {
		if (/0x(....):/) {
			# print;
			@hex = /0x(....):  (....) ?(....)? ?(....)? ?(....)? ?(....)? ?(....)? ?(....)? ?(....)?/;
			$d = hex($1);
			if ($d > 32) {
				if ($d == 48) {
					$hex = "$4$5$6$7$8$9";
				} else {
					$hex = "$2$3$4$5$6$7$8$9";
				}
				$_ = $hex;
				#s/\s+//;
				s/([0-9a-f]{2})/chr(hex($1))/eg;
				#tr/\x1F-\x7E\r\n//cd;
				$packet .= $_;
			}
		}
	}
}
