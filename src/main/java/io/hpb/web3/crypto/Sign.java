package io.hpb.web3.crypto;

import static io.hpb.web3.utils.Assertions.verifyPrecondition;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;

import io.hpb.web3.rlp.RlpDecoder;
import io.hpb.web3.rlp.RlpEncoder;
import io.hpb.web3.rlp.RlpList;
import io.hpb.web3.rlp.RlpString;
import io.hpb.web3.rlp.RlpType;
import io.hpb.web3.utils.Bytes;
import io.hpb.web3.utils.Numeric;


public class Sign {

    public static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    static final ECDomainParameters CURVE = new ECDomainParameters(
            CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
    static final BigInteger HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);

    static final String MESSAGE_PREFIX = "\u0019Hpb Signed Message:\n";
    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;

    static byte[] ghpbpbMessagePrefix(int messageLength) {
        return MESSAGE_PREFIX.concat(String.valueOf(messageLength)).getBytes();
    }

    static byte[] ghpbpbMessageHash(byte[] message) {
        byte[] prefix = ghpbpbMessagePrefix(message.length);

        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return Hash.sha3(result);
    }

    public static SignatureData signPrefixedMessage(byte[] message, ECKeyPair keyPair) {
        return signMessage(ghpbpbMessageHash(message), keyPair, false);
    }

    public static SignatureData signMessage(byte[] message, ECKeyPair keyPair) {
        return signMessage(message, keyPair, true);
    }
    
    public static boolean verify(byte[] data, ECDSASignature signature, byte[] pub) {
        ECDSASigner signer = new ECDSASigner();
        ECPublicKeyParameters params = new ECPublicKeyParameters(CURVE.getCurve().decodePoint(pub), CURVE);
        signer.init(false, params);
        try {
            return signer.verifySignature(data, signature.r, signature.s);
        } catch (NullPointerException npe) {
            return false;
        }
    }
    public static String sign(String message,String privateKey) {
		ECKeyPair ecKeyPair =ECKeyPair.create(Numeric.toBigInt(privateKey));
		SignatureData signatureData = signMessage(message.getBytes(StandardCharsets.UTF_8), ecKeyPair,false);
		List<RlpType> values = new ArrayList<>();
		values.add(RlpString.create(message));
		values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getV())));
		values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
		values.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        RlpList rlpList = new RlpList(values);
        String signMessage = Numeric.toHexString(RlpEncoder.encode(rlpList));
		return signMessage;
	}
    public static boolean verifyTransaction(String hexTransaction,String address,long chainId) throws SignatureException{
    	SignedRawTransaction rawTransaction = (SignedRawTransaction) TransactionDecoder.decode(hexTransaction);
    	SignatureData signatureData = rawTransaction.getSignatureData();
		byte[] r = signatureData.getR();
		byte[] s = signatureData.getS();
		BigInteger bv = Numeric.toBigInt(signatureData.getV());
		bv = bv.subtract(BigInteger.valueOf(CHAIN_ID_INC));
		bv = bv.subtract(BigInteger.valueOf(chainId * 2));
		bv = bv.add(BigInteger.valueOf(LOWER_REAL_V));
		verifyPrecondition(r != null && r.length == 32, "r must be 32 bytes");
		verifyPrecondition(s != null && s.length == 32, "s must be 32 bytes");

		byte b = bv.toByteArray()[0];
		int header = b & 0xFF;
		if (header < 27 || header > 34) {
			throw new SignatureException("Header byte out of range: " + header);
		}
		ECDSASignature signature = new ECDSASignature(new BigInteger(1, signatureData.getR()),
				new BigInteger(1, signatureData.getS()));
		int recId = header - 27;
		byte[] encode = TransactionEncoder.encode(rawTransaction,chainId);
		byte[] msg = Hash.sha3(encode);
		byte[] key = Sign.recoverPubBytesFromSignature(recId, signature, msg);
		BigInteger publicKey = new BigInteger(1, Arrays.copyOfRange(key, 1, key.length));
		if(address.equalsIgnoreCase(Keys.getAddress(publicKey))) {
			return verify(msg, signature, key);
		}
    	return false;
    }
    public static boolean verify(String signMessage,String address) throws SignatureException{
		    byte[] message = Numeric.hexStringToByteArray(signMessage);
	        RlpList returnList = RlpDecoder.decode(message);
	        RlpList rlpList = (RlpList) returnList.getValues().get(0);
	        byte[] msg= ((RlpString) rlpList.getValues().get(0)).getBytes();
	        byte[] v = ((RlpString) rlpList.getValues().get(1)).getBytes();
	        byte[] r = Numeric.toBytesPadded(
	            Numeric.toBigInt(((RlpString) rlpList.getValues().get(2)).getBytes()), 32);
	        byte[] s = Numeric.toBytesPadded(
	            Numeric.toBigInt(((RlpString) rlpList.getValues().get(3)).getBytes()), 32);
	        SignatureData signatureData = new SignatureData(v, r, s);
	        verifyPrecondition(r != null && r.length == 32, "r must be 32 bytes");
	        verifyPrecondition(s != null && s.length == 32, "s must be 32 bytes");
	        int header = signatureData.getV()[0] & 0xFF;
	        if (header < 27 || header > 34) {
	            throw new SignatureException("Header byte out of range: " + header);
	        }
	        ECDSASignature signature = new ECDSASignature(
	                new BigInteger(1, signatureData.getR()),
	                new BigInteger(1, signatureData.getS()));
	        int recId = header - 27;
	        byte[] key = recoverPubBytesFromSignature(recId, signature, msg);
			BigInteger publicKey = new BigInteger(1, Arrays.copyOfRange(key, 1, key.length));
			if(address.equalsIgnoreCase(Keys.getAddress(publicKey))) {
				return verify(msg, signature, key);
			}
	        return false;
	}
    public static SignatureData signMessage(byte[] message, ECKeyPair keyPair, boolean needToHash) {
        BigInteger publicKey = keyPair.getPublicKey();
        byte[] messageHash;
        if (needToHash) {
            messageHash = Hash.sha3(message);
        } else {
            messageHash = message;
        }

        ECDSASignature sig = keyPair.sign(messageHash);
        
        int recId = -1;
        for (int i = 0; i < 4; i++) {
            BigInteger k = recoverFromSignature(i, sig, messageHash);
            if (k != null && k.equals(publicKey)) {
                recId = i;
                break;
            }
        }
        if (recId == -1) {
            throw new RuntimeException(
                    "Could not construct a recoverable key. Are your credentials valid?");
        }

        int headerByte = recId + 27;

        
        byte[] v = new byte[]{(byte) headerByte};
        byte[] r = Numeric.toBytesPadded(sig.r, 32);
        byte[] s = Numeric.toBytesPadded(sig.s, 32);

        return new SignatureData(v, r, s);
    }

    
    public static BigInteger recoverFromSignature(int recId, ECDSASignature sig, byte[] message) {
    	byte[] qBytes =recoverPubBytesFromSignature(recId, sig, message);
        return new BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.length));
    }
    public static byte[] recoverPubBytesFromSignature(int recId, ECDSASignature sig, byte[] message) {
    	verifyPrecondition(recId >= 0, "recId must be positive");
        verifyPrecondition(sig.r.signum() >= 0, "r must be positive");
        verifyPrecondition(sig.s.signum() >= 0, "s must be positive");
        verifyPrecondition(message != null, "message cannot be null");
        BigInteger n = CURVE.getN();  
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = sig.r.add(i.multiply(n));
        BigInteger prime = SecP256K1Curve.q;
        if (x.compareTo(prime) >= 0) {
            
            return null;
        }
        
        ECPoint R = decompressKey(x, (recId & 1) == 1);
        if (!R.multiply(n).isInfinity()) {
            return null;
        }
        
        BigInteger e = new BigInteger(1, message);
        BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
        BigInteger rInv = sig.r.modInverse(n);
        BigInteger srInv = rInv.multiply(sig.s).mod(n);
        BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
        ECPoint q = ECAlgorithms.sumOfTwoMultiplies(CURVE.getG(), eInvrInv, R, srInv);
        return q.getEncoded(false);
    }
    
    private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte)(yBit ? 0x03 : 0x02);
        return CURVE.getCurve().decodePoint(compEnc);
    }

    
    public static BigInteger signedMessageToKey(
            byte[] message, SignatureData signatureData) throws SignatureException {
        return signedMessageHashToKey(Hash.sha3(message), signatureData);
    }

    
    public static BigInteger signedPrefixedMessageToKey(
            byte[] message, SignatureData signatureData) throws SignatureException {
        return signedMessageHashToKey(ghpbpbMessageHash(message), signatureData);
    }

    static BigInteger signedMessageHashToKey(
            byte[] messageHash, SignatureData signatureData) throws SignatureException {

        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        verifyPrecondition(r != null && r.length == 32, "r must be 32 bytes");
        verifyPrecondition(s != null && s.length == 32, "s must be 32 bytes");

        int header = signatureData.getV()[0] & 0xFF;
        
        
        if (header < 27 || header > 34) {
            throw new SignatureException("Header byte out of range: " + header);
        }

        ECDSASignature sig = new ECDSASignature(
                new BigInteger(1, signatureData.getR()),
                new BigInteger(1, signatureData.getS()));

        int recId = header - 27;
        BigInteger key = recoverFromSignature(recId, sig, messageHash);
        if (key == null) {
            throw new SignatureException("Could not recover public key from signature");
        }
        return key;
    }

    
    public static BigInteger publicKeyFromPrivate(BigInteger privKey) {
        ECPoint point = publicPointFromPrivate(privKey);

        byte[] encoded = point.getEncoded(false);
        return new BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.length));  
    }

    
    public static ECPoint publicPointFromPrivate(BigInteger privKey) {
        
        if (privKey.bitLength() > CURVE.getN().bitLength()) {
            privKey = privKey.mod(CURVE.getN());
        }
        return new FixedPointCombMultiplier().multiply(CURVE.getG(), privKey);
    }

    
    public static BigInteger publicFromPoint(byte[] bits) {
        return new BigInteger(1, Arrays.copyOfRange(bits, 1, bits.length));  
    }

    public static class SignatureData {
        private final byte[] v;
        private final byte[] r;
        private final byte[] s;

        public SignatureData(byte v, byte[] r, byte[] s) {
            this(new byte[]{v}, r, s);
        }

        public SignatureData(byte[] v, byte[] r, byte[] s) {
            this.v = v;
            this.r = r;
            this.s = s;
        }

        public byte[] getV() {
            return v;
        }

        public byte[] getR() {
            return r;
        }

        public byte[] getS() {
            return s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            SignatureData that = (SignatureData) o;

            if (!Arrays.equals(v, that.v)) {
                return false;
            }
            if (!Arrays.equals(r, that.r)) {
                return false;
            }
            return Arrays.equals(s, that.s);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(v);
            result = 31 * result + Arrays.hashCode(r);
            result = 31 * result + Arrays.hashCode(s);
            return result;
        }
    }
}
