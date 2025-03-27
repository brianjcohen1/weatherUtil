# weatherUtil
Instructions for running this utility!

This utility is run using the runUtil script. Before running the utility, make sure to read the following steps.

1. Build the utility using the following command:
   'mvn clean package'
   This will create a jar file in the target directory.
2. Grant your user permission to run the utility by running the following command:
   'chmod +x runUtil'
3. Run the utility using the following command:
   ./runUtil <args>
   where <args> are the arguments you want to pass to the utility.
4. The provided arguments can be a singular City,State combo (i.e. Seattle,WA), a singular zip code (i.e. 19104)
   or a combination of the two (i.e. Seattle,WA 19104) separated by a space.
5. If there is a space in the name of a city (i.e. New York) OR if you use a space between the city and state (i.e. Seattle, WA),
   you must use quotes around the entire argument and around each individual entry
   (i.e. ./runUtil "'New York,NY' 'Seattle, WA'")