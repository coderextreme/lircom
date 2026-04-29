#!/bin/zsh
#
# TODO broken, no class
export PATH_TO_FX=openjfx-22_windows-x64_bin-sdk/javafx-sdk-22/lib
export PATH_TO_FX_MOD=javafx-jmods-22

source env.sh

export DWN=${USER_HOME}/Downloads
export JAVAFX_LIB=${DWN}/openjfx-18.0.2_windows-x64_bin-sdk/javafx-sdk-18.0.2/lib

pushd src/main/java
${JAVAC} --module-path $JAVAFX_LIB \
	--add-modules javafx.controls,javafx.fxml \
	--add-exports java.base/jdk.internal.misc=ALL-UNNAMED\
	 -cp "${JAVAFX_LIB}/javafx.base.jar;${JAVAFX_LIB}/javafx.controls.jar;${JAVAFX_LIB}/javafx.fxml.jar;${JAVAFX_LIB}/javafx.graphics.jar;${JAVAFX_LIB}/javafx.media.jar;${JAVAFX_LIB}/javafx.swing.jar;${JAVAFX_LIB}/javafx.web.jar;${JAVAFX_LIB}/javafx-swt.jar;." lircom/HTMLEditorSample.java
echo done compiling

${JAVA} --module-path ${JAVAFX_LIB} \
	--add-modules javafx.controls,javafx.fxml \
	--add-exports java.base/jdk.internal.misc=ALL-UNNAMED \
	--add-exports javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED \
	--add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED \
	--add-exports javafx.controls/com.sun.javafx.scene.control.inputmap=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED \
	--add-exports javafx.base/com.sun.javafx.logging=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.prism=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.geom.transform=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.font=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.text=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.scene.text=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.scene.input=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.scene.traversal=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.javafx.geom=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.prism.paint=ALL-UNNAMED \
	--add-exports javafx.graphics/com.sun.scenario.effect=ALL-UNNAMED \
	-cp "${JAVAFX_LIB}javafx.base.jar;${JAVAFX_LIB}/javafx.controls.jar;${JAVAFX_LIB}/javafx.fxml.jar;${JAVAFX_LIB}/javafx.graphics.jar;${JAVAFX_LIB}/javafx.media.jar;${JAVAFX_LIB}/javafx.swing.jar;${JAVAFX_LIB}/javafx.web.jar;${JAVAFX_LIB}/javafx-swt.jar;." lircom.HTMLEditorSample
