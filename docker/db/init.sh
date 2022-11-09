#!/bin/bash

(docker stack deploy --with-registry-auth -c backend.yml db-uae)