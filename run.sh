#!/bin/bash

javac $1.java

for file in ./$1.in* ; do
    if [ -e "$file" ] ; then
        echo "<<< $file >>>"
        time java $1 < $file
    fi
done
