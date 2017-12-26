import static com.sun.javafx.fxml.expression.Expression.or;
import static com.sun.javafx.fxml.expression.Expression.or;
import static java.lang.Integer.parseInt;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static javafx.beans.binding.Bindings.or;
import static javax.management.Query.or;
import static javax.xml.bind.DatatypeConverter.parseString;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.Timer;
import net.risingworld.api.database.Database;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.Threading;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerEnterAreaEvent;
import net.risingworld.api.events.player.PlayerObjectInteractionEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.events.player.inventory.PlayerChestToInventoryEvent;
import net.risingworld.api.events.player.inventory.PlayerInventoryToChestEvent;
import net.risingworld.api.objects.Chest;
import net.risingworld.api.objects.Inventory;
import net.risingworld.api.objects.Item;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Area;
import static net.risingworld.api.utils.Crosshair.Chest;
import net.risingworld.api.utils.Definitions;
import net.risingworld.api.utils.Vector3i;
import net.risingworld.api.worldelements.World3DModel;
public class BootyBay extends Plugin implements Listener{
 protected Database db = null;
 @Override
    public void onEnable(){
        
   {
            db = getSQLiteConnection(getPath() + "/assets/Economy.db");
            db.execute("CREATE TABLE IF NOT EXISTS 'Items' ("
                        + "'ForSale' CHAR(64) NOT NULL DEFAULT ('Yes'),"
                        + "'Item' CHAR(64) NOT NULL DEFAULT ('[Noitem]'),"
			+"'Price' INTEGER);");
            db.execute("CREATE TABLE IF NOT EXISTS 'Money' ("
                        + "'User' CHAR(64) NOT NULL DEFAULT ('[NoUser]'),"
			+"'Amount' INTEGER);");
            db.execute("CREATE TABLE IF NOT EXISTS 'itemPurchase' ("
                        + "'WillBuy' CHAR(64) NOT NULL DEFAULT ('Yes'),"
                        + "'Item' CHAR(64) NOT NULL DEFAULT ('[NoUser]'),"
			+"'Amount' INTEGER);");
       		registerEventListener(this);
	}
              
    }
    @Override
    public void onDisable(){
        db.close();
        //...
    }
@EventMethod
    public void onConnect(PlayerConnectEvent event){
     area a = new area(){
    };
    Player p = event.getPlayer();
      p.setListenForObjectInteractions(true);
                  p.setAttribute("ForSale", null);
                  p.setAttribute("Item", null);
                  p.setAttribute("Price", null); 
  String GetPlayer = p.getName(); 
  db = getSQLiteConnection(getPath() + "/assets/Booty.db");
  int id = 1;
 String query = "SELECT * FROM Money WHERE `User` = '"+GetPlayer+"'";
		try (ResultSet result = db.executeQuery(query)){
                if(result.next())
			{
                        if (event.isNewPlayer()){
                            db.executeUpdate ("INSERT INTO Money (`User`,`Amount`) VALUES ('" + GetPlayer + "',2000);");
                                                }
                    }    
                p.setAttribute("TotAmount", 2000);
                        }  
                catch (SQLException e)
		{
			e.printStackTrace();
                }
                
p.setAttribute("TotAmount", 2000);
   }
   @EventMethod
    public void onSpawn(PlayerSpawnEvent event){
              Player player = event.getPlayer();
        
                String query = "SELECT * FROM Money WHERE `User` = '" + player + "'";
                  player.sendTextMessage("[#42aaf4]"+player);
            	try(ResultSet result = db.executeQuery(query)){
                    if(result.next())
                    {
                int id = result.getInt("id");
                String User= result.getString("User");
                String Amount = result.getString("Amount");
                player.sendTextMessage("[#38f43b]"+User);
                player.sendTextMessage("[#f4f138]"+Amount);
	        player.setAttribute("User", User);
                player.setAttribute("User", Amount);    
                    }}catch(SQLException e){
	    //this happens when an SQL exception occurs. You have to catch this
	    //exception. If you just want to print the error, you could call
	    //e.printStackTrace();
	}
  
  
    
    player.sendTextMessage("[#83f442]Booty Bay Menu [#81c7ea]'");
    player.sendTextMessage("[#83f442]To teleport to Booty Bay, [#f2d813]'/tp BootyBay'");
    player.sendTextMessage("[#83f442]For a List of Items Currently being Sold:[#f2d813]'/Items'");
    player.sendTextMessage("[#83f442]To See Your Dubloon Balance, [#f2d813]'/Balance'");
    player.sendTextMessage("[#83f442]There is some useful info and a list of Commands in the journal.[#f2d813](j)[#83f442]'");
  if (player.isAdmin()){
      player.sendTextMessage("[#83f442] To get a list of Admin Commands, Type [#f2d813]'/Badmin' ");
 }
    }
     @EventMethod
     public void onCommand(PlayerCommandEvent event){
         Player player = event.getPlayer();
        Server server = getServer();
        String Pname = player.getName();
        String command = event.getCommand();
        //split the command
        String[] cmd = command.split(" ");
        //check the command, example: teleport command
        if (cmd[0].equalsIgnoreCase("/Exchange")){
            if (cmd.length==4){
                int Amount = parseInt(cmd[1]);
                String thing = parseString(cmd[2]);
                String toWhom = parseString(cmd[3]); 
                
                String query = "SELECT * FROM Money WHERE `User` = '" + toWhom + "'";
                //  int Amount = result.getInt("Amount");
             
                        try (ResultSet result = db.executeQuery(query)) {
              if(result.next())
			{
		player.sendTextMessage("[#5cf442]La La La"+toWhom);
                
                          String User = result.getString("User");
                          int Getamount = result.getInt("Amount");
                          int total = (int) (Amount * 5 +Getamount);  //exbahnge Rate will change every two Weeks
                           db.executeUpdate("UPDATE `Money` SET `Amount` = '" + total + "' WHERE `User` = '" + toWhom + "' ");
 
		}
            
          } 
          catch (SQLException e)
		{e.printStackTrace();}
                }
      
     }
        if (cmd[0].equalsIgnoreCase("/SetBalance")){
        if (cmd.length==3){
                int SetAmount = parseInt(cmd[1]);
                String toWhom = parseString(cmd[2]);     
            String query = "SELECT * FROM Money WHERE `User` = '" + toWhom + "'";
              db.executeUpdate("UPDATE `Money` SET `Amount` = '" + SetAmount + "' WHERE `User` = '" + toWhom + "' ");
              player.sendTextMessage("[#FF0000]BootyBux: "+SetAmount);
        }
        

     }
        if (cmd[0].equalsIgnoreCase("/CheckUserBal")){
        if (cmd.length==2){
                
                String toWhom = parseString(cmd[1]);     
            String query = "SELECT * FROM Money WHERE `User` = '" + toWhom + "'";
           
            try (ResultSet result = db.executeQuery(query)) {
              if(result.next())
			{
                 String user = result.getString("User");
                int Balance = result.getInt("Amount");
                
                player.sendTextMessage(""+user+"'s Dubloon Balance: [#FF0000] "+Balance);   
                        }  
            
               
            }catch (SQLException e)
		{e.printStackTrace();}
        }
            
           
       
        }
        
         if (cmd[0].equalsIgnoreCase("/RemMoney")){
             if (cmd.length==3){
                int DelAmount = parseInt(cmd[1]);
                String toWhom = parseString(cmd[2]); 
                player.sendTextMessage("[#5cf442]La La La"+toWhom);
               String query = "SELECT * FROM Money WHERE `User` = '" + toWhom + "'";
             try (ResultSet result = db.executeQuery(query)) {
                  if(result.next())
			{ 
		player.sendTextMessage("[#5cf442]La La La"+toWhom);
                        String User = result.getString("User");
                        int Getamount = result.getInt("Amount");
                        int total = (int) (Getamount - DelAmount);
                db.executeUpdate("UPDATE `Money` SET `Amount` = '" + total + "' WHERE `User` = '" + toWhom + "' ");
                   player.sendTextMessage("[#FF0000]BootyBux: "+total);
         }
     } 
       catch (SQLException e)
	{
			//e.printStackTrace();
             }
       }
            }
       if(cmd[0].equalsIgnoreCase("/Items")){
            String query1 = "SELECT * FROM Items WHERE `ForSale` = 'yes'";
        
         try (ResultSet result = db.executeQuery(query1)){
                while(result.next())
			{
                          
                String ForSale = result.getString("ForSale");
                String Item = result.getString("Item");
                int Price = result.getInt("Price");
             if (ForSale.equals("yes")){
                 player.sendTextMessage("[#5cf442]"+Item+": [#D4AF37]"+Price);
             }
                    }    
                //p.setAttribute("TotAmount", 2000);
                        }  
                
                catch (SQLException e)
		{
			e.printStackTrace();
                }
       }   
       if(cmd[0].equalsIgnoreCase("/balance")){
            String query = "SELECT * FROM Money WHERE `User` = '" + Pname + "'";
        try(ResultSet result = db.executeQuery(query)){
                    if(result.next())
                    {
                String User= result.getString("User");
                int Amount = result.getInt("Amount");
                player.sendTextMessage("Your Dubloon Balance: [#f4f138]"+Amount);
	            }} catch(SQLException e){}
            }
    
       
     }
    @EventMethod
    public void PlayerInventoryToChestEvent(PlayerInventoryToChestEvent event){
            Player player = event.getPlayer();
    final int infoId = event.getChestID();
    String Pname = player.getName();       
    Chest chest = getWorld().getChest(infoId);
     Item[] Items = chest.getItems();
     //Item item = event.getItem();
     Item item = chest.getItem(1); 
     // player.sendTextMessage("yes"+NameofItem);
    if (item.getName() != null && item.getName().equals("bandage")){
        player.sendTextMessage("[#FF0000]BootyBux: ");
    }
      
         //Item item = chest.getItem();
         //Item item = event.getItem();
   //     
      //  if (items[i] !=null){
                 //  player.sendTextMessage(""+items[i]);
                 // return;
             ///  } 
       // else if (items[i].getName().equals("bandage")){
                                   //  player.sendTextMessage("yes");
       // }  
   
          
       
         
                

    
       //     player.sendTextMessage("[#ffff00] chest id: " + chest.getID());
            if (infoId == 0){
                
       
        

                 chest.clear();
   String query = "SELECT * FROM Money WHERE `User` = '" + Pname + "'";
    try (ResultSet result = db.executeQuery(query)){
                while(result.next())
			{
                String User= result.getString("User");
                int Amount = result.getInt("Amount");
             
               int addto = 100;  
               int  total = Amount+addto;             
                   db.executeUpdate("UPDATE `Money` SET `Amount` = '" + total + "' WHERE `User` = '" + Pname + "' ");
           chest.clear();
    player.sendTextMessage("[#ffff00] chest id: " + chest.getID());
               
                     }    
                        }  
                
                catch (SQLException e)
		{
			e.printStackTrace();
                }
   
       
    }
  
    }  
    
}


    