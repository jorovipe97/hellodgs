# DGS Proof of Concept
This is a proof of concept where we create resolvers that connect
to DynamoDB and Postgres databases. To see how it is to work
with those thecnologies in a GraphQL server.

Check the Spike document [here](https://geniussports.atlassian.net/wiki/spaces/GSM/pages/3743219943/GraphQL+POC+Apollo+DGS).

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
