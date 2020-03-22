# Acciones/Stocks

## Nota 1

Este repositorio provee varias herramientas para ayudar a la gestión de acciones en relación a las distintas declaraciones a realizar en España. Debido a esto, el código y demás recursos están en español para utilizar la misma terminología (o similar) que las leyes españolas.

This repository provides several tools to help the management of stocks regarding Spanish declarations and Spanish taxes. Because of that, the code and other resources use Spanish language, in order to use the same (or similar) terminology that the used by Spanish law.

## Nota 2

**No nos responsabilizamos de los posibles errores que puedan tener las herramientas o demás recursos contenidos en este repositorio, ni de un uso inadecuado.**


## Requisitos

 * java
 * maven
 
## Ejecución

La versión actual se basa en la ejecución de un test unitario que genera la salida por consola:
* Modelo 720
    * Edita el fichero `Modelo720ProcesadorCustomTest` especificando los ficheros CSV que contienen las transacciones realizadas.
    * Ejecuta `mvn test -Dtest=Modelo720ProcesadorCustomTest#test`
    * En los casos de origen M o A, la valoración es 0. En la declaración la valoración será `<numValores>*<precioAccion31Diciembre>`
