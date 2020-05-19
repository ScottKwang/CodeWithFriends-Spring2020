# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'gui.ui'
#
# Created by: PyQt5 UI code generator 5.13.0
#
# WARNING! All changes made in this file will be lost!
import os
import sys
import io
import time
import threading
from threading import Thread
from PyQt5 import QtCore, QtGui, QtWidgets

common_msg = None


class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(989, 645)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.Start_Button = QtWidgets.QPushButton(self.centralwidget)
        self.Start_Button.setGeometry(QtCore.QRect(160, 390, 201, 111))
        self.Start_Button.setObjectName("Start_Button")
        self.terminal_out = QtWidgets.QTextBrowser(self.centralwidget)
        self.terminal_out.setGeometry(QtCore.QRect(160, 30, 621, 211))
        self.terminal_out.setObjectName("terminal_out")
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 989, 26))
        self.menubar.setObjectName("menubar")
        self.menuFile = QtWidgets.QMenu(self.menubar)
        self.menuFile.setObjectName("menuFile")
        self.menuAbout = QtWidgets.QMenu(self.menubar)
        self.menuAbout.setObjectName("menuAbout")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)
        self.actionExit = QtWidgets.QAction(MainWindow)
        self.actionExit.setObjectName("actionExit")
        self.actionAbout_LeapMask = QtWidgets.QAction(MainWindow)
        self.actionAbout_LeapMask.setObjectName("actionAbout_LeapMask")
        self.actionGitHub_Page = QtWidgets.QAction(MainWindow)
        self.actionGitHub_Page.setObjectName("actionGitHub_Page")
        self.menuFile.addAction(self.actionExit)
        self.menuAbout.addAction(self.actionAbout_LeapMask)
        self.menuAbout.addAction(self.actionGitHub_Page)
        self.menubar.addAction(self.menuFile.menuAction())
        self.menubar.addAction(self.menuAbout.menuAction())

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

        self.actionExit.triggered.connect(self.exit)
        self.actionGitHub_Page.triggered.connect(self.GitHub_link)
        self.terminal_out.insertPlainText(common_msg)
        # self.Start_Button.clicked.connect(self.start_LeapMask)

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "LeapMask GUI"))
        self.Start_Button.setText(_translate("MainWindow", "Start"))
        self.menuFile.setTitle(_translate("MainWindow", "File"))
        self.menuAbout.setTitle(_translate("MainWindow", "Help"))
        self.actionExit.setText(_translate("MainWindow", "Exit"))
        self.actionAbout_LeapMask.setText(
            _translate("MainWindow", "About LeapMask"))
        self.actionGitHub_Page.setText(_translate("MainWindow", "GitHub Page"))

    def exit(self):
        sys.exit()

    def GitHub_link(self):
        os.system(
            "start \"\" https://github.com/Cipulot/CodeWithFriends-Spring2020/tree/master/projects/Cipulot")


def main_gui():
    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = Ui_MainWindow()
    ui.setupUi(MainWindow)
    MainWindow.show()
    if(app.exec_() == 0):
        return 0


if __name__ == "__main__":
    # Call main function and pass args given by user
    main_gui()
