#!perl

for $file (@ARGV) {
	open(F, $file);
	while (<F>) {
		s/Ace/0x1/;
		s/Two/0x2/;
		s/Three/0x4/;
		s/Four/0x8/;
		s/Five/0x10/;
		s/Six/0x20/;
		s/Seven/0x40/;
		s/Eight/0x80/;
		s/Nine/0x100/;
		s/Ten/0x200/;
		s/Jack/0x400/;
		s/Queen/0x800/;
		s/King/0x1000/;

		s/Clubs/0x2000/;
		s/Diamonds/0x4000/;
		s/Hearts/0x8000/;
		s/Spades/0x10000/;
		
		s/PICK/0x20000/;
		s/PLAY/0x40000/;
		s/NEWSTACK/0x80000/;
		s/DELSTACK/0x100000/;
		s/INVISIBLE/0x200000/;
		s/VISIBLE/0x400000/;

		print;
	}
	close(F);
}
