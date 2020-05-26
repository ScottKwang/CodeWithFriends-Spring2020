import React from 'react';
import ReactDOM from 'react-dom';
import "./public/stylesheets/styles.css";

import Logo from './components/Logo/index.js';
import Canvas from './components/Canvas/index.js';
import Lobby from './components/Lobby/index.js';

const socket = io();

class App extends React.Component {
	constructor(props) {
		super(props);
	}

	render() {
		return (
			<div className="container">
				Welcome to React! <br></br>
				<Lobby socket={socket}/> <br></br>
				<Canvas socket={socket}/> <br></br>
				{/* <Logo /> */}
			</div>
		);
	}
}
ReactDOM.render(<App />, document.getElementById('root'));