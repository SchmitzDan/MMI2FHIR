# MMI2FHIR

This tool is inteded to create MII Medication FHIR Resources out of the data of the MMI PharmIndex.

## Running

Due to licensing reasons the MMI Data is not provided with this tool. You have to put your copy of the MMI .CSV files into the folder /data. If the tool is run as docker container, a folder containing the .CSV files has to be mounted into the container.

## Configuration

All configuration is done in application.yml file. These values can be overridden by environment variables:

| Variable | Description | Default |
| --- | --- | --- |
|BATCH_CHUNK_SIZE | Number of datasets processed in one chunk. This is also the number of resources in one result batch resource sent to the fhir server | 1000 |
| DATA_WRITETOFILE | For test and debugging fhir batch resources can be written into files instead of sending them to a fhir server | true |
| FHIR_URL | URL of the receiving fhir server | - |
