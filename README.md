**Cargo Tracking App** 

Technologies Used

- Spring Security
- Spring Data Jpa
- Spring Web
- Postgresql
- RabbitMq
- Maven
- Docker
- Firebase Cloud Messaging



<img width="3283" alt="son proje" src="https://github.com/metehantrkmn/Cargo-App-be/assets/52075654/84d5e029-1a95-478b-830b-3ad3f55140ef">




  Cargo Tracking is a backend application for maintaining cargo related processes in a digital system. These processes generally include keeping records of data in a relational model, informing customers with proper notifications, providing a secure environment and doing all these in synchronized and consistent way. System allows scenarios for three different roles: USER, MANAGER, PERSONEL. Tough the system provides additional features  for USER like showing cargo history etc. , a customer doesnt necessarily have to register to the system. Every cargo is assigned to a 6 character cargo tracking number and there is an endpoint for everyone to reach out information about a specific cargo by using that tracking number. The both  MANAGER and PERSONEL are represented in the source application as a personel entity but thanks to authorization service they both have different roles and authorized to access different endpoints. The project consists of multiple micro services. These are:

- **Auth Server:** 

` `This service is the one that responsible from generating jwt tokens and verifying them for authentication as well as authorization. So all the requests coming for accessing protected data are redirected to auth server and it is auth server keeps and assigns roles for the users. This service also act as a user management service. All the detailed data about users are kept here and only necessary data for the relational model are sent to source application in order to prevent data replication.

- **Source Application:** 

This service is connected to the main database where cargo informations are kept. Users access data through this service. All the entities required for the application flow are created here. Adittionaly this service creates and sends complex queries to database to get information which tables don’t keep but relational model implies ie. finding current location from recent records, to find cargos currently at a specific branch… 

- **Identity Service:**   

This service is to check if an identity number is valid and also to check is it truly exits. In order to keep project simple this service isn’t connected to a real authorized identity verification system. Instead, it basically has a couple of endpoints and check if a user exists in the database.

- **Rabbit Mq**   

Rabbit mq is a server which keeps queues assigned to consumer services. This server and queues are used to implement a messaging pattern for notification services. Notification services generally are not crucial for application flow and dont need to be processed imidiatly. In order to apply an asynchron way of sending notifications, this server accepts messages and save them into proper queues depending on their routing key.

- **Mailing Service:** 

This service is one of the consumer services and responsible of sending mails to the users in predefined patterns. This service is assigned to the mailing queue in the rabbit mq server and every time a message comes to rabbit mq mailing queue this service is triggered to consume that message and continues untill all messages are consumed.

- **Firebase Push Service:**

This service is assigned to the notification queue of rabbit mq server and do the same as mailing service for firebase cloud messaging. There is a hard coded device registration token and this service sends notification to that device using firebase cloud messaging service. This service can be improved to work for devices included by an android or web app working with firebase cloud messaging service. 

**Cargo Database** 

![CARGO_ER drawio](https://github.com/metehantrkmn/Cargo-App-be/assets/52075654/0895845c-e5e2-455c-963d-b1b51901a214)


Cargo database consists of 7 tables. These are:

- **User:** This table keeps only a minimal part of user data. Since detailed informations are kept in auth server, only identity number and a couple of fields are hold here to represent user in the database. It is auth servers’ responsibility to keep user table and personel table synchronized as new users registers the system.
- **Personel:** In adition to User table this one also has a foreign key that represents branch of personel working at.
- **Cargo:** This table has a randomly generated Cargo Tracking Number (ctn) as primary key. This key can be used to access cargo details in the system by registered and unregistered user. Other than informations like receiver address etc this table also keeps user identity number of sender and the id of branch that the cargo has been delivered as foreign keys. But this branch id only represents the branch that cargo initially given. In flow of application cargo may be transmitted to other branches or directly to the customers themself. Location and the route information are got from Branch To Branch and Delivery tables(will be talked later).
- **Return:** This table show which cargo is not accepted by receiver. Every time a return record added to table, there is also another cargo record created by reversing the informations of returned cargo. This table has a ctn, user identity and branch id as foreign keys. 
- **Branch To Branch:** This table is used to keep record of branch to branch transmission of cargos. Basically give information about which cargo transmitted to which branch by whom. So foreign keys are branch id, personel id and cargo tracking number. A cargo can be transmitted from one branch to another multiple times. There are also fields 

  record date and status. Record date is the time when record saved and the status is the field that tells whether this transmission is done or currently running. So status field can have only two values: AT\_BRANCH, ON\_DELIVERY. This two values will be used in tracking cargo routes location and cargos currently at a specific branch. But only this table is not enough to determine these information. There is also need to check Delivery table

- **Dlivery:** Delivery table keeps record of deliveries to the recievers themself. Same as the branch to branch table this table has personel id and ctn as foreign key. It has field status and record date as well. This time delivery status can take three values. ON\_DELIVERY, DELIVERED, AT\_BRANCH. A cargo may have been simply DELIVERED or currently been ON\_DELIVERY. These both status say that cargo is not currently at the branch. The delivery is currently in process or is successful. On the other hand AT\_BRANH status imply two scenarios. Receiver was not at the address and delivery failed. Or personel couldnt delivered cargo until the day off. In both cases cargo is currently at the branch.

**Tracking Cargo Location:** 

- If we want to simply get the **route** of a specific cargo then it is enough to get a combined result of every record in the delivery and branch to branch table ordered by date. If there is record exists in the delivery table for a cargo this means the recent branch to branch record tells us the branch cargo reaches at last. But there isn’t any record for a cargo in the delivery table then cargo may continue to be transmitted to another branch. So having a record in delivery table means cargo reached out to the recent branch and it will be delivered from here. Either way when we call all the results by a cargo, result will be records of every move of cargo in the system with their dates and status including branch to branch transmissions and to customer deliveries.
- If we want to **get cargos which are currently at a specific branch** by branch id then we have to evaluate status of recent records in both tables. Since there are situations which delivery status can get ON\_DELIVERY, DELIVERED in delivery table and ON\_DELIVERY in branch to branch table we may face scenarios that some of the cargos are not in any branch even if they have a record of AT\_BRANCH in branch to branch table. We have to check both delivery and branch to branch tables to understand if a cargo is at a  specific branch. So for a  cargo to be at a branch in any time its recent branch to branch cargo status has to be AT\_BRANCH and the recent record of delivery cannot be ON\_DELIVERY or DELIVERED. When we filter that result with the id of branch we are looking for,  we get the cargos currently at this branch. 
- We can also check **current location of cargo** using the same approach above. If recent record in the branch to branch table have ON\_DELIVERY result it it is certainly on delivery between two branches. If it is AT\_BRANCH then we can look at delivery table to see if cargo is in ON\_DELIVERY or DELIVERED to the customer or AT\_BRANCH. 

It is worth to mention that this application doesn’t follow the location or route etc. by their real time location. Instead we follow records of personel to get a an impression of where cargo is and what is the status. So reliability of results are hugely depends on how personel use the application and how they add new record to the system.

**How To Start Application:** 

Every submodule has a docker-compose.yml file in the project. You can start modules by calling **docker-compose up** command. Remember that there is only one endpoint permitted for unauthenticated users, other endpoints require authentication and authorization. Also every identity is checked by Identity service. So before you use an identity you also need to save it to 

identity service. This approach is used to simulate a more complex scenario and for learning purposes.
