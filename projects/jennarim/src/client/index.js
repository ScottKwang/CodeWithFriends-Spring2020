import React from 'react';
import ReactDOM from 'react-dom';
import "./public/stylesheets/styles.css";

// import Header from './components/Header/index.jsx';
import Logo from './components/Logo/index.js';
import Canvas from './components/Canvas/index.js';

const socket = io();

class App extends React.Component {
	constructor(props) {
		super(props);
	}
	
	componentDidMount() {
		socket.emit('new player');

		// const paddlePos = {};

		// document.addEventListener('mousemove', function(event) {
		// 	// set paddlePos to player x and y coor
		// 	paddlePos.x = event.clientX;
		// 	paddlePos.y = event.clientY;
		// });

		// setInterval(function() {
		// 	socket.emit('player move', paddlePos);
		// }, 1000 / 60);

		// SAMPLE CODE
		/*
		const movement = {
			up: false,
			down: false,
			left: false,
			right: false
		};
		document.addEventListener('keydown', function(event) {
			switch (event.keyCode) {
				case 65: // At
					movement.left = true;
					break;
				case 87: // W
					movement.up = true;
					break;
				case 68: // D
					movement.right = true;
					break;
				case 83: // s
					movement.down = true;
					break;
			}
		});
		document.addEventListener('keyup', function(event) {
			switch (event.keyCode) {
			  case 65: // A
				movement.left = false;
				break;
			  case 87: // W
				movement.up = false;
				break;
			  case 68: // D
				movement.right = false;
				break;
			  case 83: // S
				movement.down = false;
				break;
			}
		});
		setInterval(function() {
			socket.emit('movement', movement);
		}, 1000 / 60);
		socket.on('message', function(data){
			console.log(data);
		});
		socket.on('other connect', function(data) {
			console.log(data);
		});
		*/
	}

	render() {
		return (
			<div className="container">
				Welcome to React!
				<Canvas socket={socket}/>
				<Logo />
			</div>
		);
	}
}
ReactDOM.render(<App />, document.getElementById('root'));