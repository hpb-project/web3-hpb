package io.hpb.web3.console.project;
import static io.hpb.web3.codegen.Console.exitError;
import static io.hpb.web3.console.project.ProjectImporter.COMMAND_IMPORT;

import io.hpb.web3.console.project.utills.InputVerifier;
import picocli.CommandLine;
@CommandLine.Command(name = COMMAND_IMPORT)
public class ProjectImporterCLIRunner extends ProjectCreatorCLIRunner {
    @CommandLine.Option(
            names = {"-s", "--solidity path"},
            description = "path to solidity file/folder",
            required = true)
    String solidityImportPath;
    @Override
    public void run() {
        if (InputVerifier.requiredArgsAreNotEmpty(projectName, packageName, solidityImportPath)
                && InputVerifier.classNameIsValid(projectName)
                && InputVerifier.packageNameIsValid(packageName)) {
            try {
                new ProjectImporter(root, packageName, projectName, solidityImportPath).generate();
            } catch (final Exception e) {
                exitError(e);
            }
        }
    }
}
