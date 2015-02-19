# Prácticas Máster en Big Data
## Universidad Politécnica de Valencia

# Instrucciones de instalacción.

- Descargar el proyecto
`git clone git@github.com:IvanFernandez/SparkLambdaUPV.git`
- Compilar el proyecto
Ir a la raíz del proyecto y ejecutar:
`mvn clean install -DskipTests`

## Introducción al streaming (local)
- Ejecutar la clase	EasySparkStreaming para local
spark-submit --class com.lambdooop.training_bd_rt.streaming.EasySparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
- Levantar netcat en el puerto 9999
	 nc -lk 9999
- Ver la salida por consola 

## Introducción al streaming (HDFS)
- Ejecutar la clase	EasyHDFSSparkStreaming para local
spark-submit --class com.lambdooop.training_bd_rt.streaming.EasyHDFSSparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar
- Levantar netcat en el puerto 9999
	 nc -lk 9999
- Ver la salida por consola 	 
	 
## Lambda
El proyecto consiste en hacer una Lambda donde:
- La parte batch ejecutará un map-reduce batch respecto de un fichero de entrada.
- La parte streaming ejecutará un map-reduce streaming similar a la de la clase batch.
- La serving layer consultará (son SparkSQL) el resultado de la Lambda.

### Setup
- Crear los directorios necesarios para la práctica. Si no tenemos permisos en nuestro proyecto en los directorios que se nombran a continuación se puede
cambiar por otros. En este caso habrá que modificar los atributos de la clase src/main/java/com/lambdooop/training_bd_rt/utils/LambdaConstants.java

Para local en streaming:
mkdir -p /root/tmp/spark/input/streaming/
mkdir -p /root/tmp/spark/output/streaming/

Para local en batch:
mkdir -p /root/tmp/spark/input/batch/
mkdir -p /root/tmp/spark/output/batch/

### Guión
- Descargar algunos ficheros de prueba del proyecto Gutenberg
Navegar al directorio input (/root/tmp/spark/input/batch/) para batch y ejecutar:
wget -nd -r -l 10 -A.txt ftp://ftp.ibiblio.org/pub/docs/books/gutenberg/
Dejarlo un rato (ojo son 37GB ...)

- Vamos a ejecutar el exporter en local para batch (Ir a la clase ExporterLocalSparkBatch y ver instrucciones)
- Vamos a ejecutar la clase SparkSQLLocal (ver instrucciones) y vamos a ver el resultado obtenido en el exporter batch.
- Vamos a ejecutar la clase ExporterLocalSparkStreaming (ver instrucciones) y vamos a ver como la serving layer se ha actualizado.
- Vamos a ejecutar la clase SparkSQLLocal (ver instrucciones) y vamos a ver como la serving layer se ha actualizado.

- Comprobar consola de Spark UI en localhost:4040/