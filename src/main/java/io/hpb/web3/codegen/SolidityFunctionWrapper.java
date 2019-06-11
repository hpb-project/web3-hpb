package io.hpb.web3.codegen;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Bool;
import io.hpb.web3.abi.datatypes.DynamicArray;
import io.hpb.web3.abi.datatypes.DynamicBytes;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.AbiTypes;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.ObjectMapperFactory;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.protocol.core.methods.request.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.AbiDefinition;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.ContractGasProvider;
import io.hpb.web3.utils.Collection;
import io.hpb.web3.utils.Strings;
import io.hpb.web3.utils.Version;
import io.reactivex.Flowable;


public class SolidityFunctionWrapper extends Generator {

    private static final String BINARY = "BINARY";
    private static final String WEB3J = "web3";
    private static final String CREDENTIALS = "credentials";
    private static final String CONTRACT_GAS_PROVIDER = "contractGasProvider";
    private static final String TRANSACTION_MANAGER = "transactionManager";
    private static final String INITIAL_VALUE = "initialWeiValue";
    private static final String CONTRACT_ADDRESS = "contractAddress";
    private static final String GAS_PRICE = "gasPrice";
    private static final String GAS_LIMIT = "gasLimit";
    private static final String FILTER = "filter";
    private static final String START_BLOCK = "startBlock";
    private static final String END_BLOCK = "endBlock";
    private static final String WEI_VALUE = "weiValue";
    private static final String FUNC_NAME_PREFIX = "FUNC_";

    private static final ClassName LOG = ClassName.get(Log.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(SolidityFunctionWrapper.class);

    private static final String CODEGEN_WARNING = "<p>Auto generated code.\n"
            + "<p><strong>Do not modify!</strong>\n"
            + "<p>Please use the "
            + "the " + SolidityFunctionWrapperGenerator.class.getName() + " to update.\n";

    private final boolean useNativeJavaTypes;
    private static final String regex = "(\\w+)(?:\\[(.*?)\\])(?:\\[(.*?)\\])?";
    private static final Pattern pattern = Pattern.compile(regex);
    private final GenerationReporter reporter;

    public SolidityFunctionWrapper(boolean useNativeJavaTypes) {
        this(useNativeJavaTypes, new LogGenerationReporter(LOGGER));
    }

    SolidityFunctionWrapper(boolean useNativeJavaTypes, GenerationReporter reporter) {
        this.useNativeJavaTypes = useNativeJavaTypes;
        this.reporter = reporter;
    }

    public void generateJavaFiles(
            String contractName, String bin, String abi, String destinationDir,
            String basePackageName)
            throws IOException, ClassNotFoundException {
        generateJavaFiles(contractName, bin,
                loadContractDefinition(abi),
                destinationDir, basePackageName,
                null);
    }

    void generateJavaFiles(
            String contractName, String bin, List<AbiDefinition> abi, String destinationDir,
            String basePackageName, Map<String, String> addresses)
            throws IOException, ClassNotFoundException {
        String className = Strings.capitaliseFirstLetter(contractName);

        TypeSpec.Builder classBuilder = createClassBuilder(className, bin);
        classBuilder.addMethod(buildConstructor(Credentials.class, CREDENTIALS, false));
        classBuilder.addMethod(buildConstructor(Credentials.class, CREDENTIALS, true));
        classBuilder.addMethod(buildConstructor(TransactionManager.class,
                TRANSACTION_MANAGER, false));
        classBuilder.addMethod(buildConstructor(TransactionManager.class,
                TRANSACTION_MANAGER, true));
        classBuilder.addFields(buildFuncNameConstants(abi));
        classBuilder.addMethods(
                buildFunctionDefinitions(className, classBuilder, abi));
        classBuilder.addMethod(buildLoad(className, Credentials.class, CREDENTIALS, false));
        classBuilder.addMethod(buildLoad(className, TransactionManager.class,
                TRANSACTION_MANAGER, false));
        classBuilder.addMethod(buildLoad(className, Credentials.class, CREDENTIALS, true));
        classBuilder.addMethod(buildLoad(className, TransactionManager.class,
                TRANSACTION_MANAGER, true));
        if (!bin.equals(Contract.BIN_NOT_PROVIDED)) {
            classBuilder.addMethods(buildDeploymethods(className, classBuilder, abi));
        }
        classBuilder.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", "$S", "rawtypes")
                .addMember("value", "$S", "unchecked")
                .build());
        addAddressesSupport(classBuilder, addresses);

        write(basePackageName, classBuilder.build(), destinationDir);
    }

    private void addAddressesSupport(TypeSpec.Builder classBuilder,
                                     Map<String, String> addresses) {
        if (addresses != null) {

            ClassName stringType = ClassName.get(String.class);
            ClassName mapType = ClassName.get(HashMap.class);
            TypeName mapStringString = ParameterizedTypeName.get(mapType, stringType, stringType);
            FieldSpec addressesStaticField = FieldSpec
                    .builder(mapStringString, "_addresses",
                            Modifier.PROTECTED, Modifier.STATIC, Modifier.FINAL)
                    .build();
            classBuilder.addField(addressesStaticField);

            final CodeBlock.Builder staticInit = CodeBlock.builder();
            staticInit.addStatement("_addresses = new HashMap<String, String>()");
            addresses.forEach((k, v) ->
                    staticInit.addStatement(String.format("_addresses.put(\"%1s\", \"%2s\")",
                            k, v))
            );
            classBuilder.addStaticBlock(staticInit.build());

            
            MethodSpec getAddress = MethodSpec
                    .methodBuilder("getStaticDeployedAddress")
                    .addModifiers(Modifier.PROTECTED)
                    .returns(stringType)
                    .addParameter(stringType, "networkId")
                    .addCode(
                            CodeBlock
                                    .builder()
                                    .addStatement("return _addresses.get(networkId)")
                                    .build())
                    .build();
            classBuilder.addMethod(getAddress);

            MethodSpec getPreviousAddress = MethodSpec
                    .methodBuilder("getPreviouslyDeployedAddress")
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .returns(stringType)
                    .addParameter(stringType, "networkId")
                    .addCode(
                            CodeBlock
                                    .builder()
                                    .addStatement("return _addresses.get(networkId)")
                                    .build())
                    .build();
            classBuilder.addMethod(getPreviousAddress);

        }
    }


    private TypeSpec.Builder createClassBuilder(String className, String binary) {

        String javadoc = CODEGEN_WARNING + getWeb3Version();

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(javadoc)
                .superclass(Contract.class)
                .addField(createBinaryDefinition(binary));
    }

    private String getWeb3Version() {
        String version;

        try {
            
            
            version = Version.getVersion();
        } catch (IOException | NullPointerException e) {
            version = Version.DEFAULT;
        }
        return "\n<p>Generated with web3 version " + version + ".\n";
    }

    private FieldSpec createBinaryDefinition(String binary) {
        return FieldSpec.builder(String.class, BINARY)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", binary)
                .build();
    }

    private FieldSpec createEventDefinition(
            String name,
            List<NamedTypeName> parameters) {

        CodeBlock initializer = buildVariableLengthEventInitializer(
                name, parameters);

        return FieldSpec.builder(Event.class, buildEventDefinitionName(name))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(initializer)
                .build();
    }

    private String buildEventDefinitionName(String eventName) {
        return eventName.toUpperCase() + "_EVENT";
    }

    private List<MethodSpec> buildFunctionDefinitions(
            String className,
            TypeSpec.Builder classBuilder,
            List<AbiDefinition> functionDefinitions) throws ClassNotFoundException {

        List<MethodSpec> MethodSpecs = new ArrayList<>();
        for (AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals("function")) {
                MethodSpec ms = buildFunction(functionDefinition);
                MethodSpecs.add(ms);

            } else if (functionDefinition.getType().equals("event")) {
                MethodSpecs.addAll(buildEventFunctions(functionDefinition, classBuilder));
            }
        }

        return MethodSpecs;
    }

    List<MethodSpec> buildDeploymethods(String className,
                                        TypeSpec.Builder classBuilder,
                                        List<AbiDefinition> functionDefinitions) {
        boolean constructor = false;
        List<MethodSpec> MethodSpecs = new ArrayList<>();
        for (AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals("constructor")) {
                constructor = true;
                MethodSpecs.add(buildDeploy(
                        className, functionDefinition, Credentials.class, CREDENTIALS, true));
                MethodSpecs.add(buildDeploy(
                        className, functionDefinition, TransactionManager.class,
                        TRANSACTION_MANAGER, true));
                MethodSpecs.add(buildDeploy(
                        className, functionDefinition, Credentials.class, CREDENTIALS, false));
                MethodSpecs.add(buildDeploy(
                        className, functionDefinition, TransactionManager.class,
                        TRANSACTION_MANAGER, false));
            }
        }

        
        if (!constructor) {
            MethodSpec.Builder credentialsmethodBuilder =
                    getDeployMethodSpec(className, Credentials.class, CREDENTIALS,
                            false, true);
            MethodSpecs.add(buildDeployNoParams(
                    credentialsmethodBuilder, className, CREDENTIALS,
                    false, true));

            MethodSpec.Builder credentialsmethodBuilderNoGasProvider =
                    getDeployMethodSpec(className, Credentials.class, CREDENTIALS,
                            false, false);
            MethodSpecs.add(buildDeployNoParams(
                    credentialsmethodBuilderNoGasProvider, className, CREDENTIALS,
                    false, false));

            MethodSpec.Builder transactionManagermethodBuilder =
                    getDeployMethodSpec(
                            className, TransactionManager.class, TRANSACTION_MANAGER,
                            false, true);
            MethodSpecs.add(buildDeployNoParams(
                    transactionManagermethodBuilder, className, TRANSACTION_MANAGER,
                    false, true));

            MethodSpec.Builder transactionManagermethodBuilderNoGasProvider =
                    getDeployMethodSpec(
                            className, TransactionManager.class, TRANSACTION_MANAGER,
                            false, false);
            MethodSpecs.add(buildDeployNoParams(
                    transactionManagermethodBuilderNoGasProvider, className, TRANSACTION_MANAGER,
                    false, false));
        }

        return MethodSpecs;
    }

    Iterable<FieldSpec> buildFuncNameConstants(List<AbiDefinition> functionDefinitions) {
        List<FieldSpec> fields = new ArrayList<>();
        Set<String> fieldNames = new HashSet<>();
        fieldNames.add(Contract.FUNC_DEPLOY);

        for (AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals("function")) {
                String funcName = functionDefinition.getName();

                if (!fieldNames.contains(funcName)) {
                    FieldSpec field = FieldSpec.builder(String.class,
                            funcNameToConst(funcName),
                            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", funcName)
                            .build();
                    fields.add(field);
                    fieldNames.add(funcName);
                }
            }
        }
        return fields;
    }

    @SuppressWarnings("rawtypes")
	private static MethodSpec buildConstructor(Class authType, String authName,
                                               boolean withGasProvider) {
        MethodSpec.Builder toReturn = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(String.class, CONTRACT_ADDRESS)
                .addParameter(Web3.class, WEB3J)
                .addParameter(authType, authName);

        if (withGasProvider) {
            toReturn.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addStatement("super($N, $N, $N, $N, $N)",
                            BINARY, CONTRACT_ADDRESS, WEB3J, authName, CONTRACT_GAS_PROVIDER);
        } else {
            toReturn.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addStatement("super($N, $N, $N, $N, $N, $N)",
                            BINARY, CONTRACT_ADDRESS, WEB3J, authName, GAS_PRICE, GAS_LIMIT)
                    .addAnnotation(Deprecated.class);
        }

        return toReturn.build();
    }

    @SuppressWarnings("rawtypes")
	private MethodSpec buildDeploy(
            String className, AbiDefinition functionDefinition,
            Class authType, String authName, boolean withGasProvider) {

        boolean isPayable = functionDefinition.isPayable();

        MethodSpec.Builder methodBuilder = getDeployMethodSpec(
                className, authType, authName, isPayable, withGasProvider);
        String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());

        if (!inputParams.isEmpty()) {
            return buildDeployWithParams(
                    methodBuilder, className, inputParams, authName,
                    isPayable, withGasProvider);
        } else {
            return buildDeployNoParams(methodBuilder, className, authName,
                    isPayable, withGasProvider);
        }
    }

    private static MethodSpec buildDeployWithParams(
            MethodSpec.Builder methodBuilder, String className, String inputParams,
            String authName, boolean isPayable, boolean withGasProvider) {

        methodBuilder.addStatement("$T encodedConstructor = $T.encodeConstructor("
                        + "$T.<$T>asList($L)"
                        + ")",
                String.class, FunctionEncoder.class, Arrays.class, Type.class, inputParams);
        if (isPayable && !withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall("
                            + "$L.class, $L, $L, $L, $L, $L, encodedConstructor, $L)",
                    className, WEB3J, authName, GAS_PRICE, GAS_LIMIT, BINARY, INITIAL_VALUE);
            methodBuilder.addAnnotation(Deprecated.class);
        } else if (isPayable && withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall("
                            + "$L.class, $L, $L, $L, $L, encodedConstructor, $L)",
                    className, WEB3J, authName, CONTRACT_GAS_PROVIDER, BINARY, INITIAL_VALUE);
        } else if (!isPayable && !withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, encodedConstructor)",
                    className, WEB3J, authName, GAS_PRICE, GAS_LIMIT, BINARY);
            methodBuilder.addAnnotation(Deprecated.class);
        } else {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, encodedConstructor)",
                    className, WEB3J, authName, CONTRACT_GAS_PROVIDER, BINARY);
        }

        return methodBuilder.build();
    }

    private static MethodSpec buildDeployNoParams(
            MethodSpec.Builder methodBuilder, String className,
            String authName, boolean isPayable, boolean withGasPRovider) {
        if (isPayable && !withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, \"\", $L)",
                    className, WEB3J, authName, GAS_PRICE, GAS_LIMIT, BINARY, INITIAL_VALUE);
            methodBuilder.addAnnotation(Deprecated.class);
        } else if (isPayable && withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, \"\", $L)",
                    className, WEB3J, authName, CONTRACT_GAS_PROVIDER, BINARY, INITIAL_VALUE);
        } else if (!isPayable && !withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, \"\")",
                    className, WEB3J, authName, GAS_PRICE, GAS_LIMIT, BINARY);
            methodBuilder.addAnnotation(Deprecated.class);
        } else {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, \"\")",
                    className, WEB3J, authName, CONTRACT_GAS_PROVIDER, BINARY);
        }

        return methodBuilder.build();
    }

    @SuppressWarnings("rawtypes")
	private static MethodSpec.Builder getDeployMethodSpec(
            String className, Class authType, String authName,
            boolean isPayable, boolean withGasProvider) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("deploy")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(
                        buildRemoteCall(TypeVariableName.get(className, Type.class)))
                .addParameter(Web3.class, WEB3J)
                .addParameter(authType, authName);
        if (isPayable && !withGasProvider) {
            return builder.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addParameter(BigInteger.class, INITIAL_VALUE);
        } else if (isPayable && withGasProvider) {
            return builder.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addParameter(BigInteger.class, INITIAL_VALUE);
        } else if (!isPayable && withGasProvider) {
            return builder.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER);
        } else {
            return builder.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT);
        }
    }

    @SuppressWarnings("rawtypes")
	private static MethodSpec buildLoad(
            String className, Class authType, String authName, boolean withGasProvider) {
        MethodSpec.Builder toReturn = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeVariableName.get(className, Type.class))
                .addParameter(String.class, CONTRACT_ADDRESS)
                .addParameter(Web3.class, WEB3J)
                .addParameter(authType, authName);

        if (withGasProvider) {
            toReturn.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addStatement("return new $L($L, $L, $L, $L)", className,
                            CONTRACT_ADDRESS, WEB3J, authName, CONTRACT_GAS_PROVIDER);
        } else {
            toReturn.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addStatement("return new $L($L, $L, $L, $L, $L)", className,
                            CONTRACT_ADDRESS, WEB3J, authName, GAS_PRICE, GAS_LIMIT)
                    .addAnnotation(Deprecated.class);
        }

        return toReturn.build();
    }

    String addParameters(
            MethodSpec.Builder methodBuilder, List<AbiDefinition.NamedType> namedTypes) {

        List<ParameterSpec> inputParameterTypes = buildParameterTypes(namedTypes);

        List<ParameterSpec> nativeInputParameterTypes =
                new ArrayList<>(inputParameterTypes.size());
        for (ParameterSpec parameterSpec : inputParameterTypes) {
            TypeName typeName = getWrapperType(parameterSpec.type);
            nativeInputParameterTypes.add(
                    ParameterSpec.builder(typeName, parameterSpec.name).build());
        }

        methodBuilder.addParameters(nativeInputParameterTypes);

        if (useNativeJavaTypes) {
            return Collection.join(
                    inputParameterTypes,
                    ", \n",
                    
                    this::createMappedParameterTypes);
        } else {
            return Collection.join(
                    inputParameterTypes,
                    ", ",
                    parameterSpec -> parameterSpec.name);
        }
    }

    private String createMappedParameterTypes(ParameterSpec parameterSpec) {
        if (parameterSpec.type instanceof ParameterizedTypeName) {
            List<TypeName> typeNames =
                    ((ParameterizedTypeName) parameterSpec.type).typeArguments;
            if (typeNames.size() != 1) {
                throw new UnsupportedOperationException(
                        "Only a single parameterized type is supported");
            } else {
                String parameterSpecType = parameterSpec.type.toString();
                TypeName typeName = typeNames.get(0);
                String typeMapInput = typeName + ".class";
                String componentType = typeName.toString();
                if (typeName instanceof ParameterizedTypeName) {
                    List<TypeName> typeArguments = ((ParameterizedTypeName) typeName).typeArguments;
                    if (typeArguments.size() != 1) {
                        throw new UnsupportedOperationException(
                                "Only a single parameterized type is supported");
                    }
                    TypeName innerTypeName = typeArguments.get(0);
                    componentType = ((ParameterizedTypeName) typeName).rawType.toString();
                    parameterSpecType = ((ParameterizedTypeName) parameterSpec.type).rawType
                            + "<" + componentType + ">";
                    typeMapInput = componentType + ".class,\n" + innerTypeName + ".class";
                }
                return "new " + parameterSpecType + "(\n"
                        + "        " + componentType + ".class,\n"
                        + "        io.hpb.web3.abi.Utils.typeMap("
                        + parameterSpec.name + ", " + typeMapInput + "))";
            }
        } else {
            return "new " + parameterSpec.type + "(" + parameterSpec.name + ")";
        }
    }

    private TypeName getWrapperType(TypeName typeName) {
        if (useNativeJavaTypes) {
            return getNativeType(typeName);
        } else {
            return typeName;
        }
    }

    private TypeName getWrapperRawType(TypeName typeName) {
        if (useNativeJavaTypes) {
            if (typeName instanceof ParameterizedTypeName) {
                return ClassName.get(List.class);
            }
            return getNativeType(typeName);
        } else {
            return typeName;
        }
    }

    private TypeName getIndexedEventWrapperType(TypeName typeName) {
        if (useNativeJavaTypes) {
            return getEventNativeType(typeName);
        } else {
            return typeName;
        }
    }

    static TypeName getNativeType(TypeName typeName) {

        if (typeName instanceof ParameterizedTypeName) {
            return getNativeType((ParameterizedTypeName) typeName);
        }

        String simpleName = ((ClassName) typeName).simpleName();

        if (simpleName.equals(Address.class.getSimpleName())) {
            return TypeName.get(String.class);
        } else if (simpleName.startsWith("Uint")) {
            return TypeName.get(BigInteger.class);
        } else if (simpleName.startsWith("Int")) {
            return TypeName.get(BigInteger.class);
        } else if (simpleName.equals(Utf8String.class.getSimpleName())) {
            return TypeName.get(String.class);
        } else if (simpleName.startsWith("Bytes")) {
            return TypeName.get(byte[].class);
        } else if (simpleName.equals(DynamicBytes.class.getSimpleName())) {
            return TypeName.get(byte[].class);
        } else if (simpleName.equals(Bool.class.getSimpleName())) {
            return TypeName.get(Boolean.class);  
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported type: " + typeName
                            + ", no native type mapping exists.");
        }
    }

    static TypeName getNativeType(ParameterizedTypeName parameterizedTypeName) {
        List<TypeName> typeNames = parameterizedTypeName.typeArguments;
        List<TypeName> nativeTypeNames = new ArrayList<>(typeNames.size());
        for (TypeName enclosedTypeName : typeNames) {
            nativeTypeNames.add(getNativeType(enclosedTypeName));
        }
        return ParameterizedTypeName.get(
                ClassName.get(List.class),
                nativeTypeNames.toArray(new TypeName[nativeTypeNames.size()]));
    }

    static TypeName getEventNativeType(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            return TypeName.get(byte[].class);
        }

        String simpleName = ((ClassName) typeName).simpleName();
        if (simpleName.equals(Utf8String.class.getSimpleName())) {
            return TypeName.get(byte[].class);
        } else {
            return getNativeType(typeName);
        }
    }

    static List<ParameterSpec> buildParameterTypes(List<AbiDefinition.NamedType> namedTypes) {
        List<ParameterSpec> result = new ArrayList<>(namedTypes.size());
        for (int i = 0; i < namedTypes.size(); i++) {
            AbiDefinition.NamedType namedType = namedTypes.get(i);

            String name = createValidParamName(namedType.getName(), i);
            String type = namedTypes.get(i).getType();

            result.add(ParameterSpec.builder(buildTypeName(type), name).build());
        }
        return result;
    }

    
    static String createValidParamName(String name, int idx) {
        if (name.equals("")) {
            return "param" + idx;
        } else {
            return name;
        }
    }

    static List<TypeName> buildTypeNames(List<AbiDefinition.NamedType> namedTypes) {
        List<TypeName> result = new ArrayList<>(namedTypes.size());
        for (AbiDefinition.NamedType namedType : namedTypes) {
            result.add(buildTypeName(namedType.getType()));
        }
        return result;
    }

    MethodSpec buildFunction(
            AbiDefinition functionDefinition) throws ClassNotFoundException {
        String functionName = functionDefinition.getName();

        
        
        if (!SourceVersion.isName(functionName)) {
            functionName = "_" + functionName;
        }

        MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder(functionName)
                        .addModifiers(Modifier.PUBLIC);
        String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());

        List<TypeName> outputParameterTypes = buildTypeNames(functionDefinition.getOutputs());
        if (functionDefinition.isConstant()) {
            buildConstantFunction(
                    functionDefinition, methodBuilder, outputParameterTypes, inputParams);
        } else {
            buildTransactionFunction(
                    functionDefinition, methodBuilder, inputParams);
        }

        return methodBuilder.build();
    }

    private void buildConstantFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            List<TypeName> outputParameterTypes,
            String inputParams) throws ClassNotFoundException {

        String functionName = functionDefinition.getName();

        if (outputParameterTypes.isEmpty()) {
            methodBuilder.addStatement("throw new RuntimeException"
                    + "(\"cannot call constant function with void return type\")");
        } else if (outputParameterTypes.size() == 1) {

            TypeName typeName = outputParameterTypes.get(0);
            TypeName nativeReturnTypeName;
            if (useNativeJavaTypes) {
                nativeReturnTypeName = getWrapperRawType(typeName);
            } else {
                nativeReturnTypeName = getWrapperType(typeName);
            }
            methodBuilder.returns(buildRemoteCall(nativeReturnTypeName));
            
            methodBuilder.addStatement("final $T function = "
                            + "new $T($N, \n$T.<$T>asList($L), "
                            + "\n$T.<$T<?>>asList(new $T<$T>() {}))",
                    Function.class, Function.class, funcNameToConst(functionName),
                    Arrays.class, Type.class, inputParams,
                    Arrays.class, TypeReference.class,
                    TypeReference.class, typeName);

            if (useNativeJavaTypes) {
                if (nativeReturnTypeName.equals(ClassName.get(List.class))) {
                    
                    
                    TypeName listType = ParameterizedTypeName.get(List.class, Type.class);

                    CodeBlock.Builder callCode = CodeBlock.builder();
                    callCode.addStatement(
                            "$T result = "
                                    + "($T) executeCallSingleValueReturn(function, $T.class)",
                            listType, listType, nativeReturnTypeName);
                    callCode.addStatement("return convertToNative(result)");

                    TypeSpec callableType = TypeSpec.anonymousClassBuilder("")
                            .addSuperinterface(ParameterizedTypeName.get(
                                    ClassName.get(Callable.class), nativeReturnTypeName))
                            .addMethod(MethodSpec.methodBuilder("call")
                                    .addAnnotation(Override.class)
                                    .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                                            .addMember("value", "$S", "unchecked")
                                            .build())
                                    .addModifiers(Modifier.PUBLIC)
                                    .addException(Exception.class)
                                    .returns(nativeReturnTypeName)
                                    .addCode(callCode.build())
                                    .build())
                            .build();

                    methodBuilder.addStatement("return new $T(\n$L)",
                            buildRemoteCall(nativeReturnTypeName), callableType);
                } else {
                    methodBuilder.addStatement(
                            "return executeRemoteCallSingleValueReturn(function, $T.class)",
                            nativeReturnTypeName);
                }
            } else {
                methodBuilder.addStatement("return executeRemoteCallSingleValueReturn(function)");
            }
        } else {
            List<TypeName> returnTypes = buildReturnTypes(outputParameterTypes);

            ParameterizedTypeName parameterizedTupleType = ParameterizedTypeName.get(
                    ClassName.get(
                            "io.hpb.web3.tuples.generated",
                            "Tuple" + returnTypes.size()),
                    returnTypes.toArray(
                            new TypeName[returnTypes.size()]));

            methodBuilder.returns(buildRemoteCall(parameterizedTupleType));

            buildVariableLengthReturnFunctionConstructor(
                    methodBuilder, functionName, inputParams, outputParameterTypes);

            buildTupleResultContainer(methodBuilder, parameterizedTupleType, outputParameterTypes);
        }
    }

    private static ParameterizedTypeName buildRemoteCall(TypeName typeName) {
        return ParameterizedTypeName.get(
                ClassName.get(RemoteCall.class), typeName);
    }

    private void buildTransactionFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            String inputParams) throws ClassNotFoundException {

        if (functionDefinition.hasOutputs()) {
            
            reporter.report(String.format(
                    "Definition of the function %s returns a value but is not defined as a view function. "
                            + "Please ensure it contains the view modifier if you want to read the return value",
                    functionDefinition.getName()));
            
        }

        if (functionDefinition.isPayable()) {
            methodBuilder.addParameter(BigInteger.class, WEI_VALUE);
        }

        String functionName = functionDefinition.getName();

        methodBuilder.returns(buildRemoteCall(TypeName.get(TransactionReceipt.class)));

        methodBuilder.addStatement("final $T function = new $T(\n$N, \n$T.<$T>asList($L), \n$T"
                        + ".<$T<?>>emptyList())",
                Function.class, Function.class, funcNameToConst(functionName),
                Arrays.class, Type.class, inputParams, Collections.class,
                TypeReference.class);
        if (functionDefinition.isPayable()) {
            methodBuilder.addStatement(
                    "return executeRemoteCallTransaction(function, $N)", WEI_VALUE);
        } else {
            methodBuilder.addStatement("return executeRemoteCallTransaction(function)");
        }
    }

    TypeSpec buildEventResponseObject(
            String className,
            List<io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName> indexedParameters,
            List<io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName> nonIndexedParameters) {

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        builder.addField(LOG, "log", Modifier.PUBLIC);
        for (io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName
                namedType : indexedParameters) {
            TypeName typeName = getIndexedEventWrapperType(namedType.typeName);
            builder.addField(typeName, namedType.getName(), Modifier.PUBLIC);
        }

        for (io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName
                namedType : nonIndexedParameters) {
            TypeName typeName = getWrapperType(namedType.typeName);
            builder.addField(typeName, namedType.getName(), Modifier.PUBLIC);
        }

        return builder.build();
    }

    MethodSpec buildEventFlowableFunction(
            String responseClassName,
            String functionName,
            List<io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName> indexedParameters,
            List<io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName> nonIndexedParameters)
            throws ClassNotFoundException {

        String generatedFunctionName =
                Strings.lowercaseFirstLetter(functionName) + "EventFlowable";
        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(ClassName.get(Flowable.class),
                        ClassName.get("", responseClassName));

        MethodSpec.Builder flowablemethodBuilder =
                MethodSpec.methodBuilder(generatedFunctionName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(HpbFilter.class, FILTER)
                        .returns(parameterizedTypeName);

        TypeSpec converter = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(io.reactivex.functions.Function.class),
                        ClassName.get(Log.class),
                        ClassName.get("", responseClassName)))
                .addMethod(MethodSpec.methodBuilder("apply")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(Log.class, "log")
                        .returns(ClassName.get("", responseClassName))
                        .addStatement("$T eventValues = extractEventParametersWithLog("
                                        + buildEventDefinitionName(functionName) + ", log)",
                                Contract.EventValuesWithLog.class)
                        .addStatement("$1T typedResponse = new $1T()",
                                ClassName.get("", responseClassName))
                        .addCode(buildTypedResponse("typedResponse", indexedParameters,
                                nonIndexedParameters, true))
                        .addStatement("return typedResponse")
                        .build())
                .build();

        flowablemethodBuilder
                .addStatement("return web3.hpbLogFlowable(filter).map($L)", converter);

        return flowablemethodBuilder
                .build();
    }

    MethodSpec buildDefaultEventFlowableFunction(
            String responseClassName,
            String functionName) {

        String generatedFunctionName =
                Strings.lowercaseFirstLetter(functionName) + "EventFlowable";
        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(ClassName.get(Flowable.class),
                        ClassName.get("", responseClassName));

        MethodSpec.Builder flowablemethodBuilder =
                MethodSpec.methodBuilder(generatedFunctionName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(DefaultBlockParameter.class, START_BLOCK)
                        .addParameter(DefaultBlockParameter.class, END_BLOCK)
                        .returns(parameterizedTypeName);

        flowablemethodBuilder.addStatement("$1T filter = new $1T($2L, $3L, "
                + "getContractAddress())", HpbFilter.class, START_BLOCK, END_BLOCK)
                .addStatement("filter.addSingleTopic($T.encode("
                        + buildEventDefinitionName(functionName) + "))", EventEncoder.class)
                .addStatement("return " + generatedFunctionName + "(filter)");

        return flowablemethodBuilder
                .build();
    }

    MethodSpec buildEventTransactionReceiptFunction(
            String responseClassName,
            String functionName,
            List<NamedTypeName> indexedParameters,
            List<NamedTypeName> nonIndexedParameters) {

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(List.class), ClassName.get("", responseClassName));

        String generatedFunctionName = "get" + Strings.capitaliseFirstLetter(functionName)
                + "Events";
        MethodSpec.Builder transactionmethodBuilder = MethodSpec
                .methodBuilder(generatedFunctionName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TransactionReceipt.class, "transactionReceipt")
                .returns(parameterizedTypeName);

        transactionmethodBuilder.addStatement("$T valueList = extractEventParametersWithLog("
                + buildEventDefinitionName(functionName) + ", "
                + "transactionReceipt)", ParameterizedTypeName.get(List.class,
                Contract.EventValuesWithLog.class))
                .addStatement("$1T responses = new $1T(valueList.size())",
                        ParameterizedTypeName.get(ClassName.get(ArrayList.class),
                                ClassName.get("", responseClassName)))
                .beginControlFlow("for ($T eventValues : valueList)",
                        Contract.EventValuesWithLog.class)
                .addStatement("$1T typedResponse = new $1T()",
                        ClassName.get("", responseClassName))
                .addCode(buildTypedResponse("typedResponse", indexedParameters,
                        nonIndexedParameters, false))
                .addStatement("responses.add(typedResponse)")
                .endControlFlow();


        transactionmethodBuilder.addStatement("return responses");
        return transactionmethodBuilder.build();
    }

    List<MethodSpec> buildEventFunctions(
            AbiDefinition functionDefinition,
            TypeSpec.Builder classBuilder) throws ClassNotFoundException {
        String functionName = functionDefinition.getName();
        List<AbiDefinition.NamedType> inputs = functionDefinition.getInputs();
        String responseClassName = Strings.capitaliseFirstLetter(functionName) + "EventResponse";

        List<NamedTypeName> parameters = new ArrayList<>();
        List<NamedTypeName> indexedParameters = new ArrayList<>();
        List<NamedTypeName> nonIndexedParameters = new ArrayList<>();

        for (AbiDefinition.NamedType namedType : inputs) {
            NamedTypeName parameter = new NamedTypeName(
                    namedType.getName(),
                    buildTypeName(namedType.getType()),
                    namedType.isIndexed()
            );
            if (namedType.isIndexed()) {
                indexedParameters.add(parameter);
            } else {
                nonIndexedParameters.add(parameter);
            }
            parameters.add(parameter);
        }

        classBuilder.addField(createEventDefinition(functionName, parameters));

        classBuilder.addType(buildEventResponseObject(responseClassName, indexedParameters,
                nonIndexedParameters));

        List<MethodSpec> methods = new ArrayList<>();
        methods.add(buildEventTransactionReceiptFunction(responseClassName,
                functionName, indexedParameters, nonIndexedParameters));

        methods.add(buildEventFlowableFunction(responseClassName, functionName,
                indexedParameters, nonIndexedParameters));
        methods.add(buildDefaultEventFlowableFunction(responseClassName,
                functionName));
        return methods;
    }

    CodeBlock buildTypedResponse(
            String objectName,
            List<io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName> indexedParameters,
            List<io.hpb.web3.codegen.SolidityFunctionWrapper.NamedTypeName> nonIndexedParameters,
            boolean flowable) {
        String nativeConversion;

        if (useNativeJavaTypes) {
            nativeConversion = ".getValue()";
        } else {
            nativeConversion = "";
        }

        CodeBlock.Builder builder = CodeBlock.builder();
        if (flowable) {
            builder.addStatement("$L.log = log", objectName);
        } else {
            builder.addStatement(
                    "$L.log = eventValues.getLog()",
                    objectName);
        }
        for (int i = 0; i < indexedParameters.size(); i++) {
            builder.addStatement(
                    "$L.$L = ($T) eventValues.getIndexedValues().get($L)" + nativeConversion,
                    objectName,
                    indexedParameters.get(i).getName(),
                    getIndexedEventWrapperType(indexedParameters.get(i).getTypeName()),
                    i);
        }

        for (int i = 0; i < nonIndexedParameters.size(); i++) {
            builder.addStatement(
                    "$L.$L = ($T) eventValues.getNonIndexedValues().get($L)" + nativeConversion,
                    objectName,
                    nonIndexedParameters.get(i).getName(),
                    getWrapperType(nonIndexedParameters.get(i).getTypeName()),
                    i);
        }
        return builder.build();
    }

    static TypeName buildTypeName(String typeDeclaration) {
        String type = trimStorageDeclaration(typeDeclaration);
        Matcher matcher = pattern.matcher(type);
        if (matcher.find()) {
            Class<?> baseType = AbiTypes.getType(matcher.group(1));
            String firstArrayDimension = matcher.group(2);
            String secondArrayDimension = matcher.group(3);

            TypeName typeName;

            if ("".equals(firstArrayDimension)) {
                typeName = ParameterizedTypeName.get(DynamicArray.class, baseType);
            } else {
                Class<?> rawType = getStaticArrayTypeReferenceClass(firstArrayDimension);
                typeName = ParameterizedTypeName.get(rawType, baseType);
            }

            if (secondArrayDimension != null) {
                if ("".equals(secondArrayDimension)) {
                    return ParameterizedTypeName.get(ClassName.get(DynamicArray.class), typeName);
                } else {
                    Class<?> rawType = getStaticArrayTypeReferenceClass(secondArrayDimension);
                    return ParameterizedTypeName.get(ClassName.get(rawType), typeName);
                }
            }

            return typeName;
        } else {
            Class<?> cls = AbiTypes.getType(type);
            return ClassName.get(cls);
        }
    }

    private static Class<?> getStaticArrayTypeReferenceClass(String type) {
        try {
            return Class.forName("io.hpb.web3.abi.datatypes.generated.StaticArray" + type);
        } catch (ClassNotFoundException e) {
            
            return StaticArray.class;
        }
    }

    private static String trimStorageDeclaration(String type) {
        if (type.endsWith(" storage") || type.endsWith(" memory")) {
            return type.split(" ")[0];
        } else {
            return type;
        }
    }

    private List<TypeName> buildReturnTypes(List<TypeName> outputParameterTypes) {
        List<TypeName> result = new ArrayList<>(outputParameterTypes.size());
        for (TypeName typeName : outputParameterTypes) {
            result.add(getWrapperType(typeName));
        }
        return result;
    }

    private static void buildVariableLengthReturnFunctionConstructor(
            MethodSpec.Builder methodBuilder, String functionName, String inputParameters,
            List<TypeName> outputParameterTypes) throws ClassNotFoundException {

        List<Object> objects = new ArrayList<>();
        objects.add(Function.class);
        objects.add(Function.class);
        objects.add(funcNameToConst(functionName));

        objects.add(Arrays.class);
        objects.add(Type.class);
        objects.add(inputParameters);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (TypeName outputParameterType : outputParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(outputParameterType);
        }

        String asListParams = Collection.join(
                outputParameterTypes,
                ", ",
                typeName -> "new $T<$T>() {}");

        methodBuilder.addStatement("final $T function = new $T($N, \n$T.<$T>asList($L), \n$T"
                + ".<$T<?>>asList("
                + asListParams + "))", objects.toArray());
    }

    private void buildTupleResultContainer(
            MethodSpec.Builder methodBuilder, ParameterizedTypeName tupleType,
            List<TypeName> outputParameterTypes)
            throws ClassNotFoundException {

        List<TypeName> typeArguments = tupleType.typeArguments;

        CodeBlock.Builder tupleConstructor = CodeBlock.builder();
        tupleConstructor.addStatement(
                "$T results = executeCallMultipleValueReturn(function)",
                ParameterizedTypeName.get(List.class, Type.class))
                .add("return new $T(", tupleType)
                .add("$>$>");

        String resultStringSimple = "\n($T) results.get($L)";
        if (useNativeJavaTypes) {
            resultStringSimple += ".getValue()";
        }

        String resultStringNativeList =
                "\nconvertToNative(($T) results.get($L).getValue())";

        int size = typeArguments.size();
        ClassName classList = ClassName.get(List.class);

        for (int i = 0; i < size; i++) {
            TypeName param = outputParameterTypes.get(i);
            TypeName convertTo = typeArguments.get(i);

            String resultString = resultStringSimple;

            
            
            if (useNativeJavaTypes && param instanceof ParameterizedTypeName) {
                ParameterizedTypeName oldContainer = (ParameterizedTypeName)param;
                ParameterizedTypeName newContainer = (ParameterizedTypeName)convertTo;
                if (newContainer.rawType.compareTo(classList) == 0
                        && newContainer.typeArguments.size() == 1) {
                    convertTo = ParameterizedTypeName.get(classList,
                            oldContainer.typeArguments.get(0));
                    resultString = resultStringNativeList;
                }
            }

            tupleConstructor
                    .add(resultString, convertTo, i);
            tupleConstructor.add(i < size - 1 ? ", " : ");\n");
        }
        tupleConstructor.add("$<$<");

        TypeSpec callableType = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(
                        ClassName.get(Callable.class), tupleType))
                .addMethod(MethodSpec.methodBuilder("call")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addException(Exception.class)
                        .returns(tupleType)
                        .addCode(tupleConstructor.build())
                        .build())
                .build();

        methodBuilder.addStatement(
                "return new $T(\n$L)", buildRemoteCall(tupleType), callableType);
    }

    private static CodeBlock buildVariableLengthEventInitializer(
            String eventName,
            List<NamedTypeName> parameterTypes) {

        List<Object> objects = new ArrayList<>();
        objects.add(Event.class);
        objects.add(eventName);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (NamedTypeName parameterType : parameterTypes) {
            objects.add(TypeReference.class);
            objects.add(parameterType.getTypeName());
        }

        String asListParams = parameterTypes.stream()
                .map(type -> {
                    if (type.isIndexed()) {
                        return "new $T<$T>(true) {}";
                    } else {
                        return "new $T<$T>() {}";
                    }
                })
                .collect(Collectors.joining(", "));

        return CodeBlock.builder()
                .addStatement("new $T($S, \n"
                        + "$T.<$T<?>>asList(" + asListParams + "))", objects.toArray())
                .build();
    }

    private List<AbiDefinition> loadContractDefinition(String abi) throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(abi, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    private static String funcNameToConst(String funcName) {
        return FUNC_NAME_PREFIX + funcName.toUpperCase();
    }

    private static class NamedTypeName {
        private final TypeName typeName;
        private final String name;
        private final boolean indexed;

        NamedTypeName(String name, TypeName typeName, boolean indexed) {
            this.name = name;
            this.typeName = typeName;
            this.indexed = indexed;
        }

        public String getName() {
            return name;
        }

        public TypeName getTypeName() {
            return typeName;
        }

        public boolean isIndexed() {
            return indexed;
        }
    }

}
