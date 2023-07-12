package model;

import java.util.ArrayList;

public class Menu extends ArrayList<Meal> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Menu() {

	}
	
	public void addMeal(Meal meal) {
		this.add(meal);
	}
	
	public Meal getMeal(int index) {
		return this.get(index);
	}
	
	public void removeMeal(int index) {
		this.remove(index);
	}
	
}
