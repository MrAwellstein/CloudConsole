package com.Shadowcasted.CloudConsole;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listenerzlol implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent chat){
		try{
			Utilities2.say2Chatz("[Chat] "+"<"+chat.getPlayer().getName().toString()+"> "+chat.getMessage()+"]");
		}catch(Exception e){}
	}
	
	@EventHandler
	public void onLogin(PlayerJoinEvent event){
		new Utilities2.sendPlayers().start();
	}
	
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event){
		new Utilities2.sendPlayers().start();
		
	}
}
