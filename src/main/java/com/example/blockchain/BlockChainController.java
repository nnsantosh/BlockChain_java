package com.example.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BlockChainController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private  Blockchain blockChain;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/mine_block")
    public String mineBlock(Model model) throws NoSuchAlgorithmException, JsonProcessingException {

        Block previousBlock = blockChain.getPreviousBlock();
        Integer previousProof = previousBlock.getProof();
        Integer currentProof = blockChain.proofOfWork(previousProof);
        String previousHash = blockChain.hash(previousBlock);
        Block currentBlock = blockChain.createBlock(currentProof,previousHash);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("index",currentBlock.getIndex().toString());
        responseMap.put("proof",currentBlock.getProof().toString());
        responseMap.put("previousHash",currentBlock.getPreviousHash());
        responseMap.put("timeStamp",currentBlock.getTimestamp());
        System.out.println("mineBlock : responseMap: "+responseMap);
        model.addAttribute("responseMap", responseMap.toString());
        return "mine_block";
    }

    @GetMapping("/get_block_chain")
    public String getBlockChain(Model model){
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("chain",blockChain.getChain().toString());
        responseMap.put("length",Integer.toString(blockChain.getChain().size()));
        System.out.println("getBlockChain : responseMap: "+responseMap);
        model.addAttribute("responseMap", responseMap.toString());
        return "block_chain";
    }

    @GetMapping("/is_valid_block_chain")
    public String isValidBlockChain(Model model) throws NoSuchAlgorithmException, JsonProcessingException {
        boolean isValid = blockChain.isChainValid(blockChain.getChain());
        Map<String, String> responseMap = new HashMap<>();
        if (isValid){
            responseMap.put("message","All good. The Blockchain is valid.");
        }else {
            responseMap.put("message","Houston, we have a problem. The Blockchain is not valid.");
        }
        System.out.println("isValidBlockChain : responseMap: "+responseMap.toString());
        model.addAttribute("responseMap", responseMap.toString());
        return "is_valid_block_chain";
    }
}
