for f in `cat package_names.txt`; do echo $f; sh ../bin/package.sh $f >apps_full/$f.txt; done
