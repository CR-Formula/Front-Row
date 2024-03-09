#!/bin/bash

VM_OPTIONS="--add-exports java.base/java.lang=ALL-UNNAMED \
            --add-exports java.desktop/sun.awt=ALL-UNNAMED \
            --add-exports java.desktop/sun.java2d=ALL-UNNAMED \
            -Dsun.java2d.opengl=true \
            -Djogl.GLContext.shareContextCache=false \
            -Djogl.GLContext.numContentHandles=32 \
            -Djogl.GLSharedContextSetter=full \
            -Dsun.java2d.noddraw=true"

JAVA_EXECUTABLE="java"

JAR_FILE="target/Front-Row-1.0.0-jar-with-dependencies.jar"

"$JAVA_EXECUTABLE" $VM_OPTIONS -jar "$JAR_FILE"
