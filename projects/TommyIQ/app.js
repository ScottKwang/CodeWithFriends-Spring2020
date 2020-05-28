const express = require('express');
const mongoose = require('mongoose');
const session = require('express-session');
const passport = require('passport');
const flash = require('connect-flash');
// require config
const config = require('./config/config');

const app = express();


// setting server and socket.io
const server = require('http').createServer(app);
const io = require('./socketIO/socketServer')(server);


// parsing json
app.use(express.json());

// passport config
require('./config/passport')(passport);


// DB config
const db = config.MongoURI;
// Connect to DB
mongoose.connect(db, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => {console.log('MongoDB connected...')})
  .catch(err => console.log(err));


// Set up template engine
app.set('view engine', 'ejs');


// Static files
app.use('/public', express.static('public'));


// BodyParser
app.use(express.urlencoded( { extended: false } ));


// express session
app.use(session({
  secret: 'secret',
  resave: true,
  saveUninitialized: true
}));


// passport middleware
app.use(passport.initialize());
app.use(passport.session());


// Connect flash
app.use(flash());


// Global variables for register and login
app.use((req, res, next) => {
  res.locals.success_msg = req.flash('success_msg');
  res.locals.error_msg = req.flash('error_msg');
  res.locals.email = req.flash('email');
  res.locals.error = req.flash('error');
  next();
});


// Global variables for account and posting files
app.use((req, res, next) => {
  res.locals.success_file_msg = req.flash('success_file_msg');
  res.locals.file_msg = req.flash('file_msg');
  res.locals.share_link = req.flash('share_link');
  res.locals.find_msg = req.flash('find_msg');
  res.locals.error_delete_msg = req.flash('error_delete_msg');
  res.locals.success_delete_msg = req.flash('success_delete_msg');
  next();
});


// vars for rooms and searching users
app.use((req, res, next) => {
  res.locals.search_msg_fail = req.flash('search_msg_fail');
  next();
});



// routes
app.use('/', require('./routes/index'));
app.use('/account', require('./routes/account'));
app.use('/users', require('./routes/users'));
app.use('/direct', require('./routes/direct'));

app.get('*', (req, res) => {
  res.redirect('/');
});


// Setting port
const PORT = process.env.PORT || config.PORT;

server.listen(PORT);

// app.listen(PORT, () => {
//   console.log(`Listening on ${PORT}`);
// })