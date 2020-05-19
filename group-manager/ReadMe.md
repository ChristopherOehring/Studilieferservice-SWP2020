# Group Manager
A basic group management microservice.

## Access
Index.html is available under http://localhost:8010/web/group/index
or at http://localhost:9010/web/group/index when started via docker-compose.

## Using Docker (starting via docker-compose)
This module can be started via docker-compose and works for the most part.
Only the links to the shopping-list-manager do not work because their ports are 
hard coded and would need to be changed in order to work with Docker.