# ae-demo
 
demo application for showing adjudiacation engine flow
 
# Running project
 
1. Common docker infrustrcture
   - Clone repo https://gitlab.silenteight.com/sens/common-docker-infrastructure
   - Run with `make up`
2. Run docker compose with `docker compose up`
3. Run ae-demo project
 
# Brief explenation
 
In AeController class are six endpoints that represents ae flow. 
Keep in mind that all data in this example are hardcoded. 
Remember to run endpoints in following order.
 
1. `/alerts` - create two alerts
2. `/matches` - create two matches for each alerts
3. `/dataset` - create dataset with following alerts
4. `/analysis` - create analysis with previously created dataset
5. `/recommendation` - get generated recommendation for analysis
 
For demo purpose we don't use rabbitmq. In real world scenario after generating recommendation we should wait for notification of genrated recommendation.
