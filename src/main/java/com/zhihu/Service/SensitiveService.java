package com.zhihu.Service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader buffer = new BufferedReader(reader);
            String lineTxt;
            while ((lineTxt = buffer.readLine()) != null){
                addWord(lineTxt.trim());
            }
        }catch (Exception e){
            logger.error("读取敏感词失败!" + e.getMessage());
        }
    }

    //增加关键词
    private void addWord(String lineTxt){
        TreeNode tempNode = root;
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            TreeNode node = tempNode.getSubNode(c);
            if(node == null){
                node = new TreeNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if(i == lineTxt.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class TreeNode{
        private boolean end = false;

        private Map<Character, TreeNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TreeNode node){
            subNodes.put(key, node);
        }

        TreeNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        void setKeywordEnd(boolean end){
            this.end = end;
        }
    }

    private TreeNode root = new TreeNode();

    private boolean isSymbol(char c){
        int ic = (int)c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return text;
        }
        StringBuilder result = new StringBuilder();
        TreeNode tempNode = root;
        int begin = 0;
        int positon = 0;

        while(positon < text.length()){
            Character c = text.charAt(positon);
            if(isSymbol(c)){
                if(tempNode == root){
                    result.append(c);
                    ++begin;
                }
                ++positon;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                result.append(text.charAt(begin));
                positon = begin + 1;
                begin = positon;
                tempNode = root;
            }else if(tempNode.isKeywordEnd()){
                //发现了一组敏感词
                for (int i = 0; i < positon - begin + 1; i++) {
                    result.append('*');
                }
                positon = positon + 1;
                begin = positon;
                tempNode = root;
            }else{
                ++positon;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] args){
        SensitiveService sensitiveService = new SensitiveService();
        String[] strings = {"快嫖娼", "听说包小姐和毒品更配哦", "那你"};
        String[] words = {"嫖娼", "包小姐", "毒品"};
        for(String word : words){
            sensitiveService.addWord(word);
        }
        for(String string : strings){
            System.out.println(sensitiveService.filter(string));
        }
    }

}
