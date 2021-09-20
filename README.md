# P2P-Microservices-Universitaet_Wien


## Project description


In this project the scenario is based on the routing, control and monitoring of automatically routed self-driving cars. Basically, the task is to design and implement a system,
that controls hundreds of automatically driven and routed cars. The solutions used in the scenario are hepling to calculate and optimize routes automatically, monitor the network and
Interactions between different participants, recognize deviations and enable people to intervene in the automatic routing processes. 

Accordingly, the system consists of four microservices and an MS framework component.


<details>
<summary>MSCF - Peer 2 Peer Communication Framework (P2P)</summary>
<p>
The P2P protocol design and implementation should provide simplifed P2P network
functionality which is utilized by all other MS. Overall the protocol design must provide
the following mandatory functionality:
   
+ <summary>Peer Discovery</summary>
In a truly distributed, non-centralized P2P network the most basic
way to discover other peers (i.e., microservice instances) and their provided func-
tionality is so called scanning. Hereby, after starting up a peer starts to systemati-
cally scan IP addresses and ports (in reasonable intervals) for peers. Subsequently,
messages are sent/forwarded to these peers only.



+ <summary>Routing and Forwarding</summary>
The P2P network shall utilize hop-by-hop transport to
distribute messages and information. Hereby, the data is not exchanged directly
between some source and destination peer but instead routed along a number
of intermediate peers which temporarily store and relay incoming messages to
previously discovered (see, peer discovery) and currently accessible peers.




+ <summary>Resilience</summary>
The P2P network protocol and implementation must provide basic resis-
tance to common struggles, such as, network latency or message processing issues.




+ <summary>Flexibility vs. Specialization</summary>
Be aware that your P2P implementation and pro-
tocol design must be applicable in a generic application scenario agnostic way.
Hence, you will need to decide when to use and create generic messages to foster
exibility (e.g., for the exchange of arbitrary data) and when to create and use spe-
cialized messages focusing on narrow use cases.	</p>



+ <summary>Asynchronous communication</summary>
Use asynchronous communication when sending or
receiving/processing messages. Hence, each message must be processed and ex-
changed in its own thread.	

+ <summary>Load Optimization</summary>
To reduce the network load which can originate from automatic
resilience measures one typically applies a range of optimization strategies. Take
at least the following two into account when creating your network protocol and
implementation: Time based Loop Prevention or Cache based Loop Prevention


+ <summary>Persistent Message Storage</summary>
The previously outlined routing/forwarding function-
ality along with the resilience aspects requires you to take some sort of (e.g., in
memory) message storage solution into account.
	
</p>
</details>





<details>
<summary>MS1 - Automatic Routing Service (ARS):</summary>
<p>

+ <summary>Route Calculation</summary>
Focuses on enabling to calculate an optimal route for a given
start and destination location on a map. For this you can apply a number of
simple routing algorithms, such as, breadth/depth frst search, Dijkstra algorithm,
and so on.


+ <summary>Route Optimization</summary>
Focuses on enabling to update and recalculate routes on the
fly for a car which has already started its trip. Hence, it must be possible for a car
to signal its current position such that the ARS can check if a calculated route for
this car is still relevant or must be updated.

+ <summary>Multi-Threading</summary>
If your team consists of three or more students you will need to
design and implement this MS in a way that ensures that the outlined tasks (e.g., route
calculation and optimization) are executed in a multi-threaded job-based manner.

</p>
</details>



<details>
<summary>	</summary>
<p>


</p>
</details>



<details>
<summary>	</summary>
<p>


</p>
</details>



## Project implemetation

The implementation of the project is carried out in two subtasks:


### Sub-task 1 - Status Update 

For the first sub-task it is required to deliver a class diagrams (and skeleton like implementa-
tions) of the solutions (i.e., one or more class diagrams for each MS and the MSCF) along
with a detailed defnition of our network/microservice API/protocol (technology, endpoints,
exchanged data, error handling, descriptions, and so on).



### Sub-task 2 - Implemented framework and microservices



<details>
<summary>Implementation</summary>
<p>
The team is free to chose which technology stack the service will run upon.
Any statically typed language of our liking, such as, Java is free to use. In general, it is expected that
we apply language extensions, such as, TypeScript for languages which are not capable
of static type checks out of the box.
</p>
</details>




<details>
<summary>Deployment</summary>
<p>
We have to make sure that our service's functionality is made available
to our team colleagues via a shared network (i.e. either a VPN or the Internet). In
order to be accessible via a network, our service will have to 1) actually run somewhere
(e.g., your notebook, your desktop at home, or even a hosted virtual machine, . . . ) and
2) listen on a port for incoming requests (e.g., a HTTP server listens on port 80)
</p>
</details>




<details>
<summary>Testing and Presentation</summary>
<p>
Make sure that each Microservice can be tested
even when some/all other Microservices are not available. This is necessary as, typically,
during development time not each service your implementation depends upon is avail-
able.
</p>
</details>

<details>
<summary>Error Handling</summary>
<p>
In dynamic MS landscapes errors can occur all the time and must be
compensated (if possible) and communicated along the way to react accordingly. Hence,
take error handling and error communication into account when designing your MS and
the related network API/protocol. For example, how are you handling cases where a
MS crashes and/or terminates before it can completely process all messages delivered
to it?
</p>
</details>

<details>
<summary>Zero Confguration</summary>
<p>
MSs should build up their own network and interconnections dynam-
ically, such that, it becomes, possible to simply start new MS instances which will
automatically be picked up and integrated by all other already running MS instances.
</p>
</details>



### How to test the implementation 

The microservices can be tested by 4 people connected to the same network or using VPN. Each of the 4 microservices must be started in Eclipse. Instructions and descriptions can be found in the documentation folder under "report dead".




## Project technology


**Programming Language**: Java11, HTML5 + Javascript <br/>
**IDE**: Eclipse 2019-12 <br/>
**Build tool**: Maven  <br/>
**Database**: SQLite  <br/>
**Communication Protokoll**: UDP, REST <br/>
**Securing the source code**: GitLab <br/>



