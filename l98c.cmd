@echo off
rem l98c.cmd : Wrapper script to ease the invocation of the L98 compiler on Windows platforms.
rem Copyright (C) 2013  Paulo Pinto
rem
rem This library is free software; you can redistribute it and/or
rem modify it under the terms of the GNU Lesser General Public
rem License as published by the Free Software Foundation; either
rem version 2 of the License, or (at your option) any later version.
rem
rem This library is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
rem Lesser General Public License for more details.
rem
rem You should have received a copy of the GNU Lesser General Public
rem License along with this library; if not, write to the
rem Free Software Foundation, Inc., 59 Temple Place - Suite 330,
rem Boston, MA 02111-1307, USA.

setlocal

for /f %%i in ('where java') do set JVM=%%i
if not exist %JVM% goto NOJVM

set L98C="%L98C_HOME%\L98-0.0.1-SNAPSHOT.jar"
if not exist %L98C% goto NOL98C

goto COMPILE

:NOJVM
    echo Please provide java in the execution path
    exit /b 1

:NOL98C
    echo Please set the L98C_HOME variable to the directory location
    exit /b 1
    
:COMPILE
%JVM% -jar %L98C% %1 %2 %3 %4
