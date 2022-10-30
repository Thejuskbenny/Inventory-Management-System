package com.thejus.service;

import java.util.List;

import com.thejus.repository.Items;

public class ItemAdmin extends Thread {

	
	public static Integer thresholdItemQuantity = 10;
	
	public synchronized static void ItemMonitor() {
		
		
		try {
			List<Items> input = Store.itemList;
			System.out.println("Threshold quantity for all items is : " + thresholdItemQuantity);
			{
				for(Items item : input)
				{
					if(item.getQuantity() < thresholdItemQuantity.intValue())
					{
						System.out.println("Quantity below threshold for : " + item.getCode());
						UpdateQuantity.updateQuantity(item.getCode(), ItemAdmin.thresholdItemQuantity);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
