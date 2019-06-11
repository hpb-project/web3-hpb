package io.hpb.web3.protocol.core.methods.response;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.hpb.web3.protocol.ObjectMapperFactory;
import io.hpb.web3.protocol.core.Response;

/**
 * @author will
 * 获取候选节点
 */
public class HpbGetCandidateNodes extends Response<HpbGetCandidateNodes.CandidateNodes> {

    @Override
    @JsonDeserialize(using = HpbGetCandidateNodes.ResponseDeserialiser.class)
    public void setResult(CandidateNodes result) {
        super.setResult(result);
    }

    public static class CandidateNodes {

        private List<String> cadaddresses;
        //  快照块的父哈希

        private String hash;
        // 区块号

        private long number;

        public CandidateNodes() {

        }

        public CandidateNodes(List<String> cadaddresses, String hash, long number) {
            this.cadaddresses = cadaddresses;
            this.hash = hash;
            this.number = number;
        }

        public List<String> getCadaddresses() {
            return cadaddresses;
        }

        public void setCadaddresses(List<String> cadaddresses) {
            this.cadaddresses = cadaddresses;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public long getNumber() {
            return number;
        }

        public void setNumber(long number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CandidateNodes that = (CandidateNodes) o;
            return number == that.number &&
                    Objects.equals(cadaddresses, that.cadaddresses) &&
                    Objects.equals(hash, that.hash);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cadaddresses, hash, number);
        }
    }


    public static class ResponseDeserialiser extends JsonDeserializer<HpbGetCandidateNodes.CandidateNodes> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public HpbGetCandidateNodes.CandidateNodes deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, HpbGetCandidateNodes.CandidateNodes.class);
            } else {
                // null is wrapped by Optional in above getter
                return null;
            }
        }
    }


}
