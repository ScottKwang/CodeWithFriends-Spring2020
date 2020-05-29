import os
import shutil

def createFolder(folder,path):
   if not os.path.exists(f"{path}/{folder}"):
      os.makedirs(f"{path}/{folder}")

def move(folderName,files,paths):
   for file in files: 
      shutil.move(paths+'/'+file,f"{paths}/{folderName}/{file}")

cnt_list={}
def totalCount():
    return cnt_list
def sumList():
   svalue=(cnt_list['cnt_img']+cnt_list['cnt_docs']+cnt_list['cnt_media']+
              cnt_list['cnt_exe']+cnt_list['cnt_zip']+cnt_list['cnt_bat']+
              cnt_list['cnt_dat']+cnt_list['cnt_js']+cnt_list['cnt_java']+
              cnt_list['cnt_others']+cnt_list['cnt_html'])
   return svalue

def initCount():
   cnt_list['cnt_img']=0
   cnt_list['cnt_docs']=0
   cnt_list['cnt_media']=0
   cnt_list['cnt_exe']=0
   cnt_list['cnt_zip']=0
   cnt_list['cnt_bat']=0
   cnt_list['cnt_dat']=0
   cnt_list['cnt_py']=0
   cnt_list['cnt_js']=0
   cnt_list['cnt_java']=0
   cnt_list['cnt_html']=0
   cnt_list['cnt_others']=0
   
   return cnt_list
initCount()
def runSeperator(files, path):   
   print('path:',path)
   print('dir:',files)
   sep=["seperator.py","START-CLEANER.bat","icon.ico","logo.png"]
   if sep in files:
      files.remove("seperator.py")
      files.remove("START-Window.bat")
      files.remove("icon.ico")
      files.remove("logo.png")  
   imgExts=[".png",".jpg",".jpeg",".webp",".bmp",".gif",".tiff",".psd",".tif",".eps",".raw",
            ".cr2",".nef",".sr2",".xcf",".ai",".cdr",".ico",".jbg",".jbig"]
   images=[file for file in files if os.path.splitext(file)[1].lower() in imgExts]
   if len(images)>0:
      cnt_img=len(images)
      cnt_list['cnt_img']=cnt_img
      createFolder('Images',path)
      move("Images",images,path)
   #print('image:',images)  
   docExts=[".txt",".docx",".doc",".pdf",".xlsx",".csv",".arff",".odt",".xls",".ods",".ppt",".pptx",
            ".xml",".mdb",".sav",".pps",".odp",".rtf",".wpd",".tex"]
   docs=[file for file in files if os.path.splitext(file)[1].lower() in docExts]
   #print('Before-docs:',docs) 
   if len(docs)>0:
      cnt_docs=len(docs)
      cnt_list['cnt_docs']=cnt_docs
      createFolder('Docs',path)
      move("Docs",docs,path)
   mediaExts=[".mp4",".mp3",".flv",".wmv",".webm",".mkv",".flv",".vob",".ogv",".ogg",".gifv",
              ".avi",".MTS",".TS",".M2TS",".yuv",".rm",".rmvb",".asf",".amv",".m4p",".m4v",
              ".mpg",".mpeg",".m2v",".m4v",".svi",".3gp",".3g2",".mxf",".ogg",".wav",".wpl"]
   medias=[file for file in files if os.path.splitext(file)[1].lower() in mediaExts]
   if len(medias)>0:
      cnt_media=len(medias)
      cnt_list['cnt_media']=cnt_media
      createFolder('Media',path)
      move("Media",medias,path)

   exeExts=[".exe",".c",".cpp",".o"]
   exe_file=[file for file in files if os.path.splitext(file)[1].lower() in exeExts]
   if len(exe_file)>0:
      cnt_exe=len(exe_file)
      cnt_list['cnt_exe']=cnt_exe
      createFolder('Exe-File',path)
      move("Exe-file",exe_file,path)

   zipExts=[".zip",".rar",".7z",".arj",".pkg",".rpm",".tar.gz",".z",".deb"]
   zip_file=[file for file in files if os.path.splitext(file)[1].lower() in zipExts]
   if len(zip_file)>0:
      cnt_zip=len(zip_file)
      cnt_list['cnt_zip']=cnt_zip
      createFolder('Zip-File',path)
      move("Zip-file",zip_file,path)

   batExts=[".bat"]
   bat_file=[file for file in files if os.path.splitext(file)[1].lower() in batExts]
   if len(bat_file)>0:
      cnt_bat=len(bat_file)
      cnt_list['cnt_bat']=cnt_bat
      createFolder('Bat-File',path)
      move("Bat-file",bat_file,path)

   datExts=[".dat",".db",".dbf",".sql",".tar",".log"]
   dat_file=[file for file in files if os.path.splitext(file)[1].lower() in datExts]
   if len(dat_file)>0:
      cnt_dat=len(dat_file)
      cnt_list['cnt_dat']=cnt_dat
      createFolder('Dat-File',path)
      move("Dat-file",dat_file,path)

   pyExts=[".py",".pyc",".py3",".pyw",".pyi",".pyx"]
   py_file=[file for file in files if os.path.splitext(file)[1].lower() in pyExts]
   if len(py_file)>0:
      cnt_py=len(py_file)
      cnt_list['cnt_py']=cnt_py
      createFolder('Python-File',path)
      move("Python-file",py_file,path)

   jsExts=[".js"]
   js_file=[file for file in files if os.path.splitext(file)[1].lower() in jsExts]
   if len(js_file)>0:
      cnt_js=len(js_file)
      cnt_list['cnt_js']=cnt_js
      createFolder('JavaScript-File',path)
      move("JavaScript-file",js_file,path)

   javaExts=[".java"]
   java_file=[file for file in files if os.path.splitext(file)[1].lower() in javaExts]
   if len(java_file)>0:
      cnt_java=len(java_file)
      cnt_list['cnt_java']=cnt_java
      createFolder('Java-File',path)
      move("Java-file",java_file,path)

   htmlExts=[".html",".css",".htm"]
   html_file=[file for file in files if os.path.splitext(file)[1].lower() in htmlExts]
   if len(html_file)>0:
      cnt_html=len(html_file)
      cnt_list['cnt_html']=cnt_html
      createFolder('Html-File',path)
      move("Html-File",html_file,path)

   others=[]
   for file in files:
      ext=os.path.splitext(file)[1].lower()
      if (ext not in mediaExts and ext not in imgExts and os.path.isfile(file) and ext not in docExts and ext not in exeExts and ext not in zipExts and
      ext not in batExts and ext not in datExts and ext not in pyExts and ext not in jsExts and ext not in javaExts  and ext not in htmlExts) :
         others.append(file)
   
   if len(others)>0:
      cnt_others=len(others)
      cnt_list['cnt_others']=cnt_others
      createFolder('Others',path)
      move("Others",others,path)
  
   print('Dict:',cnt_list)   
   print("Done moving.")
