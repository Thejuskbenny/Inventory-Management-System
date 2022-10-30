package com.thejus.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

import com.thejus.repository.Items;

public class UpdateQuantity extends Thread {

	//Have a boolean return; 
	//Make it producer-consumer
	public static synchronized void updateQuantity(String itemCode, Integer newQuantity)
	{
		try {
			List<Items> updateInput = Store.itemList;
			
			for (Items item : updateInput) {
				if(item.getCode().contains(itemCode))
				{
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/retailstore","root","root");
					
					PreparedStatement stmt = conn.prepareCall("update items set quantity = (?) where code = (?)");
					stmt.setInt(1, newQuantity);
					stmt.setString(2, item.getCode());
					
					int i = stmt.executeUpdate();
					System.out.println(i + "record inserted");
					conn.close();
				}
			}
			Store.fileRead();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try
		{
			for(Items iterable_element : Store.itemList)
			{
				updateQuantity(iterable_element.getCode(), 10);
			}
			synchronized(this)
			{
				notify();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
