package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collections;


public class AutocompleteHWExtra extends Autocomplete<List<List<List<String>>>> {
    private final List<List<List<String>>> selection;

    public AutocompleteHWExtra(String dict_file, int max) {
        super(dict_file, max);
        selection = new ArrayList<>();
    }

    @Override
    public List<String> getCandidates(String prefix) {
        prefix=prefix.trim();
        List<String> cand = new ArrayList<>();
        Queue<TrieNode> queue = new LinkedList<>();
        int max = getMax(),counter=0;

        List<List<String>> prefixList = new ArrayList<>();

        boolean foundPrefix = false;

        //checks if prefix exists in selection
        for(int i = 0;i<selection.size();i++){
            if(selection.get(i).get(0).get(0).equals(prefix)){
                prefixList=selection.get(i);
                foundPrefix=true;
                break;
            }
        }

        //adds all candidates in the prefixList (except the prefix itself) to cand
        if(!prefixList.isEmpty()){
            for(int i = 1;i<prefixList.size();i++){
                cand.add(prefixList.get(i).get(0));
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

        //implementing bfs
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
                    //System.out.println("DDDDDDDDDD");
                    boolean contains_word = false;
                    //System.out.println(word);

                    for(int i = 1;i<prefixList.size();i++){
                        if(prefixList.get(i).get(0).equals(word)){
                            //System.out.println("EEEEEEEEEEEE");
                            contains_word = true;
                        }
                    }
                    if(contains_word==false){
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
            if(selection.get(i).get(0).get(0).equals(prefix)){
                //checks if the candidate already exists
                boolean exist = false;
                //System.out.println("AAAAAAAAA");
                for(int j = 1;j<selection.get(i).size();j++){
                    if(selection.get(i).get(j).get(0).equals(candidate)){
                        //System.out.println("BBBBBBBBB");
                        exist=true;
                        String newInt = String.valueOf(Integer.parseInt(selection.get(i).get(j).get(1))+1);
                        selection.get(i).get(j).set(1,newInt);
//                        System.out.println("SELECTION IN THE MIDDLE: " +selection);
                        int index = j;
                        while(Integer.parseInt(selection.get(i).get(index).get(1))>=Integer.parseInt(selection.get(i).get(index-1).get(1))){
                            Collections.swap(selection.get(i), index,index-1);
                            index--;
                        }
                        break;
                    }
                }
                if(!exist){
                    //System.out.println("CCCCCCCCCC");
                    List<String> cand_cell = new ArrayList<>();
                    cand_cell.add(candidate);
                    cand_cell.add("1");
                    selection.get(i).add(cand_cell);

                    int index = selection.get(i).size()-1;

                    while(Integer.parseInt(selection.get(i).get(index).get(1))>=Integer.parseInt(selection.get(i).get(index-1).get(1))){
                        Collections.swap(selection.get(i), index,index-1);
                        index--;
                    }
                }
                found = true;
                break;
            }
        }
        if(!found){
            ArrayList<List<String>> pre =  new ArrayList<>();
            List<String> pre_cell = new ArrayList<>();
            pre_cell.add(prefix);
            pre_cell.add(String.valueOf(Integer.MAX_VALUE));
            pre.add(pre_cell);
            selection.add(pre);
            List<String> cand_cell = new ArrayList<>();
            cand_cell.add(candidate);
            cand_cell.add("1");
            selection.get(selection.size()-1).add(cand_cell);
        }
        put(candidate,selection);
//        System.out.println("selection is: " + selection);
    }

//    public static void main(String[] args) {
//        final String dict_file = "src/main/resources/dict_copy.txt";
//        final int max = 50;
//        Autocomplete<?> ac = new AutocompleteHWExtra(dict_file, max);
//        //[abel, abele, abeles, abelia, abelian, abelite, abelicea, abelmosk, abelmusk, abeltree, abelmosks, abelonian, abelmoschus]
//        System.out.println(ac.getCandidates("a"));
//
//        ac.pickCandidate("a","apple");
//        ac.pickCandidate("a","apple");
//        ac.pickCandidate("a","apple");
//        ac.pickCandidate("a","apple");
//        ac.pickCandidate("a","apple");
////
//        ac.pickCandidate("a","applepie");
//        ac.pickCandidate("a","applepie");
//        ac.pickCandidate("a","applepie");
//        ac.pickCandidate("a","applepie");
//        ac.pickCandidate("a","applepie");
//        ac.pickCandidate("app ee","123");
//        ac.pickCandidate("app","apple");
//        ac.pickCandidate("app","apple");
//        ac.pickCandidate("app","12412");
//        ac.pickCandidate("app","12412");
//        ac.pickCandidate("1","902");
//
//
//
////        ac.pickCandidate("","ct");
////        ac.pickCandidate("","ct");
////        ac.pickCandidate("","ct");
////
////        ac.pickCandidate("","applebee");
////        ac.pickCandidate("","applebee");
////        ac.pickCandidate("","applebee");
//
//
//
//
////        ac.pickCandidate("a","applepie");
////
////        ac.pickCandidate("a","apple");
//
//
//
//        //ac.pickCandidate("a","applebie");
//        System.out.println(ac.getCandidates("a"));
//        System.out.println(ac.getCandidates("app"));
//        System.out.println(ac.getCandidates("app ee"));
//        System.out.println(ac.getCandidates("1"));
//        System.out.println(ac.getCandidates(""));
//        System.out.println(ac.getCandidates("124312414"));
////        System.out.println(ac.getCandidates("be"));
////        System.out.println(ac.getCandidates("be"));
////        System.out.println(ac.getCandidates("be"));
////        System.out.println(ac.getCandidates("benign"));
//
//    }
}