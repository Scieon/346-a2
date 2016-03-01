/**
 * Class BlockStack Implements character block stack and operations upon it.
 * 
 * $Revision: 1.4 $ $Last Revision Date: 2015/02/01 $
 * 
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca; Inspired by an earlier
 *         code by Prof. D. Probst
 */

import common.*;
class BlockStack4 {

	private int accessCounter;

	/**
	 * # of letters in the English alphabet + 2
	 */
	public static final int MAX_SIZE = 28;

	/**
	 * Default stack size
	 */
	public static final int DEFAULT_SIZE = 6;

	/**
	 * Current size of the stack
	 */
	private int iSize = DEFAULT_SIZE; // changed to private

	/**
	 * Current top of the stack
	 */
	private int iTop = 3; // changed to private

	/**
	 * stack[0:5] with four defined values
	 */
	private char acStack[] = new char[] { 'a', 'b', 'c', 'd', '$', '$' }; // changed to private

	/**
	 * Return the size of the stack
	 * @return
	 */
	public int getISize() {
		return iSize;
	}

	/**
	 * Return the top index of the stack
	 * @return
	 */
	public int getITop() {
		return iTop;
	}	
	
	/**
	 * Returns the count of access to the stack
	 * @return
	 */
	public int getAccessCounter() {
		return this.accessCounter;
	}

	/**
	 * Prints the elements of acStack
	 * @return char of each element
	 */	
	public void getAcStack() // new method
	{
		for (int i = 0; i < acStack.length; i++) {
			System.out.print(this.acStack[i] + " ");
		}
	}
	
	/**
	 * Sets the value of the size
	 * @param iSize
	 */
	public void setISize(int iSize) {
		this.iSize = iSize;
	}

	/**
	 * Sets the top index
	 * @param iTop
	 */
	public void setITop(int iTop) {
		this.iTop = iTop;
	}

	/**
	 * Default constructor
	 */
	public BlockStack4() {
	}

	/**
	 * Supplied size
	 */
	public BlockStack4(final int piSize) {

		if (piSize != DEFAULT_SIZE) {
			this.acStack = new char[piSize];

			// Fill in with letters of the alphabet and keep
			// 2 free blocks
			for (int i = 0; i < piSize - 2; i++)
				this.acStack[i] = (char) ('a' + i);

			this.acStack[piSize - 2] = this.acStack[piSize - 1] = '$';

			this.iTop = piSize - 3;
			this.iSize = piSize;
		}
	}

	/**
	 * Picks a value from the top without modifying the stack
	 * 
	 * @return top element of the stack, char
	 */
	public char pick() {
		this.accessCounter++;
		return this.acStack[this.iTop];
	}

	/**
	 * Returns arbitrary value from the stack array
	 * 
	 * @return the element, char
	 */
	public char getAt(final int piPosition) {
		this.accessCounter++;
		return this.acStack[piPosition];
	}

	/**
	 * Standard push operation
	 */
	public void push(final char pcBlock) throws FullStackException {
		this.accessCounter++;
		{
			if (isEmpty()) // modified push to insert 'a' when empty
			{
				this.acStack[0] = 'a';
				this.iTop = 0;
			}
			if (this.getITop() == this.getISize() - 1) {
				throw new FullStackException("Stack full!!!"); 
			} else {
				this.acStack[++this.iTop] = pcBlock;
			}
		}
	}

	/**
	 * Standard pop operation
	 * 
	 * @return ex-top element of the stack, char
	 */
	public char pop() throws EmptyStackException { // implemented exception in
													// case stack becomes empty
		if (isEmpty()) {
			throw new EmptyStackException("Empty stack !!!!"); 
		}

		char cBlock = this.acStack[this.iTop];
		this.acStack[this.iTop--] = '$'; // Leave prev. value undefined
		this.accessCounter++;
		return cBlock;
	}

	public boolean isEmpty() {
		return (this.iTop == -1);
	}


}

// EOF
