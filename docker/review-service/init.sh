#!/bin/bash

(CONFIG_VERSION=$(date '+%s') docker stack deploy --with-registry-auth -c backend.yml backend-uae)