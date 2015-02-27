#!/bin/bash

class=${1%.*}
inputs=$2
javac "$class.java"

if [ $? -ne 0 ] ; then
    exit 1
fi

for file in judging/$inputs-*.in ; do
    root=${file%.in}
    if [ -e "$file" -a -e "$root.ans" ] ; then
        echo "<<< $file >>>"
        time java -client -Xss8m -Xmx2048m "$class" < "$file" | diff - "$root.ans"
        # if [ $? -ne 0 ] ; then
            # exit 1
        # fi
    fi
done
