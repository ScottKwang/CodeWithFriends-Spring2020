import React, { Component } from 'react';
import Timer from '../components/Timer'
import Audio from '../components/Audio'
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
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

let audioOptions1 = [
    { name: "Rain", src: rain, audio: rainAudio },
    { name: "Thunderstorm", src: thunder, audio: thunderAudio },
    { name: "Rainforest", src: rainforest, audio: rainforestAudio }
]
let audioOptions2 = [
    { name: "Classical Music", src: classical, audio: classicalAudio },
    { name: "Jazz Music", src: jazz, audio: jazzAudio }
]

class HomeContainer extends Component {
    constructor() {
        super();
        this.state = {
            timerLength: 1500,
            volume: 50,
            timer: null,
            hasStarted: false,
            isPaused: true,
            audioIsPaused: true,
            selectedAudio: null,
            audioSource: null,
            showModal: false
        };
        this.playPauseTimer = this.playPauseTimer.bind(this);
        this.resetTimer = this.resetTimer.bind(this);
        this.selectAudio = this.selectAudio.bind(this);
        this.playPauseAudio = this.playPauseAudio.bind(this);
        this.restartSound = this.restartSound.bind(this);
        this.volumeChange = this.volumeChange.bind(this);
        this.changeTimer = this.changeTimer.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }


    handleClose() {
        const input = document.getElementById('minutes');
        if (input.value) {
            clearInterval(this.myInterval)
            this.setState({
                timerLength: (input.value * 60),
                showModal: false,
                hasStarted: false,
                timer: null,
                isPaused: true
            })
        }
        else {
            this.setState({
                showModal: false
            })
        }

    }

    changeTimer() {
        this.setState({
            showModal: true
        })
    }

    volumeChange(e, val) {
        this.setState({
            volume: val
        })
        console.log(val)
        console.log(this.state.volume)
        this.player.volume = val / 100;
    }

    restartSound() {
        this.player.play();
    }

    selectAudio(name, audioSource) {
        this.setState({
            selectedAudio: name,
            audioSource: audioSource,
            audioIsPaused: false
        });
        this.player.src = audioSource;
        this.player.play();
        this.player.volume = this.state.volume / 100;
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
        this.player.pause();
        this.setState({
            hasStarted: false,
            timer: null,
            isPaused: true,
            audioIsPaused: true
        })
        alert("Your timer has ended. Take a little break.")
    }

    playPauseTimer() {
        if (this.state.audioIsPaused && this.state.selectedAudio != null && this.state.audioSource != null) {
            this.player.play();
            this.setState({
                audioIsPaused: false
            })
        }
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
        var setTimer = getTimer(this.state.timerLength)
        let button
        let playPause = (!this.state.audioIsPaused) ? <PauseIcon /> : <PlayArrowIcon />
        if (this.state.isPaused) {
            button = "Start  Timer"
        }
        else {
            button = "Pause Timer"
        }
        const listItems1 = audioOptions1.map((opt) =>
            <div className="audio-container"><button onClick={(e) => this.selectAudio(opt.name, opt.audio, e)} className={opt.name === this.state.selectedAudio ? 'image-container selected' : 'image-container'}><img alt="" src={opt.src}></img><label>{opt.name}</label></button></div>
        );

        const listItems2 = audioOptions2.map((opt) =>
            <div className="audio-container"><button onClick={(e) => this.selectAudio(opt.name, opt.audio, e)} className={opt.name === this.state.selectedAudio ? 'image-container selected' : 'image-container'}><img alt="" src={opt.src}></img><label>{opt.name}</label></button></div>
        );
        return (
            <div>
                <Modal centered show={this.state.showModal} onHide={this.handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Change Timer</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <div className="edit-time">
                            <label for="minutes" className="minutes-label">Minutes: </label>
                            <input placeholder={setTimer.min} type="number" className="minutes" id="minutes" name="minutes" min="00" max="59"></input>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            Close
                        </Button>
                        <Button variant="primary" onClick={this.handleClose}>
                            Save Changes
                        </Button>
                    </Modal.Footer>
                </Modal>
                <Audio handleChange={this.volumeChange} button={playPause} playPauseAudio={this.playPauseAudio} />


                <Timer changeTimer={this.changeTimer} timer={timer} button={button} playPauseTimer={this.playPauseTimer} />

                <div className="audio-list">
                    <div className="row1">{listItems1}</div>
                    <br></br>
                    <div className="row2">{listItems2}</div>
                    <audio ref={ref => (this.player = ref)} onEnded={this.restartSound} />
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

function getTimer(timerInSeconds) {
    var min = Math.floor(timerInSeconds / 60);

    return { "min": min }
}

export default HomeContainer;