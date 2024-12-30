#!/bin/bash

# Chemins des dossiers
SRC_DIR="src"
BIN_DIR="bin"
MAIN_CLASS="app.App"
JAVA_FX_DIR="javafx-sdk-21.0.5/lib" # Remplace par le chemin vers ton dossier lib de JavaFX
JAVA_FX_LIBS="$JAVA_FX_DIR/*"  # On inclut toutes les bibliothèques de JavaFX

# Compiler les fichiers Java
javac -d "$BIN_DIR" --module-path "$JAVA_FX_DIR" --add-modules javafx.controls,javafx.fxml $(find "$SRC_DIR" -name "*.java")

# Exécuter le fichier App.class
java --module-path "$JAVA_FX_DIR" --add-modules javafx.controls,javafx.fxml -cp "$BIN_DIR" "$MAIN_CLASS"
