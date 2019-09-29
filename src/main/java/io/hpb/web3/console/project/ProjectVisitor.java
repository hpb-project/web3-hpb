package io.hpb.web3.console.project;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
public class ProjectVisitor extends SimpleFileVisitor<Path> {
    private final String destination;
    private String temp;
    public ProjectVisitor(final String destination) {
        this.destination = destination;
        this.temp = destination;
    }
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);
        temp = getFile(dir);
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        Files.copy(
                path,
                new File(temp + File.separator + path.getFileName()).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return FileVisitResult.CONTINUE;
    }
    private String getFile(final Path path) {
        return createDirectoryFromPath(path).getAbsolutePath();
    }
    private File createDirectoryFromPath(Path path) {
        File directory = new File(temp + File.separator + path.getFileName());
        directory.mkdirs();
        return directory;
    }
}
