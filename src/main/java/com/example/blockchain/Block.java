package com.example.blockchain;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Objects;


public class Block {

    private Integer proof;
    private String previousHash;
    private Integer index;
    private String timestamp;
    private String currentHash;

    public Block(Integer proof, String previousHash, Integer index, String timestamp) {
        this.proof = proof;
        this.previousHash = previousHash;
        this.index = index;
        this.timestamp = timestamp;
        String dataToHash = Integer.toString(proof) + previousHash + Integer.toString(index) + timestamp;
        this.currentHash =  DigestUtils.sha256Hex(dataToHash);
    }

    public Integer getProof() {
        return proof;
    }

    public void setProof(Integer proof) {
        this.proof = proof;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(proof, block.proof) && Objects.equals(previousHash, block.previousHash) && Objects.equals(index, block.index) && Objects.equals(timestamp, block.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proof, previousHash, index, timestamp);
    }

    @Override
    public String toString() {
        return "Block{" +
                "proof=" + proof +
                ", previousHash='" + previousHash + '\'' +
                ", index=" + index +
                ", timestamp='" + timestamp + '\'' +
                ", currentHash='" + currentHash + '\'' +
                '}';
    }
}
