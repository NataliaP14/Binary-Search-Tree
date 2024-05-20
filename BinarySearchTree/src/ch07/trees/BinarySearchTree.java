

package ch07.trees;

import java.util.*;   // Iterator, Comparator

import ch04.queues.*;
import ch02.stacks.*;
import support.BSTNode;      

public class BinarySearchTree<T> implements BSTInterface<T>
{
  protected BSTNode<T> root;      // reference to the root of this BST
  protected Comparator<T> comp;   // used for all comparisons

  protected boolean found;   // used by remove

  public BinarySearchTree() 
  // Precondition: T implements Comparable
  // Creates an empty BST object - uses the natural order of elements.
  {
    root = null;
    comp = new Comparator<T>()
    {
       @SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(T element1, T element2)
       {
         return ((Comparable)element1).compareTo(element2);
       }
    };
  }

  public BinarySearchTree(Comparator<T> comp) 
  // Creates an empty BST object - uses Comparator comp for order
  // of elements.
  {
    root = null;
    this.comp = comp;
  }

  public boolean isFull()
  // Returns false; this link-based BST is never full.
  {
    return false;
  }

  public boolean isEmpty()
  // Returns true if this BST is empty; otherwise, returns false.
  {
    return (root == null);
  }

  public T min()
  // If this BST is empty, returns null;
  // otherwise returns the smallest element of the tree.
  {
    if (isEmpty())
       return null;
    else
    {
       BSTNode<T> node = root;
       while (node.getLeft() != null)
         node = node.getLeft();
       return node.getInfo();
    }
  }

  public T max()
  // If this BST is empty, returns null;
  // otherwise returns the largest element of the tree.
  {
    if (isEmpty())
       return null;
    else
    {
       BSTNode<T> node = root;
       while (node.getRight() != null)
         node = node.getRight();
       return node.getInfo();
    }
  }

  private int recSize(BSTNode<T> node)
  // Returns the number of elements in subtree rooted at node.
  {
    if (node == null)    
      return 0;
    else
      return 1 + recSize(node.getLeft()) + recSize(node.getRight());
  }

  public int size()
  // Returns the number of elements in this BST.
  {
    return recSize(root);
  }

  public int size2()
  // Returns the number of elements in this BST.
  {
    int count = 0;
    if (root != null)
    {
      LinkedStack<BSTNode<T>> nodeStack = new LinkedStack<BSTNode<T>>();
      BSTNode<T> currNode;
      nodeStack.push(root);
      while (!nodeStack.isEmpty())
      {
        currNode = nodeStack.top();
        nodeStack.pop();
        count++;
        if (currNode.getLeft() != null)
          nodeStack.push(currNode.getLeft());
        if (currNode.getRight() != null)
          nodeStack.push(currNode.getRight());
      }
    }
    return count;
  }

  private boolean recContains(T target, BSTNode<T> node)
  // Returns true if the subtree rooted at node contains info i such that 
  // comp.compare(target, i) == 0; otherwise, returns false.
 {
    if (node == null)
      return false;       // target is not found
    else if (comp.compare(target, node.getInfo()) < 0)
      return recContains(target, node.getLeft());   // Search left subtree
    else if (comp.compare(target, node.getInfo()) > 0)
      return recContains(target, node.getRight());  // Search right subtree
    else
      return true;        // target is found
  }

  public boolean contains (T target)
  // Returns true if this BST contains a node with info i such that 
  // comp.compare(target, i) == 0; otherwise, returns false.
  {
    return recContains(target, root);
  }

  
  private T recGet(T target, BSTNode<T> node)
  // Returns info i from the subtree rooted at node such that 
  // comp.compare(target, i) == 0; if no such info exists, returns null.
  {
    if (node == null)
      return null;             // target is not found
    else if (comp.compare(target, node.getInfo()) < 0)
      return recGet(target, node.getLeft());         // get from left subtree
    else
    if (comp.compare(target, node.getInfo()) > 0)
      return recGet(target, node.getRight());        // get from right subtree
    else
      return node.getInfo();  // target is found
  }

  public T get(T target)
  // Returns info i from node of this BST where comp.compare(target, i) == 0;
  // if no such node exists, returns null.
  {
    return recGet(target, root);
  }

  private BSTNode<T> recAdd(T element, BSTNode<T> node)
  // Adds element to tree rooted at node; tree retains its BST property.
  {
    if (node == null)
      // Addition place found
      node = new BSTNode<T>(element);
    else if (comp.compare(element, node.getInfo()) <= 0)
      node.setLeft(recAdd(element, node.getLeft()));    // Add in left subtree
    else
      node.setRight(recAdd(element, node.getRight()));   // Add in right subtree
    return node;
  }

  public boolean add (T element)
  // Adds element to this BST. The tree retains its BST property.
  {
    root = recAdd(element, root);
    return true;
  }

/*
  public boolean add (T element)
  // Adds element to this BST. The tree retains its BST property.
  {
    BSTNode<T> newNode = new BSTNode<T>(element);
    BSTNode<T> prev = null, curr = null;
    
    if (root == null)
      root = newNode;
    else
    {
      curr = root;
      while (curr != null)
      {
        if (comp.compare(element, curr.getInfo()) <= 0)
        {
          prev = curr;
          curr = curr.getLeft();
        }
        else
        {
          prev = curr;
          curr = curr.getRight();
        }
      }
      if (comp.compare(element, prev.getInfo()) <= 0)
        prev.setLeft(newNode);
      else
        prev.setLeft(newNode);
    }
    return true;
  }
*/

  private T getPredecessor(BSTNode<T> subtree)
  // Returns the information held in the rightmost node of subtree
  {
    BSTNode<T> temp = subtree;
    while (temp.getRight() != null)
      temp = temp.getRight();
    return temp.getInfo();
  }

  private BSTNode<T> removeNode(BSTNode<T> node)
  // Removes the information at node from the tree. 
  {
    T data;
    if (node.getLeft() == null)
      return node.getRight();
    else if (node.getRight() == null) 
      return node.getLeft();
    else
    {
      data = getPredecessor(node.getLeft());
      node.setInfo(data);
      node.setLeft(recRemove(data, node.getLeft()));  
      return node;
    }
  }

  private BSTNode<T> recRemove(T target, BSTNode<T> node)
  // Removes element with info i from tree rooted at node such that
  // comp.compare(target, i) == 0 and returns true; 
  // if no such node exists, returns false. 
  {
    if (node == null)
      found = false;
    else if (comp.compare(target, node.getInfo()) < 0)
      node.setLeft(recRemove(target, node.getLeft()));
    else if (comp.compare(target, node.getInfo()) > 0)
      node.setRight(recRemove(target, node.getRight()));
    else  
    {
      node = removeNode(node);
      found = true;
    }
    return node;
  }

  public boolean remove (T target)
  // Removes a node with info i from tree such that comp.compare(target,i) == 0
  // and returns true; if no such node exists, returns false.
  {
    root = recRemove(target, root);
    return found;
  }

  public Iterator<T> getIterator(BSTInterface.Traversal orderType)
  // Creates and returns an Iterator providing a traversal of a "snapshot" 
  // of the current tree in the order indicated by the argument.
  // Supports Preorder, Postorder, and Inorder traversal.
  {
    final LinkedQueue<T> infoQueue = new LinkedQueue<T>();
    if (orderType == BSTInterface.Traversal.Preorder)
      preOrder(root, infoQueue);
    else
    if (orderType == BSTInterface.Traversal.Inorder)
      inOrder(root, infoQueue);
    else
    if (orderType == BSTInterface.Traversal.Postorder)
      postOrder(root, infoQueue);

    return new Iterator<T>()
    {
      public boolean hasNext()
      // Returns true if the iteration has more elements; otherwise returns false.
      {
        return !infoQueue.isEmpty();
      }
      
      public T next()
      // Returns the next element in the iteration.
      // Throws NoSuchElementException - if the iteration has no more elements
      { 
        if (!hasNext())
          throw new IndexOutOfBoundsException("illegal invocation of next " + 
                                     " in BinarySearchTree iterator.\n");
        return infoQueue.dequeue();
      }

      public void remove()
      // Throws UnsupportedOperationException.
      // Not supported. Removal from snapshot iteration is meaningless.
      {
        throw new UnsupportedOperationException("Unsupported remove attempted on " 
                                              + "BinarySearchTree iterator.\n");
      }
    };
  }

  private void preOrder(BSTNode<T> node, LinkedQueue<T> q)
  // Enqueues the elements from the subtree rooted at node into q in preOrder.
  {
    if (node != null)
    {
      q.enqueue(node.getInfo());
      preOrder(node.getLeft(), q);
      preOrder(node.getRight(), q);
    }
  }

	  private void inOrder(BSTNode<T> node, LinkedQueue<T> q)
	  // Enqueues the elements from the subtree rooted at node into q in inOrder.  
	  {
	    if (node != null)
	    {
	      inOrder(node.getLeft(), q);
	      q.enqueue(node.getInfo());
	      inOrder(node.getRight(), q);
	    }
	  }
	
	  private void postOrder(BSTNode<T> node, LinkedQueue<T> q)
	  // Enqueues the elements from the subtree rooted at node into q in postOrder.  
	  {
	    if (node != null)
	    {
	      postOrder(node.getLeft(), q);
	      postOrder(node.getRight(), q);
	      q.enqueue(node.getInfo());
	    }
	  }
	  
	  public Iterator<T> iterator()
	  // InOrder is the default, "natural" order.
	  {
	    return getIterator(BSTInterface.Traversal.Inorder);
	  }
	
	  public int countLesser(T maxValue) {
		// TODO Auto-generated method stub
		return countLesserHelper(root, maxValue);
	}
	  //helper method
	  private int countLesserHelper(BSTNode<T> node, T maxValue) {
		  int count = 0;
		if (node == null) {
		return 0;
		}
		if (comp.compare(node.getInfo(), maxValue) <= 0) {
			count++;
		} 
		count = count + countLesserHelper(node.getLeft(), maxValue);
		count = count + countLesserHelper(node.getRight(), maxValue);
		return count;
	}
	
	public int countGreater(T minValue) {
		// TODO Auto-generated method stub
		return countGreaterHelper(root, minValue);
	}
		//helper method
	  private int countGreaterHelper(BSTNode<T> node, T minValue) {
		  int count = 0;
		if (node == null) {
		return 0;
		}
		if (comp.compare(node.getInfo(), minValue) >= 0) {
			count++;
		}
		count = count + countGreaterHelper(node.getLeft(), minValue);
		count = count + countGreaterHelper(node.getRight(), minValue);
	
		return count;
	
	}
	
	public int height() {
		// TODO Auto-generated method stub
		return heightHelper(root);
	}
	
	  private int heightHelper(BSTNode<T> node) {
		if (node == null) {
			return 0;
		}
		//getting the heights of the left and right children
		int heightL = heightHelper(node.getLeft());
		int heightR = heightHelper(node.getRight());
		return Math.max(heightL, heightR) + 1;
	}
	  public void balanceTree() {
			// TODO Auto-generated method stub
			Iterator<T> iter = getIterator(BSTInterface.Traversal.Inorder);
			List<T> list = new ArrayList<>();
			
			while (iter.hasNext()) {
				list.add(iter.next());
				
			}
		root = null;
		
	  insertTree(new ArrayList<>(list),0,list.size()-1);
	}
	
	private void insertTree(List<T> array, int low, int high) {
		// TODO Auto-generated method stub
		//test case
		 //System.out.println("Low: " + low + ", High: " + high);
		if (low == high) {
			add(array.get(low));
			
		} else if ((low + 1) == high) {
			add(array.get(low));
			add(array.get(high));
		} else {
			int mid = (low+high) / 2;
			add(array.get(mid));
			insertTree(array,low,mid-1);
			insertTree(array,mid+1,high);
		}
		
	}
	//method for breatdthfirst
	public void breadthFirstTraversal() {
		// TODO Auto-generated method stub
		LinkedQueue<BSTNode<T>> queue = new LinkedQueue<>();
		if (root != null) {
			 queue.enqueue(root);
		}
		 while (!queue.isEmpty()) {
			 BSTNode<T> node = queue.dequeue();
			 System.out.print(node.getInfo() + " ");
			 if (node.getLeft() != null) {
				 queue.enqueue(node.getLeft());
			 }
			 if (node.getRight() != null) {
				 queue.enqueue(node.getRight());
			 }
		}
	}
	//method for fratio
		public double fRatio() {	
		int minimumHeight =(int)((int)Math.ceil(size() + 1) / Math.ceil(2) - 1);
		if (height() == 0) {
			return 1.0;
			
		} else {
			return (double) minimumHeight / height();
		}
		
		}

		
		

		
	}