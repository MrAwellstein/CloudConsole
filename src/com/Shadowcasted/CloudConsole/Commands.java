package com.Shadowcasted.CloudConsole;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("terminate") && sender.hasPermission("CloudConsole.terminate")){ 
			try{
				Utilities2.resetID(Utilities2.getIDfromName(args[0]));
			}catch(Exception e){Utilities2.Error("Couldn't Terminate User");}
			return true;
		}else{
			return false;
		}
		
	}
}