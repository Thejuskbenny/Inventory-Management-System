package com.thejus.utility;

public class ShoppingCart {

	
	private String itemCode;
	private Integer quantity; // QUANTITY can be used ? When to use Bold ?
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public ShoppingCart(String itemCode, Integer quantity) {
		super();
		this.itemCode = itemCode;
		this.quantity = quantity;
	}
	
	
}
