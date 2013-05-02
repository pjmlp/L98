/* ASTDeclFunc.java : AST node for function declarations
 * Copyright (C) 1999  Paulo Pinto, Pablo Tavares
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.progtools.l98.ast;
import java.io.IOException;
import java.util.Enumeration;

import org.progtools.l98.table.Attributes;
import org.progtools.l98.CompilerError;
import org.progtools.l98.access.Label;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.generator.LabelGenerator;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeArg;
import org.progtools.l98.type.TypeFunc;
import org.progtools.l98.util.List;



/**
 * AST node for function declarations
 */
public class ASTDeclFunc extends ASTDecl {
  /**
   * Function's name.
   */
  private String m_id;

  /**
   * Argument list.
   */
  private ASTArgList m_args;
   
  /**
   * Statements bound to this function.
   */
  private ASTStat m_stat;
   
  /**
   * Function's type.
   */
  private Type m_type;

 /**
  * @param line where the expression was found.
  * @param id   function's name.
  * @param args list of arguments.
  * @param type return type.
  * @param stat function body.
  */
  public ASTDeclFunc (int line, String id, ASTArgList args, Type type, ASTStat stat) {
    super (line);
    m_id = id;
    m_args = args;
    m_type = type;
    m_stat = stat;
  }

  /**
   * @return the function's name.
   */
  public String getId () { return m_id; }

  /**
   * @return argument's list.
   */
  public ASTArgList getArgs () { return m_args; }
   
   /**
    * @return the function's type.
    */
  public Type getType () { return m_type; }   

  /**
   * @return function's statements
   */
  public ASTStat getStat () { return m_stat; }

  /**
   * @return number of 32 bit slots required to store the variable.
   */
  public int alloc () { return 0; }

  /**
   * Adds this function definition to the given environment.
   * @param label function entry point.
   * @param env   current environment.
   * @param nesting the level where the function was declared.
   * @return function's type.
   */
  private TypeFunc addFuncToEnv (String label, Environ env, int nesting) {
    Enumeration iter = m_args.elements ();
    List typeList = new List ();
    ASTArg arg;
    TypeArg argType;

    
    // makes this function part of the current environment.
    while (iter.hasMoreElements ()) {
      arg = (ASTArg) iter.nextElement ();
      argType = new TypeArg (arg instanceof ASTArgVar, arg.getType ());
       
      typeList.pushBack (argType);
    }

    TypeFunc funcType = new TypeFunc (typeList, m_type, - (m_args.length () + 1));
    Label funcLabel = new Label (nesting, label);
    env.update (m_id, new Attributes (funcType, funcLabel, false, false));
     
    return funcType;
  }
  
   /**
   * Semantic analysis and code generation.
   * @param env current environment.
   * @param err used for error messages.
   * @param gen code generator.
   * @param nesting current static lexical level.
   * @param index Pnext slot available for variables.
   * @return the next available slot for the following instructions.
   */   
  public int transverse (Environ env, CompilerError err, CodeGenerator gen, int nesting, int index) {
    try {
      String funcLabel = LabelGenerator.getLabel ();
     
      // Adds the function to the sub-routines hierarchy
      env.update ("$LastSub$", addFuncToEnv (funcLabel, env, nesting));

      // generates the function's entry point
      gen.insLabel (funcLabel);
      gen.comment ("start of function  " + m_id);
      int entriesToAlloc = m_stat.alloc ();
      if (entriesToAlloc > 0)
        gen.alloc (entriesToAlloc);
      
      // adds the parameters to the environment
      m_args.fillEnviron (env, nesting + 1);
      if (!m_stat.transverse (env, err, gen, nesting + 1, 1))
        err.message (getLine () + ": Function without return");
      else
        gen.comment ("end of function " + m_id);
   
      m_args.clearEnviron (env);

      // Removes the function from the $LastSub$ hiearchy
      env.removeLast ();
      
   }
   catch (IOException e) {
     err.message ("Error while generating code");
   }

   return index;
  }
}
