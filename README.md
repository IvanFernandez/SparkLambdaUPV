# Prácticas Máster en Big Data
# Universidad Politécnica de Valencia

## Instrucciones de instalacción.

- Descargar el proyecto  
`git clone git@github.com:IvanFernandez/SparkLambdaUPV.git`
- Navegar a la raíz del proyecto  
`cd SparkLambdaUPV`
- Ejecutar el siguiente comando  
`mvn clean install -DskipTests`

### Introducción al streaming (local)
- Ejecutar la clase	*EasySparkStreaming* para un sistema de ficheros local  
`spark-submit --class com.lambdooop.training_bd_rt.streaming.EasySparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar`
- Levantar netcat en el puerto 9999   
`nc -lk 9999`
- Ver la salida por consola   

### Introducción al streaming (HDFS)
- Ejecutar la clase	*EasyHDFSSparkStreaming* para HDFS  
`spark-submit --class com.lambdooop.training_bd_rt.streaming.EasyHDFSSparkStreaming$run target/training-bd-rt-0.1.0-SNAPSHOT.jar`
- Levantar netcat en el puerto 9999  
`nc -lk 9999`
- Ver la salida por consola 	 
	 
### Arquitectura Lambda
El proyecto consiste en hacer una Lambda tipo 'Hello, Lambda!!' donde:
- La parte batch ejecutará un map-reduce batch respecto de un fichero de entrada.
- La parte streaming ejecutará un map-reduce streaming similar a la de la clase batch.
- La serving layer consultará (son SparkSQL) el resultado de la Lambda.

#### Setup
Crear los directorios necesarios para la práctica:

Directorios para ejecutar la Lambda en un sistema de ficheros local (*batch*):  
`mkdir -p /root/tmp/spark/input/batch/`    
`mkdir -p /root/tmp/spark/output/batch/`    

Directorios para ejecutar la Lambda en un sistema de ficheros local (*streaming*):  
`mkdir -p /root/tmp/spark/input/streaming/`    
`mkdir -p /root/tmp/spark/output/streaming/`  
`mkdir -p /root/tmp/spark/output/streaming/init/`  
`touch /root/tmp/spark/output/streaming/init/empty`  

*NOTA*: Si no tenemos permisos en nuestro proyecto en los directorios que se nombran anteriormente se puede cambiar por otros directorios. En este caso habrá que modificar los atributos de la clase *LambdaConstants.java* dentro del paquete Utils.

#### Guión
- Descargar algunos ficheros de prueba del proyecto Gutenberg
Navegar al directorio input (/root/tmp/spark/input/batch/) para batch y ejecutar:  
`wget -nd -r -l 10 -A.txt ftp://ftp.ibiblio.org/pub/docs/books/gutenberg/`  

*NOTA*: Dejarlo un rato (ojo son 37GB ...)

1) Vamos a ejecutar parte batch (ir a clase ExporterLocalSparkBatch y ver instrucciones).
2) Vamos a ejecutar la serving layer(ir a clase SparkSQLLocal y ver instrucciones) y vamos a ver el resultado obtenido en el exporter batch.  
3) Vamos a ejecutar la parte streaming (ir a la clase ExporterLocalSparkStreaming y ver instrucciones) y vamos a ver como la serving layer se ha actualizado.  
4) Vamos a ejecutar la serving layer otra vez y vamos a ver como los resultados se han actualizado.  

*Depurar Spark* : Comprobar consola de Spark UI en localhost:4040/