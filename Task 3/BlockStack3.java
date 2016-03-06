package task3;
import common.*;

/**
 * Class BlockStack
 * Implements character block stack and operations upon it.
 *
 * $Revision: 1.4 $
 * $Last Revision Date: 2016/02/01 $
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca;
 * Inspired by an earlier code by Prof. D. Probst

 */
class BlockStack3
{



	//Counter for number of times stack is accessed 
	private int stack_access_counter;

	/**
	 * # of letters in the English alphabet + 2
	 */
	private static final int MAX_SIZE = 28;

	/**
	 * Default stack size
	 */
	private static final int DEFAULT_SIZE = 6;

	/**
	 * Current size of the stack
	 */
	private int iSize = DEFAULT_SIZE;

	/**
	 * Current top of the stack
	 */
	private int iTop  = 3;

	/**
	 * stack[0:5] with four defined values
	 */
	private char acStack[] = new char[] {'a', 'b', 'c', 'd', '$', '$'};

	/**
	 * Default constructor
	 */
	public BlockStack3()
	{
	}

	/**
	 * Supplied size
	 */
	public BlockStack3(final int piSize)
	{


		if(piSize != DEFAULT_SIZE)
		{
			this.acStack = new char[piSize];

			// Fill in with letters of the alphabet and keep
			// 2 free blocks
			for(int i = 0; i < piSize - 2; i++)
				this.acStack[i] = (char)('a' + i);

			this.acStack[piSize - 2] = this.acStack[piSize - 1] = '$';

			this.iTop = piSize - 3;
			this.iSize = piSize;
		}
	}

	/**
	 * Picks a value from the top without modifying the stack
	 * @return top element of the stack, char
	 */
	public char pick() throws EmptyStackException
	{
		stack_access_counter++; //Increment the value by 1 every time stack is accessed
		if(isEmpty()){
			throw new EmptyStackException();
		}
		else
			return this.acStack[this.iTop];
	}

	/**
	 * Returns arbitrary value from the stack array
	 * @return the element, char
	 */
	public char getAt(final int piPosition) throws EmptyStackException
	{
		stack_access_counter++; //Increment the value by 1 every time stack is accessed
		if(isEmpty())
			throw new EmptyStackException();
		else
			return this.acStack[piPosition];
	}

	/**
	 * Standard push operation
	 */
	public void push(final char pcBlock) throws FullStackException
	{
		stack_access_counter++; //Increment the value by 1 every time stack is accessed


		if(isEmpty()){
			this.acStack[0] = 'a';
			this.iTop = 0; //Array starts at index 0
		}
		if (iTop + 1 == iSize){ //If top = array size we have full stack

			throw new FullStackException();

		}
		else
			this.acStack[++this.iTop] = pcBlock;
	}

	/**
	 * Standard pop operation
	 * @return ex-top element of the stack, char
	 */
	public char pop() throws EmptyStackException
	{
		stack_access_counter++; //Increment the value by 1 every time stack is accessed

		if(isEmpty())
			throw new EmptyStackException();
		
		else{
			char cBlock = this.acStack[this.iTop];
			this.acStack[this.iTop--] = '$'; // Leave prev. value undefined
			return cBlock;
		}
	}

	//Defined methods below according the requirements of task1

	/**
	 * Method checks if stack is empty
	 * @return boolean whether stack is empty or not
	 */
	public boolean isEmpty(){
		return (this.iTop == -1);
	}

	/**
	 * Standard Accessor method
	 * @return top of the stack
	 */
	public int getITop(){
		return iTop;
	}

	public int getISize(){
		return iSize;
	}

	/**
	 * Standard Accessor method
	 * @return number of times stack is accessed
	 */
	public int getAccessCounter() {
		return stack_access_counter;
	}
}

// EOF
