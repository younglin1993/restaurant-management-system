package project01;

import frame.*;

public class Main {

	static String databaseName = "project01";
	static String url = "jdbc:sqlserver://localhost:1433;databaseName=" + databaseName + ";TrustServerCertificate=True";
	public static String username = "sa";
	public static String password = "789uiojkl";
	public static String filePath;
	
	public static void main(String[] args) {
		
		LoginFrame loginFrame = new LoginFrame();
		loginFrame.setVisible(true);
	}

}
