#!/bin/sh

TERM=xterm-color
export TERM

KOLJA_HOME=.
export KOLJA_HOME

if [ -n "$LESS_CP" ]; then 
	CP=$LESS_CP
else
	CP=$KOLJA_HOME/IGNORED
	for i in $KOLJA_HOME/lib/*.jar; do
		CP=$CP:$i 
	done
fi

java -Djava.awt.headless=true -Djava.library.path=$KOLJA_HOME/lib/ -Djcurses.lib.dir=$KOLJA_HOME/lib/ -cp $CP com.baulsupp.less.LessMain $*

stty sane
