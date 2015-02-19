# Prácticas Máster en Big Data
## Universidad Politécnica de Valencia

# Instrucciones de instalacción.

- Descargar el proyecto
``git clone ``
- Compilar el proyecto
Ir a la raíz del proyecto y ejecutar:
`mvn clean install -DskipTests`

- Crear los directorios necesarios para la práctica. Si no tenemos permisos en nuestro proyecto en los directorios que se nombran a continuación se puede
cambiar por otros. En este caso habrá que modificar los atributos de la clase src/main/java/com/lambdooop/training_bd_rt/utils/LambdaConstants.java

Para local en streaming:
mkdir -p /root/tmp/spark/input/streaming/
mkdir -p /root/tmp/spark/output/streaming/

Para local en batch:
mkdir -p /root/tmp/spark/input/batch/
mkdir -p /root/tmp/spark/output/batch/


- Descargar algunos ficheros de prueba del proyecto Gutenberg
Navegar al directorio input (/root/tmp/spark/input/batch/) para batch y ejecutar:
wget -nd -r -l 10 -A.txt ftp://ftp.ibiblio.org/pub/docs/books/gutenberg/
Dejarlo un rato (ojo son 37GB ...)

- Vamos a ejecutar el exporter en local para batch (Ir a la clase ExporterLocalSparkBatch y ver instrucciones)
- Vamos a ejecutar la clase SparkSQLLocal (ver instrucciones) y vamos a ver el resultado obtenido en el exporter batch.
- Vamos a ejecutar la clase ExporterLocalSparkStreaming (ver instrucciones) y vamos a ver como la serving layer se ha actualizado.
- Vamos a ejecutar la clase SparkSQLLocal (ver instrucciones) y vamos a ver como la serving layer se ha actualizado.

- Comprobar consola de Spark UI en localhost:4040/