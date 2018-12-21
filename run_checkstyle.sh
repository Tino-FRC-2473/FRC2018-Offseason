#!/bin/bash

# Download checkstyle from https://github.com/checkstyle/checkstyle/releases
# Put the path to the jar file here
CHECKSTYLE_PATH="$HOME/Desktop/checkstyle-8.15-all.jar"

java -jar $CHECKSTYLE_PATH -c checkstyle_rules.xml src