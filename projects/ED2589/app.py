#####################
# import libraries ##
#####################
# GUI
# note1a: `Pack` = a tkinter geometry manager (easiest to use)
# note1b: widgets = image + buttons in the app
# note1c: geometry manager pack widgets into frame (e.g. tops frame) => frame packed into `root` (aka app main window)
import tkinter as tk

# images
from PIL import Image, ImageTk
# audio
from playsound import playsound

# to access images of Tops and Bottoms
import os

# for 'create outfit' randomize button
import random as rd


##############
# app parms ##
##############
# app name
APP_TITLE = "Dudu Wardrobe"
#  app window dimension
WINDOW_HT = 750
WINDOW_WIDTH = 500
# image dimension
IMG_HT = 340
IMG_WIDTH = 230
# store all tops as instance variable for easy access by fcns
# (Tops images included proj dir folder & skip hidden files in `tops` directory)
# (used for updating top image index in button logic)
ALL_TOPS = [str('tops/') + imagefile for imagefile in os.listdir('tops') if not imagefile.startswith('.')]
ALL_BOTTOMS = [str('bottoms/') + imagefile for imagefile in os.listdir('bottoms') if not imagefile.startswith('.')]

##############################
# class for wardrobeApp ##
###############################


class WardrobeApp:

    '''
    class constructor
    '''
    def __init__(self, root):
        self.root = root

        ### create background
        self.create_background()

        ### Show top image in window
        self.top_list = ALL_TOPS
        # current image file path (updated in function `get_next_item()`)
        self.top_pic_path = self.top_list[0]
        # create frame (for tops images)
        self.tops_frame = tk.Frame(self.root)
        #  add top photo to tops frame (i.e. create widget in doing so)
        self.top_image_label = self.add_pic(self.top_pic_path, self.tops_frame)
        # pack top widget to top frame (at 'TOP')
        self.top_image_label.pack(side=tk.TOP)
        # pack top frame to root (has to be done AFTER widgets added to top frame cuz of 'pack' library requirements)
        self.tops_frame.pack(fill=tk.BOTH, expand=tk.YES)

        ### show bottom image in window
        self.bottom_list = ALL_BOTTOMS
        self.bottom_pic_path = self.bottom_list[0]
        self.bottoms_frame = tk.Frame(self.root)
        self.bottom_image_label = self.add_pic(self.bottom_pic_path, self.bottoms_frame)
        self.bottom_image_label.pack( side = tk.TOP)
        self.bottoms_frame.pack(fill = tk.BOTH, expand = tk.YES )

        ## add all buttons
        self.create_button()

    def create_background(self):
        # add title to window
        self.root.title(APP_TITLE)
        # change window size (dimension:  height x width)
        self.root.geometry('{0}x{1}'.format(WINDOW_WIDTH, WINDOW_HT))


    # adds an image onto frame
    def add_pic(self, image_path, frame):
        '''
        returns widget (image) linked to frame
        :param image_path: str
        :param frame: tkinter frame
        :return: tkinter widget
        '''
        # Step 1. loads image (pillow library)
        image_file = Image.open(image_path)
        # Step 2. resize loaded image
        img_resize = image_file.resize(size=(IMG_WIDTH, IMG_HT), resample = Image.ANTIALIAS)
        # Step 3. convert .png to tkinter-compatible image
        tk_pic = ImageTk.PhotoImage(img_resize)
        # Step 4. label tops frame
        #  4a. make sure to link label to `frame` argumt
        #  -`anchor` argument specifies where image located rel. to label
        pic_label = tk.Label(frame, image=tk_pic, anchor=tk.CENTER)
        # 4b. weird tkinter quirk (save reference to photo)
        pic_label.image = tk_pic
        return pic_label

    # add button + pack to a frame
    def create_button(self):
        ## tops buttons##
        # 'prev' button for tops
        top_prev_button = tk.Button(self.tops_frame, text='Prev', command=self.get_prevtop)
        # pack tops 'prev' button to tops frame
        top_prev_button.pack(side=tk.LEFT)
        # 'next' button for tops
        top_next_button = tk.Button(self.tops_frame, text='Next', command=self.get_nexttop)
        top_next_button.pack(side=tk.RIGHT)

        ## bottoms buttons##
        bottom_prev_button = tk.Button(self.bottoms_frame, text='Prev', command = self.get_prevbottom)
        bottom_prev_button.pack(side=tk.LEFT)
        bottom_next_button = tk.Button(self.bottoms_frame, text='Next', command = self.get_nextbottom)
        bottom_next_button.pack(side = tk.RIGHT)

        ## create_outfit button: randomize an outfit ##
        create_outfit_button = tk.Button(self.bottoms_frame, text = 'RANDOM OUTFIT', command = self.create_outfit)
        create_outfit_button.pack(side = tk.LEFT)
    # go to next top image (when click 'next' button in tops frame)
    def get_prevtop(self):
        self.get_item(self.top_pic_path, self.top_list,increment = False)
    # go to prev top image (^ click 'prev' button ^)
    def get_nexttop(self):
        self.get_item(self.top_pic_path, self.top_list, increment = True)

    def get_prevbottom(self):
        self.get_item(self.bottom_pic_path, self.bottom_list, increment= False)
    def get_nextbottom(self):
        self.get_item(self.bottom_pic_path, self.bottom_list, increment = True)

    # generic fcn to go prev/next (update index)
    # called BY `get_prevtop(), get_nexttop(), get_prevbot(), get_nextbot()`
    # calls `update_pic()`
    def get_item(self, curr_item, clothes_list, increment=True):
        '''
        Gets next item (file path aka type `str`) in a category (tops or bottoms) depending if user hits 'next' or 'prev' button
        :param curr_item: str
        :param clothes_list: list of image file paths (for tops or bottoms)
        :param increment: boolean
        :return: str
        '''
        # get index of current image in list of image file paths
        curr_index = clothes_list.index(curr_item)
        # index of last image
        final_index = len(clothes_list)-1
        # index to update
        index = 0

        # Step 1. get index (`index`) of NEXT image upon button click
            ## note: do NOT actually need code for 2 edge cases below cuz Python automatically accounts for (-) index
        # edge case 1: cycle back to 1st image if hit 'next' button at last image
        if increment == True and curr_index == final_index:
            index = 0
        # edge case 2: cycle to last image if hit 'prev' on 1st image
        elif increment == False and curr_index == 0:
            index = final_index
        # all other cases: increment/decrement index by 1 (to next OR prev image, respectively)
        else:
            # whether to increment OR decrement
            incrementOR = 1 if increment else -1
            # update curr_index accordingly ^
            index = curr_index + incrementOR

        # Step 2. get NEXT image (after updating `index`)
        img_next = clothes_list[index]

        # Step 3. reset file path (instance variable `self.top_pic_path`) to that of next image AND
        #   save current image label (`self.top_image_label`) to variable
        # case 1 - if top
        if img_next in self.top_list:
            self.top_pic_path = img_next
            image_label = self.top_image_label

        # case 2 - if bottom
        else:
            self.bottom_pic_path=img_next
            image_label = self.bottom_image_label
        # Step 4.
        # update image - pass into function arguments x 2 obtained from Step 3 i.e.  file path (updated in Step 3) AND image label (widget)
        self. update_pic( img_next, image_label)


    # update image when click prev/next
    # called BY `get_item()` function
    # same steps as `add_pic()` except update (not add) photo in master frame in Step 4
    def update_pic(self, next_image_path, image_label):
        # Step 1. load image
        new_image_file = Image.open(next_image_path)
        # Step 2. resize image
        image_resized =  new_image_file.resize((IMG_WIDTH,IMG_HT), Image.ANTIALIAS)
        # Step 3. make image Tkinter compatible
        image_tk = ImageTk.PhotoImage(image_resized)
        # Step 4a. update image in current widget (top_frame OR bottom_frame)
        image_label.configure(image = image_tk)
          #4b. weird Tkinter quirk
        image_label.image = image_tk

    # create outfit function
    def create_outfit(self):
        # pick random top outfit
        random_top_index = rd.randint(0,len(self.top_list)-1)
        # do so for bottom as well
        random_bottom_index = rd.randint(0, len(self.bottom_list)-1)
        # update image in tops frame w random top pic
        self.update_pic(self.top_list[random_top_index], self.top_image_label)
        # update image in bottoms frame w '' bottom''
        self.update_pic(self.bottom_list[random_bottom_index], self.bottom_image_label)

        # add noise


# main app window
root = tk.Tk()
# make sure `root` passed into WardrobeApp class to update widgets
app = WardrobeApp(root)
# run the app (an infinite loop used, wait for event to occur and process the event
#   as long as window not closed)
root.mainloop()

stack = []
while (n > 0):
    n = n // 2
    remainder = n % 2
    stack.append(remainder)
print(stack)
