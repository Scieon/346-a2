// Import (aka include) some stuff.
package task4;

import common.*;

/**
 * Class BlockManager
 * Implements character block "manager" and does twists with threads.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca;
 * Inspired by previous code by Prof. D. Probst
 *
 * $Revision: 1.5 $
 * $Last Revision Date: 2016/02/01 $

 */
public class BlockManager4
{
	/**
	 * The stack itself
	 */
	private static BlockStack4 soStack = new BlockStack4();

	/**
	 * Number of threads dumping stack
	 */
	private static final int NUM_PROBERS = 4;

	/**
	 * Number of steps they take
	 */
	private static int siThreadSteps = 5;

	/**
	 * For atomicity
	 */
	private static Semaphore mutex = new Semaphore(1);

	/*
	 * For synchronization
	 */

	/**
	 * s1 is to make sure phase I for all is done before any phase II begins
	 */

	/*
	 * Value will be -9 so s1 will increase every time it goes into phase1
	 * Phase 1 is entered 10 times therefore s1 will be 1 after phase 1 is completed
	 * This ensures phase II can only begin after phase I is completed.
	 */
	private static Semaphore s1 = new Semaphore(-9);

	/**
	 * s2 is for use in conjunction with Thread.turnTestAndSet() for phase II proceed
	 * in the thread creation order
	 */
	//private static Semaphore s2 = new Semaphore(...);


	// The main()
	public static void main(String[] argv)
	{
		try
		{
			// Some initial stats...
			System.out.println("Main thread starts executing.");
			System.out.println("Initial value of top = " + soStack.getITop() + ".");
			System.out.println("Initial value of stack top = " + soStack.pick() + ".");
			System.out.println("Main thread will now fork several threads.");

			/*
			 * The birth of threads
			 */
			AcquireBlock ab1 = new AcquireBlock();
			AcquireBlock ab2 = new AcquireBlock();
			AcquireBlock ab3 = new AcquireBlock();

			System.out.println("main(): Three AcquireBlock threads have been created.");

			ReleaseBlock rb1 = new ReleaseBlock();
			ReleaseBlock rb2 = new ReleaseBlock();
			ReleaseBlock rb3 = new ReleaseBlock();

			System.out.println("main(): Three ReleaseBlock threads have been created.");

			// Create an array object first
			CharStackProber	aStackProbers[] = new CharStackProber[NUM_PROBERS];

			// Then the CharStackProber objects
			for(int i = 0; i < NUM_PROBERS; i++)
				aStackProbers[i] = new CharStackProber();

			System.out.println("main(): CharStackProber threads have been created: " + NUM_PROBERS);

			/*
			 * Twist 'em all
			 */
			ab1.start();
			aStackProbers[0].start();
			rb1.start();
			aStackProbers[1].start();
			ab2.start();
			aStackProbers[2].start();
			rb2.start();
			ab3.start();
			aStackProbers[3].start();
			rb3.start();

			System.out.println("main(): All the threads are ready.");

			/*
			 * Wait by here for all forked threads to die
			 */
			ab1.join();
			ab2.join();
			ab3.join();

			rb1.join();
			rb2.join();
			rb3.join();

			for(int i = 0; i < NUM_PROBERS; i++)
				aStackProbers[i].join();

			// Some final stats after all the child threads terminated...
			System.out.println("System terminates normally.");
			System.out.println("Final value of top = " + soStack.getITop() + ".");
			System.out.println("Final value of stack top = " + soStack.pick() + ".");
			System.out.println("Final value of stack top-1 = " + soStack.getAt(soStack.getITop() - 1) + ".");
			System.out.println("Stack access count = " + soStack.getAccessCounter());

			System.exit(0);
		}
		catch(InterruptedException e)
		{
			System.err.println("Caught InterruptedException (internal error): " + e.getMessage());
			e.printStackTrace(System.err);
		}
		catch(Exception e)
		{
			reportException(e);
		}
		finally
		{
			System.exit(1);
		}
	} // main()


	/**
	 * Inner AcquireBlock thread class.
	 */
	static class AcquireBlock extends BaseThread
	{
		/**
		 * A copy of a block returned by pop().
		 * @see BlocStack#pop()
		 */
		private char cCopy;

		public void run()
		{

			//Entering Critical Section
			while(mutex.isLocked())
				mutex.P();
			System.out.println("AcquireBlock thread [TID=" + this.iTID + "] starts executing.");


			phase1();


			try
			{
				System.out.println("AcquireBlock thread [TID=" + this.iTID + "] requests Ms block.");

				this.cCopy = soStack.pop();

				System.out.println
				(
						"AcquireBlock thread [TID=" + this.iTID + "] has obtained Ms block " + this.cCopy +
						" from position " + (soStack.getITop() + 1) + "."
						);


				System.out.println
				(
						"Acq[TID=" + this.iTID + "]: Current value of top = " +
								soStack.getITop() + "."
						);

				System.out.println
				(
						"Acq[TID=" + this.iTID + "]: Current value of stack top = " +
								soStack.pick() + "."
						);
			}
			catch(Exception e)
			{
				reportException(e);
				System.exit(1);
			}

			finally{ //Need finally block in case we catch an exception
				mutex.V(); //Signal that we have left critical section
			}

			s1.V(); //Phase 1 signals that it is done.
			s1.P(); //Phase 2 Must wait for phase 1 to Finish
			phase2(); 
			s1.V(); //Phase 2 signals it is done


			System.out.println("AcquireBlock thread [TID=" + this.iTID + "] terminates.");
		}
	} // class AcquireBlock


	/**
	 * Inner class ReleaseBlock.
	 */
	static class ReleaseBlock extends BaseThread
	{
		/**
		 * Block to be returned. Default is 'a' if the stack is empty.
		 */
		private char cBlock = 'a';

		public void run()
		{
			//Entering Critical Section below
			while(mutex.isLocked())
				mutex.P();

			System.out.println("ReleaseBlock thread [TID=" + this.iTID + "] starts executing.");


			phase1();


			try
			{
				if(soStack.isEmpty() == false)
					this.cBlock = (char)(soStack.pick() + 1);


				System.out.println
				(
						"ReleaseBlock thread [TID=" + this.iTID + "] returns Ms block " + this.cBlock +
						" to position " + (soStack.getITop() + 1) + "."
						);

				soStack.push(this.cBlock);

				System.out.println
				(
						"Rel[TID=" + this.iTID + "]: Current value of top = " +
								soStack.getITop() + "."
						);

				System.out.println
				(
						"Rel[TID=" + this.iTID + "]: Current value of stack top = " +
								soStack.pick() + "."
						);
			}
			catch(Exception e)
			{
				reportException(e);
				System.exit(1);
			}

			finally{
				mutex.V(); //Release control as we have left critical section
			}

			s1.V(); //Phase 1 signals that is done by increasing s1 by 1
			s1.P(); //Phase 2 goes in wait mode, no other activity can occur (until s1 value becomes 1)
			phase2();
			s1.V(); //Phase 2 signals it is done (so other phas2 methods may execute)


			System.out.println("ReleaseBlock thread [TID=" + this.iTID + "] terminates.");
		}
	} // class ReleaseBlock


	/**
	 * Inner class CharStackProber to dump stack contents.
	 */
	static class CharStackProber extends BaseThread
	{
		public void run()
		{
			//Entering Critical Section
			while(mutex.isLocked())
				mutex.P();
			phase1();


			try
			{
				for(int i = 0; i < siThreadSteps; i++)
				{
					System.out.print("Stack Prober [TID=" + this.iTID + "]: Stack state: ");

					// [s] - means ordinay slot of a stack
					// (s) - current top of the stack
					for(int s = 0; s < soStack.getISize(); s++)
						System.out.print
						(
								(s == BlockManager4.soStack.getITop() ? "(" : "[") +
								BlockManager4.soStack.getAt(s) +
								(s == BlockManager4.soStack.getITop() ? ")" : "]")
								);

					System.out.println(".");

				}
			}
			catch(Exception e)
			{
				reportException(e);
				System.exit(1);
			}

			finally{
				mutex.V(); //Release control as we have left critical section
			}

			s1.V(); //Phase 1 signals that is done by increasing s1 by 1
			s1.P(); //Phase 2 goes in wait mode, no other activity can occur (until s1 value becomes 1)
			phase2();
			s1.V(); //Phase 2 signals it is done (so other phas2 methods may execute)
		}
	} // class CharStackProber


	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	private static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
} // class BlockManager

// EOF
