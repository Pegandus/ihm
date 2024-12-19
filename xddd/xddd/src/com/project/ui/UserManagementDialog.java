package com.project.ui;

import com.project.database.DatabaseConnection;
import com.project.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementDialog extends JDialog {
    private JPanel userPanel;
    private int currentUserId;
    private static final Color BACKGROUND_COLOR = new Color(240, 246, 252);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(51, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(102, 153, 255);

    public UserManagementDialog(JFrame parent, int currentUserId) {
        super(parent, "User Management", true);
        this.currentUserId = currentUserId; 
        setSize(600, 400);
        setLayout(new BorderLayout());

        
        getContentPane().setBackground(BACKGROUND_COLOR);

        
        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(PANEL_COLOR);
        JScrollPane scrollPane = new JScrollPane(userPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        loadUsers();

        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(120, 40));
        closeButton.addActionListener(e -> dispose());
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void loadUsers() {
        userPanel.removeAll();
        List<User> users = DatabaseConnection.getAllUsers(); 
        for (User user : users) {
            JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            userRow.setBackground(PANEL_COLOR);

            userRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1), 
                BorderFactory.createEmptyBorder(10, 10, 10, 10) 
            ));

            JLabel userLabel = new JLabel(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            userLabel.setPreferredSize(new Dimension(400, 30));

            JButton deleteButton = new JButton("Delete");
            deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            deleteButton.setBackground(SECONDARY_COLOR);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setPreferredSize(new Dimension(100, 30));

            
            deleteButton.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

            if (user.getId() == currentUserId) {
                
                deleteButton.setEnabled(false);
                deleteButton.setBackground(Color.GRAY);
            } else {
                
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirmed = JOptionPane.showConfirmDialog(
                            UserManagementDialog.this,
                            "Are you sure you want to delete " + user.getFirstName() + "?",
                            "Delete Confirmation",
                            JOptionPane.YES_NO_OPTION
                        );
                        if (confirmed == JOptionPane.YES_OPTION) {
                            DatabaseConnection.deleteUser(user.getId()); 
                            loadUsers(); 
                        }
                    }
                });
            }

            userRow.add(userLabel);
            userRow.add(deleteButton);
            userPanel.add(userRow);
        }

        userPanel.revalidate();
        userPanel.repaint();
    }
}
