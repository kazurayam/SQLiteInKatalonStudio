import java.sql.*

import groovy.sql.Sql
 
/**
 * http://www.octodecillion.com/groovy-with-sqlite/
 */
//Class.forName("org.sqlite.JDBC")
 
def sql = Sql.newInstance("jdbc:sqlite:sample.db", "org.sqlite.JDBC")
 
sql.execute("drop table if exists person")
sql.execute("create table person (id integer, name string)")
 
def people = sql.dataSet("person")
people.add(id:1, name:"leo")
people.add(id:2,name:'yui')
 
sql.eachRow("select * from person") {
  println("id=${it.id}, name= ${it.name}")
}