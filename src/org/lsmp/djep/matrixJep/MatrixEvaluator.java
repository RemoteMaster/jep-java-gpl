/* @author rich
 * Created on 30-Oct-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.nfunk.jep.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.lsmp.djep.vectorJep.function.*;
import org.lsmp.djep.vectorJep.values.*;
import org.nfunk.jep.function.*;
import java.util.Stack;

/**
 * This visitor evaluates a the tree representing the equation.
 * 
 * @author Rich Morris
 * Created on 30-Oct-2003
 */
public class MatrixEvaluator implements ParserVisitor
{
//	private DimensionCalculator dimCalc;
	private Stack stack = new Stack();
	private MatrixJep mjep;
	public MatrixValueI evaluate(MatrixNodeI node,MatrixJep mjep) throws ParseException
	{
		this.mjep=mjep;
		return (MatrixValueI) node.jjtAccept(this,null);
	}

	public Object visit(SimpleNode node, Object data)	{ return null;	}
	public Object visit(ASTStart node, Object data)	{ return null;	}

	/** constants **/
	public Object visit(ASTConstant node, Object data)
	{
		return ((ASTMConstant) node).getMValue();
	}
	/** multidimensions differentiable variables */
	public Object visit(ASTVarNode node, Object data) throws ParseException
	{
		MatrixVariableI var = (MatrixVariableI) node.getVar();
		if(var.hasValidValue())
			return var.getMValue();
		else
		{
			MatrixValueI res = (MatrixValueI) var.getEquation().jjtAccept(this,data);
			MatrixValueI val = var.getMValue();
			val.setEles(res);
			var.setValidValue(true);
			return res;
		}
	}
	
	/** other functions **/
	public Object visit(ASTFunNode node, Object data) throws ParseException
	{
		MatrixNodeI mnode = (MatrixNodeI) node;
		PostfixMathCommandI pfmc = node.getPFMC(); 
		if(pfmc instanceof MatrixSpecialEvaluationI)
		{
			MatrixSpecialEvaluationI se = (MatrixSpecialEvaluationI) pfmc;
			return se.evaluate(mnode,this,mjep);
		}
//		else if (pfmc instanceof SpecialEvaluationI) {
//			((SpecialEvaluationI) node.getPFMC()).evaluate(
//				node,data,this,stack,mjep.getSymbolTable());
//			mnode.getMValue().setEle(0,stack.peek());
//		}
		else if(pfmc instanceof BinaryOperatorI)
		{
			BinaryOperatorI bin = (BinaryOperatorI) node.getOperator().getPFMC();
			MatrixValueI lhsval = (MatrixValueI) node.jjtGetChild(0).jjtAccept(this,data);
			MatrixValueI rhsval = (MatrixValueI) node.jjtGetChild(1).jjtAccept(this,data);
			return bin.calcValue(mnode.getMValue(),lhsval,rhsval);
		}
		else if(pfmc instanceof UnaryOperatorI)
		{
			UnaryOperatorI uni = (UnaryOperatorI) node.getOperator().getPFMC();
			return uni.calcValue(
				mnode.getMValue(),
				(MatrixValueI) node.jjtGetChild(0).jjtAccept(this,data));
		}
		else if(pfmc instanceof NaryOperatorI)
		{
			NaryOperatorI uni = (NaryOperatorI) node.getOperator().getPFMC();
			MatrixValueI results[] = new MatrixValueI[node.jjtGetNumChildren()];
			for(int i=0;i<results.length;++i)
				results[i] = (MatrixValueI) node.jjtGetChild(i).jjtAccept(this,data);
			return uni.calcValue(mnode.getMValue(),results);
		}
		// not a clever op use old style call	
		int num = node.jjtGetNumChildren();
		for(int i=0;i<num;++i)
		{
			MatrixValueI vec = (MatrixValueI) node.jjtGetChild(i).jjtAccept(this,data);
			stack.push(vec.getEle(0));
		}
		pfmc.setCurNumberOfParameters(num);
		pfmc.run(stack);
		mnode.getMValue().setEle(0,stack.pop());
		return mnode.getMValue();
	}
}