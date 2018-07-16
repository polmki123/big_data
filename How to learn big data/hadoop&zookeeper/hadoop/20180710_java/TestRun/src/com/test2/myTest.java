package com.test2;

public class myTest {

	public static void main(String[] args) {	
		MySql mysql = new MySql();
		mysql.open();
		mysql.select_mysql();
		
		Database db = new MySql();
		db.open(); // database open -> MySql open
		
		Database db2 = new Oracle();
		db2.open(); // database open -> Oracle open
		DbOpen(new MySql());
		DbOpen(new Oracle());
		
		//Database db3 = CreateDatabase("Oracle");		
		//Database db3 = CreateDatabase("MySql");
	}	
	static Database CreateDatabase(String dbName) {
		Database db = null;
		switch(dbName) {
		case "Oracle":
			db = new Oracle();
			break;
		case "MySql":
			db = new MySql();
			break;		
		}
		return db;		
	}
	static void DbOpen(Database db) { db.open();}
}




