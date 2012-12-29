# railroutes_cmd

## Description

This demo Java command-level application loads an embedded *Neo4j* graph database with publicly available railroad locations. A graph
database consists of nodes connected by links. Nodes and links can have name / value properties. The DBMS provides an API to navigate the
resulting graph. A *Lucene* index enables an application to find a node-of-interest. 

In our case, each node is a integer-numbered "station" from a subset of public 1996 census data of the USA railroad network. A link
(track) connects two stations. Each link has a node-to-node distance property. *Neo4j* has a built-in A* shortest path algorithm that
returns an iterator across the shortest path through the graph of "stations". Our Command executable class accepts a "from" and "to" integer
pair (1..133752) from the command line, to emit Google KML for the shortest path between the two stations. It pipes the KML to a local Google Earth
executable if found.

## Workspace

Clone this repository to your file system. We used Eclipse 3.7 Juno. The directory tree is suitable to import into Eclipse as a Maven
project. You may need environmental adjustments if your computer is not a Mac on Mountain Lion.

## Three Java Executable Classes

A Java 1.6 compiler level assumed throughout. You can run these classes directly from Eclipse. Use a Run Configuration to supply the
"from" and "to" integers.

### com.ramblerag.db.util.Loader

+ Reloads the *Neo4j* graphdb from CSV data located on the class path
+ Only required if you are curious, or if new you obtain newer CSV input.

### com.ramblerag.db.util.IndexDumper

+ Displays the Lucene index of nodes (stations) from the graphdb. 
+ The index is used to find the "from" and "to" nodes to supply to the built-in A* algorithm.
+ This class is for the curious -- it is long-running, pegging CPU cores, running the fan.

### com.ramblerag.db.route.Command  <from ID> <to ID>

+ This is the demonstration goal. 
+ The From and to station IDs are integers taken from the range 1..133752.
+ The application displays a help blurb if it finds no input pair.
+ The application ignores bad input.

![Google Earth train route example](https://github.com/mauget/railroutes_cmd/blob/master/router/RailRoute.png)
