from tkinter import*
import os
from tkinter.filedialog import askdirectory
import separator 

fname=""
master=Tk()
master.title("File Separator")
master.iconbitmap('icon.ico')
master.geometry('558x345')
master.resizable(0,0)
print('Starting up...')
photo = PhotoImage(file="logo.png")
ltitle=Label(master,image=photo)
ltitle.grid(row=1,column=1)

label=Label(master, text="Folder Directory: ",font=("Helvetica", 13))
label.grid(row=2)
entry_path = Entry(master,width=50,font=("Helvetica", 8))
entry_path.grid(row=2, column=1)


def setDetails(detail,total):
    txt=Label(master, text=detail, font=2)
    txt.grid(row=3, column=1)
    tot=Label(master, text=total,font=("Helvetica", 10))
    tot.grid(row=4, column=1)

def OpenFolder():
    folder_name=askdirectory()
    fname=folder_name
    entry_path.delete(0, END)
    entry_path.insert(0,folder_name)
    total_cnt=separator.initCount()

def close_window (): 
    print('Completed shutdown')
    master.destroy()

button1=Button(master, text="Open Folder",font=("Helvetica", 10),command=OpenFolder)
button1.grid(row=50, column=1)
button2=Button(master, text="Quit",font=("Helvetica", 10),command = close_window)
button2.grid(row=50, column=2)

l3_gap=Label(master, text=" ")
l3_gap.grid(row=51, column=1)


def showHistory():
    path= entry_path.get()
    if len(path)>0:
        try:
            files=os.listdir(path)
            separator.runSeperator(files,path)
        except:
            total_cnt=separator.initCount()
            InvalidPath()
        try:
            total_cnt=separator.totalCount()
        except:
            total_cnt=separator.initCount()
            InvalidPath()
        
        sumList=0
        try:
            sumList=separator.sumList()
        except:
            total_cnt=separator.initCount()
            InvalidPath()
        value_total="Total completed : "+str(sumList)+"      "
        setDetails("     History         ",value_total)
        l1_gap=Label(master, text=" ")
        l1_gap.grid(row=5, column=1)
        value1="Images : "+str(total_cnt['cnt_img'])
        l1=Label(master, text=value1,font=("Helvetica", 10))
        value2="Docs : "+str(total_cnt['cnt_docs'])
        l2=Label(master, text=value2,font=("Helvetica", 10))
        value3="Medias : "+str(total_cnt['cnt_media'])
        l3=Label(master, text=value3,font=("Helvetica", 10))
        value4="Exe files : "+str(total_cnt['cnt_exe'])
        l4=Label(master, text=value4,font=("Helvetica", 10))
        value5="Zip files : "+str(total_cnt['cnt_zip'])
        l5=Label(master, text=value5,font=("Helvetica", 10))
        value6="Bat files : "+str(total_cnt['cnt_bat'])
        l6=Label(master, text=value6,font=("Helvetica", 10))
        value7="Dat files : "+str(total_cnt['cnt_dat'])
        l7=Label(master, text=value7,font=("Helvetica", 10))
        value8="Py files : "+str(total_cnt['cnt_py'])
        l8=Label(master, text=value8,font=("Helvetica", 10))
        value9="Js files : "+str(total_cnt['cnt_js'])
        l9=Label(master, text=value9,font=("Helvetica", 10))
        value10="HTML files : "+str(total_cnt['cnt_html'])
        l10=Label(master, text=value10,font=("Helvetica", 10))
        value11="Java files : "+str(total_cnt['cnt_java'])
        l11=Label(master, text=value11,font=("Helvetica", 10))
        value12=" Others : "+str(total_cnt['cnt_others'])
        l12=Label(master, text=value12,font=("Helvetica", 10))
        l1.grid(row=6, column=0)
        l2.grid(row=6, column=1)
        l3.grid(row=6, column=2)
        l4.grid(row=7, column=0)
        l5.grid(row=7, column=1)
        l6.grid(row=7, column=2)
        l7.grid(row=8, column=0)
        l8.grid(row=8, column=1)
        l9.grid(row=8, column=2)
        l10.grid(row=9, column=0)
        l11.grid(row=9, column=1)
        l12.grid(row=9, column=2)
        l2_gap=Label(master, text=" ")
        l2_gap.grid(row=10, column=1)
        button1.destroy()
        button2.destroy()
        button12=Button(master, text="Open Folder",font=("Helvetica", 10),command=OpenFolder)
        button12.grid(row=50, column=0)
        button22=Button(master, text="Quit",font=("Helvetica", 10),command = close_window)
        button22.grid(row=50, column=2)
        l3_gap=Label(master, text=" ")
        l3_gap.grid(row=51, column=1)
        #lversion=Label(master, text="Version 1.0.")
        #lversion.grid(row=550, column=1)
    else:
        total_cnt=separator.initCount()
        InvalidPath()
        
sep_btn=Button(master, text="Separate",font=("Helvetica", 10),command=showHistory)
sep_btn.grid(row=3)

def InvalidPath():
    setDetails("Invalid Directory","Please give valid path.")
    l1_gap=Label(master, text=" ")
    l1_gap.grid(row=5, column=1)

#InvalidPath()
#lversion=Label(master, text="Version 1.0.")
#lversion.grid(row=550, column=1)
    
if __name__=="__main__":
    master.mainloop()
