REPORT:

The system used for this assignment was the provided one. The system is a ToDo list manager.
Use: Run the file, then open /http://localhost:8282 in a browser to access the webpage.

There are several functions:

A full complement of create, read, update, and delete exist for to do lists, plus an external database system
using spring boot. It also uses H2 In-memory database, Spring Data JPA, BootStrap, Thymeleaf and Spring Tool Suite.

Instrumentation is sparse as there are only a handful of functions, so telemetry is not very extensive, but 
it is there nontheless.

Each function's trace is began at its start as one can imagine, and then ended at the end. Interesting events are
logged, such as the flipping of status or the finish of a database search. However, some of these take less than
one ms to perform. 

Telemetry visualization was done in Jaeger, which was very easy to set up. It was set up using its all-in-one
binary, and as such it was simply a console command to initiate. 

path\to\file\jaeger-all-in-one --collector.otlp.enabled=true

Jaeger intercepts OpenTelemetry's OLTP exporter, and OpenTelemetry's Jaeger exporter doesn't collect logs or
metrics so naturally the decision was made to use the OLTP exporter.

PART 3:

To make a custom sampler to filter by an attribute, all that you need to do is implement the sampler class
with a shouldSample that has an if statement referencing the given attribute as well as make sure each span
is given the correct attribute. It's kind of boring in my case as almost every function is a POST request,
but it could be very useful in nearly any other circumstance. The code for this is in the CustomSampler class,
as well as a few variables in SPringBOotTodoAppApplication.
