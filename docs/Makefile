# Makefile for the documentation

all: ps pdf

ps : L98.dvi
	dvips L98.dvi -o L98.ps


pdf : ps	
	ps2pdf L98.ps

L98.dvi : L98.tex
	#Generate documentation
	latex L98.tex
	#Rerun to get correct references
	latex L98.tex

clean:
	rm -f L98.log L98.aux L98.ps L98.pdf L98.dvi

