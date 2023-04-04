package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collections;

public class AutocompleteHW extends Autocomplete<List<List<String>>> {

    private final List<List<String>> selection;

    public AutocompleteHW(String dict_file, int max) {
        super(dict_file, max);
        selection = new ArrayList<>();
    }

    @Override
    public List<String> getCandidates(String prefix) {
        prefix=prefix.trim();
        List<String> cand = new ArrayList<>();
        Queue<TrieNode> queue = new LinkedList<>();
        int max = getMax(),counter=0;

        List<String> prefixList = new ArrayList<>();
        boolean foundPrefix = false;

        //checks if prefix exists in selection
        for(int i = 0;i<selection.size();i++){
            if(selection.get(i).get(0).equals(prefix)){
                prefixList = selection.get(i);
                foundPrefix=true;
                break;
            }
        }

        //adds all candidates in the prefixList (except the prefix itself) to cand
        if(!prefixList.isEmpty()){
            for(int i = 1;i<prefixList.size();i++){
                cand.add(prefixList.get(i));
                counter++;
                //if more candidates is in the prefixList than max
                if(counter>=max){
                    return cand;
                }
            }
        }

        if(find(prefix)==null){
            return cand;
        }

        TrieNode newRoot = find(prefix);
        queue.add(newRoot);

        //breadth first search
        while(!queue.isEmpty() && counter<max){
            TrieNode node = queue.poll();
            Map<Character,TrieNode> childrenMap = node.getChildrenMap();

            ArrayList<Character> mapCharList = new ArrayList<>();

            //hashmap is unordered, so when retrieving key from childrenmap we need to order the key first
            for (Map.Entry<Character, TrieNode> curr : childrenMap.entrySet())
            {
                mapCharList.add(curr.getKey());
            }

            Collections.sort(mapCharList);

            //adds keys to queue in alphabetical order
            for(int i = 0;i<mapCharList.size();i++){
                queue.add(childrenMap.get(mapCharList.get(i)));
            }

            if(!foundPrefix){
                if(node.isEndState()){
                    counter++;
                    String word = "";
                    while(node.getParent()!=null){
                        word = node.getKey()+word;
                        node=node.getParent();
                    }
                    cand.add(word);
                }
            }
            else {
                if(node.isEndState()){
                    String word = "";
                    while (node.getParent() != null) {
                        word = node.getKey() + word;
                        node = node.getParent();
                    }
                    if(prefixList.subList(1,prefixList.size()).contains(word)==false) {
                        counter++;
                        cand.add(word);
                    }
                }
            }
        }
        return cand;
    }

    @Override
    public void pickCandidate(String prefix, String candidate) {
        prefix = prefix.trim();
        candidate=candidate.trim();

        boolean found = false;

        for(int i = 0;i<selection.size();i++){
            //checks if prefix exists in selection
            if(selection.get(i).get(0).equals(prefix)){
                boolean exist = false;
                //checks if the candidate already exists
                for(int j = 1;j<selection.get(i).size();j++){
                    if(selection.get(i).get(j).equals(candidate)){
                        selection.get(i).remove(j);
                        selection.get(i).add(1,candidate);
                        exist = true;
                        break;
                    }
                }
                if(!exist){
                    selection.get(i).add(1,candidate);
                }
                found = true;
                break;
            }
        }

        //adds prefix to selection and candidate to index 1
        if(!found){
            ArrayList<String> pre =  new ArrayList<>();
            pre.add(prefix);
            selection.add(pre);
            selection.get(selection.size()-1).add(1,candidate);
        }
        put(candidate,selection);
    }

//    public static void main(String[] args) {
//        final String dict_file = "src/main/resources/dict.txt";
//        final int max = 100;
//        Autocomplete<?> ac = new AutocompleteHW(dict_file, max);
//        //[abel, abele, abeles, abelia, abelian, abelite, abelicea, abelmosk, abelmusk, abeltree, abelmosks, abelonian, abelmoschus]
////        System.out.println(ac.getCandidates("abel"));
////
////        ac.pickCandidate("abele","abele");
////        ac.pickCandidate("abele","abele");
////        ac.pickCandidate("abele","abele");
//
////        ac.pickCandidate("abel","abeltree");
////        ac.pickCandidate("abel","abelmusk");
//
////        System.out.println(ac.getCandidates("abel"));
//
////        System.out.println(ac.getCandidates("a"));
////        System.out.println(ac.getCandidates("apple"));
////        ac.pickCandidate("123","456");
////        ac.pickCandidate("123","451");
////        ac.pickCandidate("123","458");
////        ac.pickCandidate("123","459");
////
////        System.out.println(ac.getCandidates("123"));
////        System.out.println(ac.getCandidates("4"));
////        ac.pickCandidate("abel","abelmusk");
////        System.out.println(ac.getCandidates("abel"));
//
//
//
//
//    }
}