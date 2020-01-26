#!/usr/bin/env python

# Author John Carlson
# Copyright 2004.  Do not distribute without permission
#	Proposed SMARTpython extension
#	My first python program


# class Log:
#	This is the class that keeps logs of what is happening
#	The state of the game is recorded through the use of the write()
#	method.  You may choose to write the state to a file, or
# 	process it in memory.  This is where I see the majority
#	of the Version Space Algebra taking place to determine
#	what algorithm is being used to change the state of the game.
#	
# class CardItemMiner:
#	The basic object of the state.  Contains suit, rank, visibility
#	stack and position within the stack
# class PyGame:
#	The main log parser
# class StackMiner:
#	Used to be stack of cards.  Now is convenience functions for
#	dealing with the cards array

import string, operator, traceback, sys, getopt, os, time
from types import *
import copy, pickle
import math
import mutex
import re
import numarray.objects as obj

global log


Hearts = 0
Spades = 1
Diamonds = 2
Clubs = 3

Ace = 1
Two = 2
Three = 3
Four = 4
Five = 5
Six = 6
Seven = 7
Eight = 8
Nine = 9
Ten = 10
Jack = 11
Queen = 12
King = 13

cards = obj.array([
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	],
	[
	0,
	0,
	0,
	0
	]
	])

class Log:
	def __init__(self):
		fwstr = "logout.txt"
		self.fw = file(fwstr, 'w')
		fw2str = "stateout.txt"
		self.fw2 = file(fw2str, 'w')
		self.stamp = 0
# stuff from Log
	def writelog(self, msg):
			#String out = stamp + "|" + msg + "\n"
			out = msg + "\n"
			#stamp = stamp + 1
			sys.stdout.write(out)
			self.fw.write(out)
			self.fw.flush()
	def writeattr(self, irank, isuit, name, value):
		self.fw2.write("cards[")
		self.fw2.write(str(irank-1))
		self.fw2.write("][")
		self.fw2.write(str(isuit))
		self.fw2.write("].")
		self.fw2.write(name)
		self.fw2.write("=")
		self.fw2.write(str(value))
		self.fw2.write("\n")

# Here is where we record state changes, the operations done, and the program counter
	def write(self, pc, op, cards):
		self.fw2.write("--------------------------------------------------------------------------------\n")
		self.fw2.write("PC=" + pc + ";OP=" + op + ";\n")
		isuit = Hearts
		while isuit <= Clubs:
			irank = Ace
			while irank <= King:
				cim = cards[irank-1][isuit]
				if cim:
					self.writeattr(irank, isuit, "rank", cim.rank)
					self.writeattr(irank, isuit, "suit", cim.suit)
					self.writeattr(irank, isuit, "faceUp", cim.faceUp)
					self.writeattr(irank, isuit, "stack", cim.stack)
					self.writeattr(irank, isuit, "position", cim.position)
				else:
					self.writeattr(irank, isuit, "rank", 0)
					self.writeattr(irank, isuit, "suit", -1)
					self.writeattr(irank, isuit, "faceUp", 0)
					self.writeattr(irank, isuit, "stack", -1)
					self.writeattr(irank, isuit, "position", -1)
				irank = irank + 1
			isuit = isuit + 1
		self.fw2.flush()


log = Log()

	


class CardItemMiner:
	def __init__(self, suit, rank, faceUp, stack, position):
		global cards

		self.suit = suit
		log.write("1", "INIT1", cards)
		self.rank = rank
		log.write("2", "INIT2", cards)
		self.faceUp = faceUp
		log.write("3", "INIT3", cards)
		self.stack = stack
		log.write("4", "INIT4", cards)
		self.position = position
		log.write("5", "INIT5", cards)

	def setFaceUp(self, faceUp):
		global cards
		self.faceUp = faceUp
		if faceUp:
			log.write("12", "VISIBLE", cards)
		else:
			log.write("13", "INVISIBLE", cards)

	def setRank(self, rank):
		global cards
		self.rank = self.convertRankFromString(rank)
		log.write("14", "CHANGE RANK", cards)

	def setSuit(self, suit):
		global cards
		self.suit = self.convertSuitFromString(suit)
		log.write("15", "CHANGE SUIT", cards)

	def convertRankFromString(self, irank):
		if irank == "Ace":
			return Ace
		elif irank == "Two":
			return Two
		elif irank == "Three":
			return Three
		elif irank == "Four":
			return Four
		elif irank == "Five":
			return Five
		elif irank == "Six":
			return Six
		elif irank == "Seven":
			return Seven
		elif irank == "Eight":
			return Eight
		elif irank == "Nine":
			return Nine
		elif irank == "Ten":
			return Ten
		elif irank == "Jack":
			return Jack
		elif irank == "Queen":
			return Queen
		elif irank == "King":
			return King
		return 0

	def convertRankFromInt(self, irank):
		if irank == Ace:
			return "Ace"
		elif irank == Two:
			return "Two"
		elif irank == Three:
			return "Three"
		elif irank == Four:
			return "Four"
		elif irank == Five:
			return "Five"
		elif irank == Six:
			return "Six"
		elif irank == Seven:
			return "Seven"
		elif irank == Eight:
			return "Eight"
		elif irank == Nine:
			return "Nine"
		elif irank == Ten:
			return "Ten"
		elif irank == Jack:
			return "Jack"
		elif irank == Queen:
			return "Queen"
		if irank == King:
			return "King"
		return None

	def convertSuitFromString(self, xsuit):
		if xsuit == "Hearts":
			return Hearts
		elif xsuit == "Spades":
			return Spades
		elif xsuit == "Diamonds":
			return Diamonds
		elif xsuit == "Clubs":
			return Clubs
		else:
			return -1

	def convertSuitFromInt(self, isuit):
		if isuit == Hearts:
			return "Hearts"
		elif isuit == Spades:
			return "Spades"
		elif isuit == Diamonds:
			return "Diamonds"
		elif isuit == Clubs:
			return "Clubs"
		return None




cards = obj.array([
	[
	CardItemMiner(Hearts, Ace, 0, 0, 0),
	CardItemMiner(Spades, Ace, 0, 0, 1),
	CardItemMiner(Diamonds, Ace, 0, 0, 2),
	CardItemMiner(Clubs, Ace, 0, 0, 3)
	],
	[
	CardItemMiner(Hearts, Two, 0, 0, 4),
	CardItemMiner(Spades, Two, 0, 0, 5),
	CardItemMiner(Diamonds, Two, 0, 0, 6),
	CardItemMiner(Clubs, Two, 0, 0, 7)
	],
	[
	CardItemMiner(Hearts, Three, 0, 0, 8),
	CardItemMiner(Spades, Three, 0, 0, 9),
	CardItemMiner(Diamonds, Three, 0, 0, 10),
	CardItemMiner(Clubs, Three, 0, 0, 11)
	],
	[
	CardItemMiner(Hearts, Four, 0, 0, 12),
	CardItemMiner(Spades, Four, 0, 0, 13),
	CardItemMiner(Diamonds, Four, 0, 0, 14),
	CardItemMiner(Clubs, Four, 0, 0, 15)
	],
	[
	CardItemMiner(Hearts, Five, 0, 0, 16),
	CardItemMiner(Spades, Five, 0, 0, 17),
	CardItemMiner(Diamonds, Five, 0, 0, 18),
	CardItemMiner(Clubs, Five, 0, 0, 19)
	],
	[
	CardItemMiner(Hearts, Six, 0, 0, 20),
	CardItemMiner(Spades, Six, 0, 0, 21),
	CardItemMiner(Diamonds, Six, 0, 0, 22),
	CardItemMiner(Clubs, Six, 0, 0, 23)
	],
	[
	CardItemMiner(Hearts, Seven, 0, 0, 24),
	CardItemMiner(Spades, Seven, 0, 0, 25),
	CardItemMiner(Diamonds, Seven, 0, 0, 26),
	CardItemMiner(Clubs, Seven, 0, 0, 27)
	],
	[
	CardItemMiner(Hearts, Eight, 0, 0, 28),
	CardItemMiner(Spades, Eight, 0, 0, 29),
	CardItemMiner(Diamonds, Eight, 0, 0, 30),
	CardItemMiner(Clubs, Eight, 0, 0, 31)
	],
	[
	CardItemMiner(Hearts, Nine, 0, 0, 32),
	CardItemMiner(Spades, Nine, 0, 0, 33),
	CardItemMiner(Diamonds, Nine, 0, 0, 34),
	CardItemMiner(Clubs, Nine, 0, 0, 35)
	],
	[
	CardItemMiner(Hearts, Ten, 0, 0, 36),
	CardItemMiner(Spades, Ten, 0, 0, 37),
	CardItemMiner(Diamonds, Ten, 0, 0, 38),
	CardItemMiner(Clubs, Ten, 0, 0, 39)
	],
	[
	CardItemMiner(Hearts, Jack, 0, 0, 40),
	CardItemMiner(Spades, Jack, 0, 0, 41),
	CardItemMiner(Diamonds, Jack, 0, 0, 42),
	CardItemMiner(Clubs, Jack, 0, 0, 43)
	],
	[
	CardItemMiner(Hearts, Queen, 0, 0, 44),
	CardItemMiner(Spades, Queen, 0, 0, 45),
	CardItemMiner(Diamonds, Queen, 0, 0, 46),
	CardItemMiner(Clubs, Queen, 0, 0, 47)
	],
	[
	CardItemMiner(Hearts, King, 0, 0, 48),
	CardItemMiner(Spades, King, 0, 0, 49),
	CardItemMiner(Diamonds, King, 0, 0, 50),
	CardItemMiner(Clubs, King, 0, 0, 51)
	]
	])

class PyGame:
	def __init__(self):
		self.stacks = []
	def main(self):
		global cards
		line = sys.stdin.readline()
		linearray = re.split('\n', line)
		line = linearray[0]
		l = 0
		while line:
			v = re.split('\|', line)
			command = v[0]
			stack = v[1]
			if command == "PICK":
				position = v[2]
				rank = v[3]
				suit = v[4]
				i = 0
				for sm in self.stacks:
					if sm and sm.stack_no == stack:
						cim = sm.remove(int(position))
						log.writelog("PICK|" + stack + "|" + position + "|" + cim.convertRankFromInt(cim.rank) + "|" + cim.convertSuitFromInt(cim.suit))
				
			if command == "PLAY":
				position = v[2]
				rank = v[3]
				suit = v[4]

				# find the card in the stacks
				cim = cards[cards[0][0].convertRankFromString(rank)-1][cards[0][0].convertSuitFromString(suit)]
				i = 0
				for sm in self.stacks:
					if sm and sm.stack_no == stack:
						sm.insertElementAt(cim, int(position))
						log.writelog("PLAY|" + stack + "|" + position + "|" + cim.convertRankFromInt(cim.rank) + "|" + cim.convertSuitFromInt(cim.suit))

			if command == "INVISIBLE":
				position = v[2]
				rank = v[3]
				suit = v[4]
				i = 0
				for sm in self.stacks:
					if sm.stack_no == stack:
						cim = sm.elementAt(int(position))
						cim.setFaceUp(0)
						cim.setSuit(suit)
						cim.setRank(rank)
						log.writelog("INVISIBLE|" + stack + "|" + position + "|" + rank + "|" + suit)
			if command == "VISIBLE":
				position = v[2]
				rank = v[3]
				suit = v[4]
				i = 0
				for sm in self.stacks:
					if sm and  sm.stack_no == stack:
						cim = sm.elementAt(int(position))
						cim.setFaceUp(1)
						cim.setSuit(suit)
						cim.setRank(rank)
						log.writelog("VISIBLE|" + stack + "|" + position + "|" + rank + "|" + suit)
			if command == "NEWSTACK":
				x = v[2]
				y = v[3]
				sm = StackMiner(stack, x, y)
				self.stacks.append(sm)
				log.writelog("NEWSTACK|" + stack + "|" + x + "|" + y)
			if command == "DELSTACK":
				i = 0
				for sm in self.stacks:
					if sm and  sm.stack_no == stack:
						log.writelog("DELSTACK|" + stack)
					i = i + 1
			l = l + 1
			line = sys.stdin.readline()
			linearray = re.split('\n', line)
			line = linearray[0]


class StackMiner:
	def __init__(self, stack_no, x, y):
		self.stack_no = stack_no
		self.x = x
		self.y = y
		self.stno = int(stack_no)

	def elementAt(self, i):
		isuit = Hearts
		while isuit <= Clubs:
			irank = Ace
			while irank <= King:
				cim = cards[irank-1][isuit]
				if cim.stack == self.stno:
					if cim.position ==  i:
						icim = cim
				irank = irank + 1
			isuit = isuit + 1
		return icim

	def remove(self, i):
		icim = self.elementAt(i)
		isuit = Hearts
		while isuit <= Clubs:
			irank = Ace
			while irank <= King:
				cim = cards[irank-1][isuit]
				if cim.stack == self.stno:
					if cim.position > i:
						cim.position = cim.position - 1
						log.write("9", "MOVE--", cards)
				irank = irank + 1
			isuit = isuit + 1

		if icim:
			icim.stack = -1
			log.write("10", "REMOVE FROM STACK", cards)
			icim.position = -1
			log.write("11", "REMOVE FROM POSITION", cards)
		return icim
	def insertElementAt(self, icim, pos):
		isuit = Hearts
		while isuit <= Clubs:
			irank = Ace
			while irank <= King:
				cim = cards[irank-1][isuit]
				if cim.stack == self.stno:
					if cim.position >= pos:
						cim.position = cim.position + 1
						log.write("6", "MOVE++", cards)
				irank = irank + 1
			isuit = isuit + 1
		icim.stack = self.stno
		log.write("7", "ADD TO STACK", cards)
		icim.position = pos
		log.write("8", "ADD POSITION", cards)

game = PyGame()
game.main()
