# Application d'itinéraire

## Configuration du fichier launch.json :

{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Accueildemo",
            "request": "launch",
            "mainClass": "src.ihm.Accueildemo",
            "projectName": "B6_e071b73",
            "vmArgs" : "--module-path "JAVAFX/LIB" --add-modules=javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED",
        }
    ]
}

## Configuration du fichier settings.json :

{
    "java.project.referencedLibraries": [
        "JAVAFX/lib/*.jar",
        ".../B6/lib/*.jar"
    ],
    "java.project.sourcePaths": [
        "."
    ],
    "java.project.outputPath": "bin",
    "java.debug.settings.console": "internalConsole"
}

## Execution du .jar

Pour executer le .jar, voici la ligne de commande à taper :

java --module-path CHEMIN-VERS/javafx-sdk-22.0.1/lib --add-modules javafx.controls,javafx.fxml --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED -jar B6.jar