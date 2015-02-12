#!/bin/bash

if [ ! -d bin ]
then
    if ! mkdir bin
    then
        cat<<EOF>&2
$0 error in 'mkdir bin'
EOF
        exit 1
    fi
fi

if ! javac -d bin $(find src -type f -name '*.java')
then
    cat<<EOF>&2
$0 error in 'javac -d bin $(find src -type f -name '*.java')'
EOF
exit 1
fi
