import React, { Component } from 'react';
import Grid from '@material-ui/core/Grid';
import Slider from '@material-ui/core/Slider';
import VolumeDown from '@material-ui/icons/VolumeDown';
import VolumeUp from '@material-ui/icons/VolumeUp';

class Audio extends Component {

    render() {

        return (
            <div className="audio-play-pause-container">
                <div className="audio-btn-container">
                    <button className="audio-play-pause" onClick={this.props.playPauseAudio}>{this.props.button}</button>
                </div>

                <Grid container spacing={2}>
                    <Grid item>
                        <VolumeDown />
                    </Grid>
                    <Grid item xs>
                        <Slider defaultValue={50} onChange={(e, val) => this.props.handleChange(e, val)} aria-labelledby="continuous-slider" />
                    </Grid>
                    <Grid item>
                        <VolumeUp />
                    </Grid>
                </Grid>
            </div>
        )
    }


}

export default Audio