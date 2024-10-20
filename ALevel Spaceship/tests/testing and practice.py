#testing and practice 
#make a program to determine the larger of 2 numbers 
#include some sort of human aspect, as if to be communicating with a real person

import time #to make the time.sleep function work
import pygame
pygame.init()
pygame.mixer.init()



num1 = int(input("please enter your first number: "))
num2 = int(input("please enter your second number number: "))

print("the numbers you chose are", num1, "and", num2,)

time.sleep(1) #pauses the program

print("i will now determine the bigger of the 2 numbers")

time.sleep(1)

print("i'm thinking...")

time.sleep(1)

print("hmm okay i think i've figured it out ")

time.sleep(1)

biggerNum = 0

if num1 > num2:
     biggerNum = num1
     print("the bigger num is", str(biggerNum))  
elif num2 > num1:
    biggerNum = num2
    print("the bigger number is", str(biggerNum))
elif num1 == num2:
    print("your numbers are equal")



