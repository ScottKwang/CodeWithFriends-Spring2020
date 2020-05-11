const express = require("express");
const app = express();
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
const cors = require("cors");
const config = require("./config/db.config");
const listRoute = require('./controller/list.routes')

const port = 3000;

mongoose.Promise = global.Promise
mongoose.connect(config.DB, { useNewUrlParser: true, useUnifiedTopology: true }).then(() => 
   {console.log('connection to database succesful')},
   err => {console.log('error ' + err)}
);
mongoose.set('useFindAndModify', false);

app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use('/', listRoute)

app.listen(port, console.log("Server started on: " + port));
