package io.hpb.web3.console.project;
import static io.hpb.web3.codegen.Console.exitError;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
public class Project {
    private Project(final Builder builder) {}
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private ProjectStructure projectStructure;
        private TemplateProvider templateProvider;
        private File solidityImportPath;
        public Builder withSolidityFile(final File solidityImportPath) {
            this.solidityImportPath = solidityImportPath;
            return this;
        }
        public Builder withProjectStructure(final ProjectStructure projectStructure) {
            this.projectStructure = projectStructure;
            return this;
        }
        public Builder withTemplateProvider(final TemplateProvider templateProvider) {
            this.templateProvider = templateProvider;
            return this;
        }
        private void buildGradleProject(final String pathToDirectory)
                throws IOException, InterruptedException {
            if (!isWindows()) {
                setExecutable(pathToDirectory, "gradlew");
                executeCommand(
                        new File(pathToDirectory),
                        new String[] {"bash", "-c", "./gradlew build -q"});
            } else {
                setExecutable(pathToDirectory, "gradlew.bat");
                executeCommand(
                        new File(pathToDirectory),
                        new String[] {"cmd.exe", "/c", "gradlew.bat build -q"});
            }
        }
        private void setExecutable(final String pathToDirectory, final String gradlew) {
            final File f = new File(pathToDirectory + File.separator + gradlew);
            final boolean isExecutable = f.setExecutable(true);
        }
        private boolean isWindows() {
            return System.getProperty("os.name").toLowerCase().startsWith("windows");
        }
        private void executeCommand(final File workingDir, final String[] command)
                throws InterruptedException, IOException {
            new ProcessBuilder(command)
                    .directory(workingDir)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .start()
                    .waitFor(10, TimeUnit.SECONDS);
        }
        public Project build() {
            try {
                projectStructure.createDirectoryStructure();
                final ProjectWriter projectWriter = new ProjectWriter();
                projectWriter.writeResourceFile(
                        templateProvider.getMainJavaClass(),
                        projectStructure.getProjectName() + ".java",
                        projectStructure.getMainPath());
                projectWriter.writeResourceFile(
                        templateProvider.getGradleBuild(),
                        File.separator + "build.gradle",
                        projectStructure.getProjectRoot());
                projectWriter.writeResourceFile(
                        templateProvider.getGradleSettings(),
                        File.separator + "settings.gradle",
                        projectStructure.getProjectRoot());
                if (solidityImportPath == null) {
                    projectWriter.writeResourceFile(
                            templateProvider.getSolidityProject(),
                            "Greeter.sol",
                            projectStructure.getSolidityPath());
                } else {
                    projectWriter.importSolidityProject(
                            solidityImportPath, projectStructure.getSolidityPath());
                }
                projectWriter.writeResourceFile(
                        templateProvider.getGradlewWrapperSettings(),
                        File.separator + "gradle-wrapper.properties",
                        projectStructure.getWrapperPath());
                projectWriter.writeResourceFile(
                        templateProvider.getGradlewScript(),
                        File.separator + "gradlew",
                        projectStructure.getProjectRoot());
                projectWriter.writeResourceFile(
                        templateProvider.getGradlewBatScript(),
                        File.separator + "gradlew.bat",
                        projectStructure.getProjectRoot());
                projectWriter.copyResourceFile(
                        templateProvider.getGradlewJar(),
                        projectStructure.getWrapperPath() + File.separator + "gradle-wrapper.jar");
                buildGradleProject(projectStructure.getProjectRoot());
            } catch (final IOException | InterruptedException e) {
                exitError("Looks like an error occurred: " + e.getMessage());
            }
            return new Project(this);
        }
    }
}
