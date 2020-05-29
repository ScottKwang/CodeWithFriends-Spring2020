import React, { Component } from 'react';
import Timer from '../components/Timer'
import Audio from '../components/Audio'
import Header from '../components/Header'
import PlayArrowIcon from '@material-ui/icons/PlayArrow';
import PauseIcon from '@material-ui/icons/Pause';
import rain from '../assets/images/audioImages/rain.png';
import thunder from '../assets/images/audioImages/lightning.png';
import rainforest from '../assets/images/audioImages/rainforest.png';
import classical from '../assets/images/audioImages/violin.png';
import jazz from '../assets/images/audioImages/jazz.png';
import rainAudio from '../assets/sounds/rain.mp3'
import thunderAudio from '../assets/sounds/thunderstorm.mp3'
import classicalAudio from '../assets/sounds/classical.mp3'
import rainforestAudio from '../assets/sounds/rainforest.mp3'
import jazzAudio from '../assets/sounds/jazz.mp3'

let audioOptions1 = [
    { name: "Rain", src: rain, audio: rainAudio },
    { name: "Thunderstorm", src: thunder, audio: thunderAudio },
    { name: "Rainforest", src: rainforest, audio: rainforestAudio }
]
let audioOptions2 = [
    { name: "Classical Music", src: classical, audio: classicalAudio },
    { name: "Jazz Music", src: jazz, audio: jazzAudio },
]

class HomeContainer extends Component {
    constructor() {
        super();
        this.state = {
            timerLength: 1500,
            timer: null,
            hasStarted: false,
            isPaused: true,
            audioIsPaused: true,
            selectedAudio: null,
            audioSource: null
        };
        this.playPauseTimer = this.playPauseTimer.bind(this);
        this.resetTimer = this.resetTimer.bind(this);
        this.selectAudio = this.selectAudio.bind(this);
        this.playPauseAudio = this.playPauseAudio.bind(this);
    }

    selectAudio(name, audioSource) {
        this.setState({
            selectedAudio: name,
            audioSource: audioSource,
            audioIsPaused: false
        });
        this.player.src = audioSource;
        this.player.play();
    }

    playPauseAudio() {
        if (this.state.selectedAudio == null && this.state.audioIsPaused) {
            alert("You must select an audio type before it will play.")
        }
        else if (this.state.selectedAudio != null && this.state.audioIsPaused) {
            this.player.play();
            this.setState({
                audioIsPaused: !this.state.audioIsPaused
            })
        }
        else if (this.state.selectedAudio != null && !this.state.audioIsPaused) {
            this.player.pause();
            this.setState({
                audioIsPaused: !this.state.audioIsPaused
            })
        }
    }

    resetTimer() {
        this.setState({
            hasStarted: false,
            timer: null,
            isPaused: true
        })
    }

    playPauseTimer() {
        if (!this.state.hasStarted) {
            let timer = this.state.timerLength
            this.setState({
                timer: timer
            })
        }
        if (this.state.isPaused) {
            this.setState({
                hasStarted: true
            })
            this.myInterval = setInterval(() => {
                if (this.state.timer === 0) {

                    clearInterval(this.myInterval)
                    this.resetTimer()
                }
                else {
                    this.setState(prevState => ({
                        timer: prevState.timer - 1
                    }))
                }
            }, 1000);
        }
        else {
            clearInterval(this.myInterval)
        }
        this.setState({
            isPaused: !this.state.isPaused
        })
    }
    render() {
        var timer

        if (this.state.hasStarted) {
            timer = formatTimerHTML(this.state.timer)
        }
        else {
            timer = formatTimerHTML(this.state.timerLength)
        }
        let button
        let playPause = (!this.state.audioIsPaused) ? <PauseIcon /> : <PlayArrowIcon />
        if (this.state.isPaused) {
            button = "Start  Timer"
        }
        else {
            button = "Pause Timer"
        }
        const listItems1 = audioOptions1.map((opt) =>
            <div className="audio-container"><button onClick={(e) => this.selectAudio(opt.name, opt.audio, e)} className={opt.name === this.state.selectedAudio ? 'image-container selected' : 'image-container'}><img src={opt.src}></img><label>{opt.name}</label></button></div>
        );

        const listItems2 = audioOptions2.map((opt) =>
            <div className="audio-container"><button onClick={(e) => this.selectAudio(opt.name, opt.audio, e)} className={opt.name === this.state.selectedAudio ? 'image-container selected' : 'image-container'}><img src={opt.src}></img><label>{opt.name}</label></button></div>
        );
        return (
            <div>
                <Header />

                <Audio button={playPause} playPauseAudio={this.playPauseAudio} />


                <Timer timer={timer} button={button} playPauseTimer={this.playPauseTimer} />

                <div className="audio-list">
                    <div className="row1">{listItems1}</div>
                    <br></br>
                    <div className="row2">{listItems2}</div>
                    <audio ref={ref => (this.player = ref)} />
                </div>
            </div>
        );
    }

    componentWillUnmount() {
        clearInterval(this.myInterval)
        this.setState({
            selectedAudio: null
        })
    }
}

function formatTimerHTML(timerInSeconds) {
    var seconds = timerInSeconds % 60;
    var min = Math.floor(timerInSeconds / 60);
    if (seconds < 10) {
        seconds = `0${seconds.toString()}`
    }
    if (min < 10) {
        min = `0${min.toString()}`
    }
    return `${min}:${seconds}`
}

export default HomeContainer;