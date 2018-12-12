package shutdown;

public class DetermineOs {
    
    private static final String OS = System.getProperty("os.name").toLowerCase();    
    
    //check if os is windows
    public static boolean isWindows() 
    {
        return (OS.contains("win"));
    }

    //check if os is mac
    public static boolean isMac() 
    {
        return (OS.contains("mac"));
    }
    
    //check if os is linux
    public static boolean isUnix() 
    {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }
    
    //return short version of os name.
    public static String returnOs()
    {
        if(isWindows()){
           return "win";
        }else if (isMac()){
           return "mac";
        }else if(isUnix()){
           return "lin";
        }
        return " ";
    }
}
