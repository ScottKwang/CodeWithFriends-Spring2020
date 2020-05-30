import React, { Component } from 'react';

class Timer extends Component {

    render() {

        return (
            <div className="timer-container">
                <div onClick={this.props.changeTimer} className="timer-number">
                    <h1>{this.props.timer}</h1>
                </div>
                <button className="play-pause" onClick={this.props.playPauseTimer}>{this.props.button}</button>
            </div>
        )
    }



    componentWillUnmount() {
        clearInterval(this.myInterval)
    }
}



export default Timer