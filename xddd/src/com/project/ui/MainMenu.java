package com.project.ui;

import com.project.database.DatabaseConnection;
import com.project.model.Client;
import com.project.model.User;
import com.project.model.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainMenu extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel p1, p2, p5, p6;
    private JButton b1, bSignOut, bAddClient, bAddCar;
    private JTextField tf1;
	private static final Color BACKGROUND_COLOR = new Color(240, 246, 252);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(51, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(102, 153, 255);

    public MainMenu(User authenticatedUser) {
        setTitle("Vehicle Management");
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 800);

        p1 = new JPanel();
        p1.setBackground(PANEL_COLOR);
        p1.setLayout(new BorderLayout(10, 10));
        p1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        tf1 = new JTextField();
        tf1.setPreferredSize(new Dimension(300, 40));
        tf1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf1.setForeground(Color.GRAY);
        
        b1 = new JButton("Filter");
        b1.setBackground(PRIMARY_COLOR);
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b1.setPreferredSize(new Dimension(100, 40));

        JPanel p4 = new JPanel();
        p4.setBackground(PANEL_COLOR);
        p4.setLayout(new FlowLayout(FlowLayout.LEFT));
        p4.add(tf1);
        p4.add(b1);

        p5 = new JPanel();
        p5.setBackground(PANEL_COLOR);
        p5.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        bSignOut = createStyledButton("Sign Out");
        bAddClient = createStyledButton("Add Client");
        bAddClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddClient(MainMenu.this);
            }
        });
        
        bSignOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Signup();
            }
        });

        p5.add(bAddClient);
        p5.add(bSignOut);

        p1.add(p4, BorderLayout.WEST);
        p1.add(p5, BorderLayout.EAST);

        // Vehicle display panel
        p2 = new JPanel();
        p2.setBackground(BACKGROUND_COLOR);
        p2.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 20));
        JScrollPane scrollPane = new JScrollPane(p2);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(null);

        // Load and display vehicles
        loadVehicles();
        
        // Client display panel
        p6 = new JPanel();
        p6.setBackground(BACKGROUND_COLOR);
        p6.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 20));

        loadClients();

        add(p1, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(p6, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }

    private void loadVehicles() {
        p2.removeAll();
        List<Vehicle> vehicles = DatabaseConnection.getAvailableVehicles();
        for (Vehicle vehicle : vehicles) {
            p2.add(createVehiclePanel(vehicle));
        }
        bAddCar = createAddCarPanel();
        p2.add(bAddCar);
        p2.revalidate();
        p2.repaint();
    }
    
    private JPanel createVehiclePanel(Vehicle vehicle) {
        JPanel carPanel = new JPanel();
        carPanel.setLayout(new BorderLayout());
        carPanel.setPreferredSize(new Dimension(500, 400));
        carPanel.setBackground(PANEL_COLOR);
        carPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) 
        ));

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBackground(PANEL_COLOR);

        JLabel brandModelLabel = new JLabel(vehicle.getMarque() + " " + vehicle.getModele());
        JLabel yearLabel = new JLabel("Year: " + vehicle.getAnnee());
        JLabel fuelTypeLabel = new JLabel("Fuel: " + vehicle.getCarburant());
        JLabel priceLabel = new JLabel("Price/Day: $" + vehicle.getPrixLocationJour());

        brandModelLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fuelTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        detailPanel.add(brandModelLabel);
        detailPanel.add(yearLabel);
        detailPanel.add(fuelTypeLabel);
        detailPanel.add(priceLabel);

        carPanel.add(detailPanel, BorderLayout.CENTER);
        return carPanel;
    }

    private JButton createAddCarPanel() {
        JButton addCarButton = new JButton("+");
        addCarButton.setPreferredSize(new Dimension(500, 400));
        addCarButton.setBackground(PANEL_COLOR);
        addCarButton.setForeground(PRIMARY_COLOR);
        addCarButton.setFont(new Font("Segoe UI", Font.BOLD, 100));
        addCarButton.setBorderPainted(false);
        
        addCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCarDialog(MainMenu.this);
                loadVehicles();
            }
        });
        return addCarButton;
    }
    
    private void loadClients() {
        p6.removeAll();
        List<Client> clients = DatabaseConnection.getAvailableClients();
        for (Client client : clients) {
            p6.add(createClientPanel(client));
        }
        p6.revalidate();
        p6.repaint();
    }
    
    private JPanel createClientPanel(Client client) {
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout());
        clientPanel.setPreferredSize(new Dimension(300, 100));
        clientPanel.setBackground(PANEL_COLOR);
        clientPanel.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));

        JLabel nameLabel = new JLabel(client.getFirstName() + " " + client.getLastName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        clientPanel.add(nameLabel, BorderLayout.CENTER);
        return clientPanel;
    }
}
