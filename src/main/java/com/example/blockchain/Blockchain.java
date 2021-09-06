package com.example.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class Blockchain {

    private List<Block> chain = new ArrayList<>();

    public Blockchain(){
        super();
        this.createBlock(1,"0");
    }

    public Blockchain(List<Block> chain) throws Exception {
        if(null != chain){
            this.chain = chain;
        }else{
            throw new Exception("chain cannot be null!");
        }
    }

    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }

    public Block createBlock(Integer proof, String previousHash) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Block newBlock = new Block(proof, previousHash, chain.size() + 1, dtf.format(now));
        chain.add(newBlock);
        return newBlock;
    }

    public Block getPreviousBlock() {
        return chain.get(chain.size() - 1);
    }

    //What is proof of work?
    //Proof of work is the number of piece of data that the miners have to find in order to mine a new block
    //So we are going to define a problem and the miners have to solve that problem. An in order to solve this
    //problem they will have to find specific number that will be this proof of work
    //Define a problem that is challenging to solve but easy to verify
    //In below example miners have to find a string whose leading 4 characters are 0s in order to mine a block
    public Integer proofOfWork(Integer previousProof){
        int newProof = 1;
        boolean checkProof = false;
        while (!checkProof) {
            //Remember to use operations which are not asymmetric for input
            String input = String.valueOf(newProof * newProof - previousProof * previousProof);
            String hash_operation = DigestUtils.sha256Hex(input);
            System.out.println("hash_operation: "+hash_operation);
            if (hash_operation.startsWith("0000")) {
                checkProof = true;
            } else {
                newProof = newProof + 1;
            }
        }
        return newProof;
    }

    public String hash(Block block) throws JsonProcessingException {
        String dataToHash = Integer.toString(block.getProof()) + block.getPreviousHash() + Integer.toString(block.getIndex()) + block.getTimestamp();
        return DigestUtils.sha256Hex(dataToHash);
    }

    //Function to check if everything is right in our blockchain
    //1. If each block in the blockchain has correct proof of work
    //2. If the previousHash of each block is equal to the hash of the previous block
    public boolean isChainValid(List<Block> chain) throws JsonProcessingException {
        Block previousBlock = chain.get(0);
        int blockIndex = 1;
        while(blockIndex < chain.size()){
           Block  currentBlock = chain.get(blockIndex);
           if(!currentBlock.getPreviousHash().equalsIgnoreCase(hash(previousBlock))){
               return false;
           }
           Integer previousProof = previousBlock.getProof();
           Integer currentProof = currentBlock.getProof();
           String input = String.valueOf(currentProof * currentProof - previousProof * previousProof);
           String hash_operation = DigestUtils.sha256Hex(input);
           if (!hash_operation.startsWith("0000")) {
               return false;
           }
           previousBlock = currentBlock;
           blockIndex = blockIndex + 1;
        }
        return true;
    }

}
