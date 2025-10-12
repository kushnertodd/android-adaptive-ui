#!/bin/bash
name_dir=$1
name_file=$name_dir.txt
if [ -z "$name_dir" ] ; then
  echo usage: $0 package-file-root
  exit
fi
if [ ! -e "$name_file" ] ; then
  echo missing package-file "'$name_file'"
  echo usage: $0 package-file-root
  exit
fi
name_file=$name_dir.txt
if [ -d "$name_dir" ] ; then
   echo rm -f $name_dir/\*
   rm -f $name_dir/*
elif [ -f "$name_dir" ] ; then
   echo $name_dir is a file
else
   #echo no $name_dir
   mkdir $name_dir
fi
for f in `cat $name_dir.txt`
do 
  echo $f
  #echo sh ../bin/package.sh $f \>$name_dir/$f.txt
  sh ../../bin/package.sh $f >$name_dir/$f.txt
  cd $name_dir
  ../../../../bin/components.sh
  sh ../../../../bin/components.sh > ../${name_dir}_components.txt
  cd ..
done
