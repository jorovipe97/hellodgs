# DGS Proof of Concept
This is a proof of concept where we create resolvers that connect
to DynamoDB and Postgres databases. To see how is to work
with those thecnologies in a GraphQL server.

Check the Spike document [here](https://geniussports.atlassian.net/wiki/spaces/GSM/pages/3743219943/GraphQL+POC+Apollo+DGS) 
for more details of the conclussions.

## Prerequisites
You need to install the java sdk and gradle. We highly recommend
to use the [sdkman](https://sdkman.io/install) to do it.

`sdkman` is the equivalent to the Node's `nvm` on Java world.

## How to run
If you are going to use the Advertiser resolvers then you will need
to get AWS credentials to CI account, then you can do:
```shell
sso gsg-gsm-ci
```

If you don't have `sso` script configured on your machine you can read
this [guide](https://geniussports.atlassian.net/wiki/spaces/GSM/pages/3121808443/Guide+Kubernetes+Lens)
ignoring the Lens setup parts.

And then run the server on the same terminal session.
```shell
./gradlew bootRun
```

> **NOTE**: It is important to run the server on the same terminal 
session where you executed the `sso` command otherwise, the environment
variables with the AWS credentials won't be available and you won't
get permissions to access the DynamoDB table.

## Setting up testing Postgres database table
If you want to run the postgres resolvers you will need to run a
postgres database locally on 5432 port and create a table on the
default database.

In order to simplify this proccess you can use the `docker-compose.yml`
file that is on the root of the project. Which will run a posgres database
on localhost with the password `example` and will insert some dummy
data to the table.

To run the database cd to the root of the project and do:
```shell
docker-compose up -d
```

To stop the database do:
```shell
docker-compose down
```

If you need to fully remove the database changes, stop the
database and then do:
```shell
docker-compose rm -v
```

And finally start it again:
```shell
docker-compose up -d
```
