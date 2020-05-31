import sqlite3
from flask import g
from app import application


class Database:

    def __init__(self, database_path):
        self.database_path = database_path

    def get_db(self):
        self.db = getattr(g, '_database', None)
        if self.db is None:
            self.db = g._database = sqlite3.connect(self.database_path)
        return self.db


    def query_db(self, query, args=(), one=False):
        cur = self.get_db().execute(query, args)
        rv = cur.fetchall()
        cur.close()
        return (rv[0] if rv else None) if one else rv


    @staticmethod
    @application.teardown_appcontext
    def close_connection(exception):
        db = getattr(g, '_database', None)
        if db is not None:
            db.close()
