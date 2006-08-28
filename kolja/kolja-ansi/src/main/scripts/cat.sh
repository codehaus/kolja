#!/bin/sh

if [ -n "$TAIL_CP" ]; then 
	CP=$TAIL_CP
else
	CP=$KOLJA_HOME/IGNORED
	for i in $KOLJA_HOME/lib/*.jar; do
		CP=$CP:$i 
	done
fi

java -Djava.awt.headless=true -cp $CP com.baulsupp.kolja.ansi.CatMain $*
