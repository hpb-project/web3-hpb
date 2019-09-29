package io.hpb.web3.console.project;
import static io.hpb.web3.codegen.Console.exitError;
import static io.hpb.web3.console.project.ProjectCreator.COMMAND_NEW;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

import java.io.IOException;

import io.hpb.web3.console.project.utills.InputVerifier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
@Command(name = COMMAND_NEW, mixinStandardHelpOptions = true, version = "4.0", sortOptions = false)
public class ProjectCreatorCLIRunner implements Runnable {
    @Option(
            names = {"-o", "--outputDir"},
            description = "destination base directory.",
            required = false,
            showDefaultValue = ALWAYS)
    String root = System.getProperty("user.dir");
    @Option(
            names = {"-p", "--package"},
            description = "base package name.",
            required = true)
    String packageName;
    @Option(
            names = {"-n", "--project name"},
            description = "project name.",
            required = true)
    String projectName;
    @Override
    public void run() {
        if (InputVerifier.requiredArgsAreNotEmpty(projectName, packageName)
                && InputVerifier.classNameIsValid(projectName)
                && InputVerifier.packageNameIsValid(packageName)) {
            try {
                new ProjectCreator(root, packageName, projectName).generate();
            } catch (final IOException e) {
                exitError(e);
            }
        }
    }
}
