package shutdown;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.*;

public class Panel extends JPanel {

    private Timer timer;
    private TurnOff turnOff;
    private List<JTextField> fields;
    private List<JLabel> lables;
    private List<JButton> buttons;
    private int hours = 0,minutes = 0,seconds = 0;
    private boolean canprint = false;
    
    public Panel()
    { 
        setLayout(null);
        fields = new ArrayList<>(Arrays.asList(new JTextField(), new JTextField(), new JTextField()));
        lables = new ArrayList<>(Arrays.asList(new JLabel(":"),new JLabel(":")));
        buttons = new ArrayList<>(Arrays.asList(new JButton("Shutdown"), new JButton("Shutdown over time"), new JButton("Abort shutdown"), new JButton("Exit")));
       
        turnOff = new TurnOff();
        setupButtons();
    }
    
    private void setupButtons() 
    {    
        //Setup time inputs
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setHorizontalAlignment(JTextField.CENTER);
            fields.get(i).getDocument().addDocumentListener(new ToNext());
            fields.get(i).setBounds(10 + (110 * i),10,100 ,30);    
            add(fields.get(i));
        }
        
        for (int i = 0; i < lables.size(); i++) {
            lables.get(i).setBounds(112 + (110 * i), 10,5,30);
            add(lables.get(i));
        }
        
        //Buttons shutdown 
        for (int i = 0; i < 2; i++) {
            buttons.get(i).addActionListener(new ShutdownHandeler());
            buttons.get(i).setBounds(10 + (170 * i),50,150,150);
            add(buttons.get(i));
        }
        
        //Abort button
        buttons.get(2).addActionListener(new AbortHandeler());
        buttons.get(2).setBounds(10,210 ,160 ,30);
        add(buttons.get(2));
        
        //Exit button
        buttons.get(3).addActionListener(new CloseHandeler());
        buttons.get(3).setBounds(170,210,160 ,30);
        add(buttons.get(3)); 
    }
    
    public void update()
    {    
        remove(buttons.get(0));
        remove(buttons.get(1));
        countDown();
        timer = new Timer(1000 , (ActionEvent ae) -> { countDown(); });
        timer.setRepeats(true);
        timer.start();
    }
    
    private void countDown()
    {
        repaint();
        if(seconds != 0){
            if(seconds <= 1 && minutes <= 0 && hours <= 0){
                seconds--;
                System.exit(0);
            }
            seconds--;               
        }else if(seconds <= 0){
            minutes--;
            seconds = 59;
            
            if(minutes <= -1){
                hours--;
                minutes = 59;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        g.setFont(g.getFont().deriveFont( 80.0f ));
        int[] intArray = new int[] {hours , minutes, seconds};
        
        if(canprint){
            for (int i = 0; i < intArray.length; i++) {
                g.drawString(String.format("%02d", intArray[i]), 10 + (115 * i), 140);
                if (i + 1 != intArray.length ){
                    g.drawString(":", 105 + (110 * i), 135);
                }
            }
        }
    }
   
    //Call to TurnOff class whit correct action (now or over time).
    class ShutdownHandeler implements ActionListener {
        private int timeSec = 0;
        
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            JButton o = (JButton)ae.getSource();
            
            boolean stop = false;
            
            try{
                if(fields.get(0).getText().length() != 0){
                    hours = Integer.parseInt(fields.get(0).getText());
                }
                
                if(fields.get(1).getText().length() != 0){
                    minutes = Integer.parseInt(fields.get(1).getText());
                
                    while (minutes > 60){
                        minutes -= 60;
                        hours++;
                    }
                }
                
                if (fields.get(2).getText().length() != 0){
                    seconds = Integer.parseInt(fields.get(2).getText());
                    while (seconds > 3600){
                        seconds -= 3600;
                        hours++;
                    }
                    while (seconds > 60){
                        seconds -=60;
                        minutes++;
                    }
                }
                
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null, "This is not a time " + fields.get(0).getText() + ":" + fields.get(1).getText() + ":" + fields.get(2).getText());
                stop = true;
            }
            
            timeSec = hours * 3600;
            timeSec += minutes * 60;
            timeSec += seconds;
            
            if(!o.getText().toLowerCase().equals("shutdown") && timeSec <= 0 && !stop){
                if(JOptionPane.showConfirmDialog (null, "Forgot time? \nor do you want to shutdown now?","Warning",JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION){  
                   stop = true;
                }else{
                    turnOff.shutdown(timeSec);                
                    System.exit(0);
                }
            }else if(!stop && !o.getText().toLowerCase().equals("shutdown")){
                fields.get(0).setText(String.format("%02d", hours));
                fields.get(1).setText(String.format("%02d", minutes));
                fields.get(2).setText(String.format("%02d", seconds));
                canprint = true;
                buttons.get(2).setBounds(10, 210, 320, 30);
                remove(buttons.get(3));
                update();
                turnOff.shutdown(timeSec);
            }else if(!stop){
                turnOff.shutdown(timeSec);                
                System.exit(0);
            }
        }
    }
    
    //Go to the next input field.
    class ToNext implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent de)
        {
            if(fields.get(0).getText().length() == 2 && fields.get(1).getText().length() == 2 && fields.get(2).getText().length() == 0){
                PressKey.pressKey(KeyEvent.VK_TAB, 1);
            }else if(fields.get(0).getText().length() == 2 && fields.get(1).getText().length() == 0 && fields.get(2).getText().length() == 0){
                PressKey.pressKey(KeyEvent.VK_TAB, 1);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent de)
        {}

        @Override
        public void changedUpdate(DocumentEvent de)
        {}
    }
    
    //Stop the shutdown task.
    class AbortHandeler implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            canprint = false;
            for (int i = 0; i < fields.size(); i++) {
                fields.get(i).setText("");
            }
            hours = minutes = seconds = 0;
            buttons.get(2).setBounds(10,210 ,160 ,30);
            add(buttons.get(3));
            repaint();
            turnOff.abortShutdown();
            if(timer != null) {
                timer.stop();
                add(buttons.get(0));
                add(buttons.get(1));
            }
        }
    }
    
    //Close application.
    class CloseHandeler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            System.exit(0);
        }
    }
}
