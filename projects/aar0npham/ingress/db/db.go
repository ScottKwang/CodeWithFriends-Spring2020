package db

import (
	"database/sql"
	"errors"
	"time"
)

var db *sql.DB

var connected bool

//ErrErrNotConnected should be thrown when no connection to the database
var ErrNotConnected error = errors.New("no connection to database")

// RetryTimeout - time to wait before reconnecting to database
const RetryTimeout time.Duration = 10 * time.Second

// PingPingTimeout - time wait before cancel ping the database
const PingTimeout time.Duration = 3 * time.Second
