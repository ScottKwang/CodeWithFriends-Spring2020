import requests
from bs4 import BeautifulSoup
from selenium import webdriver
import time

# the wait time to respect the website owners
waitTime = 10

class Shop:
    """ Stores information about the shop including the url """
    def __init__(self, name):
        """ initialise the shop """
        self.prefix = ""
        self.name = name
        self.pack = {}
        
    def __repr__(self):
        """ Returns name of the shop """
        return self.name + " " + str(len(self.pack))
    
    def addItem(self, packName, packUrl):
        """ add url to the dictionary """
        self.pack[packName] = packUrl

    def setPrefix(self, prefix):
        self.prefix = prefix

    def getName(self):
        return self.name

    def getUrl(self, packName):
        """ gets the url for the pack """
        if(packName in self.pack):
            return self.prefix + self.pack[packName]

    def getHTML(self, packName):
        """Gets the Html"""
        url = self.getUrl(packName)
        if(url != None):
            return requests.get(url).text

    def getPrice(self):
        pass


class EBGames(Shop):
    """ Ebgames object """
    def __init__(self):
        super().__init__("EBGames")

    def getPrice(self, packName):
        """ returns the price of the pack """
        html = self.getHTML(packName)
        if(html != None):
            soup = BeautifulSoup(html, 'html.parser')
            # finds price
            priceLine = soup.find(attrs={"itemprop":"price"})
            if(priceLine != None):
                return priceLine.contents[0]
        time.sleep(waitTime)


class Farmers(Shop):
    """ Farmers object """
    def __init__(self):
        super().__init__("Farmers")

    def getPrice(self, packName):
        """ returns the price of the pack """
        html = self.getHTML(packName)
        if(html != None):
            soup = BeautifulSoup(html, 'html.parser')
            # finds price
            priceList = soup.find_all(class_="std-price")
            if(priceList != None):
                return priceList[1].contents[0]
        time.sleep(waitTime)


class Kmart(Shop):
    """ Farmers object """
    def __init__(self):
        super().__init__("Kmart")

    def getPrice(self, packName):
        """ returns the price of the pack """
        html = self.getHTML(packName)
        if(html != None):
            soup = BeautifulSoup(html, 'html.parser')
            # finds price
            priceLine = soup.find(class_="price")
            if(priceLine != None):
                return "$" + priceLine.contents[0].strip()
        time.sleep(waitTime)

    def getHTML(self, packName):
        """ returns html """
        url = self.getUrl(packName)
        
        if(url != None):
            options = webdriver.ChromeOptions();
            options.add_argument("headless")
            browser=webdriver.Chrome(executable_path = "chromedriver.exe", options=options)

            browser.get(url)
            html = browser.page_source
            # close web browser
            browser.close()

            return html

        return None



class ToyWorld(Shop):
    """ Toy World object """
    def __init__(self):
        super().__init__("Toy World")

    def getPrice(self, packName):
        """ returns the price of the pack """
        html = self.getHTML(packName)
        if(html != None):
            soup = BeautifulSoup(html, 'html.parser')
            # finds price
            priceList = soup.find(class_="price")
            if(priceList != None):
                return priceList.contents[0]
        time.sleep(waitTime)

