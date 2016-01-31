
import java.util.*;

public class DLBDictionary implements DictionaryInterface{
	
	DLBNode root;
	DLBNode currentNode;
	
	public DLBDictionary(){
		root = new DLBNode();
		currentNode = new DLBNode();
		root.child = currentNode;
	}
	
	public boolean add(String s){		
		if(s.equals(""))
			return false;
		return add(s,currentNode,0);
	}
	
	private boolean add(String s, DLBNode n, int x){
		DLBNode newNode = new DLBNode();
		if(s.length() > 0 && x != s.length()){
			if(n.avaliable(s.charAt(x))){
				if(n.data == ' '){
					n.data = s.charAt(x);
					n.child = newNode;
					return add(s,newNode,++x);
				}
				else if(n.data != ' ' && n.hasSibling())
					return add(s,n.sib,x);
				else if(n.data != ' ' && !n.hasSibling()){
					n.sib = newNode;
					return add(s,newNode,x);
				}
			}			
			else	
				return add(s,n.found(s.charAt(x)).child,++x);
			
		}
		else{
			if(n.data == ' '){
				n.data = '!';
				return true;
			}
			else if (n.hasSibling()){
				return add(s,n.sib,x);
			}
			else{
				newNode.data = '!';
				n.sib = newNode;
				return true;
			}
		}
		return false;
	}
	
	public int search(StringBuilder s){	
		if(s.equals(null))
			return 0;
		
		return search(s,currentNode,0);	
	}
	
	private int search(StringBuilder s, DLBNode n,int x){
		
		if(n.data != s.charAt(x) && n.hasSibling() && s.length() >= x+1)
			return search(s,n.sib,x);
		else if(n.data == s.charAt(x) && s.length() > x+1){ //more to the word and more to the LL
			return search(s,n.child,x+1);
		}
		else if(n.data == s.charAt(x) && n.child.isTerminator() && !n.child.hasSibling() && s.length() == x+1) //made it to the end of the linked list and end of the word
			return 2;
		else if (n.data == s.charAt(x) && n.child.isTerminator() && n.child.hasSibling() && s.length() == x+1)//made it to the end of the linked list but there is still more to the word
			return 3;
		else if (n.data == s.charAt(x) && !n.child.isTerminator() && s.length() == x+1)//end of the word but more to 
			return 1;
		else
			return 0;
		
	}
	
	private class DLBNode{
		public char data;
		public DLBNode sib;
		public DLBNode child;
		
		DLBNode(){
			data = ' ';
			sib = null;
			child = null;
		}
		
		private boolean hasSibling(){
			if(sib == null)
				return false;
			else
				return true;
		}
		
		private boolean avaliable(char a){
			if(data == a)
				return false;
			else if(sib == null & data != a)
				return true;
			else if(sib != null)
				return (sib.avaliable(a));
			else
				return false;
		}
		
		private DLBNode found(char a){
			if(data == a)
				return this;
			else if(hasSibling())
				return sib.found(a);
			else 
				return null;
		}
		
		private boolean isTerminator(){
			if(data == '!')
				return true;
			else if(hasSibling())
				return sib.isTerminator();
			else
				return false;
				
		}
	}
}


