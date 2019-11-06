package io.hpb.web3.console.project;
import java.io.File;
public class ProjectStructure {
    private final String packageName;
    private final String projectName;
    private final String testPath;
    private final String solidityPath;
    private final String mainPath;
    private final String wrapperPath;
    private final String root;
    private final String projectRoot;
    ProjectStructure(final String root, final String packageName, final String projectName) {
        this.root = root;
        this.packageName = packageName;
        final String formattedPackageName = formatPackageName(packageName);
        this.projectName = projectName;
        this.projectRoot = root + File.separator + projectName;
        this.mainPath = generatePath(this.projectRoot, "src", "main", "java", formattedPackageName);
        this.solidityPath = generatePath(this.projectRoot, "src", "main", "solidity");
        this.testPath = generatePath(this.projectRoot, "src", "test", "java", formattedPackageName);
        this.wrapperPath = generatePath(this.projectRoot, "gradle", "wrapper");
    }
    final String getRoot() {
        return root;
    }
    final String getProjectRoot() {
        return projectRoot;
    }
    final String getPackageName() {
        return packageName;
    }
    final String getProjectName() {
        return projectName;
    }
    final String getTestPath() {
        return testPath;
    }
    final String getSolidityPath() {
        return solidityPath;
    }
    final String getMainPath() {
        return mainPath;
    }
    final String getWrapperPath() {
        return wrapperPath;
    }
    private String generatePath(final String... a) {
        final StringBuilder finalPath = new StringBuilder();
        for (final String b : a) {
            finalPath.append(b).append(File.separator);
        }
        return finalPath.toString();
    }
    private String formatPackageName(final String packageName) {
        if (packageName.contains(".")) {
            return packageName.replaceAll("[.]", File.separator);
        }
        return packageName;
    }
    private void createDirectory(final String path) {
        final File directory = new File(path);
        directory.mkdirs();
    }
    void createDirectoryStructure() {
        createDirectory(mainPath);
        createDirectory(testPath);
        createDirectory(solidityPath);
        createDirectory(wrapperPath);
    }
}
