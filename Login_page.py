#login page for timetable app!
import hashlib, sqlite3, os

class database:
	if not os.path.isfile("login.db"):
		new_db = sqlite3.connect("login.db")
		c = new_db.cursor()
		c.execute(''' CREATE TABLE test (username, hashed_password)''')
		new_db.commit()
	else:
		pass

	def add_user(username, hashed_password):
		new_db = sqlite3.connect("login.db")
		mycursor = new_db.cursor()
		sql = ("INSERT INTO test (username, hashed_password) VALUES(?, ?)")
		mycursor.execute(sql,(username,hashed_password))

		print("executed")

		new_db.commit()

		print(mycursor.rowcount, "record inserted.")
		
	
	def check_exists(username, hashed_password):
		new_db = sqlite3.connect("login.db")
		mycursor = new_db.cursor()
		flag = False
		if flag == False:
			my_db = new_db.cursor()

			sql = ("SELECT username FROM test WHERE username = (?)")
			mycursor.execute(sql,username)
			print("fetched")

			myresult = mycursor.fetchall()
			print(myresult)

			if len(myresult) == 0:
				database.add_user(username, hashed_password)
				flag = True
				return "done"
			else: 
				return "exists"





class login:
	def login():

		username = input("enter your username \t")
		password = input("enter a password \t")
		hashed_password = (hashlib.md5((password).encode())).hexdigest()
		print(hashed_password)

		if database.check_exists(username, hashed_password) == "done":
			print("user added")

		else:
			print("username already exists")
			login.login()




login.login()
hash_object = hashlib.md5(("bruh").encode())
print(hash_object.hexdigest())

