/* ASTDeclProc.java : AST node for procedure declarations
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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.progtools.l98.table.Attributes;
import org.progtools.l98.CompilerError;
import org.progtools.l98.access.Label;
import org.progtools.l98.generator.CodeGenerator;
import org.progtools.l98.generator.LabelGenerator;
import org.progtools.l98.table.Environ;
import org.progtools.l98.type.TypeArg;
import org.progtools.l98.type.TypeProc;


/**
 * AST node for procedure declarations
 */
public class ASTDeclProc extends ASTDecl {
  /**
   * Procedure's name.
   */
  private String m_id;

  /**
   * Argument list.
   */
  private ASTArgList m_args;
   
  /**
   * Statements bound to this procedure.
   */
  private ASTStat m_stat;

 /**
  * @param line where the expression was found.
  * @param id   function's name.
  * @param args list of arguments.
  * @param stat function body.
  */
  public ASTDeclProc (int line, String id, ASTArgList args, ASTStat stat) {
    super (line);
    m_id = id;
    m_args = args;
    m_stat = stat;
  }

  /**
   * @return the procedure's name.
   */
  public String getId () { return m_id; }

  /**
   * @return argument's list.
   */
  public ASTArgList getArgs () { return m_args; }
   
  /**
   * @return procedure body
   */
  public ASTStat getStat () { return m_stat; }

  /**
   * @return number of 32 bit slots required to store the variable.
   */
  public int alloc () { return 0; }

   
  /**
   * Adds this procedure definition to the given environment.
   * @param label function entry point.
   * @param env   current environment.
   * @param nesting the level where the function was declared.
   * @return function's type.
   */
  private TypeProc addProcToEnv (String label, Environ env, int nesting) {
    Iterator<ASTArg> iter = m_args.elements ();
    Deque<TypeArg> typeList = new LinkedList<> ();
    ASTArg arg;
    TypeArg argType;

    
    // Prepares the argument list
    while (iter.hasNext()) {
      arg = (ASTArg) iter.next ();
      argType = new TypeArg (arg instanceof ASTArgVar, arg.getType ());
       
      typeList.add (argType);
    }

    // Adds the procedure to the environment
    TypeProc procType = new TypeProc (typeList);
    Label procLabel = new Label (nesting, label);
    env.update (m_id, new Attributes (procType, procLabel, false, false));
     
    return procType;
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
      String procLabel = LabelGenerator.getLabel ();
     
      // Adds the procedure to the subroutines hierachy
      env.update ("$LastSub$", addProcToEnv (procLabel, env, nesting));

      // Generate the procedure's entry point
      gen.insLabel (procLabel);
      gen.comment ("starting procedure  " + m_id);
      int entriesToAlloc = m_stat.alloc ();
      if (entriesToAlloc > 0)
        gen.alloc (entriesToAlloc);
      
      // Adds the parameters information to the environment and
      // generate the body code.
      m_args.fillEnviron (env, nesting + 1);
      m_stat.transverse (env, err, gen, nesting + 1, 1);
      m_args.clearEnviron (env);

      // Generate the procedure's exit point
      gen.ret ();
      gen.comment ("end of procedure  " + m_id);   

      // Retira o procediento da hieraquia $LastSub$
      env.removeLast ();
   }
   catch (IOException e) {
     err.message ("Error while generating code");
   }

   return index;
  }
}

