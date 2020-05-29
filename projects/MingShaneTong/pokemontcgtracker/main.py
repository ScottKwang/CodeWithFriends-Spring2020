from shops import *
from tkinter import *
from datetime import date
import csv

def processShop(setOrder):
    """ process the data in the csv """
    # create shops
    ebgames = EBGames()
    farmers = Farmers()
    kmart = Kmart()
    toyworld = ToyWorld()

    shops = [ebgames, farmers, kmart, toyworld]

    # process shop data
    retailFile = open('retailUrl.csv')
    retailReader = csv.reader(retailFile)
    retailData = list(retailReader)

    currentShop = ebgames

    # add the items
    for line in retailData:
        # not a seperator
        if(line[0] != ""):
            if(line[0] not in setOrder):
                # get shop object
                currentShop = getShop(shops, line[0])
                currentShop.setPrefix(line[1])
            else:
                # add shop item
                currentShop.addItem(line[0], line[1])

    return shops

def getShop(shopNetwork, searchShop):
    """ gets the shop object """
    for shop in shopNetwork:
        if(shop.getName() == searchShop):
            return shop
    return None

def printPrices(setOrder, shops):
    """ gets the prices of the packs by looping each pack and storing on the the table """
    # create window
    window = Tk()
    today = date.today()
    window.title(today.strftime("%d/%m") + " Pokemon Card Prices")

    labelRow = 1
    labelColumn = 1

    # header
    for shop in shops:
        Label(window, text=shop.getName()).grid(row=0, column=labelColumn)
        labelColumn += 1

    labelColumn = 1

    print("""Hi, this program incorperates webstrapping elements.
In order to reduce the amount potential traffic that I send to these website,
this program is slowed down.
Please be patient when waiting for this program.
Could take about 2 minutes.

Finished Packs: """)

    # present information
    for pack in setOrder:
        # pack name
        Label(window, text=pack).grid(row=labelRow, column=0)
        # find price of pack at each shop
        for shop in shops:
            # add the price Label
            price = shop.getPrice(pack)
            if(price != None):
                Label(window, text=price).grid(row=labelRow, column=labelColumn)
            else:
                Label(window, text="~").grid(row=labelRow, column=labelColumn)
            labelColumn += 1
        labelRow += 1
        labelColumn = 1
        print(pack)
        
def main():
    """ The Main Routine """
    setOrder = ["Assorted", "Rebel Clash", "Sword & Shield",
                "Cosmic Eclipse", "Unified Minds", "Unbroken Bonds", "Lost Thunder", "Celestial Storm", "Crimson Invasion"]
    shops = processShop(setOrder)
    printPrices(setOrder, shops)


if __name__ == "__main__":
    main()

    
            
   
