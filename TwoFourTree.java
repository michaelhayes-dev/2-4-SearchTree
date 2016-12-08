package termproject;

/**
 * Title:        Term Project 2-4 Trees
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class TwoFourTree
        implements Dictionary {

    private Comparator treeComp;
    private int size = 0;
    private TFNode treeRoot = null;

    public TwoFourTree(Comparator comp) {
        treeComp = comp;
    }

    private TFNode root() {
        return treeRoot;
    }

    private void setRoot(TFNode root) {
        treeRoot = root;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Searches dictionary to determine if key is present
     * @param key to be searched for
     * @return object corresponding to key; null if not found
     */
    @Override
    public Object findElement(Object key) {
        
        //holds the return data of FFGTE
        int indexElement;
        TFNode node = search(key);
      
         //FindFirstGreaterThanOrEqual
        while (node != null) {
            //assigns value from FFGTE
            indexElement = findFirstGreaterThanOrEqual(node, key);
            //checks to see if it is the correct value
            if (node.getItem(indexElement).key() == key) {
                return (node.getItem(indexElement));
            } 
            else {
                //reassign node to child
                node = node.getChild(indexElement);
            }   
        }
        //return null if unsuccessful
        return null;
    }
    
    protected TFNode search(Object key){
        //holds the return data of FFGTE
        int indexElement;
        TFNode node;
        //start at root node
        node = root();
      
         //FindFirstGreaterThanOrEqual
        while (node != null) {
            //assigns value from FFGTE
            indexElement = findFirstGreaterThanOrEqual(node, key);
            //checks to see if it is the correct value
            if (node.getItem(indexElement).key() == key) {
                return (node);
            } 
            else {
                //reassign node to child
                node = node.getChild(indexElement);
            }   
        }
        //return null if unsuccessful
        return null;
    }

    /**
     * Inserts provided element into the Dictionary
     * @param key of object to be inserted
     * @param element to be inserted
     */
    @Override
    public void insertElement(Object key, Object element) {
    	//creates new item based on input
        Item newItem = new Item (key, element);
        //adds a node to insert the item into if the tree doesn't have nodes yet
        if (isEmpty()) {
            treeRoot = new TFNode();
            treeRoot.insertItem(0, newItem);   
        }
        else {
            TFNode node; 
            //find proper location to insert item
            node = (TFNode) findElement(key);
            if (node != null) {
                int index = 0;
                //finds index to place in node
                while (treeComp.isLessThan(key, node.getItem(index).key())) {
                    index++;
                }
                
                if (index > 3) {
                    //add a child node and insert child
                    node.insertItem(index, newItem);
                }
                else if (node.getNumItems() == 3) {
                    //fix tree when you boot out an item
                    node.insertItem(index, newItem);
                }
                
                
            }
        }
        
        
        size++;
        
        //Always check to make sure all pointers are hooked up correctly
        checkTree();
    }

    /**
     * Searches dictionary to determine if key is present, then
     * removes and returns corresponding object
     * @param key of data to be removed
     * @return object corresponding to key
     * @exception ElementNotFoundException if the key is not in dictionary
     */
    @Override
    public Object removeElement(Object key) throws ElementNotFoundException {
        if (findElement(key) == null) {
            throw new ElementNotFoundException ("No such element exists.");
        }
        else {
            //remove the item
            
            //ALGORITHM
            //find node to delete
            //am i a leaf?
            //if so remove item from the leaf and check underflow
            //if not, replace me with inorder successor
            //perform a shifting delete to remove inorder successor
            //check if underflow
        }
        
        //Always check to make sure all pointers are hooked up correctly
        checkTree();
        
        size--;
        
        return null;
    }
    
    /**
     * 
     * @param node the node to find the inorderSucessor for
     * @return the inorderSuccessor node, if one exists
     */
    protected TFNode inorderSuccessor(TFNode node){
        return null;
    }
    
    /**
     * Find First Greater Than or Equal
     * 
     * @author Michael Hayes
     * This method is used in the search routine and is used to search the node
     * for where the key should go. Called for a node to see if the key is in a given node
     * @param node the node to search
     * @param key the item to search for in given node 
     * @return index in the array relating to the first greater than or equal
     */
    protected int findFirstGreaterThanOrEqual(TFNode node, Object key){
        
        int numOfItems = node.getNumItems();
        //set the return value to the last slot because if we don't find
        //a first greater than or equal we know we need to put it at the end
        int returnVal = numOfItems;
        
        for(int i = 0; i < numOfItems; i++){
            //if the current element's key is greater than or equal to the search key 
            if(treeComp.isGreaterThanOrEqualTo(node.getItem(i).key(), key)){
                //return the index
                returnVal = i;
            }
        }       
        return returnVal;
    }
    
    /**
     * What Child is This
     * 
     * @author Michael Hayes
     * This method takes in a child node and returns which child (i.e. 0, 1, 2...)
     * of the parent it is
     * @param child the node to determine what child it is
     * @return the array index for what child is this
     */
    protected int whatChildIsThis(TFNode child){
        
        if(child != root()){
            TFNode parent = child.getParent();
        
            //since there is always one more child than num of items
            int numOfChildren = parent.getNumItems() + 1;

            for(int i = 0; i < numOfChildren; i++){
                if(child == parent.getChild(i)){
                    return i;
                }
            }
        }
        else{
            //TODO:
            //special case for root
        }
        
        return 0;
    }
       
    /**
     * 
     * @param node the node that is fixed from overflow
     */
    protected void overflow(TFNode node){
        
        //ALGORITHM
        //take out item 2
        //make new node
        //put item 3 in new node
        //hook up new node
        //put item 2 in parent
        //hook up kids
        //check overflow on parent
        
        //Don't shift when u remove item from node 
    }
    
    /**
     * Underflow
     * 
     * @author Michael Hayes
     * This method performs the correct underflow fix technique once a node
     * has underflowed. This method should be called after checking to see
     * if a node has underflowed as this is only the fix up method.
     * @param node the node that is fixed from underflow
     */
    protected void underflow(TFNode node){
        
        if(node != root()){
           //siblings of the node
           TFNode leftSib = null;
           TFNode rightSib = null;
           
           //NOTE: we already know that because 'node' isn't the root, getParent() won't return null
           int wcit = whatChildIsThis(node);
           if(wcit > 0){
               //if we are not the furthest left sibling then give us our left sibling
               leftSib = node.getParent().getChild(wcit - 1); 
           }
           if(wcit < node.getParent().getNumItems() + 1){
               //if we are not the furthest right sibling then give us our right sibling
               rightSib = node.getParent().getChild(wcit + 1); 
           }
           
           //perform the underflow checks
           if((leftSib != null) && (leftSib.getNumItems() == 2)){
               leftTransfer(node);
           }
           else if((rightSib != null) && (rightSib.getNumItems() == 2)){
               rightTransfer(node);
           }
           else if(leftSib != null){
               leftFusion(node);
           }
           else {
               rightFusion(node);
           }
        }
        else{
            //TODO:
            //Special case for underflow at root
        }    
    }
    
    /**
     * 
     * @param node the node that the left transfer is performed on
     */
    protected void leftTransfer(TFNode node){
        
    }
    
    /**
     * 
     * @param node the node that the right transfer is performed on
     */
    protected void rightTransfer(TFNode node){
        
    }
    
    /**
     * 
     * @param node the node that the left fusion is performed on
     */
    protected void leftFusion(TFNode node){
        
    }
    
    /**
     * 
     * @param node the node that the right fusion is performed on
     */
    protected void rightFusion(TFNode node){
        
    }

    public static void main(String[] args) {
        Comparator myComp = new IntegerComparator();
        TwoFourTree myTree = new TwoFourTree(myComp);

        Integer myInt1 = new Integer(47);
        myTree.insertElement(myInt1, myInt1);
        Integer myInt2 = new Integer(83);
        myTree.insertElement(myInt2, myInt2);
        Integer myInt3 = new Integer(22);
        myTree.insertElement(myInt3, myInt3);

        Integer myInt4 = new Integer(16);
        myTree.insertElement(myInt4, myInt4);

        Integer myInt5 = new Integer(49);
        myTree.insertElement(myInt5, myInt5);

        Integer myInt6 = new Integer(100);
        myTree.insertElement(myInt6, myInt6);

        Integer myInt7 = new Integer(38);
        myTree.insertElement(myInt7, myInt7);

        Integer myInt8 = new Integer(3);
        myTree.insertElement(myInt8, myInt8);

        Integer myInt9 = new Integer(53);
        myTree.insertElement(myInt9, myInt9);

        Integer myInt10 = new Integer(66);
        myTree.insertElement(myInt10, myInt10);

        Integer myInt11 = new Integer(19);
        myTree.insertElement(myInt11, myInt11);

        Integer myInt12 = new Integer(23);
        myTree.insertElement(myInt12, myInt12);

        Integer myInt13 = new Integer(24);
        myTree.insertElement(myInt13, myInt13);

        Integer myInt14 = new Integer(88);
        myTree.insertElement(myInt14, myInt14);

        Integer myInt15 = new Integer(1);
        myTree.insertElement(myInt15, myInt15);

        Integer myInt16 = new Integer(97);
        myTree.insertElement(myInt16, myInt16);

        Integer myInt17 = new Integer(94);
        myTree.insertElement(myInt17, myInt17);

        Integer myInt18 = new Integer(35);
        myTree.insertElement(myInt18, myInt18);

        Integer myInt19 = new Integer(51);
        myTree.insertElement(myInt19, myInt19);

        myTree.printAllElements();
        System.out.println("done");

        myTree = new TwoFourTree(myComp);
        final int TEST_SIZE = 10000;


        for (int i = 0; i < TEST_SIZE; i++) {
            myTree.insertElement(new Integer(i), new Integer(i));
            //          myTree.printAllElements();
            //         myTree.checkTree();
        }
        System.out.println("removing");
        for (int i = 0; i < TEST_SIZE; i++) {
            int out = (Integer) myTree.removeElement(new Integer(i));
            if (out != i) {
                throw new TwoFourTreeException("main: wrong element removed");
            }
            if (i > TEST_SIZE - 15) {
                myTree.printAllElements();
            }
        }
        System.out.println("done");
    }

    public void printAllElements() {
        int indent = 0;
        if (root() == null) {
            System.out.println("The tree is empty");
        }
        else {
            printTree(root(), indent);
        }
    }

    public void printTree(TFNode start, int indent) {
        if (start == null) {
            return;
        }
        for (int i = 0; i < indent; i++) {
            System.out.print(" ");
        }
        printTFNode(start);
        indent += 4;
        int numChildren = start.getNumItems() + 1;
        for (int i = 0; i < numChildren; i++) {
            printTree(start.getChild(i), indent);
        }
    }

    public void printTFNode(TFNode node) {
        int numItems = node.getNumItems();
        for (int i = 0; i < numItems; i++) {
            System.out.print(((Item) node.getItem(i)).element() + " ");
        }
        System.out.println();
    }

    // checks if tree is properly hooked up, i.e., children point to parents
    public void checkTree() {
        checkTreeFromNode(treeRoot);
    }

    private void checkTreeFromNode(TFNode start) {
        if (start == null) {
            return;
        }

        if (start.getParent() != null) {
            TFNode parent = start.getParent();
            int childIndex = 0;
            for (childIndex = 0; childIndex <= parent.getNumItems(); childIndex++) {
                if (parent.getChild(childIndex) == start) {
                    break;
                }
            }
            // if child wasn't found, print problem
            if (childIndex > parent.getNumItems()) {
                System.out.println("Child to parent confusion");
                printTFNode(start);
            }
        }

        if (start.getChild(0) != null) {
            for (int childIndex = 0; childIndex <= start.getNumItems(); childIndex++) {
                if (start.getChild(childIndex) == null) {
                    System.out.println("Mixed null and non-null children");
                    printTFNode(start);
                }
                else {
                    if (start.getChild(childIndex).getParent() != start) {
                        System.out.println("Parent to child confusion");
                        printTFNode(start);
                    }
                    for (int i = childIndex - 1; i >= 0; i--) {
                        if (start.getChild(i) == start.getChild(childIndex)) {
                            System.out.println("Duplicate children of node");
                            printTFNode(start);
                        }
                    }
                }

            }
        }

        int numChildren = start.getNumItems() + 1;
        for (int childIndex = 0; childIndex < numChildren; childIndex++) {
            checkTreeFromNode(start.getChild(childIndex));
        }

    }
}
