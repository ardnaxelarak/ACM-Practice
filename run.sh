#!/bin/bash

class=${1%.*}
javac "$class.java"

if [ $? -ne 0 ] ; then
    exit 1
fi

for file in inputs/$class.in* ; do
    if [ -e "$file" ] ; then
        echo "<<< $file >>>"
        time java "$class" < "$file"
    fi
done
