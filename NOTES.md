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

## Primitive Data Types

+ Numeric
  + TINYINT, SMALLINT, INT, BIGINT
  + FLOAT
  + DOUBLE
  + DEDIMAL
+ Date/Time
  + timestamp 
    + string must be in format "YYYY-MM-DD HH:MM:SS ffffffff"
    + integers types as UNIX timestamp in seconds from UNIX epoch
    + Floating point types same as integer with decimal precision
  + Date
+ Misc
  + BOOLEAN
  + STRING
  + BINARY

## Complex/Collection Types

+ Arrays ARRAY<data_type>
+ Maps MAP<primitive_type, data_tpe>
+ Struct  struct<col:name: data_type[comment col_comment]...>
+ Union Type <UNIONTYPE <data_type, data_type, ...>


'''
create table movies (
    movie_name, string,
    participants array<string>,
    release_dates map<string, timestamp>,
    studio_addr struct<state:string, city:string, zip:string, streetnbr:int, streetname:string, unit:string>,
    complex_participants map<string, struct<address:string, attributes:map<string, string>>>,
    misc uniontype<int, string, array<double>>
)
'''

'''

select movie_name,
    release_date['USA'],
    studio_addr.zip,
    complex_participants['Leonardo Dicaprio'].attributes['fav_color'].mics,
    from movies;

'''

"inception" | 2010-07-16 00:00:00 | 91505 | "Dark Green" | {0:800}
"inception" | 2010-07-16 00:00:00 | 91505 | "Green" | {2:[1.0, 2.3, 5.6]}

### Type onversions

#### Explicit Conversion

case('13' as int)
case('This results is NULL' as int)
case('2.0' as float)
cast(cast(binary_data as string) as double)


## Table Partitions
j

## Managed Partioned Tables

'''
create table page_views (
    eventTime STRING,
    userid STRING,
    page STRING
)


partitioned by (dt string, applicationtype string)
stored as textfile

'''

## External Partioned Tables

'''
create external table page_views (
    eventTime STRING,
    userid STRING,
    page STRING
)

partitioned by (dt string, applicationtype string)
stored as textfile

'''


'''

alter table page_views add partition(dt='2013-03-92', applicationtype='Windows Phone')
location '/somewhere/on/hdfs/data';

alter table page_views add partition(dt='2013-03-92', applicationtype='iphone')
location 'hdfs://NameNode/somewhee/';

alter table page_views add if not exists
partition(dt='2013-09=09',applicationtype='iphone') location=''
partition(dt='2013-09=08',applicationtype='iphone') location=''
partition(dt='2013-09=07',applicationtype='iphone') location=''
'''

### demo

'''
create external table page_view_ext(
    logtime string, userid string, ip string, page string, ref string, os string, os_ver string, agent string
)
row formate delimited
fields terminated by "\t"
location "logs/page_ext/"

'''

'''
create external table page_view_ext(
    logtime string, userid string, ip string, page string, ref string, os string, os_ver string, agent string
)
partitioned by (y string, m string, d string)
row formate delimited
fields terminated by "\t"
location "logs/page_ext/"
'''

'''
    alter table page_view_ext add partition (y='2013', m='07', d='11') location 'logs/pk_ext/'
'''
