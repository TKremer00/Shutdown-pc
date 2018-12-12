package shutdown;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class Shutdown extends JFrame
{

    public static void main(String[] args)
    {
        JFrame frame = new Shutdown();
        frame.setSize(340, 250);
        frame.setUndecorated(true);
        FrameDragListener frameDragListener = new FrameDragListener(frame);
        frame.addMouseListener(frameDragListener);
        frame.addMouseMotionListener(frameDragListener);
        frame.setContentPane(new Panel());
        frame.setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    }
    
    public static class FrameDragListener extends MouseAdapter {

        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) 
        {
            this.frame = frame;
        }

        @Override
        public void mouseReleased(MouseEvent e) 
        {
            mouseDownCompCoords = null;
        }

        @Override
        public void mousePressed(MouseEvent e) 
        {
            mouseDownCompCoords = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) 
        {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}
