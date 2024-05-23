# Service Registry

Service registry is a key component in a microservices architecture. It is used to register and discover services dynamically. This project uses Eureka server as the service registry.

Service registry is the server itself which will have details about all the other services connected to it. It will have the details like service name, IP address, port, etc. of all the services. It is like a hub that contains all the information about the services and how many instances of each service are running.

The main advantage of microservices is that we can scale the services independently. We can have multiple instances of a service running at the same time. So, the service registry will have the details of all the instances of a service running.

Service Registry will also handle all the load balancing and failover. If one instance of a service is down, then the service registry will redirect the request to another instance of the service. All the dependent services for the registry will be the clients of the registry. They will register themselves to the registry and the registry will keep track of all the services.
