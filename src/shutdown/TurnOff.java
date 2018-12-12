package shutdown;

import java.io.IOException;

public class TurnOff {
    
    //Shutdown pc over time or now.
    public void shutdown(int time) 
    {    
        String shutdownCommand = null, t = time == 0 ? "now" : String.valueOf(time);
        
        switch(DetermineOs.returnOs()){
            case "win" :
                shutdownCommand = "shutdown.exe -s -t " + t;
                break;
            case "mac" : 
                shutdownCommand = "shutdown -h " + t;
                break;
            case "lin" :
                shutdownCommand = "shutdown -r " + t;
        }
        
        try {
            Runtime.getRuntime().exec(shutdownCommand);
        } catch (IOException ex){}
    }
    
    //Stop the shutdown command.
    public void abortShutdown()
    {    
        String command = null;
        
        switch(DetermineOs.returnOs()){
            case "win" :
                command = "shutdown.exe -a";
                break;
            case "mac" : 
                command = "killall shutdown";
                break;
            case "lin" :
                command = "shutdown -c";
        }
   
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException ex){}
    }
}