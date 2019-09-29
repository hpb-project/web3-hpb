package io.hpb.web3.console.project;
import static io.hpb.web3.codegen.Console.exitError;
import static io.hpb.web3.codegen.Console.exitSuccess;
import static io.hpb.web3.utils.Collection.tail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import picocli.CommandLine;
public class ProjectImporter extends ProjectCreator {
    public static final String COMMAND_IMPORT = "import";
    private final String solidityImportPath;
    public ProjectImporter(
            final String root,
            final String packageName,
            final String projectName,
            final String solidityImportPath)
            throws IOException {
        super(root, packageName, projectName);
        this.solidityImportPath = solidityImportPath;
    }
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals(COMMAND_IMPORT)) {
            args = tail(args);
            if (args.length == 0) {
                final InteractiveImporter options = new InteractiveImporter();
                final List<String> stringOptions = new ArrayList<>();
                stringOptions.add("-n");
                stringOptions.add(options.getProjectName());
                stringOptions.add("-p");
                stringOptions.add(options.getPackageName());
                stringOptions.add("-s");
                stringOptions.add(options.getSolidityProjectPath());
                options.getProjectDestination()
                        .ifPresent(
                                projectDest -> {
                                    stringOptions.add("-o");
                                    stringOptions.add(projectDest);
                                });
                args = stringOptions.toArray(new String[0]);
            }
        }
        CommandLine.run(new ProjectImporterCLIRunner(), args);
    }
    void generate() {
        final File solidityFile = new File(solidityImportPath);
        try {
            Project.builder()
                    .withProjectStructure(projectStructure)
                    .withTemplateProvider(templateProvider)
                    .withSolidityFile(solidityFile)
                    .build();
            exitSuccess(
                    "Project created with name: "
                            + projectStructure.getProjectName()
                            + " at location: "
                            + projectStructure.getProjectRoot());
        } catch (final Exception e) {
            exitError(e);
        }
    }
}
