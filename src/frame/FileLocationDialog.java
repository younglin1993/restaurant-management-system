package frame;

import javax.swing.JFileChooser;

import java.io.IOException;
import java.sql.SQLException;
import model.RestaurantDao;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.MenuDao;

public class FileLocationDialog {
	
    public FileLocationDialog(String openOrSave) throws SQLException, IOException {
    	
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV & TXT Files", "csv", "txt");
        fileChooser.setFileFilter(filter);
        int result = 0;
        switch (openOrSave) {
			case "open" : 
				result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					openFile(fileChooser.getSelectedFile().getAbsolutePath());
				}
				break;
			case "save" :
				result = fileChooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					saveFile(fileChooser.getSelectedFile().getAbsolutePath());
				}
				break;
        }
    }
    
	public void openFile(String filePath) throws SQLException, IOException {
		if (MainFrame.currentPanel == 0) {
			RestaurantDao restaurantDao = new RestaurantDao();
			restaurantDao.importData(filePath, "Ms950");
		} else if (MainFrame.currentPanel == 1) {
			MenuDao menuDao = new MenuDao();
			menuDao.importData(filePath, "Ms950", MainFrame.currentRestaurant);
		}
	}

	public void saveFile(String filePath) throws SQLException, IOException {
		if (MainFrame.currentPanel == 0) {
			RestaurantDao restaurantDao = new RestaurantDao();
			restaurantDao.exportData(filePath, "Ms950");
		} else if (MainFrame.currentPanel == 1) {
			MenuDao menuDao = new MenuDao();
			menuDao.exportData(filePath, "Ms950", MainFrame.currentRestaurant);
		}
	}
}

