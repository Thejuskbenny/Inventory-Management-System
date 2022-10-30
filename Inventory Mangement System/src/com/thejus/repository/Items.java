package com.thejus.repository;

public class Items {
	private String code;
	private String name;
	private Integer price;
	private Integer quantity;
	private Integer discount;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Items(String code, String name, Integer price, Integer quantity, Integer discount) {
		super();
		this.code = code;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.discount = discount;
	}
	@Override
	public String toString() {
		return "Items [code=" + code + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", discount="
				+ discount + "]";
	}// try to work on it without toString()
	
	
	
}
