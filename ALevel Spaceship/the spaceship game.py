#the spaceship game
#NEVER NAME FILE PYGAME IT DOES NOT RUN
import pygame #2D graphics library that allows you to make little games
import os #importing os is used to allow the OS of the computer to interact and then upload the image assets into the program
pygame.font.init()#font library


WIDTH, HEIGHT = 900,500 #it is good practice to create 2 variables to state what size resolution you want the game window to run in
WIN = pygame.display.set_mode((WIDTH, HEIGHT))#the first thing you need to do is make a surface this is where all the events of the game take place on
#it is also good to create constant variable names in all capitals; it is easier to understand and makes more sense as the program goes on

pygame.display.set_caption("need innovative name") # renames the window

WHITE = (255,255,255) #instead of creating a variable called white we can simply enter the values in the WIN.fill function to be 255,255,255 as a mix of red green and blue on the maxiumum value would produce white
#however we learn that by simple telling the program to fill with white it is not sufficient because the display has to be updated
BLACK = (0,0,0)
RED = (255, 0, 0)
YELLOW = (255, 255, 0)

BORDER = pygame.Rect(WIDTH//2 - 5, 0, 10, HEIGHT) #the same rules apply for this rect command in terms of arguments, the x and y position then the width and height
# so in the above line of code we divide the width by 2 in order to place the border in the middle of the screen, however we need to keep in mind it is still being drawin in w/ respect to the 0,0; so if the border is 10 pixels wide it should be drawn 5 pixels away from the centre in this case 450 because 900/2 is 450, this command draws in our border at 445 which is 450-5.

HEALTH_FONT = pygame.font.SysFont("comicsans", 40)
WINNER_FONT = pygame.font.SysFont("comicsans", 100)

FPS = 60

VEL = 5 #a value for how many pixels are to be moved when the =+ or =- functions are used

BULLET_VEL = 7

MAX_BULLETS = 3

SPACESHIP_WIDTH, SPACESHIP_HEIGHT = 55,40 #since this is a variable we might call often it makes sesne for  it to be under a variable name

YELLOW_HIT = pygame.USEREVENT + 1 #represents the code for a custom user event
RED_HIT = pygame.USEREVENT + 2

#uploads the images into the program, this does not draw them onto our screen though, it is a separate function
YELLOW_SPACESHIP_IMAGE = pygame.image.load(
    os.path.join("Assets", "spaceship_yellow.png"))
YELLOW_SPACESHIP = pygame.transform.rotate(pygame.transform.scale
(YELLOW_SPACESHIP_IMAGE, (SPACESHIP_WIDTH, SPACESHIP_HEIGHT)), 90) #the end part of the code specifies what angle it has to be rotated on 

RED_SPACESHIP_IMAGE = pygame.image.load(
    os.path.join("Assets", "spaceship_red.png"))
RED_SPACESHIP = pygame.transform.rotate(pygame.transform.scale
(RED_SPACESHIP_IMAGE, (SPACESHIP_WIDTH, SPACESHIP_HEIGHT)), 270)

SPACE = pygame.transform.scale(
    pygame.image.load(os.path.join("Assets", "pixelart_bg.png")), (WIDTH, HEIGHT))

def yellow_handle_movement(keys_pressed, yellow):
    if keys_pressed[pygame.K_a] and yellow.x - VEL > 0: #LEFT
            yellow.x -= VEL #the -= function is used here becauase we want it to move to the left or in other words closer to the 0,0 point and due to where the spaceship is placed it needs to move left from its current position
    if keys_pressed[pygame.K_d] and yellow.x + VEL + yellow.width < BORDER.x : #RIGHT
            yellow.x += VEL
    if keys_pressed[pygame.K_w] and yellow.y - VEL > 0: #UP
            yellow.y -= VEL #once again this must be minus in order to move upwards because it has to be closer to the 0,0 in the top left
    if keys_pressed[pygame.K_s] and yellow.y + VEL + yellow.height < HEIGHT - 15: #DOWN
            yellow.y += VEL #when we go down we are adding to the y value 

def red_handle_movement(keys_pressed, red):
    if keys_pressed[pygame.K_LEFT] and red.x - VEL > BORDER.x + BORDER.width : #LEFT
            red.x -= VEL 
    if keys_pressed[pygame.K_RIGHT]and red.x + VEL + red.width < WIDTH : #RIGHT
            red.x += VEL
    if keys_pressed[pygame.K_UP] and red.y + VEL > 0 : #UP
            red.y -= VEL 
    if keys_pressed[pygame.K_DOWN]and red.y + VEL + red.width < HEIGHT - 15: #DOWN
            red.y += VEL 

def handle_bullets(yellow_bullets, red_bullets, yellow, red): #will move bullets, handle collisions, handle collisions with characters, we will loop thru all the yellow bullets and see if they have collided with the red spaceship or end of the screen and vice versa
    for bullet in yellow_bullets:
        bullet.x += BULLET_VEL # we use += to move to the right, away from 0,0
        if red.colliderect(bullet): #checks whether our red rect has been collided with
            pygame.event.post(pygame.event.Event(RED_HIT)) #making a new event saying red player was hit
            yellow_bullets.remove(bullet)
        elif bullet.x > WIDTH:
            yellow_bullets.remove(bullet)
        
    
    for bullet in red_bullets:
        bullet.x -= BULLET_VEL # we use += to move to the right, away from 0,0
        if yellow.colliderect(bullet): #checks whether our red rect has been collided with
            pygame.event.post(pygame.event.Event(YELLOW_HIT)) #making a new event saying red player was hit
            red_bullets.remove(bullet)
        elif bullet.x < 0:
            red_bullets.remove(bullet)
        

def draw_window(red, yellow, red_bullets, yellow_bullets, red_health, yellow_health): #we call the variables inside the redraw function because we want the spaceships to be where the rect function is on the screen
     WIN.blit(SPACE, (0,0))
     #WIN.fill(WHITE) if we wanted to have a white bg #in pygame colors work based of the RGB spectrum meaning red green and blue, the max value for a color is 255 so for example if the value was set to be 255,0,0 the color displayed would be red, similary if the value entered was 255,0,255 it would display purple becauase a mix of the maximum value of red and blue produce purple
     pygame.draw.rect(WIN, BLACK, BORDER) #instead of using a blit function we use the rect because its values and RBG color are already defined
     
     red_health_text = HEALTH_FONT.render("Health: " + str(red_health), 1, WHITE) #using to render in the text
     yellow_health_text = HEALTH_FONT.render("Health: " + str(yellow_health), 1, WHITE) #at this point weve created text objects but now have to draw it on
     WIN.blit(red_health_text,(WIDTH - red_health_text.get_width()-10, 10))
     WIN.blit(yellow_health_text, (10,10))

     WIN.blit(YELLOW_SPACESHIP, (yellow.x, yellow.y)) #this is where the image will be drawn in from the top left onwards
     WIN.blit(RED_SPACESHIP, (red.x, red.y))

     for bullet in red_bullets:
        pygame.draw.rect(WIN, RED, bullet)

     for bullet in yellow_bullets:
        pygame.draw.rect(WIN, YELLOW, bullet)

     pygame.display.update()

def draw_winner(text):
    draw_text = WINNER_FONT.render(text, 1, WHITE)
    WIN.blit(draw_text, (WIDTH/2 - draw_text.get_width()/2, HEIGHT/2 - draw_text.get_height()/2))
    pygame.display.update()
    pygame.time.delay(5000)

#a main loop as suggest in the name controls all the main functioning of the program, for example redrawing the window, checking for collisions, updating the score, etc
def main(): 
    red = pygame.Rect(700, 300, SPACESHIP_WIDTH, SPACESHIP_HEIGHT) #these rectangles represent the player and the spaceships, in order to control where they move; the arguments for the rect are x, y width and height
    yellow = pygame.Rect(100, 300, SPACESHIP_WIDTH, SPACESHIP_HEIGHT)

    red_bullets = [] 
    yellow_bullets = []

    red_health = 10
    yellow_health = 10

    clock = pygame.time.Clock() #controls the speed of the while loop
    run = True #this loop is a while loop and can be thought of as an infinite loop, that keeps running until it is defined to be as false which is most often when the window is closed

    while run:
        clock.tick(FPS) #ensures its running at that cap of 60 FPS, in the circumstance it is unable to it runs at its maximum ability
        for event in pygame.event.get(): #inside this loop we check for events that occur in the program
            if event.type == pygame.QUIT: #the first event that we most commonly check first is whether or not the user has quit the program
                run = False #if the user has quit the program the while loop's condition is now false and hence instructs the code to stop running and in essence close the program
                pygame.quit()
                
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_LCTRL and len(yellow_bullets) < MAX_BULLETS: #for the left control the bullet must move towards the right
                    bullet = pygame.Rect(
                        yellow.x + yellow.width, yellow.y + yellow.height//2 - 2, 10, 5) # the height argyument divides its height by 2 in order to put it exactly in the middle the width is 10 and the height of the bullet is 5
                    yellow_bullets.append(bullet)

                if event.key == pygame.K_RCTRL and len(red_bullets) < MAX_BULLETS: 
                    bullet = pygame.Rect(
                        red.x, red.y + red.height//2 - 2, 10, 5) # the height argyument divides its height by 2 in order to put it exactly in the middle the width is 10 and the height of the bullet is 5
                    red_bullets.append(bullet)

            if event.type == RED_HIT:
                red_health -= 1 #minuses from the 10 total health

            if event.type == YELLOW_HIT:
                yellow_health -= 1

        winner_text = ""        
        if red_health <= 0:
            winner_text = "Yellow Wins!"

        if yellow_health <=0:
            winner_text = "Red Wins!"

        if winner_text != "":
            draw_winner(winner_text)
            break

            
        keys_pressed = pygame.key.get_pressed() #checks what kets are being pressed down by the user everytime the while loops reaches this point which is 60 times a second, it woudl also register if the key is still being held down and register that as an input.
        yellow_handle_movement(keys_pressed, yellow) #created a def fucntion instead of including it in the main loop
        red_handle_movement(keys_pressed, red) 

        handle_bullets(yellow_bullets, red_bullets, yellow, red)

        draw_window(red, yellow, red_bullets, yellow_bullets, red_health, yellow_health)

    main()

if __name__ == "__main__":
    main() #these 2 lines of code state that the main function should only be run when the code is run from this file directly and not imported through another basis, so in essence only run this code if it is called directly


