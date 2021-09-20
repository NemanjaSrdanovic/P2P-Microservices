# P2P-Microservices-Universitaet_Wien


## Project description


In this project the scenario is based on the routing, control and monitoring of automatically routed self-driving cars. Basically, the task is to design and implement a system,
that controls hundreds of automatically driven and routed cars. The solutions used in the scenario are hepling to calculate and optimize routes automatically, monitor the network and
Interactions between different participants, recognize deviations and enable people to intervene in the automatic routing processes. 

Accordingly, the system consists of four microservices and an MS framework component.


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

The microservices can be tested by 4 people connected to 4 different networks (IPs) or using VPN. Each of the 4 microservices must be started in Eclipse. Instructions and descriptions can be found in the documentation folder under "report dead".




## Project technology


**Programming Language**: Java11, HTML5 + Javascript <br/>
**IDE**: Eclipse 2019-12 <br/>
**Build tool**: Maven  <br/>
**Database**: SQLite  <br/>
**Communication Protokoll**: UDP, REST <br/>
**Securing the source code**: GitLab <br/>



