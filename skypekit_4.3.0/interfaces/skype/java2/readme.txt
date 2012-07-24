1. Build the wrapper and the client

# 1 compile the wrapper
cd api
ant

# 2 compile the client
cd ../client
ant




2. Run the wrapper and the client

2.1 with ipc hardening

# launch the runtime with ipc hardening disabled
./skypekit 
# launch the client
java -jar skypekitclient.jar -t certificate.pem

you must have created a certificate.der

2.1 without ipc hardening

# launch the runtime with ipc hardening disabled
./skypekit -n
# launch the client
java -jar skypekitclient.jar -n -t certificate.pem

you may also filter all the events.

java -jar skypekitclient.jar -n -t certificate.pem


3. API changes,
- introduction of strongly typed getters for each properties, there is still a more generic type base getter, but it
  has been renamed
- action and event using arrays are now typed with ArrayList<T> instead of T[]
- listeners take strongly typed object on which the event occurs, so that no intermediate casting is needed
- OnChangedProperty callback takes an int for the changed value instead of an Object (only int-like value are anyway immediatly available) and an enum type for the properties
- property enums are following the convention P_PROPERTY_NAME, was property_name
- a ClientConfiguration class is provided and fed when parsing the command line
- a TransportFactory class can be derived to add the local socket on linux, the new factory can be set in the 
  ClientConfiguration
- Skype goes through init() then start(), not more Close() but a stop()
- a ConnectionListener has replaced a ErrorListener
- use of Java enumerated type

Transport Log:
they are not started during the runtime, but decided right at the configuration and they follow the c++ and python wrapper
naming conventions that sid/binpsnif.lua and tools/sidmulator/log2uc.lua can be used on them.

Debug Log:
not implemented

Code status:
There is a design error as calling notify() before wait() doesn't trigger the wait() in Java. 
So the implementation shall be modified to use usual concurrent event for the synchronization.
Error handling needs cleaning. Not much debugging so far...

But the client app can connect, pass the handshake, retrieve the version string and few event
 and only deadlocks when the login is attempted because of the design error.







