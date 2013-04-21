/* l98lib.c : Contains the runtime library code.
 * Copyright (C) 1999  Paulo Pinto
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

#include <stdio.h>
#include <string.h>

// Forward declarations to allow for a common naming.
void print_int(int i) asm ("print_int");
int read_int(void) asm ("read_int");
void print_bool(int flag) asm ("print_bool");
int read_bool(void) asm ("read_bool");
void print_ln(void) asm ("print_ln");

/**
 * Implementation of L98 print_int funtion.
 * @param i The number to display.
 */
void print_int(int i)
{
    printf("%d", i);
}

/**
 * Implementation of L98 read_int funtion.
 * @return number read from standard input.
 */
int read_int(void)
{
    int num;
    scanf("%d", &num);
    return num;
}

/**
 * Implementation of L98 print_bool funtion.
 * @param flag The boolean value to display as string.
 */
void print_bool(int flag)
{
    printf(flag ? "TRUE" : "FALSE");
}

/**
 * Implementation of L98 read_bool funtion.
 * @return boolean value read from standard input. 0 is false and 1 true
 */
int read_bool(void)
{
    char buff[10];
    fgets(buff, sizeof buff, stdin);
    return (strcasecmp(buff, "true") == 0);
}

/**
 * Implementation of L98 print_ln funtion.
 */
void print_ln()
{
    putchar('\n');
}

// Assembly entry point
extern void P_START() asm ("P_START");


int main(void)
{
    P_START();
    return 0;
}
