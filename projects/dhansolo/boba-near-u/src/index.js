import React from 'react';
import ReactDOM from 'react-dom'
import axios from 'axios'
import './index.css';
import boba from './img/boba.png'

import Grid from '@material-ui/core/Grid';
import CircularProgress from '@material-ui/core/CircularProgress';
import Divider from '@material-ui/core/Divider';

let lat;
let long;
let intro;
let results;
let notFound;
let loadingAnim;

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: null,
            loading: false,
            displayIntro: true,
            displayResults: false,
            displayNotFound: false
        }
        this.handleBobaClick = this.handleBobaClick.bind(this);
    }

    componentDidMount() {
        if(navigator.geolocation) {
            // console.log("getting current location")
            navigator.geolocation.getCurrentPosition(
            function(position) {
                lat = position.coords.latitude;
                long = position.coords.longitude;
                // console.log("location acquired");
                document.getElementById('current-location').innerText = "Current Location: " + lat.toFixed(3) + ", " + long.toFixed(3);
                document.getElementById('boba-button').style.display = "inline";
            },
            function(error) {
                // console.log("permission denied");
                document.getElementById("intro").remove();
                document.getElementById("boba-button").remove();
                document.getElementById('current-location').innerText = "Please enable location permissions and refresh"

            });
        } else {
            console.log("What'd you do bruh?");
        }
    }

    handleBobaClick() {
        if(!lat || !long || this.state.loading) { return; }
        this.setState({ loading: true });
        if(navigator.geolocation) {
            // console.log("getting current location")
            navigator.geolocation.getCurrentPosition(
            function(position) {
                lat = position.coords.latitude;
                long = position.coords.longitude;
                document.getElementById('current-location').innerText = "Current Location: " + lat.toFixed(3) + ", " + long.toFixed(3);
            },
            function(error) {
                console.log(error);
            });
        } else {
            console.log("What'd you do bruh?");
        }
        if(lat && long) {
            intro = null;
            results = null;
            document.getElementById('boba-button').innerText = "Refresh";
            document.getElementById('boba-button').style.width = "100px";
            this.setState({ displayIntro: false, displayResults: false });
            axios({
                "method":"GET",
                "url":"https://cors-anywhere.herokuapp.com/api.yelp.com/v3/businesses/search",
                "headers":{
                    "Access-Control-Allow-Origin":"*",
                    "Authorization": `Bearer ${process.env.REACT_APP_YELP_KEY}`,
                },
                "params":{
                    "term":process.env.REACT_APP_SEARCH_TERM,
                    "latitude": lat,
                    "longitude": long,
                    "sort_by":"distance",
                    "limit": 1,
                    "radius": 40000
                }
            })
            .then((response)=> {
                if(response.data.businesses.length > 0) {
                    this.setState({
                        data: response.data.businesses,
                        displayResults: true,
                        loading: false
                    })
                    // console.log(this.state.data)
                    loadingAnim = null;
                } else {
                    this.setState({
                        displayNotFound: true,
                        loading: false
                    })
                }
            })
            .catch((error)=> {
                console.log(error);
                return;
            })
        }
    }

    render() {
        if(this.state.displayIntro) { intro = <IntroPage /> }
        if(this.state.displayResults) {  results = <DisplayNearest data={this.state.data}/>}
        if(this.state.displayNotFound) { notFound = <NoBobaFoundPage />}
        if(this.state.loading) { loadingAnim = <LoadAnimation />} else { loadingAnim = null }
        return (
            <div>
                <Grid container justify="center">
                    <div id="main">
                        <h1>Boba Near U</h1>
                        <div id="current-location">Current Location: Loading...</div>
                        {intro}
                        <button id="boba-button" onClick={this.handleBobaClick}>Find Me Boba!</button>
                        {loadingAnim}
                        {results}
                        {notFound}
                    </div>
                </Grid>
                <div id="boba-image">
                    <img alt="boba" src={boba} height="250" width="250"></img>
                </div>
            </div>
        )
    }
}

class IntroPage extends React.Component {
    render() {
        return (
            <div id="intro">
                <p>This application that takes your current location and finds the nearest boba/bubbletea</p>
                <p>Please be sure to enable location permissions and click on the button below to begin</p>
            </div>
        )
    }
}

class NoBobaFoundPage extends React.Component {
    redner() {
        return (
            <div>
                <p>No Boba/Bubbletea joints near you</p>
            </div>
        )
    }
}

class DisplayNearest extends React.Component {
    render() {
        let distance = (this.props.data[0].distance * 0.000621371)
        return (
            <div id="results">
                <h1>{this.props.data[0].name}</h1>
                <p>
                    {this.props.data[0].location.address1}<br></br>
                    {this.props.data[0].location.city}, {this.props.data[0].location.state} {this.props.data[0].location.zip_code}<br></br>
                    {this.props.data[0].display_phone}<br></br>
                </p>
                <p>{distance.toFixed(3)} miles away</p>
                <p><a href={this.props.data[0].url}>View on Yelp</a></p>
                <Divider orientation="vertical" flexItem/>
            </div>
        )
    }
}

class LoadAnimation extends React.Component {
    render() {
        return (
            <div id="loading">
                <CircularProgress />
            </div>
        )
    }
}

ReactDOM.render(<App />, document.getElementById('root'));