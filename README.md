This sample code randomly transform the operation code in the code.

# Run the demo


## Way 1


```
cd mutationagent
mvn clean package

cd victim
mvn clean package

java -javaagent:mutationagent/target/mutationagent-0.0.1-jar-with-dependencies.jar=com.pinjia.victim.App -jar victim/target/victim-0.0.1.jar
```

## Way 2

TODO