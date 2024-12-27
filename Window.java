import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class Window extends JFrame {
    private Engine engine;

    public Window(Engine engine) {
        this.engine = engine;
        setTitle("Movie Picker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and add components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

       // Button for random movie
    // Button for random movie
    JButton randomMovieButton = new JButton("Random Movie");
    randomMovieButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<String> unseenMovies = engine.getFirstUnseenMoviesByMainTitle(); // Get first unseen movies
                if (!unseenMovies.isEmpty()) {
                    // Randomly select one from the unseen movies
                    String randomMovie = unseenMovies.get(new Random().nextInt(unseenMovies.size()));
                    JOptionPane.showMessageDialog(null, "Recommended Movie:\n" + randomMovie);
                } else {
                    JOptionPane.showMessageDialog(null, "No unseen movies available.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error selecting random movie: " + ex.getMessage());
                ex.printStackTrace(); // Print stack trace for more info
            }
        }
    });

        // Button for movie list
        JButton movieListButton = new JButton("Movie List");
        movieListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> nextMovies = engine.collectNextMovies();
                    if (!nextMovies.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Next unwatched movies:\n" + String.join("\n", nextMovies));
                    } else {
                        JOptionPane.showMessageDialog(null, "No unwatched movies available.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error displaying movie list: " + ex.getMessage());
                    ex.printStackTrace(); // Print stack trace for more info
                }
            }
        });

        // Button for marking a movie as watched
        JButton markAsWatchedButton = new JButton("Mark Movie as Watched");
        markAsWatchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieTitle = JOptionPane.showInputDialog("Enter movie title to mark as watched:");
                if (movieTitle != null && !movieTitle.trim().isEmpty()) {
                    try {
                        if (engine.markMovieAsWatched(movieTitle.trim())) {
                            JOptionPane.showMessageDialog(null, "Movie marked as watched!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Movie not found!");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error marking movie as watched: " + ex.getMessage());
                        ex.printStackTrace(); // Print stack trace for more info
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Movie title cannot be empty.");
                }
            }
        });

        // Button for adding a new movie
        JButton addMovieButton = new JButton("Add Movie");
        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mainTitle = JOptionPane.showInputDialog("Enter Main Title:");
                String movieName = JOptionPane.showInputDialog("Enter Movie Name:");

                if (mainTitle != null && !mainTitle.trim().isEmpty() && movieName != null && !movieName.trim().isEmpty()) {
                    try {
                        engine.addMovie(mainTitle.trim(), movieName.trim());
                        JOptionPane.showMessageDialog(null, "Movie added!");
                        restartApp(); // Restart the application
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error adding movie: " + ex.getMessage());
                        ex.printStackTrace(); // Print stack trace for more info
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Both Main Title and Movie Name must be provided.");
                }
            }
        });

        // Add buttons to the panel
        panel.add(randomMovieButton);
        panel.add(movieListButton);
        panel.add(markAsWatchedButton);
        panel.add(addMovieButton);

        // Add panel to the frame
        add(panel);
        setVisible(true);
    }

    // Method to restart the application via the App class
    private void restartApp() {
        dispose(); // Close the current JFrame
        try {
            // Restart the application by invoking the App.main method
            App.main(new String[0]);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error restarting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
