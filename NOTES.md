> root
> hadoop
> hadoop fs -ls /
> wget http://www.grouplens.org/system/files/ml-100k.zip
> hadoop fs  -put
> hadoop fs  -get
> hadoop fs  -cp
> hadoop fs  -rm
> hadoop fs  -rmr -skipTrash
> hadoop fs  -cat
 


## HIVE syntax

> select col1 + col2 as col3 from some_table
> select '(ID|Name)?+.+' from some_table

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

>
CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_ name
[COMMENT some_comment]
[LOCATION hdfs_path]
[withdbproperties(property_name=property_value...)]
