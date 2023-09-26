import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WordMemorisationProgram {

	private synchronized List<String> readWordsFromFile() throws IOException {
		List<String> words = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader("words.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Split the line into words and add them to the list
				String[] lineWords = line.split("\\s+");
				for (String word : lineWords) {
					if (word.startsWith("A")) { // Only add words that start with "A"
						words.add(word);
					}
				}
			}
		}

		return words;
	}

	private synchronized void writeWordToFile(String word) throws IOException {
		// Create a BufferedWriter object to write to the file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("memory.txt", true))) {
			// Write the word to the file
			writer.write(word);
			// Write a newline character to the file to start a new line for the next word
			writer.newLine();
		} // The BufferedWriter is automatically closed by the try-with-resources
			// statement
	}

	public static Runnable createTask(List<String> words) {
		AtomicInteger counter = new AtomicInteger(0);// Using so the words are in order
		WordMemorisationProgram program = new WordMemorisationProgram();
		return new Runnable() {
			@Override
			public void run() {
				try {
					// Read the word at the current index
					int index = counter.getAndIncrement();
					if (index >= words.size()) {
						return; // all words have been processed
					}
					String word = words.get(index);

					// Sleep for a random amount of time (between 1 and 5 seconds)
					long sleepTime = ThreadLocalRandom.current().nextInt(1000, 5000);
					Thread.sleep(sleepTime);

					// Write the word to the output file
					program.writeWordToFile(word);
				} catch (InterruptedException | IOException e) {
					// Exception occurred, log the exception
					e.printStackTrace();
				}
			}
		};
	}

	public static void main(String[] args) {
        WordMemorisationProgram program = new WordMemorisationProgram();
        List<String> words;
        List<Runnable> tasks = new ArrayList<>();
        try {
            words = program.readWordsFromFile(); // Read the words from the input file
            for (String word : words) { // Create tasks for each word
                tasks.add(program.createTask(words));
            }

            ThreadPoolManager threadPoolManager = new ThreadPoolManager();// Create a thread pool manager and run the tasks
            threadPoolManager.runTasks(tasks);
            
            threadPoolManager.waitForCompletion();// Wait for all tasks to complete before closing the BufferedReader and BufferedWriter
        } catch (IOException e) {
            e.printStackTrace();//If an IOException occurred while reading the words, print the stack trace
        }
    }
}
