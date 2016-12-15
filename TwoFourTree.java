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
     * helper class to make searching MUCH easier
     */
    private class nodeIndexPair {
        private TFNode node;
        private int index;
        private boolean exactMatch;
        
        void nodeIndexPair(){
            node = null;
            index = 0;
            exactMatch = false;
        }
    }
    
    /**
     * Searches dictionary to determine if key is present
     * @param key to be searched for
     * @return object corresponding to key; null if not found
     */
    @Override
    public Object findElement(Object key) {
        
        nodeIndexPair pair = search(key);
        
        if(pair.exactMatch){
            return (pair.node.getItem(pair.index));
        }
        
        return null;
    }
    
    /**
     * Searches for a key within the tree
     * 
     * @param key the key to search for
     * @return the nodeIndexPair containing data from the search
     */
    protected nodeIndexPair search(Object key){
        //holds the return data of FFGTE
        nodeIndexPair pair = new nodeIndexPair();
        int indexElement;
        TFNode node;
        //start at root node
        node = root();
      
        //Always check if the node is not null so that no problems arise.
        while (node != null) {
            //find the first greater than or equal
            indexElement = findFirstGreaterThanOrEqual(node, key);
            
            //if all elements in the current node are less, we need to go down the furthest right child
            if (indexElement == -1) {
                
                //if we don't have a furthest right child then the item should be in this node's furthest right spot
                if(node.getChild(node.getNumItems()) == null){
                    pair.node = node;
                    pair.index = node.getNumItems();
                    return pair;
                }
                else{
                    //if we do have a furthest right child go down the furthest right child
                    node = node.getChild(node.getNumItems());
                }  
            } 
            
            //see if the element from FFGTOE is the item we want
            //if it is, then we simply return the node and index.
            else if (treeComp.isEqual(node.getItem(indexElement).key(), key)) {
                pair.index = indexElement;
                pair.node = node;
                pair.exactMatch = true;
                return pair;
            } 
            
            //if not, then we need to go down that child
            else {
                //if we don't have a child in that spot then the item should be in this spot
                if(node.getChild(indexElement) == null){
                    pair.node = node;
                    pair.index = indexElement;
                    return pair;
                }
                else{
                    //if we do have a child there go down the that child
                    node = node.getChild(indexElement);
                } 
            }    
        }
        //return null if all else fails
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
            treeRoot.setChild(0, null);
            treeRoot.setParent(null);
        }
        
        //if the tree already has nodes we need to find where the new element should go
        else {
            nodeIndexPair pair;
            //find the place where the element should be inserted
            pair = search(key);
            
            //if that key already exists in the tree put it at the inorder successor.
            if(pair.exactMatch){
                pair = inorderSuccessor(pair);
            }
            
            //perform a shifting insert
            pair.node.insertItem(pair.index, newItem);
                       
            checkOverflow(pair.node);     
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
        
        nodeIndexPair pair = search(key);
        Item out;
         
        if (findElement(key) == null) {
            throw new ElementNotFoundException ("No such element exists.");
        }
        else {
            
            //if we are not a leaf
            if(pair.node.getChild(0) != null){
                //replace with inorder successor
                nodeIndexPair inOrdSuc = inorderSuccessor(pair);
                Item tempItem = inOrdSuc.node.getItem(inOrdSuc.index);
                inOrdSuc.node.replaceItem(inOrdSuc.index, pair.node.getItem(pair.index));
                pair.node.replaceItem(pair.index, tempItem);
            }
            
            //do a shifting remove on new inorder successor
            pair = inorderSuccessor(pair);
            pair.node.removeItem(pair.index);
            
            size--;
            //check for underflow
            checkTree();
            checkUnderflow(pair.node);
        }
        return null;
    }
    
    /**
     * 
     * @param pair the nodeIndex pair
     * @return the inorderSuccessor node and index, if one exists
     */
    protected nodeIndexPair inorderSuccessor(nodeIndexPair pair){
        
        nodeIndexPair returnPair = new nodeIndexPair();
        TFNode returnNode;
        
        //find the inorder successer node
        //if we don't have a right child then put the duplicate right after our position
        if(pair.node.getChild(pair.index) == null){
            returnPair.node = pair.node;
            returnPair.index = pair.index;
        }
        else{
            //get the right child
            returnNode = pair.node.getChild(pair.index+1);
            
            //get the furthest left child
            while(returnNode.getChild(0) != null){
                returnNode = returnNode.getChild(0);
            }
            returnPair.node = returnNode;
            returnPair.index = 0;
        }
        return returnPair;
    }
    
    /**
     * Find First Greater Than or Equal
     * 
     * @author Michael Hayes
     * This method is used in the search routine and is used to search the node
     * for where the key should go. Called for a node to see if the key is in a given node
     * @param node the node to search
     * @param key the item to search for in given node 
     * @return index in the array relating to the first greater than or equal. If -1 then all items are less than current item
     */
    protected int findFirstGreaterThanOrEqual(TFNode node, Object key){
        
        int numOfItems = node.getNumItems();
        
        for(int i = 0; i < numOfItems; i++){
            //if the current element's key is greater than or equal to the search key 
            if(treeComp.isGreaterThanOrEqualTo(node.getItem(i).key(), key)){
                //return the index
                return i;
            }
        } 
        //else if all items are smaller return -1
        return -1;
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
        else {
            //returns -1 if the passed node is the root.

            return -1;            
        }        

        return 0;
    }
    
    /**
     * Method that checks to see if a node overflowed
     * and calls the fix-up routine if so
     * @param node 
     */
    protected void checkOverflow(TFNode node){
        if(node.getNumItems() > node.getMaxItems()){
            overflow(node);
        }
    }
    
    /**
     * Method that checks to see if a node underflowed
     * and calls the fix-up routine if so
     * @param node 
     */
    protected void checkUnderflow(TFNode node){
        if(node.getNumItems() == 0){
            underflow(node);
        }
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

        if(node != root()){
            //determine what child it is
            int wcit = whatChildIsThis(node);
            TFNode parent = node.getParent();
            TFNode newNode = new TFNode();
            
            Item i3 = node.deleteItem(3);
            Item i2 = node.deleteItem(2);
            newNode.addItem(0, i3);
            
            newNode.setParent(parent);
            parent.insertItem(wcit, i2);
            parent.setChild(wcit+1, newNode);
            
            //hook up the new children
            newNode.setChild(0, node.getChild(3));
            newNode.setChild(1, node.getChild(4));
            node.setChild(3, null);
            node.setChild(4, null);
            
            //hook up children to parents
            if(newNode.getChild(0) != null){
                newNode.getChild(0).setParent(newNode);
            }
            if(newNode.getChild(1) != null){
                newNode.getChild(1).setParent(newNode);
            }
            
            //make sure the tree is correct 
            checkTree();
            checkOverflow(parent);
        }
        else{
            //the node that overflows is the root
            //we will need to grow the root
            TFNode newRoot = new TFNode();
            Item newRootItem = node.getItem(2);
            newRoot.insertItem(0, newRootItem);
            node.setParent(newRoot);
            setRoot(newRoot);
            
            TFNode newChild = new TFNode();
            newChild.insertItem(0, node.deleteItem(3));
            newChild.setParent(newRoot);
            node.deleteItem(2);
                        
            //hook up the parents to children
            newRoot.setChild(0, node);
            newRoot.setChild(1, newChild);
            newChild.setChild(0, node.getChild(3));
            newChild.setChild(1, node.getChild(4));
            
            //null out old children
            node.setChild(3, null);
            node.setChild(4, null);
            
            //hook up the children to parents
            if(newChild.getChild(0) != null){
                newChild.getChild(0).setParent(newChild);
            }
            if(newChild.getChild(1) != null){
                newChild.getChild(1).setParent(newChild);
            }
                        
            checkTree();
        }      
    }
    
    /**
     * Returns the left sibling of a given node
     * @param node the node to find the left sibling of
     * @return the left sibling
     */
    protected TFNode leftSib(TFNode node){
        
        if(node != root()){
            int wcit = whatChildIsThis(node);
            if(wcit > 0){
               //if we are not the furthest left sibling then return our left sibling
               return node.getParent().getChild(wcit - 1); 
            }
        }
        
        return null;       
    }
    
    /**
     * Returns the right sibling of a given node
     * @param node the node to find the right sibling of
     * @return the right sibling
     */
    protected TFNode rightSib(TFNode node){
        
        if(node != root()){
            int wcit = whatChildIsThis(node);
            if(wcit < node.getParent().getNumItems() + 1){
               //if we are not the furthest right sibling then give us our right sibling
               return node.getParent().getChild(wcit + 1); 
           }
        }
        
        return null;       
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
           
           TFNode leftSib = leftSib(node);
           TFNode rightSib = rightSib(node);
           
           //perform the underflow checks
           if((leftSib != null) && (leftSib.getNumItems() >= 2)){
               leftTransfer(node);
           }
           else if((rightSib != null) && (rightSib.getNumItems() >= 2)){
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
            //Special case for underflow at root
            setRoot(root().getChild(0));
        } 
    }
    
    /**
     * 
     * @param node the node that the left transfer is performed on
     */
    protected void leftTransfer(TFNode node){
        TFNode leftSib = leftSib(node);
        TFNode parent = node.getParent();
        
        //move parent item down
        node.insertItem(0, parent.getItem(whatChildIsThis(leftSib)));
        //move left sib item up
        parent.replaceItem(whatChildIsThis(leftSib), leftSib.removeItem(1));
        
        //hook up children
        node.setChild(0, leftSib.getChild(2));
        leftSib.setChild(2, null);
              
        checkTree();
    }
    
    /**
     * 
     * @param node the node that the right transfer is performed on
     */
    protected void rightTransfer(TFNode node){
        TFNode rightSib = rightSib(node);
        TFNode parent = node.getParent();
        
        //move parent item down
        node.insertItem(0, parent.getItem(whatChildIsThis(node)));
        //move left sib item up
        parent.replaceItem(whatChildIsThis(node), rightSib.removeItem(0));
        
        //hook up children
        node.setChild(1, rightSib.getChild(0));
        rightSib.setChild(0, null);
        
        checkTree();
    }
    
    /**
     * 
     * @param node the node that the left fusion is performed on
     */
    protected void leftFusion(TFNode node){
        
        TFNode parent = node.getParent();
        TFNode leftSib = leftSib(node);
        int wcit = whatChildIsThis(node);
        
        node.insertItem(0, parent.getItem(wcit-1));
        node.insertItem(0, leftSib.getItem(leftSib.getNumItems()-1));
        
        //connect the parent to the children
        node.setChild(0, leftSib.getChild(leftSib.getNumItems()-1));
        node.setChild(1, leftSib.getChild(leftSib.getNumItems()));
        
        //null the old children out
        leftSib.setChild(leftSib.getNumItems()-1, null);
        leftSib.setChild(leftSib.getNumItems(), null);
        
        //connect the children to the parent
        if(node.getChild(0) != null){
            node.getChild(0).setParent(node);
        }
        if(node.getChild(1) != null){
            node.getChild(1).setParent(node);
        }
        
        //null out the item
        leftSib.removeItem(leftSib.getNumItems()-1);
        
        //copy over the child
        parent.setChild(wcit-2, parent.getChild(wcit-1));
        
        //remove the item from the parent node
        parent.removeItem(wcit-1);
        
        checkTree();
        checkUnderflow(parent);
    }
    
    /**
     * 
     * @param node the node that the right fusion is performed on
     */
    protected void rightFusion(TFNode node){
        if(node != root()){
            TFNode parent = node.getParent();
            TFNode rightSib = rightSib(node);
            int wcit = whatChildIsThis(node);

            node.insertItem(0, rightSib.getItem(0));
            node.insertItem(0, parent.getItem(wcit));  

            //connect the parent to the children
            node.setChild(1, rightSib.getChild(0));
            node.setChild(2, rightSib.getChild(1));

            //null the old children out
            rightSib.setChild(0, null);
            rightSib.setChild(1, null);
            
            //connect the children to the parent
            if(node.getChild(1) != null){
                node.getChild(1).setParent(node);
            }
            if(node.getChild(2) != null){
                node.getChild(2).setParent(node);
            }

            //null out the item
            rightSib.removeItem(0);

            //copy over the child
            parent.setChild(wcit+1, parent.getChild(wcit));

            //remove the item from the parent node
            parent.removeItem(wcit);

            checkTree();
            checkUnderflow(parent); 
        }
        else{
            //If it is the root
        }
        
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

        Integer myInt18 = new Integer(97);
        myTree.insertElement(myInt18, myInt18);

        Integer myInt19 = new Integer(51);
        myTree.insertElement(myInt19, myInt19);

        myTree.printAllElements();
        System.out.println("done");

        myTree = new TwoFourTree(myComp);
        final int TEST_SIZE = 15;


        for (int i = 0; i < TEST_SIZE; i++) {
            myTree.insertElement(new Integer(i), new Integer(i));
                    //myTree.printAllElements();
                    myTree.checkTree();
        }
        System.out.println("removing");
        for (int i = 0; i < TEST_SIZE; i++) {
            myTree.removeElement(i);
            //int out = (Integer) myTree.removeElement(new Integer(i));
            //if (out != i) {
               // throw new TwoFourTreeException("main: wrong element removed");
            //}
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
