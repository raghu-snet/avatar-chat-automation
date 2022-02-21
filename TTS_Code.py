import pyttsx3
import time
import speech_recognition as sr

engine = pyttsx3.init() # object creation


rate = engine.getProperty('rate')   # getting details of current speaking rate
print (rate)                        #printing current voice rate
engine.setProperty('rate', 175)     # setting up new voice rate


volume = engine.getProperty('volume')   #getting to know current volume level (min=0 and max=1)
print (volume)                          #printing current volume level
engine.setProperty('volume',1.0)        # setting up volume level  between 0 and 1

voices = engine.getProperty('voices')

print(pyttsx3.drivers)

for voice in voices:
    #print(str(voice.languages[0]))
    #print(voice.id)
    #print(voice.name)
    if voice.name == u'english-us':
        engine.setProperty('voice', voice.id)
        print(voice.name)
        print(voice.gender)    
        engine.setProperty('voice', voice.id)
        engine.say('I don\'t understand.')
        engine.runAndWait()
    #time.sleep(2.0)
