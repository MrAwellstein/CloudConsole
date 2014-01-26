package com.Shadowcasted.CloudConsole;


import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.Shadowcasted.CloudConsole.Updater.ReleaseType;

public class CloudConsole extends JavaPlugin {
	
	public static boolean update = false;
	public static String name = "";
	public static ReleaseType type = null;
	public static String version = "";
	public static String link = "";
public static String moderators = "null";
public static String admins = "null";
public static String adminpermissions = "null";
public static String modpermissions = "null";
public static String owner = "null";
public static String ownerpermissions = "null";
public static int port = 25566;

	public String ConfigStuff(String varable, String msg){
		if (getCustomConfig().getString(varable) == null) {
			getCustomConfig().set(varable, msg);
			saveCustomConfig();
			return msg;
		}else
			return getCustomConfig().getString(varable);
	}
	
	public static class Server extends Thread {
		public void run() {
			System.out.println("Starting CloudConsole...");
			new Utilities2.ThreadManager().start();
		}
	}
	private Updater.UpdateType Update;
	private int updater = 0;
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new Listenerzlol(), this);
	}
	
	@Override
	public void onLoad() {
		try{
			Utilities2.CUL = ConfigStuff("chatOnlyUsers","none");
			Utilities2.MUL = ConfigStuff("moderators","none");
			Utilities2.AllowedModeratorCommands = ConfigStuff("modpermissions","none");
			Utilities2.AUL = ConfigStuff("admins","none");
			Utilities2.AllowedAdminCommands = ConfigStuff("adminpermissions","none");
			Utilities2.OUL = ConfigStuff("owners","none");
			Utilities2.AllowedOwnerCommands = ConfigStuff("ownerspermissions","all");
			Utilities2.ThreadManager.MC = (int) Double.parseDouble(ConfigStuff("MaxConnections", "5"));
			Utilities2.Port = (int) Double.parseDouble(ConfigStuff("Port", "1337"));
			Utilities2.debug = (int) Double.parseDouble(ConfigStuff("Debug_Settings", "1"));
			Utilities2.error = (int) Double.parseDouble(ConfigStuff("Error_Settings", "1"));
			updater = (int) Double.parseDouble(ConfigStuff("Update_Settings","2"));
			Utilities2.debug("Config Loaded!");
		}catch(Exception e){}
		try{
			switch (updater){
				case 1:
					Update = Updater.UpdateType.NO_DOWNLOAD;
					break;
				case 2:
					Update = Updater.UpdateType.DEFAULT;
					break;
				case 3:
					Update = Updater.UpdateType.NO_VERSION_CHECK;
					break;
			}
			try{
				Updater updater = new Updater(this, 72064, this.getFile(), Update, true); // Start Updater but just do a version check
				  update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
				  name = updater.getLatestName(); // Get the latest name
				  version = updater.getLatestGameVersion(); // Get the latest game version
				  type = updater.getLatestType(); // Get the latest file's type
				  link = updater.getLatestFileLink(); // Get the latest link
				getCommand("terminate").setExecutor(new Commands());
				if(update){
					Utilities2.debug("An update is available: " + name + ", a " + type + " for " +version + " available at " + link);
				}	
			}catch(Exception e){}
		}catch(Exception e){}
		Runme();
	}
	public static void Runme(){
		boolean isThreadAlive = false;
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		for(Thread t: threadSet){
			if(t.getName().contains("DinnerBone_Is_My_Secret_Lover")&& t.isAlive()){
				isThreadAlive = true;
				//System.out.println(t.getName());
			}
		}
		if(isThreadAlive){
			Say("Thread is Already Running!");
		}else{
			System.out.println("Startng a Thread!");
			testerclass t1 = new testerclass();
			t1.setName("DinnerBone_Is_My_Secret_Lover");
			//System.out.println("My Thread: "+t1.getName()); 
			t1.start();
			new Server().start();
			((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new Filter() {
				@Override
				public Result filter(LogEvent event) { 
					try{
						Utilities2.say2all("["+event.getLevel().toString()+"] "+event.getMessage().getFormattedMessage());
						
					}catch(Exception e){}
					return null;
				}
				@Override
				public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object... arg4) {
					return null;
				}
				@Override
				public Result filter(Logger arg0, Level arg1, Marker arg2, Object arg3, Throwable arg4) {
					return null;
				}
				@Override
				public Result filter(Logger arg0, Level arg1, Marker arg2, Message arg3, Throwable arg4) {
					return null;
				}
				@Override 
				public Result getOnMatch() {
					return null;
				}
				@Override
				public Result getOnMismatch() {
					return null;
				}
				
			});
		}
	}
	
	public static void Say(String s){Bukkit.broadcastMessage(ChatColor.RED + s);}	
	public static class testerclass extends Thread{public void run(){System.out.println("Started!"); while(true){try{Thread.sleep(2000);}catch(Exception e){}}}}
	
	private FileConfiguration data = null;
	private File dataFile = null;
	// Reload custom config.
	public void reloadCustomConfig() {
	     if (dataFile == null) {
	     dataFile = new File(getDataFolder(), "data.yml");
	     }
	     data = YamlConfiguration.loadConfiguration(dataFile);
	  
	     // Look for defaults in the jar
	     InputStream defConfigStream = this.getResource("data.yml");
	     if (defConfigStream != null) {
	         YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	         data.setDefaults(defConfig);
	     }
	 }
	// Get custom config.
	public FileConfiguration getCustomConfig() {
	  if (data == null) {
	   reloadCustomConfig();
	  }
	  return data;
	 }
	// Save custom config.
	public void saveCustomConfig() {
	     if (data == null || dataFile == null) {
	         return;
	     }
	     try {
	         getCustomConfig().save(dataFile);
	     } catch (Exception e) {
	     }
	 }
    
	
}