public class App {
    public static void main(String[] args) {
        // Specify the path to the movie file
        String filePath = "C:\\Users\\Admin\\Desktop\\oskarhessler\\projects\\moviePicker.java\\src\\Filmbanken.txt";

        // Initialize the Engine with the file path
        Engine engine = new Engine(filePath);

        // Initialize and display the Window
        new Window(engine);
    }
}
