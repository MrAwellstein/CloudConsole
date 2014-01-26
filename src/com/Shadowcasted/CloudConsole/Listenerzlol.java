package com.Shadowcasted.CloudConsole;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Listenerzlol implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent chat){
		try{
			System.out.println("Bla");
			Utilities2.say2Chatz("[Chat] "+"<"+chat.getPlayer().getName().toString()+"> "+chat.getMessage()+"]");
		}catch(Exception e){}
	}
}
