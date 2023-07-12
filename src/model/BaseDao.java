package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import project01.ConnectionFactory;

public abstract class BaseDao {

	public String sql;
	public Connection conn = ConnectionFactory.getConnection();
	public Statement stmt;
	public PreparedStatement pstmt;
	public ResultSet rs;
	
	public abstract void createTable(String tableName) throws SQLException;

	public void dropTable(String tableName) {
		try {
			stmt = conn.createStatement();
			sql = "DROP TABLE IF EXISTS " + tableName;
			stmt.executeUpdate(sql);
			
			closeResource();
		} catch (SQLException e) {
		
		}
	}
	
	public boolean readTable(String tableName) {
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + tableName);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public abstract void insert(Object object) throws SQLException;
		
	public abstract void read(String name) throws SQLException;

	public abstract void update(Object object) throws SQLException;
	
	public abstract void delete(Object object) throws SQLException;
		
	public void closeResource() throws SQLException {
		if(rs != null) stmt.close();
		if(pstmt != null) stmt.close();
		if(stmt != null) stmt.close();
	}
}
