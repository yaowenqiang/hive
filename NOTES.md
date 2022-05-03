> root
> export HADOOP_USER_NAME=hive # specifiy non-root username
> hadoop
> hadoop fs -ls /
> wget https://www.grouplens.org/system/files/ml-100k.zip
> hadoop fs  -put
> hadoop fs  -copyFromLocal
> hadoop fs  -get
> hadoop fs  -copyToLocal
> hadoop fs  -cp
> hadoop fs  -rm
> hadoop fs  -rmr -skipTrash
> hadoop fs  -cat
 


ways to run hive commands

+ JDBC
+ ODBC
+ HiveCLI
+ Hive Web UI
+ HDInsight


## HIVE syntax

> select col1 + col2 as col3 from some_table

### REGEX Column Specification

> select '(ID|Name)?+.+' from some_table # exclude id and name column

> select mycol from (
    select col_a + colb as mycol
    from some_table;
        ) subq;

select col_1 + col_b as mycol 
from some_table
union all
select col_1 + col_b as mycol 
from another_table;

> select t3.mycol 
from (
select col_1 + col_b as mycol 
from some_table
union all
    select col_my as mycol
    from another_table
     ) t3
     join t4 on (t4.col_x = t3.mycol);


FROM
    some_table
SELECT
    exp1, exp2, exp3
WHERE
    where_condition

>
CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_ name
[COMMENT some_comment]
[LOCATION hdfs_path]
[withdbproperties(property_name=property_value...)]


create [external]table[if not exists][db_name]table_name
[(col_name data type [comment col_comment])]
[partitioned by (col_name data type [comment col_comment])]
[row format row_format][stored as file_format]
[location hdfs_path]
[tabproperties(property_name=property_value, ...)];

> show tables "*oa*"
> describe table_name;
> describe extended table_name; # can see table location
> describe formatted table_name; # can see table location with formatted format

> describe database_name # can see db location

'''
CREATE TABLE movies(
    movie_id INT
    ...
)

ROW FORMAT DELIMITED
FIELDS TERMINATED BY "|"
STORED AS TEXTFILE;

'''


hive > LOAD DATA INPATH
hive > LOAD DATA INPATH  'demo/movies/' overwrite into table movies; this will move the data if file is on the hdfs, if file is local ,the file will be copied to hdfs


hive > create table external table users(
    user_id int,
    age int,
    gender string,
    occupation string,
    zip_code string
    )
    row format delimited
    fields terminated by "|"
    stored as textfile
    location "demo/userinfo";


hive > create table occupation_count
    stored as RCFile
    as select count(*), occupation from users group by occupation;

hive> create table occupation_count2 like occupation_count;

## Data Types

