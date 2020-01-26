#!/usr/bin/python           # This is client.py file

import socket               # Import socket module
import sys
from threading import Thread
import threading
from datetime import datetime

s = socket.socket()         # Create a socket object
host, aliaslist, lan_ip = socket.gethostbyname_ex(socket.gethostname()) # Get local machine name
port = 8180                 # Reserve a port for your service.
nick = "malvok"

class Message:
	def __init__(self):
		self.sequenceno = 0
		self.clientno = 0
		self.clientno += 1
		self.rec = {}
		dt = datetime.now()
		self.fr = ":".join(lan_ip)+":"+str(dt.microsecond+self.clientno)
		self.rec["*"] = "*"

	def generate(self, nick, message, to):
		self.sequenceno += 1
		error = 0
		language = "en"
		print "}{".join(to)
		dt = datetime.now()
		return "{"+"{"+("}{".join(to))+"}"+"}"+"{"+self.fr+"}"+"{"+str(dt.microsecond)+"}"+"{"+str(self.sequenceno)+"}"+"{"+str(error)+"}"+"{"+language+"}"+"{"+nick+"}"+message+"\n"

	def parse(self, line):
		tb = line.find("{")
                toe = tb
                tob = line.find("{", toe+1)
                while (toe+1 == tob):
                    toe = line.find("}", tob+1)
                    to = line[tob+1:toe]
                    m.rec[to] = to
                    tob = line.find("{", toe)
                te = line.find("}", toe+1)
                #from
                fb = line.find("{", te+1)
                fe = line.find("}", fb+1)
                # timestamp
                sb = line.find("{", fe+1)
                se = line.find("}", sb+1)
                # sequence no
                qb = line.find("{", se+1)
                qe = line.find("}", qb+1)
                # error
                eb = line.find("{", qe+1)
                ee = line.find("}", eb+1)
                # language
                lb = line.find("{", ee+1)
                le = line.find("}", lb+1)
                # nick
                nb = line.find("{", le+1)
                ne = line.find("}", nb+1)
                # TODO handle nicks with braces in them
                if (fb >= 0 and fe >= 0):
                    m.fr = line[fb+1:fe]
                if (sb >= 0 and se >= 0):
                    m.timestamp = long(line[sb+1:se])
                if (qb >= 0 and qe >= 0):
                    m.sequenceno = long(line[qb+1:qe])
                if (eb >= 0 and ee >= 0):
                    m.error = line[eb+1:ee]
                if (lb >= 0 and le >= 0):
                    m.language = line[lb+1:le]
                if (nb >= 0 and ne >= 0):
                    m.nick = line[nb+1:ne]
                if (ne >= 0):
                    m.message = line[ne+1:]
		return m


s.connect((host, port))
m = Message()
stop = threading.Event()

def readsocket(s, stop):
	while(not stop.is_set()):
		data = s.recv(1024)
		msg = m.parse(data)
		if msg.message.startswith("/fight"):
			
			s.send(m.generate(nick, "Ah, a brave soul!", {msg.fr : msg.fr}))
			s.send(m.generate(nick, "You die!", {msg.fr : msg.fr}))
		elif msg.message.startswith("/run"):
			s.send(m.generate(nick, "Wimp!", {msg.fr : msg.fr}))
		elif msg.message.startswith("/sing"):
			s.send(m.generate(nick, "You sound like Barry Manilow!", {msg.fr : msg.fr}))
		elif msg.message.startswith("/horoscope"):
			s.send(m.generate(nick, "You will /run /sing /fight and /die!", {msg.fr : msg.fr}))
		elif msg.message.startswith("/die"):
			s.send(m.generate(nick, "You die! No, wait you're still alive!", {msg.fr : msg.fr}))
		if not data: continue
		# print data
		print "<"+msg.nick+">"+msg.message
		m.rec[msg.fr] = msg.fr

t = Thread(target=readsocket, args=(s,stop))
t.start()

while True:
	try:
		line = sys.stdin.readline()
		if line.startswith("/quit"):
			sys.exit(0)
		s.send(m.generate(nick, line, m.rec))
		print "<"+nick+">"+line
	except KeyboardInterrupt:
		stop.set()
		break

	if not line:
		stop.set()
		break

s.close                     # Close the socket when done
