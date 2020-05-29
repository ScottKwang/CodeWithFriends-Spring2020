import React, { Component } from 'react';

class Timer extends Component {

    render() {

        return (
            <div className="timer-container">
                <h1>{this.props.timer}</h1>
                <button className="play-pause" onClick={this.props.playPauseTimer}>{this.props.button}</button>
            </div>
        )
    }



    componentWillUnmount() {
        clearInterval(this.myInterval)
    }
}



export default Timer