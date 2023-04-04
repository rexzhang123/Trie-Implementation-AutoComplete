package edu.emory.cs.trie;

import java.util.ArrayList;
import java.util.List;

public class TrieQuiz extends Trie<Integer> {
    /**
     * PRE: this trie contains all country names as keys and their unique IDs as values
     * (e.g., this.get("United States") -> 0, this.get("South Korea") -> 1).
     * @param input the input string in plain text
     *              (e.g., "I was born in South Korea and raised in the United States").
     * @return the list of entities (e.g., [Entity(14, 25, 1), Entity(44, 57, 0)]).
     */
    List<Entity> getEntities(String input) {
        //if the input is an empty string returns null
        if(input.length()<1){
            return null;
        }
        List<Entity> ans = new ArrayList<>();
        //iterates through the input and checks if substring is in trie
        for(int i = 0;i<input.length();i++){
            for(int j = i+1;j<input.length()+1;j++){

                //checks if the substring exists in trie
                TrieNode<Integer> node = find(input.substring(i,j));
                if(node!=null && node.isEndState()){
                    ans.add(new Entity(i,j,node.getValue()));
                }

                //this is here to reduce runtime; break, if the substring is not found at any moment,
                else if(node==null){
                    break;
                }
            }
        }
        return ans;
    }

//    public static void main(String[] args) {
//        String input = "I live in American ui";
//        List<String> L = List.of("America", "American");
//        TrieQuiz trie = new TrieQuiz();
//        for (int i = 0; i < L.size(); i++)
//            trie.put(L.get(i), i);
//
//        List<Entity> ans = trie.getEntities(input);
//        //System.out.println(trie.getRoot().getChild('U').getChild('n').getChild('i').getChild('t').getChild('e').getChild('d').getChild(' ').getChild('S').getChild('t').getChild('a').getChild('t').getChild('e'));
//        System.out.println(ans);
//    }

}