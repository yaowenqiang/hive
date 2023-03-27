# Hive Architecture

+ Thift Server
  + JDBC
  + ODBC
  + Hive CLI
  + Hive Web UI
  + HDInsight
+ HiveQL
+ MetaStore
+ Driver(Query processing, compiling , optimization)

> SELECT * from my_table limit 100
> This will not require any MapReduce at all


Hive Principles - The Hive Warehouse

Hive warehouse

+ Meta data about all the objects known to Hive, persisited in the meta store
+ Consists of
  + Databases
  + Tables
  + Partitions
  + Buckets/Clusters
+ Local Hive warehouse
  + Managed by Hive
  + Typically under /hive/warehouse
  + Dropping a table will drop the data just as well as the meta-data.
+ External Tables
  + Hive manages the meta-data only
  + Anywhere on the Hadoop file system
  + Dropping a table will only remove the table's definition, data remains untouched.




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


exclude column

set hive.support.quoted.identifiers=none;
select `(code)?+.+` from sample_07 limit 1;

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
    movie_name  string,
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



## Repair partition

> MSCK REPAIR table page_view_ext;
> load data local INPATRH "/media/log" overwrite into table page_views partition(y='2022', m='66',d='01')

'''
    alter table page_view_ext add partition (y='2013', m='07', d='11') location "/logs/pv_ext/somedata_for_7_11";"
    '
'''


### Multiple inserts

from from_statement
    insert overwrite table table[partition(part.col1=col1, partcol2=val2][select_statment1])
    insert into table table2[partition(part.col1=col1, partcol2=val2][if not exists][select_statment2])
    insert overwrite directory'path' select_statment3;


'''
    from movies
        insert overwrite table horror_movies select * where horror = 1 and release_date = '8/25/2015'
        insert into action_movies select * where action=1 and release_date=''8/25/2015;
'''

'''
    from (select * from movies where release_date = '8/23/2015') src
    insert overwrite table horror_movies select * where horror=1
    insert into action_movies select * where action=1

## Dynamic Partition inserts

    '''
    create table views_stg(eventTime string, userid String) partitioned by (dt string, applicationtype string, page string);
    '''

    '''
    from page_view_ext src
    insert overwrite table views_stg partition(dt='2013-09-13', applicationtype='Web', page='Home')
        select src.eventTime, src.userid where dt='2013-09-13' and applicationtype='Web' and  page='Home'
    insert overwrite table views_stg partition(dt='2013-09-14', applicationtype='Web', page='Cart')
        select src.eventTime, src.userid where dt='2013-09-13' and applicationtype='Web', page='Cart'
    insert overwrite table views_stg partition(dt='2013-09-15', applicationtype='Web', page='Checkout')
        select src.eventTime, src.userid where dt='2013-09-13' and applicationtype='Web', page='Checkout'
    '''

    '''
    from page_views src
        insert overwrite table views_stg partition(applicationtype='Web', db, page)
        select src.eventtime, src.userid, src.dt, src.page where applicationtype='Web'
    '''

+ Dynamically determine partitions to create and populate
+ Use input date to determine partitions

+ Default maxinum dynamic partitions=1000
  + hive.exec.max.dynamic.partitions
  + hive.exec.max.dynamic.partitions.pemode
+ Enable/Disbale dynamic partitions inserts
  + hive.exec.dynamic.partition=true
+ Use strict mode when in doubt
  + hive.exec.dynamic.partition.mode=strict
+ Increase max number of files a data node can service in (hdfs-sit.xml)
  + dfs.datanode.max.xclevers=4096


> hive > SET hive.exec.dyamic.partition.mode=nostrict;

'''
create external table staging(
logtime string,
userid int,
ip string,
page string,
ref string,
os string,
os_ver string,
agent string)
row format delimited
fields terminated by "\t"
location '/data/logs/multi_insert';

create  table staging(
logtime string,
userid int,
ip string,
page string,
ref string,
os string,
os_ver string,
agent string)
row format delimited
fields terminated by "\t";


'''

> hadoop fs -mkdir /logs/multi_insert/
> hadoop fs -put logs/*.log /logs/multi_insert/


   '''
insert into table page_views partition(y, m, d) 
select logtime, useerid, ip, page, ref, os, os_ver, agent, substr(logtime, 7,4), substr(lgtiem, 1,2), substr(logtime, 4,2)
from staging;

> Not everything results in partition pruning.

# Hive Query Language


select 
    a, b, sum(c)
from t1
group by
    a, b

## grouping sets, cube, rollup


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


## Cube

'''
select a, b, c, sum(d) from t1 group by a, b with cube
'''

'''
select a, b , c, sum(d) from t1 group by a, b, ,c grouping sets
((a,b,c), (a, b), (b,c), (a,c), a,b,c,())
'''

## rollup

'''
select a, b, c, sum(d) from t1 group by a, b with rollup
'''

'''
select a, b, c, sum(d) from t1 group by a, b, c grouping sets
((a,b,c), (a,b), a, ())

# Functions in Hive

+ Built-in Functions
  + Mathematical
  + Collection
  + Type conversion
  + Date
  + Conditonal
  + String
  + Misc
  + xPath
+ UDAFs(user defined aggregate functions)
+ UDTFs(user defined table functions)
'''


+ Mathematical

'''
select rand(), a from t1;
select rand(3), rand(a) from t1;
select pos(a, b) from t2;
select tan(a) from t3;
'''

'''
abs(doubel a)
round(double a, int d)
floor(double a)
'''

+ Collections

'''
size(Map]<K,V>)
map_keys(Map<K,V>)
map_values(Map<K,v>)
select array_contains(a, 'test') from t1;

'''

+ Date

unix_timestamp()
year(string d), month(string d), day(string d), hour, second
datediff(string enddate, string startdate)
date_add(string startdate , int days)
date_sub(string startdate, int days)
to_date(string timestamp)

+ Conditional

select if(a = b, 'true result', 'false result') from t1;
select coalesce(a,b, c) from t1;
select case a when 123 then 'first' when 456 then 'second' else 'none' end from t1;
select case when a = 13 then c else d end ffrom t1;

+ String 

select concat(a,b) from t1;
select concat_ws(sep, a,b) from t1;
select regex_replace('Hive Rocks', 'ive', 'hadoop') from dumy;
select substr(string|binary a, int start)
select substr(string|binary a, int start, int length)
select substring(string|binary a, int start, int length)
select sentences(string, str, string lang, string locale);
select sentences("loving this course! Hive is awesome.") from dummy;

+ Built-in Aggregate Functions(UDAFs)

count(*)
count(expr)
count(distinct expr)
sum(col)
sum(distinct col)

avg
min
max
variance
stdev_pop
histogram_numeric(col, b)
returns array<struct {'x', 'y'>}


+ Having & group by

Having syntax

select
    a, b, sum(c)
from
    t1
group by
    a,b
having 
    sum(c) > 2



group by on function

select 
    concat(a,b) as r,
    sum(c)
from 
    t1
group by 
    concat(a,b)
having
    sum(c) > 2


## Sorting in Hive

ORDER BY

select x, y, z from t1 order by x asc;


SORT BY

select x, y, z from t1 sort by x;


controlling Data Flow

distribute by 


select x, y, z from t1 distribute by y;


distribute by with sort by

select x, y, z from t1 distribute by y sort by z;


cluster by

select x, y, z from t1 cluster by y


## The CLI

> hive -e 'select a, b from t1 where c = 15'
> hive -S -e 'select a, b from t1 where c = 15' > result.txt
> hive -f /my/local/file/.sql


## Variable Substitution

4 namespaces

+ hivevar
  + -d -define, -hivevar
  + set hivevar:name=value

+ hiveconf
  + -hiveconf
  + sethiveconf:property=value
+ system
  + set system:property=value
+ env
  + set env:property=value

hive -d srctable=movies

hive > set hivevar:cond=123
hive> select a,b,c from pluralsight.${hivevar:sorctable} where a = ${hivevar:cond};

hive -v -d src=movies -d db=pluralsight -e 'select * from ${hivevar:db}.${hivevar:src} limit 100;'


## Bucketing


+ Tables of Partitions can be bucketed
+ Bucketing is an approach to distribute or cluster table data
  + More efficient compiling
  + Better performance with Map-side joins
  + Used with partition  or w/o when partitiong doesn't work for your data set
+ Buckets can also be sorted
  + Sort-Merge-Bucket(5MB))joins

create table t1(a int, b string, c string) clustered by (b)  into 256 buckets;

create table t1(a int, b string, c string) 
partitioned by(dt string)
clustered by (b)  sorted by (c) into 64 buckets;




+ Hive doesn't control or enforce bucketing on data loaded into table
+ 2 aproaches
  
set mapred.reduce.tasks=64;
insert overwrite table t1 select a, b, c from t2 cluster by b;

or

set hive.enforce.bucketing=true;
insert overwrite table t1 select a, b, c from t2;


Number of reducers and hence number of output files equals the number of buckets

Sampling data becomes a simple task.


create table t1(a int,  b string, c string) clustered by (b) into 256 buckets

create table t1(a int,  b string, c string) 
partitioned by (dt string)
clustered by (b) sorted by (c) into 256 buckets




## Bucket Sampling


select * from source tablesample(bucket x out of y[on colname])

create table page_views(userid int, page string, views int) 
partitioned by (dt string)
clustered by (userid) sorted by (dt) into 64 buckets;


select * from page_views tablesample(bucket 3 out of 64 on userid);
select * from page_views tablesample(bucket 3 out of 64 on rand());

## Block sampling

+ Based on HDFS blocks(64/128/256 etc.)
+ Percentage of data size (notice this is note # of rows)
+ Returns at least the percentage specified
+ Doesn't always work
  + Depends on compression and input format(CombineHiveInputFormat)

### Block Sampling Syntax

select * from source tablesample (n percent);
select * from source tablesample (n M);
select * from source tablesample (n rows);


## Join

Join types

+ JOIn(inner join)
+ Left, RIGHT, FULL(OUTER] JOIN
+ LEFT SEMI JOIN
+ CROSS JOIN)


Equality joins only(equi-joins)


### Inner join

select a.val, b.val from a join b on (a.key = b.key)

### left, right ,full[outer] join

select a.val, b.val from a left outer join b on (a.key = b.key) join c on (c.key = a.key)


### left semi join

select a.val from a where a.key in (select bkey from b)  - note supported
select a.val from a where exists(select 1 from b where b.key = a.key) - not supported

select a.val from a left semi join b on (a.key = b.key);


### cross join

select a.*, b.* from a cross join b


STREAMING

select a.*, b.*, c.*
from a
left join b on (a.key = b.key)
join c on (a.xyz = c.xyz)


select streamable(a )a.*, b.*, c.*
from a
left join b on (a.key = b.key)
join c on (a.xyz = c.xyz)


Joins- Merging MR JObs

select a.*, b.*, c.*
from a
left join b on (a.key = b.key)
join c on (a.key = c.key)



Map-size Joins

+ ALL tables involoved in a join are small enough to fit into memory except which is streamed through the mapper
+ Hash table is used

select map join(b) a.* b.* from a
join b on (a.key = b.key)

+ No Full or right Outer Joins
+ No UNIONs between multiple queries


set hive.auto.convert.join = true
select a.*,b.* from a
join b on (a.key = b.key)



Map-side Joins for Bucketed Tables

Buckets can be joined with each other ()Map-side Join) when:

+ Table being joined are bucketed on join columns(Clustered)
+ Number of buckets in one table is a mulitple of the number of buckets in the other table
+ Set hive.optimize.bucket.mapjoin=true

Sort.Marge.Join

+ Table being joined are bucketed on join columns(Clustered)
+ The have the same number of buckets
+ Buckets are also sorted

hive.input.format=org.apache.hadoop.hive.qo.io.BucketizedHiveInputformat;
hive.optimize.bucket.mapjoin=true
hive.optimize.bucket.mapjoi.sortedmegen=true



## Distributed Cache

+ An approach usec by MapReduce to distribute files across data nodes
+ Provides a means for data nodes to access files local to the data node itself(cashed copy)
+ Typically used with 
  + Text files
  + Archiveds(compressed file)
  + Jars and other program files.
+ Used to distribute hash archive of a table for Map-Side jobs

addfile mydata.txt;
add archive sendme.zip;
add jar myprogram.jar;
listfiles|jars|archives| [filepath]

Table-Generating Functions(UDTF)

Advanced Hive Functions


Explode()

+ Take array as input
+ No other expresions allowed in SELECT
+ Can't be nested
+ group by / cluster by /Distribute by / sort not supported

Lateral View

+ Table UDTF function as input
+ Provides virtual table for accessing combined results


select a, b, columnAlias
from baseTable
lateral view udtf(expression) tableAlias as columnAlias;

select a, b, col1, col2
from baseTable
    lateral view(udtf(x) t1 as col1,
    lateral view udtf(col1) t2 as col2;
    )

select explode(actors) as a from movies;

select movie_id, title, actor from movies lateral view explode(actors) actorTable as actor;


outer lateral views

select movie_id, title, actor from movies lateral view outer  explode(actors) actorTable as actor;


## using UDF

> yum install java-devel


> java -target 1.0 -cp $(ls /usr/lib/hive/lib/hive-exec*.jar);/usr/lib/hadoop/hadoop-core.jar com/my/udf.ava

> java -cf myudf.jar com/my/udf.jar


> hive > jad_jar /path/to/jar
> hive > create temporary function revs as  'com.my.udf.myfunc';

> list jars

## What about the TEMPORARY function

+ Function only exists in current user's session
| use the -i option when launching hive from the command line
  + Provide a initialization file
+ Use the .hiverc file
  + User's home directory
  + Hive's bin directory /usr/lib/hive/bin/


##Distributed Cache, Again

+ Hive functions are add to the distributed cache
+ Accessing files on the distributed cache is just a matter of referencing the file

> File f = new File("./samplefile.csv")




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





