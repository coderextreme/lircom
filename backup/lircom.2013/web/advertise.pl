#!E:/perl/bin/perl
#!/usr/bin/perl

use CGI;

print "Content-type: text/plain\n\n";

$query = new CGI;

if ($query->param('nick')) {
	$nick = $query->param('nick');
}
if ($query->param('port')) {
	$port = $query->param('port');
}

$host = $ENV{'REMOTE_HOST'};
if (!$host) {
	$host = $ENV{'REMOTE_ADDR'};
}

($sec,$min,$hour,$mday,$mon,$year,$wday,$yday) = gmtime(time);

$date = ($year+1900)."-".($mon+1)."-$mday $hour:$min:$sec";

if ($nick && $port) {
	open(SERVERS, ">>servers.txt");
	print SERVERS $host."|".$port."|".$nick."|".$date."\n";
	close(SERVERS);
}

open(SERVERS,"servers.txt");
while (<SERVERS>) {
	$line = $_;
	@line = split(/|/);
	$key = join("|", ($nick, $host, $port));
	$servers{$key} = $_;
}
close(SERVERS);

open(SERVERS, ">servers.txt");
foreach $key (keys %servers) {
	print SERVERS $servers{$key};
	print $servers{$key};
}
close(SERVERS);
