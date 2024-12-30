# Chemins des dossiers
SRC_DIR="src"
BIN_DIR="bin"
MAIN_CLASS="partie2.Main"

# Compiler les fichiers Java de partie1 et partie2 (sans inclure app)
javac -d "$BIN_DIR" $(find "$SRC_DIR/partie1" "$SRC_DIR/partie2" -name "*.java")


# Vérifier le nombre d'arguments
if [ $# -gt 0 ]; then
    # Exécuter le fichier Main.class en passant l'argument s'il existe
    java -cp "$BIN_DIR" "$MAIN_CLASS" "$1"
else
    # Exécuter le fichier Main.class sans arg
    java -cp "$BIN_DIR" "$MAIN_CLASS"
fi