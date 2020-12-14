import groovy.sql.Sql
import groovy.sql.DataSet

/**
 * https://blog.mrhaki.com/2009/10/groovy-goodness-groovy-sql-dataset.html
 * 
 */
// Create connection to SQLite with classic JDBC DriverManager.
Sql db = Sql.newInstance("jdbc:sqlite:sample2.db", "org.sqlite.JDBC")
 
// Create a new table
db.execute 'drop table if exists languages'
db.execute '''
    create table languages(
        name varchar(20) not null,
        primary key(name)
    )
'''
//https://www.sqlitetutorial.net/sqlite-primary-key/#:~:text=The%20rowid%20column%20is%20a,alias%20for%20the%20rowid%20column.
//When you create a table without specifying the WITHOUT ROWID option, 
//SQLite adds an implicit column called rowid that stores 64-bit signed integer. 
//The rowid column is a key that uniquely identifies the rows in the table.
 
// Create a DataSet.
DataSet languageSet = db.dataSet("languages")
languageSet.add(name: 'Groovy')
languageSet.add(name: 'Java')
languageSet.add(name: 'JRuby')
languageSet.add(name: 'Scala')

// Get data with each method.
List<String> result = []
languageSet.each {
	result << it.name
}
assert 4 == result.size()
assert ['Groovy', 'Java', 'JRuby', 'Scala'] == result
 
// Use findAll and sort to define a query condition.
DataSet firstItems = languageSet.findAll{ it.rowid < 3 }.sort{ it.name }
// No database acccess yet, only the query is constructed.
// We can see the query with getSql()
assert 'select * from languages where rowid < ? order by name' == firstItems.getSql()
// We can see the parameters with getParameters()
assert [3] == firstItems.getParameters()
 
// We call each to really access the database.
firstItems.findAll { it.name == 'Groovy' }.each { row ->
	//assert 1 == row.rowid
	assert 'Groovy' == row.name
}




//2020-12-14 18:38:43.141 ERROR c.k.katalon.core.main.TestCaseExecutor   - ❌ Test Cases/TC2 FAILED.
//Reason:
//groovy.lang.GroovyRuntimeException: DataSet unable to evaluate expression. AST not available for closure: Script1607931888293$_run_closure2. Is the source code on the classpath?
//	at TC2.run(TC2:49)
//	a


//https://stackoverflow.com/questions/18201213/ast-not-available-exception-while-filtering-dataset