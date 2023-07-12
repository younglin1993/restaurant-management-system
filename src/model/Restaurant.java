package model;

public class Restaurant {
	
	private int no;
	private String name;
	private String address;
	private String phone;
	private Menu menu;
	
	public Restaurant() {
		this.menu = new Menu();
	}
	
	public Restaurant(String name, String address, String phone) {
		this.menu = new Menu();
		this.name = name;
		this.address = address;
		this.phone = phone;
	}
	
	public Restaurant(int no, String name, String address, String phone) {
		this.menu = new Menu();
		this.no = no;
		this.name = name;
		this.address = address;
		this.phone = phone;
	}
	
	public int getNo() {
		return no;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public void setNo(int no) {
		this.no = no;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
	};

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(no);
		builder.append(".");
		builder.append(name);
		return builder.toString();
	}
	
	
}
