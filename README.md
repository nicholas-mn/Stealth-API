# Stealth API

Multi-service content aggregator.

Build a feed and fetch data from multiple services, with a single API.

## Instances

Official instance: [https://stealth.cosmosapps.org](https://stealth.cosmosapps.org/).

## Documentation

API documentation can be found on Stealth's [homepage](https://stealth.cosmosapps.org).

OpenAPI specification is defined here: [stealth-api.yaml](/server/src/main/resources/stealth-api.yaml).

## Supported Services

- [Reddit](https://www.reddit.com/)
- [Teddit](https://teddit.net/)
- [Lemmy](https://join-lemmy.org/)

More services will be supported in the future.

## Installation

To install and run the Stealth API using Docker, follow these steps:

1. **Obtain the Docker Image**:
   - You have two options to obtain the Docker image:
     - **Pull the pre-built image**:
       - You can pull the pre-built Docker image from the GitHub Container Registry using the following command:
         ```bash
         docker pull ghcr.io/nicholas-mn/stealth-api:latest
         ```
     - **Build it yourself**:
       - Ensure you have Docker installed on your system.
       - Clone the repository and navigate to the project directory.
       - Build the Docker image using the following command:
         ```bash
         docker build -t stealth-api .
         ```

2. **Run the Docker Container**:
   - Once the image is built, you can run the container with:
     ```bash
     docker run -p 8080:8080 stealth-api
     ```

This will start the Stealth API server, and it will be accessible at `http://localhost:8080`.

The Dockerfile provided in the repository handles the setup and execution of the application within a containerized environment.

## Privacy Policy

Stealth **does not** log, collect, or store any personal information.

It does, however, connect to third party services to serve content. Your IP address will _by default_ be used to fetch content from the services supported by Stealth. This information is subject to their respective privacy policies.

## License

Copyright 2023 CosmosDev

Licensed under the GPLv3: http://www.gnu.org/licenses/gpl-3.0.html
