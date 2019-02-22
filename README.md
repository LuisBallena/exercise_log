# Code Review / Refactoring

Rpta 1: 
-El método "LogMessage" realiza demasiadas funcionalidades motivo por el cual debe refactorizarse.
-Existe una variable "String l" la cual nunca es utilizado su valor
-Los parámetros de entrada son muy redundantes (logToConsole, logToFile, LogToDatabase, etc.)
-Se identifica que cada vez que se procede a realizar una traza, se procede a instanciar o a levantar toda la integración (ejemplo la conexión a la base de datos).
-No se tiene un punto de cierre cuando se realiza los insert a la base de datos (en jdbc se indica que siempre se ejecute el close para un statement o preparestament)

Recomendaciones y/o Corrección
-Con respecto al método "logMessage" se deberia separar todas las integraciones que tiene (console,database y file), estas deberian estar heredando de una clase abstracta, la cual gestione los parámetros de entradas generales que puedan existir. Con esto se indica que cada integracion deberia ser una clase por separado y para su creación y/o utilización deberia estar usandose una clase factoria. Con respecto a los niveles (etiquetas de los mensajes que se indican ejemplo "error","warning" ,etc.) y al tratamiento del contenido del mensaje debe estar gestionado por una clase superior o una clase Manager.

-Con respecto a los parámetros de entrada de integracion puede usarse un arreglo de String el cual tenga referenciado un Enum el cual permita ayudar a un mayor entendimiento del código (esto en un futuro permitira una mejor referencia con un archivo properties).

-Con respecto a los parámetros de niveles (error, warning, message) tambien deberia usarse un enum (esto en un futuro permitira una mejor referencia con un archivo properties).

-Con respecto a que cuando se realizaba una traza y este instanciaba nuevamente todas las conexiones, en las clases separadas ya se deberia contar con un metodo init y que sea la clase Manager que los ejecute en un único momento (esto se podria tambien ser ha demanda y bajo el patrón singleton).

-Siendo más práctico en algunos casos se ha mejorado ciertas clases y se ha separado las constantes más significativas para no tener valores mágicos.