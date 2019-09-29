package io.hpb.web3.console;
import static io.hpb.web3.codegen.SolidityFunctionWrapperGenerator.COMMAND_SOLIDITY;
import static io.hpb.web3.console.project.ProjectCreator.COMMAND_NEW;
import static io.hpb.web3.console.project.ProjectImporter.COMMAND_IMPORT;
import static io.hpb.web3.utils.Collection.tail;

import io.hpb.web3.codegen.Console;
import io.hpb.web3.codegen.SolidityFunctionWrapperGenerator;
import io.hpb.web3.console.project.ProjectCreator;
import io.hpb.web3.console.project.ProjectImporter;
import io.hpb.web3.utils.Version;
public class Runner {
    private static final String USAGE = "Usage: web3 version|wallet|solidity|new|import ...";
    private static final String LOGO =
            "\n" 
                    + "              _      _____ _     _        \n"
                    + "             | |    |____ (_)   (_)       \n"
                    + "__      _____| |__      / /_     _   ___  \n"
                    + "\\ \\ /\\ / / _ \\ '_ \\     \\ \\ |   | | / _ \\ \n"
                    + " \\ V  V /  __/ |_) |.___/ / | _ | || (_) |\n"
                    + "  \\_/\\_/ \\___|_.__/ \\____/| |(_)|_| \\___/ \n"
                    + "                         _/ |             \n"
                    + "                        |__/              \n";
    public static void main(String[] args) throws Exception {
        System.out.println(LOGO);
        if (args.length < 1) {
            Console.exitError(USAGE);
        } else {
            switch (args[0]) {
                case "wallet":
                    WalletRunner.run(tail(args));
                    break;
                case COMMAND_SOLIDITY:
                    SolidityFunctionWrapperGenerator.main(tail(args));
                    break;
                case COMMAND_NEW:
                    ProjectCreator.main(args);
                    break;
                case COMMAND_IMPORT:
                    ProjectImporter.main(args);
                    break;
                case "version":
                    Console.exitSuccess(
                            "Version: "
                                    + Version.getVersion()
                                    + "\n"
                                    + "Build timestamp: "
                                    + Version.getTimestamp());
                    break;
                default:
                    Console.exitError(USAGE);
            }
        }
    }
}
