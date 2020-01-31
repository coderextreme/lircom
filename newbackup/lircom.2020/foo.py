import subprocess, sys

p = subprocess.Popen(["powershell.exe", 
			"-Command",
			 "Add-Type –AssemblyName System.Speech; $ss = New-Object -TypeName System.Speech.Synthesis.SpeechSynthesizer; $ss.Speak('this is really simple, just type in what you want the computer to say');"], 

              stdout=sys.stdout)
p.communicate()
