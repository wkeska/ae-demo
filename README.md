# ae-demo
 
Demo application to show adjudication-engine flow.
 
## Running project
 
Install `Common docker infrastructure`:

    git clone git@gitlab.silenteight.com:sens/common-docker-infrastructure.git
    cd common-docker-infrastructure
    make up

Start Adjudication Engine:

    docker-compose up -d

Run this project
 
## Brief explanation
 
AeController class has six endpoints. You may invoke each of them to "simulate" events/activities in AE flow.

Keep in mind that all data and many data properties in this example are hardcoded. 

To simulate some events, please just run endpoints in following order.
 
1. `/alerts` - create two alerts
2. `/matches` - create two matches for each alerts
3. `/dataset` - create dataset with following alerts
4. `/analysis` - create analysis with previously created dataset
5. `/recommendation` - get generated recommendation for analysis
 
For sake of simplicity, we do not use RabbitMQ.

In a real world scenario RabbitMQ will notify us that the recommendation is ready to be collected. Then (and only then) we should get a generated recommendation (presented in point no. 5 above).
