use strict;
use warnings;

my $filename = "component_declarations.txt";
open(my $fh, '<', $filename) or die "Could not open file '$filename': $!";
my $count = 0;
while (my $line = <$fh>) {
    chomp $line; # Remove the trailing newline character
    if ($line =~ /(\w+)\t/) {
        print STDERR "read line: $line\n";
        print "      $count -> \"".ucfirst($1)."\"\n";
        $count++
    } else {
        print STDERR "bad format: $line\n";
    }
}
