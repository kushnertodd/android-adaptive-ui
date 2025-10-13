use strict;
use warnings;

my $filename = "packages_new_components.txt";
open(my $fh, '<', $filename) or die "Could not open file '$filename': $!";
my $count = 0;
while (my $line = <$fh>) {
    chomp $line; # Remove the trailing newline character
    if ($line =~ /^(.*)\t(.*)$/) {
        print STDERR "read line: $line\n";
        print <<EOF;
            $count -> component = ComponentName(
                        "$1",
                        "$2"
                    )     
EOF
    $count++
    } else {
        print STDERR "bad format: $line\n";
    }
}
