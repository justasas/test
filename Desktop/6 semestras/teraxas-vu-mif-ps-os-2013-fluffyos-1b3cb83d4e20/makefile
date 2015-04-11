OUTPUT = bin
JC = javac
ENV = java
CP = src/fluffy/*.java src/fluffy/*/*.java src/fluffy/*/*/*.java



default: basic os

basic:
	mkdir -p bin

os:	src/fluffy/Main.java
	echo Fluffying source...
	$(JC) -cp ../ -d $(OUTPUT) $(CP)

clean:
	rm -rf $(OUTPUT)

run:
	$(ENV) -cp $(OUTPUT) fluffy.Main
