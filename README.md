# Construction-of-Simplified-DHT

Distributed Hashtables (DHTs) are structured peer-to-peer (P2P) systems that have demonstrable scaling properties. The simplified DHT that I built uses information about a small subset – O(log N) – of peers within the system to make local decisions that ensure efficient routing of messages. This effort included development of wire formats for several control and data plane messages to construct routing tables and route content. DHTs are often used to support discovery and lookup services at scale. (Java)

###


1)In this program, I have all my code in two package structures :

cs455/overlay/node/* and cs455/overlay/wireformats/*

2) The provided make file will compile all the java files.

Enter  $make command in the directory which has the folder cs455

3)The Registry has to be run manually by executing the following command:

	$java cs455.overlay.node.Registry 7689

4) You can run the test_overlay.sh script for the messaging nodes.



