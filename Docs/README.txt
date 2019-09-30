How to launch the system:

1. Prepare the database:
	1.1 Create a postgres server with a database for the app
	1.2 Run the script that creates the tables and the script that fills the tables with initial date. These scripts are located at the RESTserver

2. Prepare the server
	2.1 Fill the Settings.XML in the REST server with the database data (username, password, db name and adress)
	2.2 Run the server

3. Android client
	To run the client, it is required to have a device that runs android version 7 (Nougat) or higher.
	3.1 The client sends HTTP request to the address that is specified in the firebase database. Therefore it is required to set it correctly.
	3.2 Enable required permissions for full functionality.