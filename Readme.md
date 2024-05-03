# Introduction

  This is a compiler for the L98 language, a compiler project developed during the university, based on the first edition of the [Tiger Book](http://www.cs.princeton.edu/~appel/modern/java/).

# Overview

Originally this compiler was developed during the compiler design course, it needed to be cleaned up in a few spots before making it available on GitHub, namely:

- translation of code comments into English;
- conversion of the build procedure into a Maven build file;
- fixing Java 1.1 related bugs no longer accepted in modern JDKs;
- replaced the NASM macros by an Assembly generator backend using AT&T syntax

For more information check the existing [PDF](docs/L98.pdf) documentation.

# Roadmap

Later I upgraded the language version to Java 9, and now decided that it would be a good opportunity to rejuvenate the toy compiler with modern Java, as it gets improved.

Now it also allows for compiling into native code via GraalVM, when using `mvn -Pnative -DskipTests package` as build command.

# License

This code is under the GPL v2 license.