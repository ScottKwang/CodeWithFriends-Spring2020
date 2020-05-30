from flask_script import Manager

from app import application

manager = Manager(application)

# Not sure if I need a database yet
# db = SQLAlchemy(application)
# migrate = Migrate(application, db)

# manager.add_command('db', MigrateCommand)

if __name__ == '__main__':
    manager.run()
