# DNSClient Application
There are in total six classes. 

1. DNSHeader: creates all the parameters for header 
2. DNSQuestion: creates all the parameters for question
3. DNSClient: main program which calls DNSClientParameters, ClientServerConnection and DNSServerResponse
4. DNSClientParameters: verifies all the input parameters and defines timeout, retries, port number, DNS server and domain
5. ClientServerConnection: creates client-server connection using UDP 
6. DNSServerResponse: prints output behaviour for all answers (A, NS, MX and CNAME)


To run the program, perform the following steps in your terminal:

1. javac DNSClient.java
2. java DNSClient [-t timeout] [-r max-retries] [-p port] [-mx|-ns] @server name


Note
Test results can be found in DNSResults.txt for 7 domains with different combinations of input parameters. 
