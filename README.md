# Worms
A simulation of natural selection and predator-prey population cycles written in Java.

# Compiling
Place all files in a directory and generate the class files with the following:
> $ javac WormFrame.java


# Running
> $ java WormFrame

The simulation will output (and overwrite) two CSV files. One has the size of the worm population and the bacterial population written out every 100 ticks of simulation time. (popdata.txt) 
The other (survivor.txt) has a line for every worm that dies, showing its age at death and its number of offspring.
Stopping the simulation requires pressing Ctrl-C at the command line.

