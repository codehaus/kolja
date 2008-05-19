#!/bin/sh

export TERM=xterm-color

if [ -n "$LESS_CP" ]; then 
	CP=$LESS_CP
else
	CP=$KOLJA_HOME/IGNORED
	for i in $KOLJA_HOME/lib/*.jar; do
		CP=$CP:$i 
	done
fi

java -Djava.awt.headless=true -Djava.library.path=lib/ -cp $CP com.baulsupp.less.LessMain $*

stty sane
