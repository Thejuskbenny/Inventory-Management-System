package com.thejus.ims;

import com.thejus.repository.Items;
import com.thejus.service.ItemAdmin;
import com.thejus.service.Store;
import com.thejus.service.UpdateQuantity;

public class Application {

	static {
		
		Store.fileRead();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub	
				ItemAdmin.ItemMonitor();
			}
		});
		
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {

				for(Items item : Store.itemList) {
					
					if(item.getQuantity()<10)
					{
						UpdateQuantity.updateQuantity(item.getCode(), 10);
					}
				}
			}
		});
		
		t1.start();
		t2.start();
		
		Store s = new Store();
		s.start();
	}
	
	
}
