#!/bin/bash

javac $1.java

if [ $? -ne 0 ] ; then
    exit 1
fi

for file in ./$1.in* ; do
    if [ -e "$file" ] ; then
        echo "<<< $file >>>"
        time java $1 < $file
    fi
done
