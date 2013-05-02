/* EvironFactory.java : Creates an pre-configured environment for L98.
 * Copyright (C) 2013  Paulo Pinto
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
package org.progtools.l98.table;

import org.progtools.l98.Attributes;
import org.progtools.l98.access.Access;
import org.progtools.l98.access.Builtin;
import org.progtools.l98.generator.PreDefinedRoutines;
import org.progtools.l98.type.Type;
import org.progtools.l98.type.TypeArg;
import org.progtools.l98.type.TypeBool;
import org.progtools.l98.type.TypeFunc;
import org.progtools.l98.type.TypeInt;
import org.progtools.l98.type.TypeProc;
import org.progtools.l98.util.List;

/**
 * Creates an pre-configured environment for L98.
 */
public final class EvironFactory {
    public static Environ getEnvironment() {
        Environ env = new Environ (); 
        List args;
        Type subType;
        Attributes attr;
        Access subAccess;

        // Creates the readint function
        subType = new TypeFunc (new List (), new TypeInt (), 1);
        subAccess = new Builtin (PreDefinedRoutines.READINT);
        attr = new Attributes (subType, subAccess, false, false);
        env.update ("readint", attr);


        // Creates the readbool function
        subType = new TypeFunc (new List (), new TypeBool (), 1);
        subAccess = new Builtin (PreDefinedRoutines.READBOOL);
        attr = new Attributes (subType, subAccess, false, false);
        env.update ("readbool", attr);

        // Creates the printint procedure
        args = new List ();
        args.pushBack (new TypeArg (false, new TypeInt ()));

        subType = new TypeProc (args);
        subAccess = new Builtin (PreDefinedRoutines.PRINTINT);
        attr = new Attributes (subType, subAccess, false, false);
        env.update ("printint", attr);

        // Creates the printbool procedure
        args = new List ();
        args.pushBack (new TypeArg (false, new TypeBool ()));

        subType = new TypeProc (args);
        subAccess = new Builtin (PreDefinedRoutines.PRINTBOOL);
        attr = new Attributes (subType, subAccess, false, false);
        env.update ("printbool", attr);

        // Creates the println procedure
        subType = new TypeProc (new List ());
        subAccess = new Builtin (PreDefinedRoutines.PRINTLN);
        attr = new Attributes (subType, subAccess, false, false);
        env.update ("println", attr);

        return env;
    }
}
