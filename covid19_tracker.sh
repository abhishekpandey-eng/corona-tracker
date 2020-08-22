##directory where jar file is located    
dir=target
##jar file name
jar_name=corona-tracker-0.0.1-SNAPSHOT.jar

mvn clean install

java -jar $dir/$jar_name
