import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MovieLayout {
JFrame frameobj;
MovieLayout(){

frameobj = new JFrame("Movie Selection"); 
frameobj.getContentPane().setBackground(Color.WHITE);

 //labels
JLabel topLabel = new JLabel("WELCOME MOVIE WATCHERS", JLabel.CENTER);
JLabel label1 = new JLabel("Recommended Movies", JLabel.LEFT);
topLabel.setFont(new Font("Georgia", Font.BOLD, 28)); 
label1.setFont(new Font("SansSerif", Font.BOLD, 20)); 
topLabel.setForeground(new Color(51, 51, 51));
label1.setForeground(new Color(51, 51, 51)); // Dark gray text
label1.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

//buttons
JButton b1 = createCardButton("Paradise"); JButton b2 = createCardButton("Doraemon"); JButton b3 = createCardButton("Baahubali"); 
JButton b4 = createCardButton("Avatar");JButton b5 = createCardButton("RRR");JButton b6 = createCardButton("Avengers");
JButton b7 = createCardButton("Jailer");JButton b8 = createCardButton("Kalki");JButton b9 = createCardButton("Vikram");JButton b10 = createCardButton("Mission Impossible");

//panel for buttons
JPanel buttonPanel = new JPanel();
buttonPanel.setBackground(Color.WHITE);
buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Left aligned like a list
buttonPanel.add(b1); 
buttonPanel.add(b2); 
buttonPanel.add(b3);
buttonPanel.add(b4);
buttonPanel.add(b5);
buttonPanel.add(b6);
buttonPanel.add(b7);
buttonPanel.add(b8);
buttonPanel.add(b9);
buttonPanel.add(b10);

//panel for labels
frameobj.setLayout(new BorderLayout()); 

topLabel.setHorizontalAlignment(SwingConstants.CENTER); 
label1.setHorizontalAlignment(SwingConstants.LEFT);  

JPanel topPanel = new JPanel(new BorderLayout());
topPanel.setBackground(Color.WHITE);
topPanel.add(topLabel,BorderLayout.NORTH); 
topPanel.add(label1,BorderLayout.SOUTH); 
frameobj.add(topPanel,BorderLayout.NORTH); 

buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); 
frameobj.add(buttonPanel); 
frameobj.setSize(900, 400); 
frameobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frameobj.setLocationRelativeTo(null); // Center the window on the screen
frameobj.setVisible(true);
}

private JButton createCardButton(String movieTitle) {
String cardText = movieTitle;
    
ImageIcon rawIcon = new ImageIcon(movieTitle + ".jpeg"); 
Image scaledImage = rawIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
ImageIcon scaledIcon = new ImageIcon(scaledImage);

JButton button = new JButton( scaledIcon);
button.setBackground(Color.ORANGE);
button.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
button.setFocusPainted(false);

button.setPreferredSize(new Dimension(120, 120));
button.setCursor(new Cursor(Cursor.HAND_CURSOR));

button.addActionListener(e -> {
    frameobj.setVisible(false);
    new MovieTicketBookingSystem(movieTitle).setVisible(true);
});

return button;
}


public static void main(String [] args){ 
new MovieLayout(); 
}} 