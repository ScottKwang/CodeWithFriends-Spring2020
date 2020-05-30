
/* Schema for user database */

CREATE TABLE subscriber (
   id       INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,
   phone    CHAR(13)                       NOT NULL,
   role_arn CHAR(50)                       NOT NULL,
   username CHAR(50)                       NOT NULL,
   created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


/* Triggers */
CREATE TRIGGER [UpdateLastTime]  
    AFTER   
    UPDATE  
    ON subscriber
    FOR EACH ROW   
    WHEN NEW.updated <= OLD.updated  
BEGIN  
    UPDATE subscriber SET updated=CURRENT_TIMESTAMP WHERE id=OLD.id;  
END;
