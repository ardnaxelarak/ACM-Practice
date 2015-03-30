#!/bin/bash

if [ $# -eq 0 ] ; then
    exit 1
fi
class=${1%.*}

if [ $# -eq 1 ] ; then
    inputs=$class
else
    inputs=$2
fi

javac "$class.java"

if [ $? -ne 0 ] ; then
    exit 1
fi

if [ $# -eq 2 ] ; then
    location=cases
else
    location=$3
fi

for file in $location/$inputs-*.in ; do
    root=${file%.in}
    if [ -e "$file" ] ; then
        if [ -e "$root.ans" ] ; then
            echo "<<< $file diff >>>"
            time java -client -Xss8m -Xmx2048m "$class" < "$file" | diff - "$root.ans"
        else
            echo "<<< $file >>>"
            time java -client -Xss8m -Xmx2048m "$class" < "$file"
        fi
    fi
done
