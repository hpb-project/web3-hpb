package io.hpb.web3.codegen;

import static io.hpb.web3.codegen.Console.exitError;
import static io.hpb.web3.utils.Collection.tail;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.hpb.web3.protocol.ObjectMapperFactory;
import io.hpb.web3.protocol.core.methods.response.AbiDefinition;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.utils.Files;
import io.hpb.web3.utils.Strings;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


public class SolidityFunctionWrapperGenerator extends FunctionWrapperGenerator {
    public static final String COMMAND_SOLIDITY = "solidity";
    public static final String COMMAND_GENERATE = "generate";
    public static final String COMMAND_PREFIX = COMMAND_SOLIDITY + " " + COMMAND_GENERATE;

    

    private final File binFile;
    private final File abiFile;

    private SolidityFunctionWrapperGenerator(
            File binFile,
            File abiFile,
            File destinationDir,
            String basePackageName,
            boolean useJavaNativeTypes) {

        super(destinationDir, basePackageName, useJavaNativeTypes);
        this.binFile = binFile;
        this.abiFile = abiFile;
    }

    static List<AbiDefinition> loadContractDefinition(File absFile)
            throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(absFile, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    private void generate() throws IOException, ClassNotFoundException {
        String binary = Contract.BIN_NOT_PROVIDED;
        if (binFile != null) {
            byte[] bytes = Files.readBytes(binFile);
            binary = new String(bytes);
        }

        byte[] bytes = Files.readBytes(abiFile);
        String abi = new String(bytes);

        List<AbiDefinition> functionDefinitions = loadContractDefinition(abiFile);

        if (functionDefinitions.isEmpty()) {
            exitError("Unable to parse input ABI file");
        } else {
            String contractName = getFileNameNoExtension(abiFile.getName());
            String className = Strings.capitaliseFirstLetter(contractName);
            System.out.printf("Generating " + basePackageName + "." + className + " ... ");
            new SolidityFunctionWrapper(useJavaNativeTypes).generateJavaFiles(
                    contractName, binary, abi, destinationDirLocation.toString(), basePackageName);
            System.out.println("File written to " + destinationDirLocation.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals(COMMAND_SOLIDITY)) {
            args = tail(args);
        }

        if (args.length > 0 && args[0].equals(COMMAND_GENERATE)) {
            args = tail(args);
        }

        CommandLine.run(new PicocliRunner(), args);
    }

    @Command(name = COMMAND_PREFIX, mixinStandardHelpOptions = true, version = "4.0",
            sortOptions = false)
    static class PicocliRunner implements Runnable {
        @Option(names = { "-a", "--abiFile" },
                description = "abi file with contract definition.",
                required = true)
        private File abiFile;

        @Option(names = { "-b", "--binFile" },
                description = "bin file with contract compiled code "
                        + "in order to generate deploy methods.",
                required = false)
        private File binFile;

        @Option(names = { "-o", "--outputDir" },
                description = "destination base directory.",
                required = true)
        private File destinationFileDir;

        @Option(names = { "-p", "--package" },
                description = "base package name.",
                required = true)
        private String packageName;

        @Option(names = { "-jt", JAVA_TYPES_ARG },
                description = "use native java types.",
                required = false,
                showDefaultValue = ALWAYS)
        private boolean javaTypes = true;

        @Option(names = { "-st", SOLIDITY_TYPES_ARG },
                description = "use solidity types.",
                required = false)
        private boolean solidityTypes;

        @Override
        public void run() {
            try {
                
                
                boolean useJavaTypes = !(solidityTypes);
                new SolidityFunctionWrapperGenerator(binFile, abiFile, destinationFileDir,
                        packageName, useJavaTypes).generate();
            } catch (Exception e) {
                exitError(e);
            }
        }
    }
}