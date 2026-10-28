import javax.swing.* ;
import java.awt.* ;
import java.util.* ;

public class MovieTicketBookingSystem extends JFrame {

    private static final int ROWS = 5;
    private static final int COLS = 6;

    private final Color AVAILABLE = new Color(46, 204, 113);
    private final Color BOOKED = new Color(231, 76, 60);
    private final Color SELECTED = new Color(52, 152, 219);

    private JButton[] seats = new JButton[ROWS * COLS];

    private JComboBox<String> languageBox;
    private JComboBox<String> timeBox;

    private JTextArea ticket;
    private String movieTitle;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Stores booked seats separately for every show.
    private Map<String, Set<String>> bookedSeats = new HashMap<>();

    // Seats currently selected by the user.
    private Set<String> selectedSeats = new HashSet<>();

    private String currentShow;

    public MovieTicketBookingSystem(String movieTitle) {
        this.movieTitle = movieTitle;
        setTitle("Selecting seats for:"+movieTitle);
        setSize(650, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(seatBookingPage(), "SEATS");
        mainPanel.add(receiptPage(), "RECEIPT");

        add(mainPanel);

        changeShow();
        cardLayout.show(mainPanel, "SEATS");
    }

    // ---------------- SEAT BOOKING PAGE ----------------

    private JPanel seatBookingPage() {
        JPanel page = new JPanel(new BorderLayout(10, 10));

        // Language and showtime dropdowns
        JPanel topPanel = new JPanel(new FlowLayout());

        String[] languages = {
            "Telugu (2D)",
            "Hindi (2D)",
            "Hindi (3D)",
            "English (2D)",
            "Tamil (2D)"
        };

        String[] times = {
            "11:00 AM",
            "02:30 PM",
            "06:45 PM",
            "09:30 PM"
        };

        languageBox = new JComboBox<>(languages);
        timeBox = new JComboBox<>(times);

        topPanel.add(new JLabel("Language:"));
        topPanel.add(languageBox);
        topPanel.add(new JLabel("Showtime:"));
        topPanel.add(timeBox);

        page.add(topPanel, BorderLayout.NORTH);

        // Screen and seat grid
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JLabel screen = new JLabel(
            "--------------- SCREEN THIS WAY ---------------",
            JLabel.CENTER
        );
        screen.setFont(new Font("Arial", Font.BOLD, 15));
        screen.setBorder(
            BorderFactory.createEmptyBorder(15, 5, 10, 5)
        );

        centerPanel.add(screen, BorderLayout.NORTH);

        JPanel seatGrid = new JPanel(
            new GridLayout(ROWS, COLS, 10, 10)
        );

        seatGrid.setBorder(
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        );

        for (int i = 0; i < ROWS * COLS; i++) {
            char row = (char) ('A' + i / COLS);
            int number = (i % COLS) + 1;
            String seatName = "" + row + number;

            JButton button = new JButton(seatName);
            button.setFont(new Font("Arial", Font.BOLD, 13));
            button.setOpaque(true);
            button.setFocusPainted(false);

            button.addActionListener(e -> selectSeat(button));

            seats[i] = button;
            seatGrid.add(button);
        }

        centerPanel.add(seatGrid, BorderLayout.CENTER);

        // Seat color legend
        JPanel legend = new JPanel(new FlowLayout());
        legend.add(createLegend("Available", AVAILABLE));
        legend.add(createLegend("Selected", SELECTED));
        legend.add(createLegend("Booked", BOOKED));

        centerPanel.add(legend, BorderLayout.SOUTH);

        page.add(centerPanel, BorderLayout.CENTER);

        // Booking button
        JButton bookButton = new JButton("Book Selected Seats");
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.addActionListener(e -> bookSeats());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(bookButton);

        page.add(bottomPanel, BorderLayout.SOUTH);

        languageBox.addActionListener(e -> changeShow());
        timeBox.addActionListener(e -> changeShow());

        return page;
    }

    private JLabel createLegend(String text, Color color) {
        JLabel label = new JLabel("  " + text + "  ");
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.WHITE);
        return label;
    }

    // ---------------- SEAT AVAILABILITY LOGIC ----------------

    private String getShowKey() {
        return languageBox.getSelectedItem() + " | "
             + timeBox.getSelectedItem();
    }

    // Creates different sample availability for each show.
    private Set<String> generateBookedSeats(String showKey) {
        Set<String> booked = new HashSet<>();
        Random random = new Random(showKey.hashCode());

        for (int i = 0; i < ROWS * COLS; i++) {
            if (random.nextDouble() < 0.35) {
                char row = (char) ('A' + i / COLS);
                int number = (i % COLS) + 1;

                booked.add("" + row + number);
            }
        }

        return booked;
    }

    private void changeShow() {
        currentShow = getShowKey();
        selectedSeats.clear();

        bookedSeats.putIfAbsent(
            currentShow,
            generateBookedSeats(currentShow)
        );

        refreshSeats();
    }

    private void selectSeat(JButton button) {
        String seat = button.getText();

        if (bookedSeats.get(currentShow).contains(seat)) {
            return;
        }

        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
        } else {
            selectedSeats.add(seat);
        }

        refreshSeats();
    }

    private void refreshSeats() {
        Set<String> booked = bookedSeats.get(currentShow);

        for (JButton button : seats) {
            String seat = button.getText();

            if (booked.contains(seat)) {
                button.setBackground(BOOKED);
                button.setForeground(Color.WHITE);
                button.setEnabled(false);

            } else if (selectedSeats.contains(seat)) {
                button.setBackground(SELECTED);
                button.setForeground(Color.WHITE);
                button.setEnabled(true);

            } else {
                button.setBackground(AVAILABLE);
                button.setForeground(Color.WHITE);
                button.setEnabled(true);
            }
        }
    }

    // ---------------- BOOKING TO RECEIPT CONNECTION ----------------

    private void bookSeats() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please select at least one seat."
            );
            return;
        }

        // Preserve the selected seats for the receipt.
        Set<String> confirmedSeats = new TreeSet<>(selectedSeats);

        // Mark confirmed seats as booked for this show.
        bookedSeats.get(currentShow).addAll(confirmedSeats);

        // Generate the receipt using the booking information.
        generateReceipt(confirmedSeats);

        selectedSeats.clear();
        refreshSeats();

        // Navigate to the receipt page.
        cardLayout.show(mainPanel, "RECEIPT");
    }

    // ---------------- RECEIPT PAGE ----------------

    private JPanel receiptPage() {
        JPanel page = new JPanel(new BorderLayout(10, 10));

        ticket = new JTextArea();
        ticket.setEditable(false);
        ticket.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        ticket.setMargin(new Insets(20, 20, 20, 20));

        JLabel title = new JLabel(
            "MOVIE TICKET / RECEIPT",
            JLabel.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 20));

        page.add(title, BorderLayout.NORTH);
        page.add(new JScrollPane(ticket), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton printButton = new JButton("Print Receipt");
        printButton.addActionListener(e -> {
            try {
                ticket.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not print receipt."
                );
            }
        });

        JButton backButton = new JButton("Back to Seats");
        backButton.addActionListener(e ->
            cardLayout.show(mainPanel, "SEATS")
        );

        bottomPanel.add(printButton);
        bottomPanel.add(backButton);

        page.add(bottomPanel, BorderLayout.SOUTH);

        return page;
    }

    private void generateReceipt(Set<String> confirmedSeats) {
        StringBuilder receipt = new StringBuilder();

        receipt.append("================================\n");
        receipt.append("       MOVIE BOOKING TICKET     \n");
        receipt.append("================================\n\n");

        receipt.append("Movie Name: ").append(movieTitle).append("\n");

        receipt.append("Language : ")
               .append(languageBox.getSelectedItem())
               .append("\n");

        receipt.append("Showtime : ")
               .append(timeBox.getSelectedItem())
               .append("\n");

        receipt.append("Seats    : ")
               .append(confirmedSeats)
               .append("\n");

        receipt.append("Seat Count: ")
               .append(confirmedSeats.size())
               .append("\n\n");

        receipt.append("================================\n");
        receipt.append("       BOOKING CONFIRMED        \n");
        receipt.append("================================\n");

        ticket.setText(receipt.toString());
        ticket.setCaretPosition(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new MovieLayout()
        );
    }
}


        