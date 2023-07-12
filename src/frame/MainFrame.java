package frame;

import model.Meal;
import model.Menu;
import model.MenuDao;
import model.Restaurant;
import model.RestaurantDao;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JButton;
import java.sql.SQLException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JComboBox;

public class MainFrame extends JFrame {

	/**
	 * 刪除功能BUG
	 */
	private static final long serialVersionUID = 1L;
	public static int currentPanel = 0;
	
	public static RestaurantDao restaurantDao = new RestaurantDao();
	public static MenuDao menuDao = new MenuDao();
	public static List<Restaurant> defaultRestaurantList;
	public static Menu currentMenu;
	public static Restaurant currentRestaurant;
	public static String title = "便當管理系統";
	
	private Container cp;
	private DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable(model);
	private JScrollPane jsp = new JScrollPane(table);
	private JPanel jpnN = new JPanel(new BorderLayout());
	private JComboBox<String> comboBox = new JComboBox<>();;
	private JButton readBtn = new JButton("查詢");
	private JPanel jpnE = new JPanel(new GridLayout(15, 1, 3, 3));
	private JButton insertBtn = new JButton("新增");
	private JButton updateBtn = new JButton("修改");
	private JButton deleteBtn = new JButton("刪除");
	private JButton importBtn = new JButton("開啟檔案");
	private JButton exportBtn = new JButton("另存新檔");
	
	private void init() {
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		this.setBounds(300, 150, 700, 600);
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cp.add(jsp, BorderLayout.CENTER);
		cp.add(jpnN, BorderLayout.NORTH);
		cp.add(jpnE, BorderLayout.EAST);
		jpnN.add(comboBox, BorderLayout.CENTER);
		jpnN.add(readBtn, BorderLayout.EAST);
		jpnE.add(insertBtn);
		jpnE.add(updateBtn);
		jpnE.add(deleteBtn);
		jpnE.add(importBtn);
		jpnE.add(exportBtn);
		
		readBtn.addActionListener(e -> {
			currentPanel = 1;
			int index = comboBox.getSelectedIndex();
			currentRestaurant = defaultRestaurantList.get(index);
			currentMenu = menuDao.getMenu(currentRestaurant);
			showField();
			model.setRowCount(0);
			fillTable(currentMenu);
			this.setTitle(currentRestaurant.getName());
		});
		
		insertBtn.addActionListener(e -> {
			if(currentPanel == 0) insertRestaurant();
			else if(currentPanel == 1) insertMeal();
		});
		
		updateBtn.addActionListener(e -> {
			if(currentPanel == 0) updateRestaurant();
			else if(currentPanel == 1) updateMeal();
		});
		
		deleteBtn.addActionListener(e -> {
			if(currentPanel == 0) deleteRestaurant();
			else if(currentPanel == 1) deleteMeal();
		});
		
		importBtn.addActionListener(e -> {
			if (currentPanel == 0) {
				try {
					new FileLocationDialog("open");
					defaultRestaurantList = restaurantDao.getAllRestaurant();
					restaurantDao.refeshDatabase("restaurant", defaultRestaurantList);
					cleanTable();
					showField();
					fillTable(defaultRestaurantList);

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "檔案格式錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			} else if (currentPanel == 1) {
				try {
					new FileLocationDialog("open");
					currentMenu = menuDao.getMenu(currentRestaurant);
					menuDao.refeshDatabase(currentRestaurant, currentMenu);
					model.setRowCount(0);
					showField();
					fillTable(currentMenu);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "檔案格式錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		exportBtn.addActionListener(e -> {
			if (currentPanel == 0) {
				try {
					new FileLocationDialog("save");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "檔案格式錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			} else if (currentPanel == 1) {
				try {
					new FileLocationDialog("save");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "檔案格式錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public MainFrame() {
		showField();
		defaultRestaurantList = restaurantDao.getAllRestaurant();
		fillTable(defaultRestaurantList);
		init();
	}
	
	
	
	private void showField() {
		model.setColumnCount(0);
		if (currentPanel == 0) {
			model.addColumn("NO");
			model.addColumn("NAME");
			model.addColumn("ADDRESS");
			model.addColumn("PHONE");
		} else if (currentPanel == 1) {
			model.addColumn("NO");
			model.addColumn("NAME");
			model.addColumn("PRICE");
		}
	}
	
	private void addRow(Restaurant restaurant) {
		Object[] rowData = new Object[4];
		rowData[0] = restaurant.getNo();
		rowData[1] = restaurant.getName();
		rowData[2] = restaurant.getAddress();
		rowData[3] = restaurant.getPhone();
		model.addRow(rowData);
		comboBox.addItem(restaurant.toString());
	}
	
	private void addRow(Meal meal) {
		Object[] rowData = new Object[3];
		rowData[0] = meal.getNo();
		rowData[1] = meal.getName();
		rowData[2] = meal.getPrice();
		model.addRow(rowData);
	}
	
	private void fillTable(List<Restaurant> restaurantList) {
		for(Restaurant restaurant : restaurantList) {
			addRow(restaurant);
		}
	}
	
	private void fillTable(Menu menu) {
		for(Meal meal : menu) {
			addRow(meal);
		}
	}
	
	private void cleanTable() {
		model.setRowCount(0);
		comboBox.removeAllItems();
	}	
	
	private void insertRestaurant() {
		JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        Object[] inputFields = {"餐廳名稱：", nameField, "地址：", addressField, "電話：", phoneField};
        int option = 
        		JOptionPane.showConfirmDialog(null, inputFields, "新增餐廳", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
            	String name = nameField.getText();
            	String address = addressField.getText();
            	String phone = phoneField.getText();
            	if(name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            		throw new NullPointerException();
            	}
            	Restaurant newRestaurant = new Restaurant(name, address, phone);
				restaurantDao.insert(newRestaurant);
				defaultRestaurantList.add(newRestaurant);
				newRestaurant.setNo(defaultRestaurantList.indexOf(newRestaurant)+1);
				restaurantDao.refeshDatabase("restaurant", defaultRestaurantList);
				addRow(newRestaurant);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "請填入完整訊息", "錯誤", JOptionPane.ERROR_MESSAGE);
			}
        }
	}
	
	private void updateRestaurant() {
		JTextField noField = new JTextField();
		Object[] inputNoFields = { "餐廳編號：", noField };
		int option = JOptionPane.showConfirmDialog(null, inputNoFields, "修改餐廳", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			String no = noField.getText();
			try {
				int index = Integer.parseInt(no) - 1;
				Restaurant restaurant = defaultRestaurantList.get(index);
				
				JTextField nameField = new JTextField();
				JTextField addressField = new JTextField();
				JTextField phoneField = new JTextField();
				Object[] inputFields = { "餐廳名稱：", nameField, "地址：", addressField, "電話：", phoneField };
				option = JOptionPane.showConfirmDialog(null, inputFields, "修改餐廳", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (option == JOptionPane.OK_OPTION) {
					try {
						String name = nameField.getText();
						String address = addressField.getText();
						String phone = phoneField.getText();
						if(name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
		            		throw new NullPointerException();
		            	}
						restaurantDao.update(restaurant);
						restaurant.setName(name);
						restaurant.setAddress(address);
						restaurant.setPhone(phone);
						restaurantDao.refeshDatabase("restaurant", defaultRestaurantList);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
					} catch (NullPointerException e) {
						JOptionPane.showMessageDialog(null, "請填入完整訊息", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
				}
				restaurantDao.refeshDatabase("restaurant", defaultRestaurantList);
				defaultRestaurantList = restaurantDao.getAllRestaurant();
				cleanTable();
				fillTable(defaultRestaurantList);
				
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "請輸入數字", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null, "NO:" + no + " 不在列表中", "錯誤", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void deleteRestaurant() {
		JTextField noField = new JTextField();
        Object[] inputFields = {"餐廳編號：", noField};
        int option = 
        		JOptionPane.showConfirmDialog(null, inputFields, "刪除餐廳", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
           
            String no = noField.getText();
            try {
            	int index = Integer.parseInt(no)-1;
            	defaultRestaurantList.remove(index);
            	restaurantDao.refeshDatabase("restaurant", defaultRestaurantList);
            	defaultRestaurantList = restaurantDao.getAllRestaurant();
            	cleanTable();
				fillTable(defaultRestaurantList);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "請輸入數字", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null, "NO:" +  no + " 不在列表中", "錯誤", JOptionPane.ERROR_MESSAGE);
			}
        }
	}
	
	private void insertMeal() {
		JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        Object[] inputFields = {"餐點名稱：", nameField, "價格：", priceField};
        int option = 
        		JOptionPane.showConfirmDialog(null, inputFields, "新增餐點", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
            	String name = nameField.getText();
            	String price = priceField.getText();
            	if(name.isEmpty() || price.isEmpty()) {
            		throw new NullPointerException();
            	}
            	Meal newMeal = new Meal(name, Integer.parseInt(price));
				menuDao.insert(newMeal, currentRestaurant);
				currentMenu.add(newMeal);
				newMeal.setNo(currentMenu.indexOf(newMeal)+1);
				addRow(newMeal);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "請填入完整訊息", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
			}
        }
	}
	
	private void updateMeal() {
		JTextField noField = new JTextField();
		Object[] inputNoFields = { "餐點編號：", noField };
		int option = JOptionPane.showConfirmDialog(null, inputNoFields, "修改餐點", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			String no = noField.getText();
			try {
				int index = Integer.parseInt(no) - 1;
				Meal meal = currentMenu.getMeal(index);
				JTextField nameField = new JTextField();
				JTextField priceField = new JTextField();
				Object[] inputFields = { "餐點名稱：", nameField, "價格：", priceField};
				option = JOptionPane.showConfirmDialog(null, inputFields, "修改餐點", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (option == JOptionPane.OK_OPTION) {
					try {
						String name = nameField.getText();
						String price = priceField.getText();
						if(name.isEmpty() || price.isEmpty()) {
		            		throw new NullPointerException();
		            	}
						menuDao.update(meal, currentRestaurant);
						meal.setName(name);
						meal.setPrice(Integer.parseInt(price));
						menuDao.refeshDatabase(currentRestaurant, currentMenu);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
					} catch (NullPointerException e) {
						JOptionPane.showMessageDialog(null, "請填入完整訊息", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
				}
				menuDao.refeshDatabase(currentRestaurant, currentMenu);
				currentMenu = menuDao.getMenu(currentRestaurant);
				model.setRowCount(0);
				fillTable(currentMenu);
				
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "請輸入數字", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null, "NO:" + no + " 不在列表中", "錯誤", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void deleteMeal() {
		JTextField noField = new JTextField();
        Object[] inputFields = {"餐點編號：", noField};
        int option = 
        		JOptionPane.showConfirmDialog(null, inputFields, "刪除餐點", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
           
            String no = noField.getText();
            try {
            	int index = Integer.parseInt(no)-1;
            	currentMenu.remove(index);
            	menuDao.refeshDatabase(currentRestaurant, currentMenu);
            	currentMenu = menuDao.getMenu(currentRestaurant);
            	model.setRowCount(0);
				fillTable(currentMenu);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "請輸入數字", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "輸入內容有誤", "錯誤", JOptionPane.ERROR_MESSAGE);
			} catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null, "NO:" +  no + " 不在列表中", "錯誤", JOptionPane.ERROR_MESSAGE);
			}
        }
	}
}
