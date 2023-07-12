package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import project01.ConnectionFactory;
import project01.IOUtil;

public class MenuDao extends RestaurantDao {
	
	public Menu menu = new Menu();
	
	public Menu getMenu(Restaurant restaurant) {
		try {
			Menu menu = new Menu();
	        stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + restaurant.getName());
			while (rs.next()) {
				menu.add(new Meal(rs.getInt(1), 
							  	  rs.getString(2), 
							  	  rs.getInt(3)));
			}
			return menu;
		} catch (SQLException e) {
			return new Menu();
		}
	}
	
	public void refeshDatabase(Restaurant restaurant, Menu menu) throws SQLException {
    	dropTable(restaurant.getName());
    	createTable(restaurant.getName());
    	for (Meal meal : menu) {
    		insert(meal, restaurant);
			closeResource();
    	}
    }
	
	@Override
	public void createTable(String tableName) {
		try {
			stmt = conn.createStatement();
			sql = "CREATE TABLE " + tableName + " (" + "  NO INT IDENTITY(1,1) PRIMARY KEY,"
					+ "  NAME nvarchar(50) NOT NULL," + "  PRICE INT NOT NULL);";
			stmt.executeUpdate(sql);

			closeResource();
			return;
		} catch (SQLException e) {
			dropTable(tableName);
			createTable(tableName);
		}
	}


	public void importData(String filePath, String charsetName, Restaurant restaurant) throws SQLException, IOException {
		dropTable(restaurant.getName());
		BufferedReader br = IOUtil.getBufferedReader(filePath, charsetName);
		createTable(restaurant.getName());
		
		String line = br.readLine();
		while((line = br.readLine()) != null) {
			String[] data = line.split(",");
			Meal meal = new Meal(data[0], Integer.parseInt(data[1]));
			menu.add(meal);
			insert(meal, restaurant);
			meal.setNo(menu.indexOf(meal)+1);
		}
		
		IOUtil.closeResource();
	}

	public void exportData(String filePath, String charsetName, Restaurant restaurant) throws SQLException, IOException {
		BufferedWriter bw = IOUtil.getBufferedWriter(filePath, charsetName);

		// select table
		readTable(restaurant.getName());

		// get columnName
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = rsmd.getColumnName(i);
			bw.write(columnName);
			if (i != columnCount) {
				bw.write(",");
			}
		}
		bw.newLine();

		// get data
		while (rs.next()) {

			for (int i = 1; i <= columnCount; i++) {
				String value;
				try {
					value = rs.getString(i);
				} catch (Exception e) {
					value = Integer.toString(rs.getInt(i));
				}
				bw.write(value);
				if (i != columnCount) {
					bw.write(",");
				}
			}
			bw.newLine();
		}

		rs.close();
		stmt.close();
		IOUtil.closeResource();
	}
	
	public void insert(Meal meal, Restaurant restaurant) throws SQLException {
			sql = "INSERT INTO " + restaurant.getName() + " (NAME, PRICE) VALUES (?, ?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, meal.getName());
			pstmt.setInt(2, meal.getPrice());
			pstmt.executeUpdate();
			closeResource();
	}

	@Override
	public void read(String name) throws SQLException {

	}

	public void update(Meal meal, Restaurant restaurant) throws SQLException {
			sql = "UPDATE " + restaurant.getName() + " SET NAME = ?, PRICE = ? WHERE NO = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, meal.getName());
			pstmt.setInt(2, meal.getPrice());
			pstmt.setInt(3, meal.getNo());
			pstmt.executeUpdate();
			pstmt.close();
		
	}

	public void delete(Meal meal, Restaurant restaurant) throws SQLException {
			sql = "DELETE FROM " + restaurant.getName() + "WHERE NO = ?";
			Connection conn = ConnectionFactory.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, meal.getNo());
			pstmt.executeUpdate();

			pstmt.close();
		
	}


}
