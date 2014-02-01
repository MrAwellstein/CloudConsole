package com.Shadowcasted.CloudConsole;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Nikky
 * @helper TJ
 * 
**/
public class Utilities2 {
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here

		while(true){
			sleep(1000);
			say2all("Apples");
		}
	}
	public static int TAlive;
	public static int TCount;
	public static Boolean[] Alive;
	public static int Port = 1337;
	public static String[] Session;
	public static String[] username;
	
	public static void send2Client(int ID, String s){
		try{
			ThreadManager.writer[ID].write(s);
			ThreadManager.writer[ID].newLine();
			ThreadManager.writer[ID].flush();
		}catch(Exception e){}
	}
	
	public static void say2all(String s){
		try{
			int counter = 0;
			for(BufferedWriter writerz: ThreadManager.writer)
				try{
					if(Session[counter]!= null && Session[counter] != "ChatOnly" && !s.contains("Login ")/* && !s.contains("] ["+username[counter].toString()+"]")*/){
						writerz.write(s);
						writerz.newLine();
						writerz.flush();
					}
					counter++;
				}catch(Exception e){}
		}catch(Exception e){}
	}   
	public static void say2Chatz(String s){
		try{
			int counter = 0;
			for(BufferedWriter writerz: ThreadManager.writer)
				try{
					if(Session[counter]!= null){
						writerz.write(s);
						writerz.newLine();
						writerz.flush();
					}
					counter++;
				}catch(Exception e){}
		}catch(Exception e){}
	}   
	
	
	
	
	
	public static class ThreadManager extends Thread{

		public static double[] Version;
	    public static double allowedVersion = 1.0;
		public static ServerSocket server;
		public static Socket[] sock;
		public static BufferedReader[] reader;
		public static BufferedWriter[] writer;
		public static int MC;
		
		public ThreadManager(){
			try{
				Session = new String[MC];
				server = new ServerSocket(Port);
				sock = new Socket[MC];
				reader = new BufferedReader[MC];
				writer = new BufferedWriter[MC];
				Alive = new Boolean[MC];
				username = new String[MC];
				Version = new double[MC];
			}catch(Exception e){Error("Issues Bro");}
		}
		
		public void run(){
			while(true){
				try{    
					if(TCount == TAlive && TCount < MC){
						isDead();
						new Connectionz(TOJ, server).start();
						TCount++;
					}
					sleep(500);
				}catch(Exception e){}
			}
		}
		
		public static int TOJ = 0;
		
		public static void isDead(){
			int c = -1;
			for(Boolean temp: Alive){
				c++;
				try{
					if(temp == null){
						TOJ = c;
						break;
					}else if(temp == true){
					}else {
						TOJ = c;
						break;
					}
				}catch(Exception e){}
			}
		}

		public static class Connectionz extends Thread{
			public int ID = 0;
			public ServerSocket serverz;
			
			public Connectionz(int Number, ServerSocket sever){
				ID = Number;
				serverz = server;
			}
			
			public void run(){
				connect(serverz);
			}
			
			public void connect(ServerSocket server){
				debug(ID, "Started!");
				try{
					sock[ID] = server.accept();
					TAlive++;
					Alive[ID] = true;
					try{
						debug("Connected from " + sock[ID].getInetAddress() + " on port "
					             + sock[ID].getPort() + " to port " + sock[ID].getLocalPort() + " of "
					             + sock[ID].getLocalAddress());
					}catch(Exception e){}
					try{
						reader[ID] = new BufferedReader(new InputStreamReader(sock[ID].getInputStream()));
						writer[ID] = new BufferedWriter(new OutputStreamWriter(sock[ID].getOutputStream()));
						try{
							new Inbox(reader[ID], ID).start();
							try{
                                send2Client(ID, "[Update] -info");
                                new UpdateChecker2(ID).start();
                            }catch(Exception e){}
						}catch(Exception e){Error(ID, "Error Starting InboxThread");}
					}catch(Exception e){Error(ID, "Error Aquiring Streams");}
				}catch(Exception e){Error(ID, "Error Accepting Connection11");}
			}
			
			
			
			public static class UpdateChecker2 extends Thread{
                private int ID;
                public UpdateChecker2(int ID){
                    this.ID = ID;
                }
                @Override
                public void run(){
                    Utilities2.sleep(700);
                    if(Version[ID] < allowedVersion){
                        send2Client(ID,"You Dont Have The Correct Version Of CloudClient. You Need To Update!");
                        logout(ID);
                    }else{
                    	
                    }
                }
            }
			
			
			
			
			public static class Inbox extends Thread{
				private BufferedReader MyReader;
				public int MyID;
				
				public Inbox(BufferedReader read, int ID){
					MyReader = read;
					MyID = ID;
					
				}
				
				public void run(){
					String s = "";
					while(true){
						try{
							while((s = MyReader.readLine())!= null){
								gameMaster(MyID, s);
							}
						}catch(Exception e){resetID(MyID); break;}
					}
				}   
			}   
		}   
	}
	
public static class sendPlayers extends Thread{

		public void run(){
			try{
				sleep(100);
				String list = "[Players] ";
				Player[] ps = Bukkit.getOnlinePlayers();
				for(Player p: ps){
					list+=p.getName().toString()+", ";
				}
				say2Chatz(list);
			} catch(Exception e) {Error("Failed to Send Players");}
		}
	}








	public static class sendPlayerInfo extends Thread{
		int ID = 0;
		Player p;
		public sendPlayerInfo(int ID, Player player){
			this.ID = ID;
			p = player;
		}
		public void run(){
			try{
				if(!Session[ID].equals("ChatOnly")|| Session[ID] != null){
					String info = "[Player] -info ";
					info += "[Name]"+p.getName().toString()+"[Name]";
					info += "[Locate]X: "+(int) p.getLocation().getX() + "  Y: " + (int) p.getLocation().getY() + "  Z: " + (int) p.getLocation().getZ()+"[Locate]";
					info += "[Health]"+getHealthPercent(p)+"[Health]";
					info += "[IP]"+p.getAddress().toString()+"[IP]";
					send2Client(ID, info);
				}
			}catch(Exception e){}
		}
	}	

	public static String getHealthPercent(Player p){
		try{
			int temp = (int) ((p.getHealth() / 20d)*100d);
			return temp + "%";
		}catch(Exception e){return "Unable To Parse Health";}
	}
	public static void playerParsng(int ID, String msg){
		if(msg.startsWith("[Players]")){
			new sendPlayers().start();
		}else if(msg.startsWith("[Player] -info ")){
			try{
				String temp = msg.replace("[Player] -info ", "");
				new sendPlayerInfo(ID, Bukkit.getPlayer(temp)).start();
			}catch(Exception e){send2Client(ID, "Failed to grab info");}
		}
		
		
	}
	
	
	
	
	public static void resetID(int ID){
		try{
			debug("The Guy who's IP is " + ThreadManager.sock[ID].getInetAddress() + " on port "
		             + ThreadManager.sock[ID].getPort() + " just disconnected :\\");
		}catch(Exception e){}
		try{
			ThreadManager.sock[ID].close();
		}catch(Exception e){}
		try{
			Session[ID] = null; 
			username[ID] = null; 
			Alive[ID]= false; 
			try{
				ThreadManager.writer[ID] = null; 
				ThreadManager.reader[ID] = null; 
				try{
					ThreadManager.sock[ID] = null;
					debug(ID,"Disconnected Successfully!"); 
					debug(ID," This Thread Just Got Closed");
				}catch(Exception e){Error(ID,"Failed To Wipe Current ID.Sock");}
			}catch(Exception e){Error(ID,"Failed To Wipe Current ID.Stream");}
			TAlive--; TCount--;
		}catch(Exception e){Error(ID,"Failed To Wipe Current ID.Info");}
	}
	
	public static void logout(int ID){
			resetID(ID);
	}
	/*public static void p(String s){
		System.out.println(s);
	}
	
	public static void p(int ID, String s){
		System.out.println("["+ID+"] " +s);
	}*/
	
	public static void sleep(int Time){
		try{
			Thread.sleep(Time);
		}catch(Exception e){}
	}
	
	public static int error = 1;
	public static int debug = 1;
	
	public static void Error(String msg) {
		switch (error) {
		case 0:
			break;
		case 1:
			say2all("[ERROR] " + msg);
			break;
		case 2:
			Broadcast(ChatColor.BLACK + "[" + ChatColor.DARK_RED + "ERROR" + ChatColor.BLACK + "] " + ChatColor.RED + msg);
			break;
		}
	}
	
	public static void debug(String msg) {
		switch (debug) {
		case 0:
			break;
		case 1:
			say2all("[Debug] " + msg);
			break;
		case 2:
			Broadcast(ChatColor.BLACK + "[" + ChatColor.DARK_AQUA + "Debug" + ChatColor.BLACK + "] " + ChatColor.AQUA + msg);
			say2all("[Debug] " + msg);
			break;
		}
	}
	
	public static void Error(int ID, String msg) {
		switch (error) {
		case 0:
			break;
		case 1:
			say2all("[ERROR] ["+ID+"] " + msg);
			break;
		case 2:
			Broadcast(ChatColor.BLACK + "[" + ChatColor.DARK_RED + "ERROR" + ChatColor.BLACK + "] ["+ID+"] "+ ChatColor.RED + msg);
			say2all("[ERROR] ["+ID+"] " + msg);
			break;
		}
	}
	
	public static void debug(int ID, String msg) {
		switch (debug) {
		case 0:
			break;
		case 1:
			say2all("[Debug] ["+ID+"] " + msg);
			break;
		case 2:
			Broadcast(ChatColor.BLACK + "[" + ChatColor.DARK_AQUA + "Debug" + ChatColor.BLACK + "] ["+ID+"] " + ChatColor.AQUA + msg);
			say2all("[Debug] ["+ID+"] " + msg);
			break;
		}
	}
	
	public static void Broadcast(String msg) {Bukkit.broadcastMessage(msg);}
	
	public static String OUL = "";
	public static String MUL = "";
	public static String AUL = "";
	public static String CUL = "";
	
	public static void login(int ID, String s){
		try{
			
			String userandpass = s.replace("Login ", "");
			if(!userandpass.startsWith("none")&& !isAlreadyLoggedIn(ID, userandpass)){
				if(MUL.contains((userandpass + "|"))){
					Session[ID] = "Mod";
					send2Client(ID, "[Debug] You Have Successfully Logged In!");
				}else if(AUL.contains((userandpass + "|"))){
					Session[ID] = "Admin";
					send2Client(ID, "[Debug] You Have Successfully Logged In!");
				}else if(OUL.contains((userandpass + "|"))){
					Session[ID] = "Owner";
					send2Client(ID, "[Debug] You Have Successfully Logged In!");
				}else if(CUL.contains((userandpass + "|"))){
					Session[ID] = "ChatOnly";
					send2Client(ID, "[Debug] You Have Successfully Logged In!");
				}else{
					send2Client(ID, "[Debug] Sorry, But Your User:Pass was invalid!");
					
					resetID(ID);
					return;
				}
				setUsername(ID, s);
			}
		}catch(Exception e){}
	}
	
	public static boolean isAlreadyLoggedIn(int ID, String Username){
		try{
			try{
			String[] temp = Username.split(":");
			Username = temp[0];
			}catch(Exception e){}
			for (String user: username){
				if(user!=null){
					if(user.contains(Username)){
						send2Client(ID,"[Debug] Sorry, Another User Is Already Logged In With That Name");
						resetID(ID);
						return true;
					}
				}
			}
		}catch(Exception e){Error("Failed to Do Username Thing");}
		return false;
	}

	public static void setUpdateInfo(int ID, String msg){
		try{
			if(msg.contains("[Update] I'm v")){
				String temp = msg.replace("[Update] I'm v", "");
				ThreadManager.Version[ID] = Double.parseDouble(temp);
			}
		}catch(Exception e){send2Client(ID, "Issues Resolving Your Version");}
	}

	public static void setUsername(int ID, String s){
		try{
			String usernameandpass = s.replace("Login ", "");
			String[] SplitUsernameAndPassword = usernameandpass.split(":");
			username[ID] = SplitUsernameAndPassword[0];
			debug("["+ID+"]  " + username[ID].toString() + " Logged In");
			p("[" + ID + "] "+username[ID].toString()+" Logged in!");
		}catch(Exception e){}
	}
	
	public static void gameMaster(int ID, String msg) {
		if(msg.startsWith("Login ")){login(ID, msg);}
		if(Session[ID].equals("Admin")){adminstuff(ID, msg);}
		if(Session[ID].equals("Mod")){modstuff(ID, msg);}
		if(Session[ID].equals("Owner")){ownerstuff(ID, msg);}
		if(Session[ID].equals("ChatOnly")){chatstuff(ID, msg);}
	
	}
	

	
	
	
	
	public static String AllowedOwnerCommands = "all";
	public static String AllowedAdminCommands = "all";
	public static String AllowedModeratorCommands = "reload";
	public static void p(String msg){
		System.out.println(msg);
	}
	public static void adminstuff(int ID, String msg){
		try{
			if(msg.startsWith("!c ")){
				p("["+ID+"] [A] ["+username[ID].toString() + "]: " + msg );
				Commands(ID, AllowedAdminCommands, msg);
			} else if(msg.startsWith("@c ")){
				String temp = msg.replace("@c ", "");
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.DARK_GRAY + "] ["+ChatColor.RED+"A"+ChatColor.DARK_GRAY+"] <" + ChatColor.WHITE+ username[ID] + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + temp);
				say2Chatz("[Chat] [CloudConsole] [A] <"+ username[ID] + "> " +temp);
			} else if(msg.contains("[Update] ")){setUpdateInfo(ID, msg);}
			else if (msg.contains("[Player")){playerParsng(ID,msg);}
		}catch(Exception e){}
	}    
	
	public static void ownerstuff(int ID, String msg){
		try{
			if(msg.startsWith("!c ")){
				p("["+ID+"] [O] ["+username[ID].toString() + "]: " + msg );
				Commands(ID, AllowedOwnerCommands, msg);
			}
			else if(msg.startsWith("@c ")){
				String temp = msg.replace("@c ", "");
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.DARK_GRAY + "] ["+ChatColor.GREEN+"O"+ChatColor.DARK_GRAY+"] <" + ChatColor.WHITE+ username[ID] + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + temp);
				say2Chatz("[Chat] [CloudConsole] [O] <"+ username[ID] + "> " +temp);//Bukkit.broadcastMessage(ChatColor.BLACK+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.BLACK + "]");
			} else if(msg.contains("[Update] ")){setUpdateInfo(ID, msg);}
			else if (msg.contains("[Player")){playerParsng(ID,msg);}
            
		}catch(Exception e){}
	}    
	public static void chatstuff(int ID, String msg){
		try{
			if(msg.startsWith("@c ")){
				String temp = msg.replace("@c ", "");
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.DARK_GRAY + "] ["+ChatColor.LIGHT_PURPLE+"C"+ChatColor.DARK_GRAY+"] <" + ChatColor.WHITE+ username[ID] + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + temp);//Bukkit.broadcastMessage(ChatColor.BLACK+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.BLACK + "]");
				say2Chatz("[Chat] [CloudConsole] [C] <"+ username[ID] + "> " +temp);//Bukkit.broadcastMessage(ChatColor.BLACK+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.BLACK + "]");
			} else if(msg.contains("[Update] ")){setUpdateInfo(ID, msg);}
			else if (msg.contains("[Player")){playerParsng(ID,msg);}
            
		}catch(Exception e){}
	}
	public static void modstuff(int ID, String msg){
		try{
			if(msg.startsWith("!c ")){
				p("["+ID+"] [M] ["+username[ID].toString() + "]: " + msg );
				Commands(ID, AllowedModeratorCommands, msg);
			}
			else if(msg.startsWith("@c ")){
				String temp = msg.replace("@c ", "");
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.DARK_GRAY + "] ["+ChatColor.BLUE+"M"+ChatColor.DARK_GRAY+"] <" + ChatColor.WHITE+ username[ID] + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + temp);//Bukkit.broadcastMessage(ChatColor.BLACK+"["+ChatColor.AQUA+"CloudConsole"+ChatColor.BLACK + "]");
				say2Chatz("[Chat] [CloudConsole] [M] <"+ username[ID] + "> " +temp);
			} if(msg.contains("[Update] ")){setUpdateInfo(ID, msg);}
			else if (msg.contains("[Player")){playerParsng(ID,msg);}
            
		}catch(Exception e){}
	}

	public static void Commands(int ID, String permissions, String attempted){
		String filtered = attempted.replace("!c ", "");
		String[] Command = filtered.split(" ");
		if(permissions.contains(Command[0]) || permissions.contains("all")){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), filtered);
		}else{
			Error(ID,"["+username[ID].toString()+"] Permissions Denied!");
			send2Client(ID, "Permissions Denied!");
		}
	}
	
	public static int getIDfromName(String Username){
		int counter = 0;
		for(String user: username){
			if(user!= null){
				if(user.equals(Username)){
					return counter;
				}
			}
			counter++;
		}
		return 666;
	}

	
	
}