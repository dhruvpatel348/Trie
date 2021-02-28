package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
    
		TrieNode root = new TrieNode(null,null,null);
		if(allWords.length == 0)
			return root;
		
		Indexes index = new Indexes(0,(short)(0), (short)(allWords[0].length()-1));
		root.firstChild = new TrieNode(index,null,null);
		
		for(int i = 1; i<allWords.length;i++)
		{
			buildTrie(root, allWords,i,allWords[i],0);
		}
		
		return root;
       }
	
	private static void buildTrie(TrieNode rChild,String[] aWord, int i, String sWord,int cValue)
	{
		TrieNode curr = rChild.firstChild;
		TrieNode prev = rChild;
		
		int pSize = 0;
		
		while(curr!=null)
		{
			
			if(aWord[curr.substr.wordIndex].charAt(cValue) == sWord.charAt(cValue))
			{
				while(aWord[curr.substr.wordIndex].charAt(cValue+pSize) == sWord.charAt(cValue+pSize))
				{
					if(curr.substr.endIndex == cValue+pSize)
					{
						buildTrie(curr,aWord,i,sWord,cValue+pSize+1);
						return;
					}
					pSize++;
				}
				
				int eIndex = curr.substr.endIndex;
				Indexes ind = new Indexes(curr.substr.wordIndex,(short)cValue,(short)(pSize+cValue-1));
				
				curr.substr = ind;
				curr.firstChild = new TrieNode(new Indexes(curr.substr.wordIndex,(short)(cValue+pSize),(short)(eIndex)),curr.firstChild,null);
				curr.firstChild.sibling = new TrieNode(new Indexes(i,(short)(cValue+pSize),(short)(sWord.length()-1)),null,null);
				
				return;
				
			}
			if(curr.sibling == null)
			{
				prev = curr;
			}
			
			curr = curr.sibling;
			
		}
		
		prev.sibling = new TrieNode(new Indexes(i,(short)(cValue),(short)(sWord.length()-1)),null,null);
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		
		ArrayList<TrieNode> cList = new ArrayList<>();
		TrieNode curr = root.firstChild;
		
		while(curr!=null)
		{
			String cStr = allWords[curr.substr.wordIndex].substring(curr.substr.startIndex, curr.substr.endIndex+1);
			int cValue = compare(cStr,prefix);
			
			
			if(cValue == 0)
			{
				curr = curr.sibling;
				continue;
			}
			
			
			if(cValue>0 && cValue<prefix.length())
			{
				curr = curr.firstChild;
				prefix = prefix.substring(curr.substr.startIndex);
				
				continue;
			}
			
			if(cValue == prefix.length())
			{
				break;
			}
			
			
		}
		
		if(curr == null) return null;
		
		cList.addAll(allCList(curr));
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return cList;
	}
	
	private static ArrayList<TrieNode> allCList(TrieNode curr)
	{
		ArrayList<TrieNode> cList = new ArrayList<>();
		
		if(curr.firstChild == null)
		{
			cList.add(curr);
		}
		else
		{
			TrieNode rChild = curr.firstChild;
			
			while(rChild!=null)
			{
			  cList.addAll(allCList(rChild));
				rChild = rChild.sibling;
			}
			
		}
		return cList;
	}
	
	private static int compare(String word1, String word2)
	{
		int cValue = 0;

		for(int i=0; i < (Math.min(word1.length(),word2.length()));i++)
		{
			if(word1.charAt(i) == word2.charAt(i))
			{
				cValue++;
			}
		}
		return cValue;

	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
