package io.hpb.web3.console.project;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Optional;
import java.util.Scanner;
class InteractiveOptions {
    private final Scanner scanner;
    private final Writer writer;
    InteractiveOptions() {
        this(System.in, System.out);
    }
    InteractiveOptions(final InputStream inputStream, final OutputStream outputStream) {
        this.scanner = new Scanner(inputStream);
        this.writer = new PrintWriter(outputStream);
    }
    protected final String getProjectName() {
        print("Please enter the project name (Required Field): ");
        return getUserInput();
    }
    protected final String getPackageName() {
        print("Please enter the package name for your project (Required Field): ");
        return getUserInput();
    }
    protected final Optional<String> getProjectDestination() {
        print("Please enter the destination of your project (current by default): ");
        final String projectDest = getUserInput();
        return projectDest.isEmpty() ? Optional.empty() : Optional.of(projectDest);
    }
    String getUserInput() {
        return scanner.nextLine();
    }
    private void print(final String text) {
        System.out.println(text);
    }
}
