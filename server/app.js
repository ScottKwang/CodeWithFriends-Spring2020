const createError = require('http-errors');
const express = require('express');
const app = express();
const path = require('path');

const indexRouter = require('./routes/index');

// socket.io setup
const server = require('http').Server(app);
const io = require('socket.io')(server); // attach server to existing http server

const gameState = {
	players: {}
}

let interval;

// define what server will do on connection
io.on('connection', socket => {
	if (interval) {
		clearInterval(interval);
	}

	socket.on('new player', (name) => {
		gameState.players[socket.id] = {
			name: name
		};
		console.log(gameState.players[socket.id]);
	});

	// if (players.length === 1) {
	// 	io.to(players[0]).emit('party leader', true);
	// }

	console.log(socket.id, 'has connected!');

	socket.on('disconnect', function() {
		// if (socket.id === players[0]) {
		// 	// Let everyone know if party leader leaves
		// 	players = [];
		// 	socket.broadcast.emit('party leader disconnected', 'Party leader disconnected. Please refresh the page.');
		// } else {
		// 	// Remove disconnected player on server
		// 	const indexOfDisconnectedPlayer = players.indexOf(socket.id);
		// 	players.splice(indexOfDisconnectedPlayer, 1);

		// 	// Let everyone know someone disconnected
		// }
		delete gameState.players[socket.id];
		clearInterval(interval);
		// console.log(socket.id, 'disconnected');
	});

	interval = setInterval(() => {
		io.sockets.emit('state', gameState);
	}, 1000/60);

	socket.on('chat message', function(msg) {
		console.log('got message:', msg);
		io.sockets.emit('chat message', msg);
	});

	socket.on('mouse', function(data) {
		data.id = socket.id;
		console.log(data);
		socket.broadcast.emit('other mouse', data);
	});

	socket.on('direction', function(direction) {
		const data = socket.id + " inputted " + direction;
		console.log(data);
	})
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


server.listen(3000);

module.exports = app;
