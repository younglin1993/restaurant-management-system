package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import project01.ConnectionFactory;
import project01.IOUtil;

public class RestaurantDao extends BaseDao {
	
	public List<Restaurant> restaurantList = new ArrayList<Restaurant>();
	
	public List<Restaurant> getAllRestaurant() {
		try {
			restaurantList = new ArrayList<>();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM restaurant");
			while (rs.next()) {
				Restaurant newRestaurant = new Restaurant();
				newRestaurant.setNo(rs.getInt(1));
				newRestaurant.setName(rs.getString(2));
				newRestaurant.setAddress(rs.getString(3));
				newRestaurant.setPhone(rs.getString(4));
				MenuDao menuDao = new MenuDao();
				newRestaurant.setMenu(menuDao.getMenu(newRestaurant));
				restaurantList.add(newRestaurant);
			}
			return restaurantList;
		} catch (SQLException e) {
			return null;
		}
	}
    
    public void refeshDatabase(String tableName, List<Restaurant> restaurantList) throws SQLException {
    	dropTable(tableName);
    	createTable(tableName);
    	for (Restaurant restaurant : restaurantList) {
    		insert(restaurant);
    	}
    }
	
	@Override
	public void createTable(String tableName) {
		try {
			stmt = conn.createStatement();
			sql = "CREATE TABLE " + tableName + " (" + 
					"  NO INT IDENTITY(1,1) PRIMARY KEY," + 
					"  NAME nvarchar(20) NOT NULL," + 
					"  ADDRESS nvarchar(50) DEFAULT NULL," + 
					"  PHONE nvarchar(12) NOT NULL);";
			stmt.executeUpdate(sql);
			closeResource();
			return;
		} catch (SQLException e) {
			dropTable(tableName);
			createTable(tableName);
		}
	}
	
	public void importData(String file, String charsetName) throws SQLException, IOException {
		
		dropTable("restaurant");
		BufferedReader br = IOUtil.getBufferedReader(file, charsetName);
		createTable("restaurant");
		
		String line = br.readLine();
		while((line = br.readLine()) != null) {
			String[] data = line.split(",");
			Restaurant restaurant = new Restaurant(data[0], data[1], data[2]);
			restaurantList.add(restaurant);
			insert(restaurant);
			restaurant.setNo(restaurantList.indexOf(restaurant)+1);
		}
		
		IOUtil.closeResource();
	}
	
	public void exportData(String filePath, String charsetName) throws SQLException, IOException {
		
		BufferedWriter bw = IOUtil.getBufferedWriter(filePath, charsetName);
		
		//select table
		readTable("restaurant");
		
		//get columnName
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
	    
	    //get data
		while (rs.next()) {
			
			for (int i = 1; i <= columnCount; i++) {
				String value;
				try {
					value = rs.getString(i);
				} catch (SQLException  e) {
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
	
	@Override
	public void insert(Object object) throws SQLException {
		
		if (object instanceof Restaurant) {
			Restaurant restaurant = (Restaurant) object;
			sql = "INSERT INTO restaurant (NAME, ADDRESS, PHONE) VALUES (?, ?, ?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, restaurant.getName());
			pstmt.setString(2, restaurant.getAddress());
			pstmt.setString(3, restaurant.getPhone());
			pstmt.executeUpdate();
			dropTable(restaurant.getName());
			MenuDao menuDao = new MenuDao();
			menuDao.createTable(restaurant.getName());
			closeResource();
		}
	}

	@Override
	public void read(String name) throws SQLException {
//		sql = "SELECT * FROM restaurant WHERE NAME = ?";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		pstmt.setString(1, name);
//		ResultSet rs = pstmt.executeQuery(sql);
//		rs.next();
//		System.out.println(rs.getString("NO") + "\t" + 
//				   rs.getNString("NAME") + "\t" + 
//				   rs.getNString("ADDRESS") + "\t" + 
//				   rs.getNString("PHONE"));
//		
//		pstmt.close();
	}

	@Override
	public void update(Object object) throws SQLException {
		
		if (object instanceof Restaurant) {
			Restaurant restaurant = (Restaurant) object;
			Menu menu = restaurant.getMenu();
			dropTable(restaurant.getName());
			sql = "UPDATE restaurant SET NAME = ?, ADDRESS = ?, PHONE = ? WHERE NO = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, restaurant.getName());
			pstmt.setString(2, restaurant.getAddress());
			pstmt.setString(3, restaurant.getPhone());
			pstmt.setInt(4, restaurant.getNo());
			pstmt.executeUpdate();
			MenuDao menuDao = new MenuDao();
			menuDao.refeshDatabase(restaurant, menu);
			pstmt.close();
		}
	}

	@Override
	public void delete(Object object) throws SQLException {
		if (object instanceof Restaurant) {
			Restaurant restaurant = (Restaurant) object;
			sql = "DELETE FROM restaurant WHERE NO = ?";
			Connection conn = ConnectionFactory.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, restaurant.getNo());
			pstmt.executeUpdate();
			dropTable(restaurant.getName());
			pstmt.close();
		}
	}

	
}
