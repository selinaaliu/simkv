## simkv
A simple in-memory key-value store with a few functionalities, based on the [simple database challenge](https://www.thumbtack.com/challenges/simple-database).

It supports the following basic functionalities:
1. `SET <key> <val>`: Add the key and value pair to the store. 
2. `UNSET <key>`: Remove the existing key-value pair from the store if it exists.
3. `GET <key>`: Returns the corresponding value if exists. If not, return NULL.
4. `NUMWITHVALUE <val>`: Returns the number of keys with the same given val.

It also supports the following slightly more advanced functionalities: 
1. `BEGIN`: begin a new transaction block
2. `ROLLBACK`: rollback the changes made by the most recent transaction block
3. `COMMIT`: commit all changes made from the presently open blocks

The store should read commands from an input file, and output the results to an output file.

Sample input and corresponding output files with correct results are included.


