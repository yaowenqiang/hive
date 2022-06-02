> root
> export HADOOP_USER_NAME=hive # specifiy non-root username
> hadoop
> hadoop fs -ls /
> wget https://www.grouplens.org/system/files/ml-100k.zip
> hadoop fs  -put
> hadoop fs -mkdir   /demo/
> hadoop fs -mkdir   /demo/movies/
> hadoop fs -mkdir   /demo/users/
> hadoop fs -put u.item  /demo/movies/
> hadoop fs -put u.info  /demo/users/
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
create table movies (
    movie_id INT,
    movie_title STRING,
    release_date STRING,
    video_release_date STRING,
    imdb_url STRING,
    unknown INT,
    action INT,
    adventure INT,
    animation INT,
    children INT,
    comedy INT,
    crime INT,
    documentary INT,
    drema INT,
    fantasy INT,
    film_noir INT,
    horror INT,
    musical INT,
    mystery INT,
    romance INT,
    sci_fi INT,
    thrillier INT,
    war INT,
    Western INT
)
 row format delimited
 fields terminated by "|"
 stored as textfile;

'''

> hadoop  fs -ls  /apps/hive/warehouse/

hive > LOAD DATA INPATH
hive > load data inpath "/demo/movies" into table movies;

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
  + ttimestamp 
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
    participants[0],
    release_date['USA'],
    studio_addr.zip,
    complex_participants['Leonardo Dicaprio'].attributes['fav_color'].mics,
    from movies;

'''

"inception" | 2010-07-16 00:00:00 | 91505 | "Dark Green" | {0:800}
"inception" | 2010-07-16 00:00:00 | 91505 | "Green" | {2:[1.0, 2.3, 5.6]}

### Type conversions

#### Explicit Conversion

case('13' as int)
case('This results is NULL' as int)
case('2.0' as float)
cast(cast(binary_data as string) as double)


## Table Partitions


## Managed Partitioned Tables

'''
create table page_views (
    eventTime STRING,
    userid STRING,
    page STRING
)

partitioned by (dt string, applicationtype string)
stored as textfile;

'''


'''
    LOAD DATA INPATH "/mydata/android/Aug_10_2013/pageviews/" into  table page_views
    partition(dt="2013-108-10", applicationtype="android")
'''

'''
    LOAD DATA INPATH "/mydata/android/Aug_10_2013/pageviews/" 
    OVERWRITE INTO  table page_views
    partition(dt="2013-108-10", applicationtype="android")
'''

## Virtual Partition Columns


'''
    select dt as eventDate, page, count(*) as previewCount from page_views where applicationtype="iphone" group by  dt, page;
'''




## External Partitioned Tables

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
row format delimited
fields terminated by "\t"
location "/logs/page_ext/";

'''

'''
create external table page_view_ext(
    logtime string, userid string, ip string, page string, ref string, os string, os_ver string, agent string
)
partitioned by (y string, m string, d string)
row format delimited
fields terminated by "\t"
location "/logs/page_ext/";
'''
> hadoop fs  -put   log_2013811_16136.log  /logs/pv_ext/somedata_for_7_11

## Repaire partition

> MSCK REPAIR table page_view_ext;
> load data local into "/media/log" overwrite into table page_views partition(y='2022', m='66',d='01')

'''
    alter table page_view_ext add partition (y='2013', m='07', d='11') location "/logs/pv_ext/somedata_for_7_11";"
    '
'''


### Multiple inserts

from from_statement
    insert overwrite table table[partition(part.col1=col1, partcol2=val2][select_statment1])
    insert into table table2[partition(part.col1=col1, partcol2=val2][if not exists][select_statment2])
    insert overwrite directory'path' select_statment3<F3>;


'''
    from movies
        insert overwrite table horror_movies select * where horror = 1 and release_date = '8/25/2015'
        insert into action_movies select * where action=1 and release_date=''<F3>8/25/2015;
'''

'''
    from (select * from movies where release_date = '8/23/2015') src
    insert overwrite table horror_movies select * where horror=1
    insert into action_movies select * where action=1


select 
    a, b, sum(c)
from t1
group by
    a, b


select a, b, sum(c) from t1 group by a, b, grouping sets ((a, b), a)
'''
equal to 


select a, b, sum(c) from t1 group by a, b
union all
select a, null, sum(c) from t1 group by a


select a, b, sum(c) from t1 group by a, b grouping sets(a, b, ())

equal to

select a, null, sum(c) from t1 group by a
union all
 select null, b, sum(c) from t1 group by b
 union all
 select null, null, sum(c) from t


## Bucketing


create table t1(a int,  b string, cstring) clustered (b) into 256 buckets



## Bucket Sampling


select * from source tablesample(bucket x out of y[on colname])


## Join

### Inner join

select a.val, b.val from a join b on (a.key = b.key)

### left, right ,full[outer] join

select a.val, b.val from a left outer join b on (a.key = b.key) join c on (c.key = a.key)


### left semi join

select a.val from a where a.key in (select bkey from b)  - note supported
select a.val from a where exists(select 1 from b where b.key = a.key) - not supported

select a.val from a left semi join b on (a.key = b.key);


### cross join

### bucketed tables


## Distributed cache

add file mdata.txt;
add archive sendme.zip
add jar myprogram.jar
>list files | jars | archives [filepath];



## Extend hive


> @description
> __FUNC__



## Hadoop Streaming

> select transform (col1, [col2, col3])
    using 'code file | program' [as (list of columns [and casts])]
    from SourceTable;

## Windowing and Analytic Functions 

LEAD/LAG

first_value
last_value
partition by
over
window

##  File Formats and SerDes

+ row format


> stored as 

+ Text file

>   input format "org.apache.hadoop.mapred.TextInputFormat"
    outputformat "org.apache.hadoop.hive.qLioHiveIgnoreKeyTextOutput"
    row format serde "org.apache.hadoop.hive.serde2lazy"

+ Sequence File

>   input format "org.apache.hadoop.mapred.SequenceFileInputFormat"
    outputformat "org.apache.hadoop.hive.HiveSequenceFileOutputFormat"
    row format serde "org.apache.hadoop.hive.serde2.lazyLazySimpleSerDe"


+ stored as 
  + textfile

SerDe





