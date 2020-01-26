#!/usr/bin/env python

import string, operator, traceback, sys, getopt, os, time
from types import *
import copy, pickle
import math
import mutex
import numarray.objects as obj;


class CardItemMiner:

	def __init__(self, suit, rank, faceUp, stack, position):
		self.suit = suit
		self.rank = rank
		self.faceUp = faceUp
		self.stack = stack
		self.position = position

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
	]);

print cards[12][Clubs];
