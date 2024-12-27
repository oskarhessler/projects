import java.io.*;
import java.util.*;

public class Engine {
    private Random rand = new Random();
    private Map<String, List<String>> groupedMovies;
    private String filePath;

    // Constructor
    public Engine(String filePath) {
        this.filePath = filePath;
        groupedMovies = groupMoviesByMainTitle(filePath);
    }

    // Method to group movies by their main title
    private Map<String, List<String>> groupMoviesByMainTitle(String filePath) {
        Map<String, List<String>> groupedMovies = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty() || line.equals(":")) {
                    continue; // Skip empty or invalid lines
                }

                boolean isWatched = line.startsWith("*");

                if (isWatched) {
                    line = line.substring(1).trim();
                }

                String mainTitle = getMainTitle(line);
                String fullTitle = formatTitle(line);
                fullTitle = removeMainTitleFromMovieName(mainTitle, fullTitle);

                groupedMovies.computeIfAbsent(mainTitle, k -> new ArrayList<>()).add(isWatched ? "*" + fullTitle : fullTitle);
            }
        } catch (Exception e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return groupedMovies;
    }

    private String getMainTitle(String line) {
        if (line.contains(":")) {
            String[] parts = line.split(":", 2);
            return toTitleCase(parts[0].trim());
        }
        return toTitleCase(line);
    }

    private String removeMainTitleFromMovieName(String mainTitle, String movieName) {
        if (movieName.startsWith(mainTitle)) {
            return movieName.substring(mainTitle.length()).trim();
        }
        return movieName;
    }

    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder titleCase = new StringBuilder();
        boolean capitalizeNext = true;

        for (char character : input.toCharArray()) {
            if (Character.isWhitespace(character) || character == '-' || character == '\'') {
                capitalizeNext = true;
                titleCase.append(character);
            } else if (capitalizeNext) {
                titleCase.append(Character.toTitleCase(character));
                capitalizeNext = false;
            } else {
                titleCase.append(Character.toLowerCase(character));
            }
        }
        return titleCase.toString();
    }

    private String formatTitle(String line) {
        return line.replace(":", "").trim();
    }

    public void addMovie(String mainTitle, String movieName) {
        String formattedMovie = mainTitle + ": " + movieName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(formattedMovie);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        groupedMovies.computeIfAbsent(mainTitle, k -> new ArrayList<>()).add(formattedMovie);
    }

    // Randomly selects an unwatched movie
    // Method to get the first unseen movie
public String getFirstUnseenMovie() {
    for (Map.Entry<String, List<String>> entry : groupedMovies.entrySet()) {
        for (String movie : entry.getValue()) {
            if (!movie.startsWith("*")) { // Check if the movie is not marked as watched
                return movie; // Return the first unseen movie
            }
        }
    }
    return null; // No unseen movies found
}

// Method to get the first unseen movie from each main title
public List<String> getFirstUnseenMoviesByMainTitle() {
    List<String> unseenMovies = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : groupedMovies.entrySet()) {
        for (String movie : entry.getValue()) {
            if (!movie.startsWith("*")) { // Check if the movie is not marked as watched
                unseenMovies.add(movie); // Add the first unseen movie from this main title
                break; // Move to the next main title after finding one unseen movie
            }
        }
    }
    return unseenMovies; // Return the list of first unseen movies
}
    // Collects a list of unwatched movies
    public List<String> collectNextMovies() {
        List<String> unwatchedMovies = new ArrayList<>();
        for (List<String> movies : groupedMovies.values()) {
            for (String movie : movies) {
                if (!movie.startsWith("*")) { // Exclude watched movies
                    unwatchedMovies.add(movie);
                }
            }
        }
        return unwatchedMovies;
    }

    // Marks a specific movie as watched
    public boolean markMovieAsWatched(String movieTitle) {
        boolean movieFound = false;

        for (Map.Entry<String, List<String>> entry : groupedMovies.entrySet()) {
            List<String> movies = entry.getValue();
            for (int i = 0; i < movies.size(); i++) {
                String movie = movies.get(i);
                if (movie.equalsIgnoreCase(movieTitle) && !movie.startsWith("*")) {
                    movies.set(i, "*" + movie); // Mark as watched
                    movieFound = true;
                }
            }
        }

        if (movieFound) {
            saveToFile(); // Persist changes to the file
        }
        return movieFound;
    }

    // Saves the updated movie list back to the file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, List<String>> entry : groupedMovies.entrySet()) {
                String mainTitle = entry.getKey();
                for (String movie : entry.getValue()) {
                    String prefix = movie.startsWith("*") ? "*" : "";
                    writer.write(prefix + mainTitle + ": " + movie.replaceFirst("\\*", ""));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }
}
