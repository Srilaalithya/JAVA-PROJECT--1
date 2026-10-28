import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeatBookingSystem extends JFrame {

    // Configuration constants
    private static final int ROWS = 5;
    private static final int COLS = 6;
    
    // UI Styling colors
    private final Color AVAILABLE_COLOR = new Color(46, 204, 113); // Emerald Green
    private final Color BOOKED_COLOR = new Color(231, 76, 60);    // Alizarin Red
    private final Color SELECTED_COLOR = new Color(52, 152, 219);  // Blue (for clicking)

    public SeatBookingSystem(String movieTitle) {
        // Set up the main window frame
        setTitle("Select seats for:"+movieTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null); // Center on screen
        setLayout(new BorderLayout(10, 10));

        // 1. TOP PANEL: Selection Drop-down Boxes (Language & Showtimes)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Language Dropbox Setup
        String[] languages = {"English (2D)", "English (3D)", "Spanish (2D)", "French (2D)"};
        JComboBox<String> comboLanguage = new JComboBox<>(languages);
        comboLanguage.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Showtime Dropbox Setup
        String[] showtimes = {"02:30 PM", "06:45 PM", "09:30 PM", "11:00 AM"};
        JComboBox<String> comboShowtimes = new JComboBox<>(showtimes);
        comboShowtimes.setFont(new Font("Arial", Font.PLAIN, 14));

        // Wrap dropdowns with descriptive labels
        JPanel langWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        langWrapper.add(new JLabel("Language:"));
        langWrapper.add(comboLanguage);

        JPanel timeWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timeWrapper.add(new JLabel("Time:"));
        timeWrapper.add(comboShowtimes);

        headerPanel.add(langWrapper);
        headerPanel.add(timeWrapper);
        add(headerPanel, BorderLayout.NORTH);

        // 2. CENTER PANEL: Grid Layout for Seats
        JPanel seatGridPanel = new JPanel(new GridLayout(ROWS, COLS, 8, 8));
        seatGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Generate interactive seat buttons
        for (int i = 0; i < ROWS * COLS; i++) {
            char rowChar = (char) ('A' + (i / COLS));
            int seatNum = (i % COLS) + 1;
            String seatLabel = "" + rowChar + seatNum;

            JButton seatButton = new JButton(seatLabel);
            seatButton.setFont(new Font("Arial", Font.BOLD, 11));
            seatButton.setFocusPainted(false);

            // Simulating a few pre-booked seats randomly for demonstration
            if (i == 3 || i == 7 || i == 14 || i == 22) {
                seatButton.setBackground(BOOKED_COLOR);
                seatButton.setForeground(Color.WHITE);
                seatButton.setEnabled(false); // Disable clicking for already booked seats
                seatButton.setToolTipText("Already Booked");
            } else {
                seatButton.setBackground(AVAILABLE_COLOR);
                seatButton.setForeground(Color.WHITE);
                seatButton.setToolTipText("Available");
                
                // Add click listener to toggle seat selection
                seatButton.addActionListener(new SeatClickListener());
            }

            seatGridPanel.add(seatButton);
        }
        add(seatGridPanel, BorderLayout.CENTER);

        // 3. BOTTOM PANEL: Screen Indicator and Legend
        JPanel footerPanel = new JPanel(new BorderLayout());
        
        JLabel lblScreen = new JLabel("------- SCREEN THIS WAY -------", SwingConstants.CENTER);
        lblScreen.setFont(new Font("Arial", Font.ITALIC, 12));
        lblScreen.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        footerPanel.add(lblScreen, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);
    }

    // Click handler to toggle between Available and Selected states
    private class SeatClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton) e.getSource();
            
            if (sourceButton.getBackground().equals(AVAILABLE_COLOR)) {
                sourceButton.setBackground(SELECTED_COLOR);
                sourceButton.setToolTipText("Selected");
            } else if (sourceButton.getBackground().equals(SELECTED_COLOR)) {
                sourceButton.setBackground(AVAILABLE_COLOR);
                sourceButton.setToolTipText("Available");
            }
        }
    }

    public static void main(String[] args) {
        // Run UI code on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            new SeatBookingSystem("test movie").setVisible(true);
        });
    }
}