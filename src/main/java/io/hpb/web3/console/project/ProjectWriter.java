package io.hpb.web3.console.project;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
class ProjectWriter {
    final void writeResourceFile(
            final String file, final String fileName, final String writeLocation)
            throws IOException {
        Files.write(Paths.get(writeLocation + File.separator + fileName), getBytes(file));
    }
    private byte[] getBytes(final String file) {
        return file.getBytes();
    }
    final void copyResourceFile(final InputStream file, final String destinationPath)
            throws IOException {
        Files.copy(file, Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
    }
    final void importSolidityProject(final File file, final String destination) throws IOException {
        if (file != null && file.exists()) {
            Files.walkFileTree(file.toPath(), new ProjectVisitor(destination));
        }
    }
}
