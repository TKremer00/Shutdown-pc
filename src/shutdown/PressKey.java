package shutdown;

import java.awt.AWTException;
import java.awt.Robot;

public class PressKey {
    
    private static Robot keyPresser;

    public static void pressKey(int key, int times) 
    {
        try {
            keyPresser = new Robot();
        
            for (int i = 0; i < times; i++)
            {
                keyPresser.keyPress(key);
                keyPresser.keyRelease(key);        
            }
        } catch (AWTException e) {}
    }
}
