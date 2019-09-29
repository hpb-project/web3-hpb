package io.hpb.web3.abi.datatypes;

import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.TypeReference.StaticArrayTypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Bool;
import io.hpb.web3.abi.datatypes.DynamicArray;
import io.hpb.web3.abi.datatypes.DynamicBytes;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.abi.datatypes.generated.Bytes1;
import io.hpb.web3.abi.datatypes.generated.Bytes10;
import io.hpb.web3.abi.datatypes.generated.Bytes11;
import io.hpb.web3.abi.datatypes.generated.Bytes12;
import io.hpb.web3.abi.datatypes.generated.Bytes13;
import io.hpb.web3.abi.datatypes.generated.Bytes14;
import io.hpb.web3.abi.datatypes.generated.Bytes15;
import io.hpb.web3.abi.datatypes.generated.Bytes16;
import io.hpb.web3.abi.datatypes.generated.Bytes17;
import io.hpb.web3.abi.datatypes.generated.Bytes18;
import io.hpb.web3.abi.datatypes.generated.Bytes19;
import io.hpb.web3.abi.datatypes.generated.Bytes2;
import io.hpb.web3.abi.datatypes.generated.Bytes20;
import io.hpb.web3.abi.datatypes.generated.Bytes21;
import io.hpb.web3.abi.datatypes.generated.Bytes22;
import io.hpb.web3.abi.datatypes.generated.Bytes23;
import io.hpb.web3.abi.datatypes.generated.Bytes24;
import io.hpb.web3.abi.datatypes.generated.Bytes25;
import io.hpb.web3.abi.datatypes.generated.Bytes26;
import io.hpb.web3.abi.datatypes.generated.Bytes27;
import io.hpb.web3.abi.datatypes.generated.Bytes28;
import io.hpb.web3.abi.datatypes.generated.Bytes29;
import io.hpb.web3.abi.datatypes.generated.Bytes3;
import io.hpb.web3.abi.datatypes.generated.Bytes30;
import io.hpb.web3.abi.datatypes.generated.Bytes31;
import io.hpb.web3.abi.datatypes.generated.Bytes32;
import io.hpb.web3.abi.datatypes.generated.Bytes4;
import io.hpb.web3.abi.datatypes.generated.Bytes5;
import io.hpb.web3.abi.datatypes.generated.Bytes6;
import io.hpb.web3.abi.datatypes.generated.Bytes7;
import io.hpb.web3.abi.datatypes.generated.Bytes8;
import io.hpb.web3.abi.datatypes.generated.Bytes9;
import io.hpb.web3.abi.datatypes.generated.Int104;
import io.hpb.web3.abi.datatypes.generated.Int112;
import io.hpb.web3.abi.datatypes.generated.Int120;
import io.hpb.web3.abi.datatypes.generated.Int128;
import io.hpb.web3.abi.datatypes.generated.Int136;
import io.hpb.web3.abi.datatypes.generated.Int144;
import io.hpb.web3.abi.datatypes.generated.Int152;
import io.hpb.web3.abi.datatypes.generated.Int16;
import io.hpb.web3.abi.datatypes.generated.Int160;
import io.hpb.web3.abi.datatypes.generated.Int168;
import io.hpb.web3.abi.datatypes.generated.Int176;
import io.hpb.web3.abi.datatypes.generated.Int184;
import io.hpb.web3.abi.datatypes.generated.Int192;
import io.hpb.web3.abi.datatypes.generated.Int200;
import io.hpb.web3.abi.datatypes.generated.Int208;
import io.hpb.web3.abi.datatypes.generated.Int216;
import io.hpb.web3.abi.datatypes.generated.Int224;
import io.hpb.web3.abi.datatypes.generated.Int232;
import io.hpb.web3.abi.datatypes.generated.Int24;
import io.hpb.web3.abi.datatypes.generated.Int240;
import io.hpb.web3.abi.datatypes.generated.Int248;
import io.hpb.web3.abi.datatypes.generated.Int256;
import io.hpb.web3.abi.datatypes.generated.Int32;
import io.hpb.web3.abi.datatypes.generated.Int40;
import io.hpb.web3.abi.datatypes.generated.Int48;
import io.hpb.web3.abi.datatypes.generated.Int56;
import io.hpb.web3.abi.datatypes.generated.Int64;
import io.hpb.web3.abi.datatypes.generated.Int72;
import io.hpb.web3.abi.datatypes.generated.Int8;
import io.hpb.web3.abi.datatypes.generated.Int80;
import io.hpb.web3.abi.datatypes.generated.Int88;
import io.hpb.web3.abi.datatypes.generated.Int96;
import io.hpb.web3.abi.datatypes.generated.Uint104;
import io.hpb.web3.abi.datatypes.generated.Uint112;
import io.hpb.web3.abi.datatypes.generated.Uint120;
import io.hpb.web3.abi.datatypes.generated.Uint128;
import io.hpb.web3.abi.datatypes.generated.Uint136;
import io.hpb.web3.abi.datatypes.generated.Uint144;
import io.hpb.web3.abi.datatypes.generated.Uint152;
import io.hpb.web3.abi.datatypes.generated.Uint16;
import io.hpb.web3.abi.datatypes.generated.Uint160;
import io.hpb.web3.abi.datatypes.generated.Uint168;
import io.hpb.web3.abi.datatypes.generated.Uint176;
import io.hpb.web3.abi.datatypes.generated.Uint184;
import io.hpb.web3.abi.datatypes.generated.Uint192;
import io.hpb.web3.abi.datatypes.generated.Uint200;
import io.hpb.web3.abi.datatypes.generated.Uint208;
import io.hpb.web3.abi.datatypes.generated.Uint216;
import io.hpb.web3.abi.datatypes.generated.Uint224;
import io.hpb.web3.abi.datatypes.generated.Uint232;
import io.hpb.web3.abi.datatypes.generated.Uint24;
import io.hpb.web3.abi.datatypes.generated.Uint240;
import io.hpb.web3.abi.datatypes.generated.Uint248;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.abi.datatypes.generated.Uint32;
import io.hpb.web3.abi.datatypes.generated.Uint40;
import io.hpb.web3.abi.datatypes.generated.Uint48;
import io.hpb.web3.abi.datatypes.generated.Uint56;
import io.hpb.web3.abi.datatypes.generated.Uint64;
import io.hpb.web3.abi.datatypes.generated.Uint72;
import io.hpb.web3.abi.datatypes.generated.Uint8;
import io.hpb.web3.abi.datatypes.generated.Uint80;
import io.hpb.web3.abi.datatypes.generated.Uint88;
import io.hpb.web3.abi.datatypes.generated.Uint96;

@SuppressWarnings("rawtypes")
public class AllReferenceType{
	public static TypeReference getStaticType(String type,int size ,boolean isIndexed) {
        switch (type) {
            case "address":
                    return new StaticArrayTypeReference<StaticArray<Address>>(size) {};
            case "bool":
            	    return new StaticArrayTypeReference<StaticArray<Bool>>(size) {};
            case "string":
                    return new StaticArrayTypeReference<StaticArray<Utf8String>>(size) {};
            case "bytes":
                    return new StaticArrayTypeReference<StaticArray<DynamicBytes>>(size) {};
            case "uint8":
                    return new StaticArrayTypeReference<StaticArray<Uint8>>(size) {};
            case "int8":
                    return new StaticArrayTypeReference<StaticArray<Int8>>(size) {};
            case "uint16":
                    return new StaticArrayTypeReference<StaticArray<Uint16>>(size) {};
            case "int16":
                    return new StaticArrayTypeReference<StaticArray<Int16>>(size) {};
            case "uint24":
                    return new StaticArrayTypeReference<StaticArray<Uint24>>(size) {};
            case "int24":
                    return new StaticArrayTypeReference<StaticArray<Int24>>(size) {};
            case "uint32":
                    return new StaticArrayTypeReference<StaticArray<Uint32>>(size) {};
            case "int32":
                    return new StaticArrayTypeReference<StaticArray<Int32>>(size) {};
            case "uint40":
                    return new StaticArrayTypeReference<StaticArray<Uint40>>(size) {};
            case "int40":
                    return new StaticArrayTypeReference<StaticArray<Int40>>(size) {};
            case "uint48":
                    return new StaticArrayTypeReference<StaticArray<Uint48>>(size) {};
            case "int48":
                    return new StaticArrayTypeReference<StaticArray<Int48>>(size) {};
            case "uint56":
                    return new StaticArrayTypeReference<StaticArray<Uint56>>(size) {};
            case "int56":
                    return new StaticArrayTypeReference<StaticArray<Int56>>(size) {};
            case "uint64":
                    return new StaticArrayTypeReference<StaticArray<Uint64>>(size) {};
            case "int64":
                    return new StaticArrayTypeReference<StaticArray<Int64>>(size) {};
            case "uint72":
                    return new StaticArrayTypeReference<StaticArray<Uint72>>(size) {};
            case "int72":
                    return new StaticArrayTypeReference<StaticArray<Int72>>(size) {};
            case "uint80":
                    return new StaticArrayTypeReference<StaticArray<Uint80>>(size) {};
            case "int80":
                    return new StaticArrayTypeReference<StaticArray<Int80>>(size) {};
            case "uint88":
                    return new StaticArrayTypeReference<StaticArray<Uint88>>(size) {};
            case "int88":
                    return new StaticArrayTypeReference<StaticArray<Int88>>(size) {};
            case "uint96":
                    return new StaticArrayTypeReference<StaticArray<Uint96>>(size) {};
            case "int96":
                    return new StaticArrayTypeReference<StaticArray<Int96>>(size) {};
            case "uint104":
                    return new StaticArrayTypeReference<StaticArray<Uint104>>(size) {};
            case "int104":
                    return new StaticArrayTypeReference<StaticArray<Int104>>(size) {};
            case "uint112":
                    return new StaticArrayTypeReference<StaticArray<Uint112>>(size) {};
            case "int112":
                    return new StaticArrayTypeReference<StaticArray<Int112>>(size) {};
            case "uint120":
                    return new StaticArrayTypeReference<StaticArray<Uint120>>(size) {};
            case "int120":
                    return new StaticArrayTypeReference<StaticArray<Int120>>(size) {};
            case "uint128":
                    return new StaticArrayTypeReference<StaticArray<Uint128>>(size) {};
            case "int128":
                    return new StaticArrayTypeReference<StaticArray<Int128>>(size) {};
            case "uint136":
                    return new StaticArrayTypeReference<StaticArray<Uint136>>(size) {};
            case "int136":
                    return new StaticArrayTypeReference<StaticArray<Int136>>(size) {};
            case "uint144":
                    return new StaticArrayTypeReference<StaticArray<Uint144>>(size) {};
            case "int144":
                    return new StaticArrayTypeReference<StaticArray<Int144>>(size) {};
            case "uint152":
                    return new StaticArrayTypeReference<StaticArray<Uint152>>(size) {};
            case "int152":
                    return new StaticArrayTypeReference<StaticArray<Int152>>(size) {};
            case "uint160":
                    return new StaticArrayTypeReference<StaticArray<Uint160>>(size) {};
            case "int160":
                    return new StaticArrayTypeReference<StaticArray<Int160>>(size) {};
            case "uint168":
                    return new StaticArrayTypeReference<StaticArray<Uint168>>(size) {};
            case "int168":
                    return new StaticArrayTypeReference<StaticArray<Int168>>(size) {};
            case "uint176":
                    return new StaticArrayTypeReference<StaticArray<Uint176>>(size) {};
            case "int176":
                    return new StaticArrayTypeReference<StaticArray<Int176>>(size) {};
            case "uint184":
                    return new StaticArrayTypeReference<StaticArray<Uint184>>(size) {};
            case "int184":
                    return new StaticArrayTypeReference<StaticArray<Int184>>(size) {};
            case "uint192":
                    return new StaticArrayTypeReference<StaticArray<Uint192>>(size) {};
            case "int192":
                    return new StaticArrayTypeReference<StaticArray<Int192>>(size) {};
            case "uint200":
                    return new StaticArrayTypeReference<StaticArray<Uint200>>(size) {};
            case "int200":
                    return new StaticArrayTypeReference<StaticArray<Int200>>(size) {};
            case "uint208":
                    return new StaticArrayTypeReference<StaticArray<Uint208>>(size) {};
            case "int208":
                    return new StaticArrayTypeReference<StaticArray<Int208>>(size) {};
            case "uint216":
                    return new StaticArrayTypeReference<StaticArray<Uint216>>(size) {};
            case "int216":
                    return new StaticArrayTypeReference<StaticArray<Int216>>(size) {};
            case "uint224":
                    return new StaticArrayTypeReference<StaticArray<Uint224>>(size) {};
            case "int224":
                    return new StaticArrayTypeReference<StaticArray<Int224>>(size) {};
            case "uint232":
                    return new StaticArrayTypeReference<StaticArray<Uint232>>(size) {};
            case "int232":
                    return new StaticArrayTypeReference<StaticArray<Int232>>(size) {};
            case "uint240":
                    return new StaticArrayTypeReference<StaticArray<Uint240>>(size) {};
            case "int240":
                    return new StaticArrayTypeReference<StaticArray<Int240>>(size) {};
            case "uint248":
                    return new StaticArrayTypeReference<StaticArray<Uint248>>(size) {};
            case "int248":
                    return new StaticArrayTypeReference<StaticArray<Int248>>(size) {};
            case "uint256":
                    return new StaticArrayTypeReference<StaticArray<Uint256>>(size) {};
            case "int256":
                    return new StaticArrayTypeReference<StaticArray<Int256>>(size) {};
            case "bytes1":
                    return new StaticArrayTypeReference<StaticArray<Bytes1>>(size) {};
            case "bytes2":
                    return new StaticArrayTypeReference<StaticArray<Bytes2>>(size) {};
            case "bytes3":
                    return new StaticArrayTypeReference<StaticArray<Bytes3>>(size) {};
            case "bytes4":
                    return new StaticArrayTypeReference<StaticArray<Bytes4>>(size) {};
            case "bytes5":
                    return new StaticArrayTypeReference<StaticArray<Bytes5>>(size) {};
            case "bytes6":
                    return new StaticArrayTypeReference<StaticArray<Bytes6>>(size) {};
            case "bytes7":
                    return new StaticArrayTypeReference<StaticArray<Bytes7>>(size) {};
            case "bytes8":
                    return new StaticArrayTypeReference<StaticArray<Bytes8>>(size) {};
            case "bytes9":
                    return new StaticArrayTypeReference<StaticArray<Bytes9>>(size) {};
            case "bytes10":
                    return new StaticArrayTypeReference<StaticArray<Bytes10>>(size) {};
            case "bytes11":
                    return new StaticArrayTypeReference<StaticArray<Bytes11>>(size) {};
            case "bytes12":
                    return new StaticArrayTypeReference<StaticArray<Bytes12>>(size) {};
            case "bytes13":
                    return new StaticArrayTypeReference<StaticArray<Bytes13>>(size) {};
            case "bytes14":
                    return new StaticArrayTypeReference<StaticArray<Bytes14>>(size) {};
            case "bytes15":
                    return new StaticArrayTypeReference<StaticArray<Bytes15>>(size) {};
            case "bytes16":
                    return new StaticArrayTypeReference<StaticArray<Bytes16>>(size) {};
            case "bytes17":
                    return new StaticArrayTypeReference<StaticArray<Bytes17>>(size) {};
            case "bytes18":
                    return new StaticArrayTypeReference<StaticArray<Bytes18>>(size) {};
            case "bytes19":
                    return new StaticArrayTypeReference<StaticArray<Bytes19>>(size) {};
            case "bytes20":
                    return new StaticArrayTypeReference<StaticArray<Bytes20>>(size) {};
            case "bytes21":
                    return new StaticArrayTypeReference<StaticArray<Bytes21>>(size) {};
            case "bytes22":
                    return new StaticArrayTypeReference<StaticArray<Bytes22>>(size) {};
            case "bytes23":
                    return new StaticArrayTypeReference<StaticArray<Bytes23>>(size) {};
            case "bytes24":
                    return new StaticArrayTypeReference<StaticArray<Bytes24>>(size) {};
            case "bytes25":
                    return new StaticArrayTypeReference<StaticArray<Bytes25>>(size) {};
            case "bytes26":
                    return new StaticArrayTypeReference<StaticArray<Bytes26>>(size) {};
            case "bytes27":
                    return new StaticArrayTypeReference<StaticArray<Bytes27>>(size) {};
            case "bytes28":
                    return new StaticArrayTypeReference<StaticArray<Bytes28>>(size) {};
            case "bytes29":
                    return new StaticArrayTypeReference<StaticArray<Bytes29>>(size) {};
            case "bytes30":
                    return new StaticArrayTypeReference<StaticArray<Bytes30>>(size) {};
            case "bytes31":
                    return new StaticArrayTypeReference<StaticArray<Bytes31>>(size) {};
            case "bytes32":
                    return new StaticArrayTypeReference<StaticArray<Bytes32>>(size) {};
            default:
                    throw new UnsupportedOperationException("Unsupported type encountered: "
                            + type);
        }
    }
	public static TypeReference getDynamicType(String type,boolean isIndexed) {
		switch (type) {
		case "address":
			return new TypeReference<DynamicArray<Address>>(isIndexed) {};
		case "bool":
			return new TypeReference<DynamicArray<Bool>>(isIndexed) {};
		case "string":
			return new TypeReference<DynamicArray<Utf8String>>(isIndexed) {};
		case "bytes":
			return new TypeReference<DynamicArray<DynamicBytes>>(isIndexed) {};
		case "uint8":
			return new TypeReference<DynamicArray<Uint8>>(isIndexed) {};
		case "int8":
			return new TypeReference<DynamicArray<Int8>>(isIndexed) {};
		case "uint16":
			return new TypeReference<DynamicArray<Uint16>>(isIndexed) {};
		case "int16":
			return new TypeReference<DynamicArray<Int16>>(isIndexed) {};
		case "uint24":
			return new TypeReference<DynamicArray<Uint24>>(isIndexed) {};
		case "int24":
			return new TypeReference<DynamicArray<Int24>>(isIndexed) {};
		case "uint32":
			return new TypeReference<DynamicArray<Uint32>>(isIndexed) {};
		case "int32":
			return new TypeReference<DynamicArray<Int32>>(isIndexed) {};
		case "uint40":
			return new TypeReference<DynamicArray<Uint40>>(isIndexed) {};
		case "int40":
			return new TypeReference<DynamicArray<Int40>>(isIndexed) {};
		case "uint48":
			return new TypeReference<DynamicArray<Uint48>>(isIndexed) {};
		case "int48":
			return new TypeReference<DynamicArray<Int48>>(isIndexed) {};
		case "uint56":
			return new TypeReference<DynamicArray<Uint56>>(isIndexed) {};
		case "int56":
			return new TypeReference<DynamicArray<Int56>>(isIndexed) {};
		case "uint64":
			return new TypeReference<DynamicArray<Uint64>>(isIndexed) {};
		case "int64":
			return new TypeReference<DynamicArray<Int64>>(isIndexed) {};
		case "uint72":
			return new TypeReference<DynamicArray<Uint72>>(isIndexed) {};
		case "int72":
			return new TypeReference<DynamicArray<Int72>>(isIndexed) {};
		case "uint80":
			return new TypeReference<DynamicArray<Uint80>>(isIndexed) {};
		case "int80":
			return new TypeReference<DynamicArray<Int80>>(isIndexed) {};
		case "uint88":
			return new TypeReference<DynamicArray<Uint88>>(isIndexed) {};
		case "int88":
			return new TypeReference<DynamicArray<Int88>>(isIndexed) {};
		case "uint96":
			return new TypeReference<DynamicArray<Uint96>>(isIndexed) {};
		case "int96":
			return new TypeReference<DynamicArray<Int96>>(isIndexed) {};
		case "uint104":
			return new TypeReference<DynamicArray<Uint104>>(isIndexed) {};
		case "int104":
			return new TypeReference<DynamicArray<Int104>>(isIndexed) {};
		case "uint112":
			return new TypeReference<DynamicArray<Uint112>>(isIndexed) {};
		case "int112":
			return new TypeReference<DynamicArray<Int112>>(isIndexed) {};
		case "uint120":
			return new TypeReference<DynamicArray<Uint120>>(isIndexed) {};
		case "int120":
			return new TypeReference<DynamicArray<Int120>>(isIndexed) {};
		case "uint128":
			return new TypeReference<DynamicArray<Uint128>>(isIndexed) {};
		case "int128":
			return new TypeReference<DynamicArray<Int128>>(isIndexed) {};
		case "uint136":
			return new TypeReference<DynamicArray<Uint136>>(isIndexed) {};
		case "int136":
			return new TypeReference<DynamicArray<Int136>>(isIndexed) {};
		case "uint144":
			return new TypeReference<DynamicArray<Uint144>>(isIndexed) {};
		case "int144":
			return new TypeReference<DynamicArray<Int144>>(isIndexed) {};
		case "uint152":
			return new TypeReference<DynamicArray<Uint152>>(isIndexed) {};
		case "int152":
			return new TypeReference<DynamicArray<Int152>>(isIndexed) {};
		case "uint160":
			return new TypeReference<DynamicArray<Uint160>>(isIndexed) {};
		case "int160":
			return new TypeReference<DynamicArray<Int160>>(isIndexed) {};
		case "uint168":
			return new TypeReference<DynamicArray<Uint168>>(isIndexed) {};
		case "int168":
			return new TypeReference<DynamicArray<Int168>>(isIndexed) {};
		case "uint176":
			return new TypeReference<DynamicArray<Uint176>>(isIndexed) {};
		case "int176":
			return new TypeReference<DynamicArray<Int176>>(isIndexed) {};
		case "uint184":
			return new TypeReference<DynamicArray<Uint184>>(isIndexed) {};
		case "int184":
			return new TypeReference<DynamicArray<Int184>>(isIndexed) {};
		case "uint192":
			return new TypeReference<DynamicArray<Uint192>>(isIndexed) {};
		case "int192":
			return new TypeReference<DynamicArray<Int192>>(isIndexed) {};
		case "uint200":
			return new TypeReference<DynamicArray<Uint200>>(isIndexed) {};
		case "int200":
			return new TypeReference<DynamicArray<Int200>>(isIndexed) {};
		case "uint208":
			return new TypeReference<DynamicArray<Uint208>>(isIndexed) {};
		case "int208":
			return new TypeReference<DynamicArray<Int208>>(isIndexed) {};
		case "uint216":
			return new TypeReference<DynamicArray<Uint216>>(isIndexed) {};
		case "int216":
			return new TypeReference<DynamicArray<Int216>>(isIndexed) {};
		case "uint224":
			return new TypeReference<DynamicArray<Uint224>>(isIndexed) {};
		case "int224":
			return new TypeReference<DynamicArray<Int224>>(isIndexed) {};
		case "uint232":
			return new TypeReference<DynamicArray<Uint232>>(isIndexed) {};
		case "int232":
			return new TypeReference<DynamicArray<Int232>>(isIndexed) {};
		case "uint240":
			return new TypeReference<DynamicArray<Uint240>>(isIndexed) {};
		case "int240":
			return new TypeReference<DynamicArray<Int240>>(isIndexed) {};
		case "uint248":
			return new TypeReference<DynamicArray<Uint248>>(isIndexed) {};
		case "int248":
			return new TypeReference<DynamicArray<Int248>>(isIndexed) {};
		case "uint256":
			return new TypeReference<DynamicArray<Uint256>>(isIndexed) {};
		case "int256":
			return new TypeReference<DynamicArray<Int256>>(isIndexed) {};
		case "bytes1":
			return new TypeReference<DynamicArray<Bytes1>>(isIndexed) {};
		case "bytes2":
			return new TypeReference<DynamicArray<Bytes2>>(isIndexed) {};
		case "bytes3":
			return new TypeReference<DynamicArray<Bytes3>>(isIndexed) {};
		case "bytes4":
			return new TypeReference<DynamicArray<Bytes4>>(isIndexed) {};
		case "bytes5":
			return new TypeReference<DynamicArray<Bytes5>>(isIndexed) {};
		case "bytes6":
			return new TypeReference<DynamicArray<Bytes6>>(isIndexed) {};
		case "bytes7":
			return new TypeReference<DynamicArray<Bytes7>>(isIndexed) {};
		case "bytes8":
			return new TypeReference<DynamicArray<Bytes8>>(isIndexed) {};
		case "bytes9":
			return new TypeReference<DynamicArray<Bytes9>>(isIndexed) {};
		case "bytes10":
			return new TypeReference<DynamicArray<Bytes10>>(isIndexed) {};
		case "bytes11":
			return new TypeReference<DynamicArray<Bytes11>>(isIndexed) {};
		case "bytes12":
			return new TypeReference<DynamicArray<Bytes12>>(isIndexed) {};
		case "bytes13":
			return new TypeReference<DynamicArray<Bytes13>>(isIndexed) {};
		case "bytes14":
			return new TypeReference<DynamicArray<Bytes14>>(isIndexed) {};
		case "bytes15":
			return new TypeReference<DynamicArray<Bytes15>>(isIndexed) {};
		case "bytes16":
			return new TypeReference<DynamicArray<Bytes16>>(isIndexed) {};
		case "bytes17":
			return new TypeReference<DynamicArray<Bytes17>>(isIndexed) {};
		case "bytes18":
			return new TypeReference<DynamicArray<Bytes18>>(isIndexed) {};
		case "bytes19":
			return new TypeReference<DynamicArray<Bytes19>>(isIndexed) {};
		case "bytes20":
			return new TypeReference<DynamicArray<Bytes20>>(isIndexed) {};
		case "bytes21":
			return new TypeReference<DynamicArray<Bytes21>>(isIndexed) {};
		case "bytes22":
			return new TypeReference<DynamicArray<Bytes22>>(isIndexed) {};
		case "bytes23":
			return new TypeReference<DynamicArray<Bytes23>>(isIndexed) {};
		case "bytes24":
			return new TypeReference<DynamicArray<Bytes24>>(isIndexed) {};
		case "bytes25":
			return new TypeReference<DynamicArray<Bytes25>>(isIndexed) {};
		case "bytes26":
			return new TypeReference<DynamicArray<Bytes26>>(isIndexed) {};
		case "bytes27":
			return new TypeReference<DynamicArray<Bytes27>>(isIndexed) {};
		case "bytes28":
			return new TypeReference<DynamicArray<Bytes28>>(isIndexed) {};
		case "bytes29":
			return new TypeReference<DynamicArray<Bytes29>>(isIndexed) {};
		case "bytes30":
			return new TypeReference<DynamicArray<Bytes30>>(isIndexed) {};
		case "bytes31":
			return new TypeReference<DynamicArray<Bytes31>>(isIndexed) {};
		case "bytes32":
			return new TypeReference<DynamicArray<Bytes32>>(isIndexed) {};
		default:
			throw new UnsupportedOperationException("Unsupported type encountered: "
					+ type);
		}
	}
}
