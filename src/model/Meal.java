package model;

public class Meal {
	
	private int no;
	private String name;
	private int price;
	
	public Meal() {
	}
	
	public Meal(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
	public Meal(int no, String name, int price) {
		this.no = no;
		this.name = name;
		this.price = price;
	}
	
	public int getNo() {
		return no;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setNo(int no) {
		this.no = no;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Meal [no=");
		builder.append(no);
		builder.append(", name=");
		builder.append(name);
		builder.append(", price=");
		builder.append(price);
		builder.append("]");
		return builder.toString();
	}
	
	
}
