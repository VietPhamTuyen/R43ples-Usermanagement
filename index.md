---
layout: index
---

R43ples
=======

R43ples (Revision for triples) is an open source Revision Management Tool for the Semantic Web.

It provides different revisions of named graphs via a SPARQL interface. All information about revisions, changes, commits, branches and tags are stored in additional named graphs beside the original graph in an attached external triple store.

[![Build Status](https://travis-ci.org/plt-tud/r43ples.png?branch=master)](https://travis-ci.org/plt-tud/r43ples)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/2125/badge.svg)](https://scan.coverity.com/projects/2125)
[![Ohloh Project Status](https://www.ohloh.net/p/r43ples/widgets/project_thin_badge.gif)](https://www.ohloh.net/p/r43ples)


This project provides an enhanced SPARQL endpoint for revision management of named graphs.
R43ples uses an internal Jena TDB is attached to an existing SPARQL endpoint of a Triple Store and acts as another endpoint both for normal SPARQL queries
as well as for revision-enhanced SPARQL queries, named R43ples queries.
The R43ples endpoint allows to specify revisions which should be queried for each named graph used inside a SPARQL query.
The whole revision information is stored in additional graphs in the attached Jena TDB.

The javadoc can be found at the [website](http://plt-tud.github.io/r43ples) under [http://plt-tud.github.io/r43ples/javadoc/](http://plt-tud.github.io/r43ples/javadoc/).

A running test server should be available under [http://eatld.et.tu-dresden.de:9998/r43ples/sparql](http://eatld.et.tu-dresden.de:9998/r43ples/sparql)


Dependencies
------------
* JDK 1.7
* Maven

```
sudo apt-get install maven default-jdk
```

Releases
--------
Releases are stored on [GitHub](https://github.com/plt-tud/r43ples/releases).
They just have to be unzipped and started with Java

    unzip R43ples.zip
    java -jar R43ples.jar

Debian packages are going to be deployed soon.    


Compiling
---------
Ant is used for compiling

    mvn exec:java
    
Releases can be be built with:

    mvn assembly:single
    
    
Configuration
-------------
There is a configuration file named *resources/r43ples.conf* where all parameters are configured:

* *sparql.endpoint* - SPARQL endpoint of triplestore which stores all information
* *sparql.username* - username for connected SPARQL endpoint allowed to write data
* *sparql.password* - password for specified user
* *revision.graph* - named graph which is used by R43ples to store revision graph information
* *service.port* - port under which R43ples provides its services
* *service.uri* - URI under which R43ples provides its services

The logging configuration is stored in *resources/log4j.properties*


Interfaces
---------
SPARQL endpoint is available at:

    [uri]:[port]/r43ples/sparql

The endpoint directly accepts SPARQL queries with HTTP GET parameters for *query* and *format*: 

    [uri]:[port]/r43ples/sparql?query=[]&format=(HTML|JSON)

There are some additional keywords which can be used to control the revisions of graphs:

* Create graph

        CREATE GRAPH <graph>
        
* Select query

        SELECT * 
        WHERE { 
        	GRAPH <graph> REVISION "23" {?s ?p ?o}
    	}
        
* Update query

        USER "mgraube" MESSAGE "test commit" 
        INSERT {
            GRAPH <test> REVISION "2" {
                <a> <b> <c> .
            }
        }

* Branching

        USER "mgraube"
        MESSAGE "test commit"
        BRANCH GRAPH <test> REVISION "2" TO "unstable"
        
* Tagging

        USER "mgraube"
        MESSAGE "test commit"
        TAG GRAPH <test> REVISION "2" TO "v0.3-alpha"

* Merging

		USER "mgraube"
		MESSAGE "merge example"
		MERGE GRAPH <test> BRANCH "branch-1" INTO "branch-2"


SPARQL Join option
------------------
There is a new option for R43ples which improves the performance. The necessary revision is not temporarily generated anymore.
The SPARQL query is rewritten in such a way that the branch and the change sets are directly joined inside the query. This includes the order of the change sets.
It is currently under development and further research.

The option can be enabled by:

```
OPTION r43ples:SPARQL_JOIN
```

It currently supports:

* Multiple Graphs
* Multiple TriplePath
* FILTER
* MINUS

For more details, have a look into the *doc/* directory.


Algorithm
-----------
Without SPARQL Join option the algorithms are very simple:
    
```
For each named graph 'g' in a query, a temporary graph 'TempGraph_g_r' is generated for the specified revision 'r' according to this formula ('g_x' = full materialized revision 'x' of graph 'g'):
    TempGraph_g_r = g_nearestBranch + SUM[revision i= nearestBranch to r]( deleteSet_g_i - addSet_g_i )
```

```
def select_query(query_string):
    for (graph,revision) in query_string.get_named_graphs_and_revisions():   
        execQuery("COPY GRAPH <"+graph+"> TO GRAPH <tmp-"+graph+"-"+revision+">")
        for rev in graph.find_shortest_path_to_revision(revision):
            execQuery("REMOVE GRAPH "+ rev.add_set_graph+" FROM GRAPH <tmp-"+graph+"-"+revision+">")
            execQuery("ADD GRAPH "+ rev.delete_set_graph+" TO GRAPH <tmp-"+graph+"-"+revision+">")
        query_string.replace(graph, "tmp-"+graph+"-"+revision)
    result = execQuery(query_string)
    execQuery("DROP GRAPH <tmp-*>")
    return result
```
  
``` 
def update_query(query_string):
    for (graph,revision) in query_string.get_named_graphs_and_revisions():
        newRevision = revision +1
        execQuery("ADD GRAPH "+ rev.delete_set_graph+" TO GRAPH <tmp-"+graph+"-"+revision+">")
        ...
```


Used libraries and frameworks
------------------------------
Following libraries are used in R43ples:

* [Jersey](https://jersey.java.net/) for RestFul web services in Java
* [Grizzly](https://grizzly.java.net/) as web server
* [Jena ARQ](https://jena.apache.org/documentation/query/index.html) for processing SPARQL results
* [Jena TDB](https://jena.apache.org/documentation/tdb/index.html) as triplestore
* [jQuery](http://jquery.com/) as JavaScript framework
* [Bootstrap](http://getbootstrap.com/)
* [Mustache](https://mustache.github.io/) as template engine
* [viz.js](http://mdaines.github.io/viz.js/example.html) as JavaScript framework for rendering GraphViz
 
