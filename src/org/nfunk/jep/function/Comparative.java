/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.nfunk.jep.function;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

/** 
 * Implements the comparative operations <, >, <=, >=, != and ==.
 * Caverts should work where arguments are Double, Complex or String
 * for the last two only != and == work.
 * For other types care might be needed.
 * 
 * @author N Funk and R Morris
 * @since 2.3.1 a bit of a rewrite to make sub classing easier, now allows Complex to be compared to Double i.e. 1+0 i == 1.
 * 
 */
public class Comparative extends PostfixMathCommand
{
	int id;
	double tolerance;
	public static final int LT = 0;
	public static final int GT = 1;
	public static final int LE = 2;
	public static final int GE = 3;
	public static final int NE = 4;
	public static final int EQ = 5;

	public Comparative(int id_in)
	{
		id = id_in;
		numberOfParameters = 2;
		tolerance = 1e-6;
	}
	
	public void run(Stack inStack)	throws ParseException 
	{
		checkStack(inStack);// check the stack
		
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();
		Object res=null;
		switch(id)
		{
		case LT: res = lt(param1,param2); break;
		case GT: res = gt(param1,param2); break;
		case LE: res = le(param1,param2); break;
		case GE: res = ge(param1,param2); break;
		case NE: res = ne(param1,param2); break;
		case EQ: res = eq(param1,param2); break;
		}
		inStack.push(res);
	}
	
	public Object lt(Object param1,Object param2) 	throws ParseException
	{
		if ((param1 instanceof Complex) || (param2 instanceof Complex))
			throw new ParseException("< not defined for complex numbers");
		if ((param1 instanceof Number) && (param2 instanceof Number))
		{
			double x = ((Number)param1).doubleValue();
			double y = ((Number)param2).doubleValue();
			int r = (x<y) ? 1 : 0;
			return new Double(r);
		}
		throw new ParseException("< not defined for object of type "+param1.getClass().getName()+" and "+param1.getClass().getName());
	}
	public Object gt(Object param1,Object param2)	throws ParseException
	{
		if ((param1 instanceof Complex) || (param2 instanceof Complex))
			throw new ParseException("> not defined for complex numbers");
		if ((param1 instanceof Number) && (param2 instanceof Number))
		{
			double x = ((Number)param1).doubleValue();
			double y = ((Number)param2).doubleValue();
			int r = (x>y) ? 1 : 0;
			return new Double(r);
		}
		throw new ParseException("> not defined for object of type "+param1.getClass().getName()+" and "+param1.getClass().getName());
	}
	public Object le(Object param1,Object param2)	throws ParseException
	{
		if ((param1 instanceof Complex) || (param2 instanceof Complex))
			throw new ParseException("<= not defined for complex numbers");
		if ((param1 instanceof Number) && (param2 instanceof Number))
		{
			double x = ((Number)param1).doubleValue();
			double y = ((Number)param2).doubleValue();
			int r = (x<=y) ? 1 : 0;
			return new Double(r);
		}
		throw new ParseException("<= not defined for object of type "+param1.getClass().getName()+" and "+param1.getClass().getName());
	}
	public Object ge(Object param1,Object param2)	throws ParseException
	{
		if ((param1 instanceof Complex) || (param2 instanceof Complex))
			throw new ParseException(">= not defined for complex numbers");
		if ((param1 instanceof Number) && (param2 instanceof Number))
		{
			double x = ((Number)param1).doubleValue();
			double y = ((Number)param2).doubleValue();
			int r = (x>=y) ? 1 : 0;
			return new Double(r);
		}
		throw new ParseException(">= not defined for object of type "+param1.getClass().getName()+" and "+param1.getClass().getName());
	}

	public Object eq(Object param1,Object param2)	throws ParseException
	{
		if ((param1 instanceof Complex) && (param2 instanceof Complex))
		{
			int r = ((Complex)param1).equals((Complex)param2,tolerance) ? 1 : 0;
			return new Double(r);
		}
		if ((param1 instanceof Complex) && (param2 instanceof Double))
		{
			int r = ((Complex)param1).equals(new Complex((Number) param2),tolerance) ? 1 : 0;
			return new Double(r);
		}
		if ((param2 instanceof Complex) && (param1 instanceof Double))
		{
			int r = ((Complex)param2).equals(new Complex((Number) param1),tolerance) ? 1 : 0;
			return new Double(r);
		}
		int r = param1.equals(param2) ? 1 : 0;
		return new Double(r);
	}
	
	public Object ne(Object param1,Object param2)	throws ParseException
	{
		if ((param1 instanceof Complex) && (param2 instanceof Complex))
		{
			int r = ((Complex)param1).equals((Complex)param2,tolerance) ? 0 : 1;
			return new Double(r);
		}
		if ((param1 instanceof Complex) && (param2 instanceof Double))
		{
			int r = ((Complex)param1).equals(new Complex((Number) param2),tolerance) ? 0 : 1;
			return new Double(r);
		}
		if ((param2 instanceof Complex) && (param1 instanceof Double))
		{
			int r = ((Complex)param2).equals(new Complex((Number) param1),tolerance) ? 0 : 1;
			return new Double(r);
		}
		int r = param1.equals(param2) ? 0 : 1;
		return new Double(r);
	}


/*
		if ((param1 instanceof Complex) || (param2 instanceof Complex))
			throw new ParseException(">= not defined for complex numbers");
		if ((param1 instanceof Number) && (param2 instanceof Number))
		{
			double x = ((Number)param1).doubleValue();
			double y = ((Number)param2).doubleValue();
			int r = (x>=y) ? 1 : 0;
			return new Double(r);
		}
		throw new ParseException(">= not defined for object of type "+param1.getClass().getName()+" and "+param1.getClass().getName());
	}
	
		{
			int r;
			
			switch (id)
			{
				case NE:
					r = ((Complex)param1).equals((Complex)param2,tolerance) ? 0 : 1;
					break;
				case EQ:
					r = ((Complex)param1).equals((Complex)param2,tolerance) ? 1 : 0;
					break;
				default:
					throw new ParseException("Relational operator type error");
			}
			
			inStack.push(new Double(r));//push the result on the inStack
		}
		else if ((param1 instanceof Number) && (param2 instanceof Number))
		{
			double x = ((Number)param1).doubleValue();
			double y = ((Number)param2).doubleValue();
			int r;
			
			switch (id)
			{
				case LT:
					r = (x<y) ? 1 : 0;
					break;
				case GT:
					r = (x>y) ? 1 : 0;
					break;
				case LE:
					r = (x<=y) ? 1 : 0;
					break;
				case GE:
					r = (x>=y) ? 1 : 0;
					break;
				case NE:
					r = (x!=y) ? 1 : 0;
					break;
				case EQ:
					r = (x==y) ? 1 : 0;
					break;
				default:
					throw new ParseException("Unknown relational operator");
			}
			
			inStack.push(new Double(r));//push the result on the inStack
		} 
		else if ((param1 instanceof String) && (param2 instanceof String))
		{
			int r;
			
			switch (id)
			{
				case NE:
					r = ((String)param1).equals((String)param2) ? 0 : 1;
					break;
				case EQ:
					r = ((String)param1).equals((String)param2) ? 1 : 0;
					break;
				default:
					throw new ParseException("Relational operator type error");
			}
			
			inStack.push(new Double(r));//push the result on the inStack
		} else
		{
			throw new ParseException("Invalid parameter type");
		}
		
		
		return;
	}
	*/
	/**
	 * Returns the tolerance used for comparing complex numbers
	 */
	public double getTolerance() {
		return tolerance;
	}

	/**
	 * Sets the tolerance used for comparing complex numbers
	 * @param d
	 */
	public void setTolerance(double d) {
		tolerance = d;
	}

}
