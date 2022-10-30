package com.thejus.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.thejus.repository.Items;
import com.thejus.utility.ImsException;
import com.thejus.utility.ShoppingCart;

public class Store extends Thread{ // is extends Thread here is good standard or not ?
	
	
	public static List<Items> itemList = new ArrayList<Items>(); // Check if vector<> is better or not ? or Map To include Price as Well; or Map<ArrayList<String>,Integer>;
	
	public static synchronized void fileRead()
	{
		itemList.clear(); // Null Check and All for products;
		
		try {
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/retailstore","root","root");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from items"); // Try Batch Processing and all , stmt.execute() , 
			
			while(rs.next()) {
				Items item = new Items(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5));
				itemList.add(item);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		System.out.println(itemList.toString());
	}
	
	public static Integer checkQuantity(String itemCode)
	{
		int out = 0;
		for(Items item : itemList)
		{
			if(item.getCode().contains(itemCode))
			{
				out = item.getQuantity();
				break; // can we apply BinarySearch and All ? If Sorted already and List contains 1000s of records;
			}
		}
		
		return out; // even int to initialized if returning it
		
	}
	
	
	public static void generateBill(List<ShoppingCart> cart)
	{
		DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
		int SERIAL_NO = 1;
		String extension = "";
		if(LocalTime.now().getHour()>12)
			extension = "PM";// Check Null Pointer or Exception Scenarios;
		else
			extension = "AM";
		
		System.out.println("\n--------------C-SHOP--------------");
		System.out.println("------------BILL-------------");
		System.out.println("\nDate : " +LocalDate.now() + "Time : "+LocalTime.now().format(format) + extension);
		
		try
		{
			final int LOYALTY_DISCOUNT = 1;
			
			double totalPrice = 0D; //0D is correct format
			for(ShoppingCart sc :cart)
			{
				for(Items item : itemList)
				{
					if(item.getCode().contains(sc.getItemCode()))
					{
						int price = item.getPrice();
						int discount = item.getDiscount();
						int quantity = sc.getQuantity();
						
						double netPrice = (quantity * price) - (quantity * price * discount /100);
						
						System.out.println(+SERIAL_NO + "Name = " +item.getName() + " Quantity = "+sc.getQuantity() +" Price = " +item.getPrice() + "Discount = " +item.getDiscount() + "%" + " Net Price =" +netPrice);
						
						totalPrice+=netPrice;
						SERIAL_NO++;
					}
				}
			}
			
			double finalAmount = totalPrice - (totalPrice*LOYALTY_DISCOUNT/100);
			
			System.out.println("\nTotal Price after Discount : "+finalAmount);
			
			System.out.println("----THANK YOU----\n----VISIT AGAIN----");
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	
	@Override
	public void run()
	{
		
		String option = "y";
		List<ShoppingCart> sc = new ArrayList<>(); // Learn more about need of TypeCasting ArrayList
		Scanner s = null;
		
		while(!option.contains("n"))
		{
			
			if(option.contains("n"))
				break;
			
			System.out.println("\nTo Purachse \nEnter Item code: ");
			try {
				s = new Scanner(System.in);
				
				String itemCode = s.next();
				String regex = "(IT10[0-9])";
				
				if(!Pattern.matches(regex, itemCode))
					throw new ImsException("Invalid Item Code [Try between IT100-IT109]");
				
				System.out.println("\nEnter Quantity: ");
				Integer quantity = s.nextInt();
				String regex2 = "([0-9]*)";
				
				if(!Pattern.matches(regex2, quantity.toString()))
					throw new ImsException("Invalid Quantity");
				
				Integer quantityRemaining = Store.checkQuantity(itemCode);
				
				if(quantityRemaining<quantity)
					throw new ImsException("Out of Stock");
				else
				{
					UpdateQuantity.updateQuantity(itemCode,quantityRemaining-quantity);
					ShoppingCart cart = new ShoppingCart(itemCode, quantity);
					sc.add(cart);
					System.out.println("Product Added to cart Successfully");
					
				}
				
				System.out.println("\nEnter y if you want to add more Items");
				option = s.next();
				while(!option.contains("y") && !option.contains("n"))
				{
					System.out.println("\nTo add more enter : y\nTo Print Bill: n");
					option = s.next();
				}
			}
			catch(ImsException | InputMismatchException e)
			{
				e.printStackTrace();
			}
		}
		
		
		
		Comparator<ShoppingCart> cm = Comparator.comparing(ShoppingCart::getQuantity);
		Collections.sort(sc,cm);

		System.out.println("Order is: ");
		
		for(ShoppingCart order : sc)
		{
			System.out.println("\nItem id: "+order.getItemCode() +"quantity: "+order.getQuantity());
		}
		
		Store.generateBill(sc);
	}
	
}
