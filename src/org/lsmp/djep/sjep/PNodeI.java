/* @author rich
 * Created on 17-Dec-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.sjep;
import org.nfunk.jep.*;
/**
 * @author Rich Morris
 * Created on 17-Dec-2004
 */
public interface PNodeI {
	/** Adds a node to this one. **/
	public PNodeI add(PNodeI node)  throws ParseException;
	/** Subtracts the argument from this. **/
	public PNodeI sub(PNodeI node)  throws ParseException;
	/** negates node i.e. -x */
	public PNodeI negate() throws ParseException;
	/** Multiplies this by the argument. **/
	public PNodeI mul(PNodeI node) throws ParseException;
	/** Divides this by the argument. **/
	public PNodeI div(PNodeI node) throws ParseException;
	/** Raise this to the argument. **/
	public PNodeI pow(PNodeI node)  throws ParseException;
	/** inverts node i.e. 1/x */
	public PNodeI invert() throws ParseException;
	/** True is nodes represent the same parse trees */
	public boolean equals(PNodeI node);
	/** Returns positive if this node comes after the argument. */
	public int compareTo(PNodeI fun);
	/** Converts the node to standard Jep format. */
	public Node toNode() throws ParseException;
	/** Produces a string representation of the argument. */
	public String toString();
	/** True if node represents zero */
	public boolean isZero();
	/** True if node represents one */
	public boolean isOne();
	/** complete expansion. (1+x)^2 --> 1+2*x+x^2 */
	public PNodeI expand() throws ParseException; 
}