#!/bin/sh

if [ -n "$TAIL_CP" ]; then 
	CP=$TAIL_CP
else
	CP=lib/curses-list.jar:lib/kolja-curses.jar
	CP=$CP:lib/gloogy.jar:lib/kolja-viewer.jar
	CP=$CP:lib/kolja-ansi.jar:lib/kolja-util.jar:lib/jline.jar
	CP=$CP:lib/spring-beans.jar:lib/spring-context.jar:lib/spring-core.jar
	CP=$CP:lib/jcurses.jar:lib/commons-primitives.jar:lib/commons-logging.jar
	CP=$CP:lib/log4j.jar:lib/commons-lang.jar
	CP=$CP:lib/commons-cli.jar
fi

java -Djava.awt.headless=true -cp $CP com.baulsupp.kolja.ansi.TailMinusEffMain $*
