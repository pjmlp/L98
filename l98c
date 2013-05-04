#!/bin/sh
# l98c : Wrapper script to ease the invocation of the L98 compiler.
# Copyright (C) 1999  Paulo Pinto, Pablo Tavares
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the
# Free Software Foundation, Inc., 59 Temple Place - Suite 330,
# Boston, MA 02111-1307, USA.
#java -jar L98-0.0.1-SNAPSHOT.jar $1 $2

JVM=`which java`
if [ ! -x $JVM ]; then
    echo "Please provide java in the execution path";
    exit 1;
fi

L98C="$L98C_HOME/L98-0.0.1-SNAPSHOT.jar"
if [ ! -e  $L98C ]; then
    echo "Please set the L98C_HOME variable to the directory location";
    exit 1;
fi

$JVM -jar $L98C $1 $2