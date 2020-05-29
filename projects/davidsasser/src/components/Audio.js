import React, { Component } from 'react';


class Audio extends Component {

    render() {

        return (
            <div className="audio-play-pause-container">
                <button className="audio-play-pause" onClick={this.props.playPauseAudio}>{this.props.button}</button>
            </div>
        )
    }


}

export default Audio