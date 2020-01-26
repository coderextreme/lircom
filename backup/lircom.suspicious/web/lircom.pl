#!/usr/bin/perl

print "Content-type: application/x-java-jnlp-file\n\n";

open (JNLP, "lircom.jnlp");

while (<JNLP>) {
	print;
}
close(JNLP);
