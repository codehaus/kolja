#!/bin/sh

export TERM=xterm-color

if [ -n "$LESS_CP" ]; then 
	CP=$LESS_CP
else
	CP=lib/curses-list.jar:lib/kolja-curses.jar
	CP=$CP:lib/gloogy.jar:kolja-viewer/target/kolja-viewer.jar
	CP=$CP:lib/kolja-ansi.jar
	CP=$CP:lib/jcurses.jar:lib/commons-primitives.jar
	CP=$CP:lib/log4j.jar:lib/commons-lang.jar
	CP=$CP:lib/commons-cli.jar
fi

java -Djava.awt.headless=true -Djava.library.path=lib/ -cp $CP com.baulsupp.less.LessMain $*

stty sane
