import React from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import './index.css';

import * as moment from 'moment/moment';

import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import MenuItem from '@material-ui/core/MenuItem';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';
import Avatar from '@material-ui/core/Avatar';
import Chip from '@material-ui/core/Chip';
import CircularProgress from '@material-ui/core/CircularProgress';
import Link from '@material-ui/core/Link';

import {VictoryChart, VictoryZoomContainer, VictoryTooltip, VictoryLabel, VictoryLine, VictoryScatter} from 'victory';

// Create a date string in the format YYYY-MM-DD
let date = moment().subtract(1, 'days');
let current;
let currentDate;
if(date.month() + 1 < 10) {
    if(date.date() < 10) {
        currentDate = date.year() + "-0" + (date.month() + 1) + "-0" + date.date();
    } else {
        currentDate = date.year() + "-0" + (date.month() + 1) + "-" + date.date();
    }
} else {
    if(date.date() < 10) {
        currentDate = date.year() + "-" + (date.month() + 1) + "-0" + date.date();
    } else {
        currentDate = date.year() + "-" + (date.month() + 1) + "-" + date.date();
    }
}

let thirtyDayArray = [];
let info = null;
let type = 'County';

let minConfirmed = null;
let minDeaths = null;
let maxConfirmed = null;
let maxDeaths = null;


class CountyData extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            county: "",
            state: "",
            loading: false,
            data: null,
            notFound: false,
        }
        this.handleCountyChange = this.handleCountyChange.bind(this);
        this.handleStateChange = this.handleStateChange.bind(this);
        this.handleSearchClick = this.handleSearchClick.bind(this);
    }

    // Every keypress in the County input field will cause an update
    handleCountyChange(event) {
        if(event.target.value) {
            this.setState({
                county: event.target.value,
                data: null,
                notFound: false
            });
        } else if(event.target.value === "") {
            this.setState({
                county: "",
                data: null,
                notFound: false
            });
        }
    }

    // When a state is selected on the dropdown menu
    handleStateChange(event) {
        this.setState({
            state: event.target.value,
            data: null,
            notFound: false
        });
    }

    // This is where all of the movement occurs
    handleSearchClick() {
        if(this.state.county === "" || this.state.state === "") { return; }
        // Reset all necessary variables before you search again
        thirtyDayArray = [];
        this.setState({ data: null});
        info = null;
        minConfirmed = null;
        minDeaths = null;
        maxConfirmed = null;
        maxDeaths = null;
        let count = 0;

        this.setState({ county: this.state.county, loading: true, notFound: false, data: null}); 
        // Looping 7 times to get 7 days worth of data
        while(count < 30) {  
            date = moment().subtract(1 + count, 'days');
            current = "";
            if(date.month() + 1 < 10) {
                if(date.date() < 10) {
                    current = date.year() + "-0" + (date.month() + 1) + "-0" + date.date();
                } else {
                    current = date.year() + "-0" + (date.month() + 1) + "-" + date.date();
                }
            } else {
                if(date.date() < 10) {
                    current = date.year() + "-" + (date.month() + 1) + "-0" + date.date();
                } else {
                    current = date.year() + "-" + (date.month() + 1) + "-" + date.date();
                }
            }
            this.getData(current);
            count++;
        }
    }

    // Function to make the Axios http request so that the above looks cleaner
    getData(current) {
        // API doesn't like the word 'county' so I'm reformatting the user's terms here to omit 'county'
        // as well as adjusting the casing as the API likes exactness
        let reformattedCounty = this.state.county.toLowerCase();
        if(reformattedCounty.includes("county")) {
            reformattedCounty = reformattedCounty.substring(0, reformattedCounty.indexOf("county") - 1);
        } else if(reformattedCounty.includes("borough")) {
            reformattedCounty = reformattedCounty.substring(0, reformattedCounty.indexOf("borough") - 1);
        } else if(reformattedCounty.includes("parish")) {
            reformattedCounty = reformattedCounty.substring(0, reformattedCounty.indexOf("parish") - 1);
        }
        return axios({
            "method":"GET",
            "url":"https://covid-19-statistics.p.rapidapi.com/reports",
            "headers":{
                "content-type":"application/octet-stream",
                "x-rapidapi-host": process.env.REACT_APP_HOST,
                "x-rapidapi-key": process.env.REACT_APP_API_KEY
            },
            "params":{
                "region_province":this.state.state,
                "iso":"USA",
                "region_name":"US",
                "city_name":reformattedCounty,
                "date":current,
                "q":"US " + this.state.state
            }
        })
        .then((response)=>{
            if(response.data.data.length > 0) {
                if(current === currentDate) {
                    this.setState({ data: response.data.data[0].region.cities[0], notFound: false});
                }
                if(response.data.data[0].region.cities[0].confirmed_diff < minConfirmed || minConfirmed === null) { 
                    minConfirmed = response.data.data[0].region.cities[0].confirmed_diff; 
                }
                if(response.data.data[0].region.cities[0].confirmed_diff > maxConfirmed || maxConfirmed === null) { 
                    maxConfirmed = response.data.data[0].region.cities[0].confirmed_diff; 
                }
                if(response.data.data[0].region.cities[0].deaths_diff < minDeaths || minDeaths === null) { 
                    minDeaths = response.data.data[0].region.cities[0].deaths_diff; 
                }
                if(response.data.data[0].region.cities[0].deaths_diff > maxDeaths || maxDeaths === null) { 
                    maxDeaths = response.data.data[0].region.cities[0].deaths_diff; 
                }
                // Push data into thirtyDayArray array
                thirtyDayArray.push(
                    {
                        date: current,
                        confirmed: response.data.data[0].region.cities[0].confirmed_diff,
                        deaths: response.data.data[0].region.cities[0].deaths_diff
                    }
                )
                // Sort it by date after every push
                thirtyDayArray.sort((a, b) => new Date(a.date) - new Date(b.date));
                if(thirtyDayArray.length === 30) {
                    // Will not stop loading until there is exactly 7 items in the array
                    this.setState({ loading: false })
                }
            } else {
                // Data not found
                this.setState({ data: null, notFound: true, loading: false});
            }
        })
        .catch((error)=>{
            console.log(error);
            return;
        }) 
    }

    render() {
        if(this.state.data && !this.state.loading) {
            // Throwing everything to the Info component to render
            info = <Info 
                confirmed={this.state.data.confirmed} 
                deaths={this.state.data.deaths} 
                county={this.state.data.name} 
                state={this.state.state} 
                minConfirmed={minConfirmed} 
                minDeaths={minDeaths}
                maxConfirmed={maxConfirmed}
                maxDeaths={maxDeaths}
                />;
        }
        let loadImage = null;
        let notfound = null;
        if(this.state.loading) {loadImage = <LoadingScreen id="loading"/>}
        if(this.state.notFound) {notfound = <NotFound />}
        return (
            <div>
                <div class="search">
                    <TextField id="search-county" type="text" label="County" variant="filled" value={this.state.county} onChange={this.handleCountyChange}></TextField>
                    <FormControl variant="filled" id="search-state">
                        <InputLabel>State</InputLabel>
                        <Select type="text" value={this.state.state} onChange={this.handleStateChange}>
                            <MenuItem value="Alabama">Alabama</MenuItem>
                            <MenuItem value="Alaska">Alaska</MenuItem>
                            <MenuItem value="Arizona">Arizona</MenuItem>
                            <MenuItem value="Arkansas">Arkansas</MenuItem>
                            <MenuItem value="California">California</MenuItem>
                            <MenuItem value="Colorado">Colorado</MenuItem>
                            <MenuItem value="Connecticut">Connecticut</MenuItem>
                            <MenuItem value="Delaware">Delaware</MenuItem>
                            <MenuItem value="Florida">Florida</MenuItem>
                            <MenuItem value="Georgia">Georgia</MenuItem>
                            <MenuItem value="Hawaii">Hawaii</MenuItem>
                            <MenuItem value="Idaho">Idaho</MenuItem>
                            <MenuItem value="Illinois">Illinois</MenuItem>
                            <MenuItem value="Indiana">Indiana</MenuItem>
                            <MenuItem value="Iowa">Iowa</MenuItem>
                            <MenuItem value="Kansas">Kansas</MenuItem>
                            <MenuItem value="Kentucky">Kentucky</MenuItem>
                            <MenuItem value="Louisiana">Louisiana</MenuItem>
                            <MenuItem value="Maine">Maine</MenuItem>
                            <MenuItem value="Maryland">Maryland</MenuItem>
                            <MenuItem value="Massachusetts">Massachusetts</MenuItem>
                            <MenuItem value="Michigan">Michigan</MenuItem>
                            <MenuItem value="Minnesota">Minnesota</MenuItem>
                            <MenuItem value="Mississippi">Mississippi</MenuItem>
                            <MenuItem value="Missouri">Missouri</MenuItem>
                            <MenuItem value="Montana">Montana</MenuItem>
                            <MenuItem value="Nebraska">Nebraska</MenuItem>
                            <MenuItem value="Nevada">Nevada</MenuItem>
                            <MenuItem value="New Hampshire">New Hampshire</MenuItem>
                            <MenuItem value="New Jersey">New Jersey</MenuItem>
                            <MenuItem value="New Mexico">New Mexico</MenuItem>
                            <MenuItem value="New York">New York</MenuItem>
                            <MenuItem value="North Carolina">North Carolina</MenuItem>
                            <MenuItem value="North Dakota">North Dakota</MenuItem>
                            <MenuItem value="Ohio">Ohio</MenuItem>
                            <MenuItem value="Oklahoma">Oklahoma</MenuItem>
                            <MenuItem value="Oregon">Oregon</MenuItem>
                            <MenuItem value="Pennsylvania">Pennsylvania</MenuItem>
                            <MenuItem value="Rhode Island">Rhode Island</MenuItem>
                            <MenuItem value="South Carolina">South Carolina</MenuItem>
                            <MenuItem value="South Dakota">South Dakota</MenuItem>
                            <MenuItem value="Tennessee">Tennessee</MenuItem>
                            <MenuItem value="Texas">Texas</MenuItem>
                            <MenuItem value="Utah">Utah</MenuItem>
                            <MenuItem value="Vermont">Vermont</MenuItem>
                            <MenuItem value="Virginia">Virginia</MenuItem>
                            <MenuItem value="Washington">Washington</MenuItem>
                            <MenuItem value="West Virginia">West Virginia</MenuItem>
                            <MenuItem value="Wisconsin">Wisconsin</MenuItem>
                            <MenuItem value="Wyoming">Wyoming</MenuItem>
                        </Select>
                    </FormControl>
                </div>
                <Button id="search-button" variant="contained" color="secondary" onClick={this.handleSearchClick}>Search</Button>
                <div id="result">
                    {info}
                    {loadImage}
                    {notfound}
                </div>
            </div>
        )
    }
}

class Info extends React.Component {
    render() {
        let rate = (this.props.deaths/this.props.confirmed) * 100;
        if(this.props.state === "Alaska") { type = "Borough"}
        if(this.props.state === "Louisiana") { type = "Parish"}
        return (
            <div>
                <Grid container justify="center">
                    <div class="info">
                        {/* <div>Graphs are draggable to the left and right. Hovering over points will show the exact numbers.</div> */}
                        {/* <Paper variant='elevation' elevation={24}> */}
                            <h3>Statistics for {this.props.county} {type}, {this.props.state}</h3>
                            <Chip 
                                id="county-confirmed" 
                                label={
                                    <div>
                                        <div><b>Confirmed:</b></div>
                                        <div>{this.props.confirmed}</div>
                                    </div>
                                }>
                            </Chip>
                            <Chip 
                                id="county-deaths" 
                                label={
                                    <div>
                                        <div><b>Fatalities:</b></div>
                                        <div>{this.props.deaths}</div>
                                    </div>
                                }>
                            </Chip>
                            <Chip 
                                id="county-rate" 
                                label={
                                    <div>
                                        <div><b>Fatality Rate:</b></div>
                                        <div>{rate.toFixed(3) + "%"}</div>
                                    </div>
                                }>
                            </Chip>
                            {/* <p><b>Confirmed:</b> {this.props.confirmed} | <b>Fatalities:</b> {this.props.deaths} | <b>Approximate Fatality Rate:</b> {rate.toFixed(3)}%</p> */}
                        {/* </Paper> */}
                    </div>
                </Grid>
                <Grid container justify="center">
                    <Paper id="visualize-confirmed" variant='elevation' elevation={24}><VisualizeConfirmed county={this.props.county} minConfirmed={this.props.minConfirmed} maxConfirmed={this.props.maxConfirmed}/></Paper>
                    <Paper id="visualize-deaths" variant='elevation' elevation={24}><VisualizeDeaths county={this.props.county} minDeaths={this.props.minDeaths} maxDeaths={this.props.maxDeaths}/></Paper>
                </Grid>
            </div>
        )  
    }
}

class VisualizeConfirmed extends React.Component {
    render() {
        return(
            <div>
                {/* <p>Changes in Confirmed Cases Over 30 Days for {this.props.county} County</p> */}
                <VictoryChart containerComponent={<VictoryZoomContainer zoomDimension="x" zoomDomain={{x:[22,30]}} allowZoom={false}/>}>
                <VictoryLabel text={"Changes in Confirmed cases Over 30 days for " + this.props.county + " " + type} x={225} y={30} textAnchor="middle"/>
                <VictoryLine
                    name="dates"
                    interpolation="natural"
                    style={{
                        data: { stroke: "#c43a31" },
                        parent: { border: "1px solid black", width: "10%"},
                    }}
                    data={[
                        {x: thirtyDayArray[0].date.substring(5, thirtyDayArray[0].date.length), y: thirtyDayArray[0].confirmed},
                        {x: thirtyDayArray[1].date.substring(5, thirtyDayArray[1].date.length), y: thirtyDayArray[1].confirmed},
                        {x: thirtyDayArray[2].date.substring(5, thirtyDayArray[2].date.length), y: thirtyDayArray[2].confirmed},
                        {x: thirtyDayArray[3].date.substring(5, thirtyDayArray[3].date.length), y: thirtyDayArray[3].confirmed},
                        {x: thirtyDayArray[4].date.substring(5, thirtyDayArray[4].date.length), y: thirtyDayArray[4].confirmed},
                        {x: thirtyDayArray[5].date.substring(5, thirtyDayArray[5].date.length), y: thirtyDayArray[5].confirmed},
                        {x: thirtyDayArray[6].date.substring(5, thirtyDayArray[6].date.length), y: thirtyDayArray[6].confirmed},
                        {x: thirtyDayArray[7].date.substring(5, thirtyDayArray[7].date.length), y: thirtyDayArray[7].confirmed},
                        {x: thirtyDayArray[8].date.substring(5, thirtyDayArray[8].date.length), y: thirtyDayArray[8].confirmed},
                        {x: thirtyDayArray[9].date.substring(5, thirtyDayArray[9].date.length), y: thirtyDayArray[9].confirmed},
                        {x: thirtyDayArray[10].date.substring(5, thirtyDayArray[10].date.length), y: thirtyDayArray[10].confirmed},
                        {x: thirtyDayArray[11].date.substring(5, thirtyDayArray[11].date.length), y: thirtyDayArray[11].confirmed},
                        {x: thirtyDayArray[12].date.substring(5, thirtyDayArray[12].date.length), y: thirtyDayArray[12].confirmed},
                        {x: thirtyDayArray[13].date.substring(5, thirtyDayArray[13].date.length), y: thirtyDayArray[13].confirmed},
                        {x: thirtyDayArray[14].date.substring(5, thirtyDayArray[14].date.length), y: thirtyDayArray[14].confirmed},
                        {x: thirtyDayArray[15].date.substring(5, thirtyDayArray[15].date.length), y: thirtyDayArray[15].confirmed},
                        {x: thirtyDayArray[16].date.substring(5, thirtyDayArray[16].date.length), y: thirtyDayArray[16].confirmed},
                        {x: thirtyDayArray[17].date.substring(5, thirtyDayArray[17].date.length), y: thirtyDayArray[17].confirmed},
                        {x: thirtyDayArray[18].date.substring(5, thirtyDayArray[18].date.length), y: thirtyDayArray[18].confirmed},
                        {x: thirtyDayArray[19].date.substring(5, thirtyDayArray[19].date.length), y: thirtyDayArray[19].confirmed},
                        {x: thirtyDayArray[20].date.substring(5, thirtyDayArray[20].date.length), y: thirtyDayArray[20].confirmed},
                        {x: thirtyDayArray[21].date.substring(5, thirtyDayArray[21].date.length), y: thirtyDayArray[21].confirmed},
                        {x: thirtyDayArray[22].date.substring(5, thirtyDayArray[22].date.length), y: thirtyDayArray[22].confirmed},
                        {x: thirtyDayArray[23].date.substring(5, thirtyDayArray[23].date.length), y: thirtyDayArray[23].confirmed},
                        {x: thirtyDayArray[24].date.substring(5, thirtyDayArray[24].date.length), y: thirtyDayArray[24].confirmed},
                        {x: thirtyDayArray[25].date.substring(5, thirtyDayArray[25].date.length), y: thirtyDayArray[25].confirmed},
                        {x: thirtyDayArray[26].date.substring(5, thirtyDayArray[26].date.length), y: thirtyDayArray[26].confirmed},
                        {x: thirtyDayArray[27].date.substring(5, thirtyDayArray[27].date.length), y: thirtyDayArray[27].confirmed},
                        {x: thirtyDayArray[28].date.substring(5, thirtyDayArray[28].date.length), y: thirtyDayArray[28].confirmed},
                        {x: thirtyDayArray[29].date.substring(5, thirtyDayArray[29].date.length), y: thirtyDayArray[29].confirmed},
                    ]}
                    animate={{
                        duration: 2000,
                        opacity: 0.0,
                        onLoad: { 
                            duration: 1000,
                            opacity: 1.0
                        },
                    }}
                    domain={{
                        y: [0, this.props.maxConfirmed + 50]
                    }}
                />
                <VictoryScatter 
                    data={[
                        {x: thirtyDayArray[0].date.substring(5, thirtyDayArray[0].date.length), y: thirtyDayArray[0].confirmed},
                        {x: thirtyDayArray[1].date.substring(5, thirtyDayArray[1].date.length), y: thirtyDayArray[1].confirmed},
                        {x: thirtyDayArray[2].date.substring(5, thirtyDayArray[2].date.length), y: thirtyDayArray[2].confirmed},
                        {x: thirtyDayArray[3].date.substring(5, thirtyDayArray[3].date.length), y: thirtyDayArray[3].confirmed},
                        {x: thirtyDayArray[4].date.substring(5, thirtyDayArray[4].date.length), y: thirtyDayArray[4].confirmed},
                        {x: thirtyDayArray[5].date.substring(5, thirtyDayArray[5].date.length), y: thirtyDayArray[5].confirmed},
                        {x: thirtyDayArray[6].date.substring(5, thirtyDayArray[6].date.length), y: thirtyDayArray[6].confirmed},
                        {x: thirtyDayArray[7].date.substring(5, thirtyDayArray[7].date.length), y: thirtyDayArray[7].confirmed},
                        {x: thirtyDayArray[8].date.substring(5, thirtyDayArray[8].date.length), y: thirtyDayArray[8].confirmed},
                        {x: thirtyDayArray[9].date.substring(5, thirtyDayArray[9].date.length), y: thirtyDayArray[9].confirmed},
                        {x: thirtyDayArray[10].date.substring(5, thirtyDayArray[10].date.length), y: thirtyDayArray[10].confirmed},
                        {x: thirtyDayArray[11].date.substring(5, thirtyDayArray[11].date.length), y: thirtyDayArray[11].confirmed},
                        {x: thirtyDayArray[12].date.substring(5, thirtyDayArray[12].date.length), y: thirtyDayArray[12].confirmed},
                        {x: thirtyDayArray[13].date.substring(5, thirtyDayArray[13].date.length), y: thirtyDayArray[13].confirmed},
                        {x: thirtyDayArray[14].date.substring(5, thirtyDayArray[14].date.length), y: thirtyDayArray[14].confirmed},
                        {x: thirtyDayArray[15].date.substring(5, thirtyDayArray[15].date.length), y: thirtyDayArray[15].confirmed},
                        {x: thirtyDayArray[16].date.substring(5, thirtyDayArray[16].date.length), y: thirtyDayArray[16].confirmed},
                        {x: thirtyDayArray[17].date.substring(5, thirtyDayArray[17].date.length), y: thirtyDayArray[17].confirmed},
                        {x: thirtyDayArray[18].date.substring(5, thirtyDayArray[18].date.length), y: thirtyDayArray[18].confirmed},
                        {x: thirtyDayArray[19].date.substring(5, thirtyDayArray[19].date.length), y: thirtyDayArray[19].confirmed},
                        {x: thirtyDayArray[20].date.substring(5, thirtyDayArray[20].date.length), y: thirtyDayArray[20].confirmed},
                        {x: thirtyDayArray[21].date.substring(5, thirtyDayArray[21].date.length), y: thirtyDayArray[21].confirmed},
                        {x: thirtyDayArray[22].date.substring(5, thirtyDayArray[22].date.length), y: thirtyDayArray[22].confirmed},
                        {x: thirtyDayArray[23].date.substring(5, thirtyDayArray[23].date.length), y: thirtyDayArray[23].confirmed},
                        {x: thirtyDayArray[24].date.substring(5, thirtyDayArray[24].date.length), y: thirtyDayArray[24].confirmed},
                        {x: thirtyDayArray[25].date.substring(5, thirtyDayArray[25].date.length), y: thirtyDayArray[25].confirmed},
                        {x: thirtyDayArray[26].date.substring(5, thirtyDayArray[26].date.length), y: thirtyDayArray[26].confirmed},
                        {x: thirtyDayArray[27].date.substring(5, thirtyDayArray[27].date.length), y: thirtyDayArray[27].confirmed},
                        {x: thirtyDayArray[28].date.substring(5, thirtyDayArray[28].date.length), y: thirtyDayArray[28].confirmed},
                        {x: thirtyDayArray[29].date.substring(5, thirtyDayArray[29].date.length), y: thirtyDayArray[29].confirmed},
                    ]}
                    domain={{
                        y: [0, this.props.maxConfirmed + 50]
                    }}
                    size={6}
                    labels={({ datum }) => `${datum.y} new cases on ${datum.x}`}
                    labelComponent={<VictoryTooltip dy={0}/>}
                />
                </VictoryChart>
            </div>
        )
    }
}

class VisualizeDeaths extends React.Component {
    render() {
        return(
            <div>
                {/* <p>Number of Fatalities Over 30 Days for {this.props.county} County</p> */}
                <VictoryChart containerComponent={<VictoryZoomContainer zoomDimension="x" zoomDomain={{x:[22,30]}} allowZoom={false}/>}>
                    <VictoryLabel text={"Number of Fatalities Over 30 days for " + this.props.county + " " + type} x={225} y={30} textAnchor="middle"/>
                    <VictoryLine
                        interpolation="natural"
                        style={{
                            data: { stroke: "#c43a31" },
                            parent: { border: "1px solid black", width: "10%"}
                        }}
                        data={[
                            {x: thirtyDayArray[0].date.substring(5, thirtyDayArray[0].date.length), y: thirtyDayArray[0].deaths},
                            {x: thirtyDayArray[1].date.substring(5, thirtyDayArray[1].date.length), y: thirtyDayArray[1].deaths},
                            {x: thirtyDayArray[2].date.substring(5, thirtyDayArray[2].date.length), y: thirtyDayArray[2].deaths},
                            {x: thirtyDayArray[3].date.substring(5, thirtyDayArray[3].date.length), y: thirtyDayArray[3].deaths},
                            {x: thirtyDayArray[4].date.substring(5, thirtyDayArray[4].date.length), y: thirtyDayArray[4].deaths},
                            {x: thirtyDayArray[5].date.substring(5, thirtyDayArray[5].date.length), y: thirtyDayArray[5].deaths},
                            {x: thirtyDayArray[6].date.substring(5, thirtyDayArray[6].date.length), y: thirtyDayArray[6].deaths},
                            {x: thirtyDayArray[7].date.substring(5, thirtyDayArray[7].date.length), y: thirtyDayArray[7].deaths},
                            {x: thirtyDayArray[8].date.substring(5, thirtyDayArray[8].date.length), y: thirtyDayArray[8].deaths},
                            {x: thirtyDayArray[9].date.substring(5, thirtyDayArray[9].date.length), y: thirtyDayArray[9].deaths},
                            {x: thirtyDayArray[10].date.substring(5, thirtyDayArray[10].date.length), y: thirtyDayArray[10].deaths},
                            {x: thirtyDayArray[11].date.substring(5, thirtyDayArray[11].date.length), y: thirtyDayArray[11].deaths},
                            {x: thirtyDayArray[12].date.substring(5, thirtyDayArray[12].date.length), y: thirtyDayArray[12].deaths},
                            {x: thirtyDayArray[13].date.substring(5, thirtyDayArray[13].date.length), y: thirtyDayArray[13].deaths},
                            {x: thirtyDayArray[14].date.substring(5, thirtyDayArray[14].date.length), y: thirtyDayArray[14].deaths},
                            {x: thirtyDayArray[15].date.substring(5, thirtyDayArray[15].date.length), y: thirtyDayArray[15].deaths},
                            {x: thirtyDayArray[16].date.substring(5, thirtyDayArray[16].date.length), y: thirtyDayArray[16].deaths},
                            {x: thirtyDayArray[17].date.substring(5, thirtyDayArray[17].date.length), y: thirtyDayArray[17].deaths},
                            {x: thirtyDayArray[18].date.substring(5, thirtyDayArray[18].date.length), y: thirtyDayArray[18].deaths},
                            {x: thirtyDayArray[19].date.substring(5, thirtyDayArray[19].date.length), y: thirtyDayArray[19].deaths},
                            {x: thirtyDayArray[20].date.substring(5, thirtyDayArray[20].date.length), y: thirtyDayArray[20].deaths},
                            {x: thirtyDayArray[21].date.substring(5, thirtyDayArray[21].date.length), y: thirtyDayArray[21].deaths},
                            {x: thirtyDayArray[22].date.substring(5, thirtyDayArray[22].date.length), y: thirtyDayArray[22].deaths},
                            {x: thirtyDayArray[23].date.substring(5, thirtyDayArray[23].date.length), y: thirtyDayArray[23].deaths},
                            {x: thirtyDayArray[24].date.substring(5, thirtyDayArray[24].date.length), y: thirtyDayArray[24].deaths},
                            {x: thirtyDayArray[25].date.substring(5, thirtyDayArray[25].date.length), y: thirtyDayArray[25].deaths},
                            {x: thirtyDayArray[26].date.substring(5, thirtyDayArray[26].date.length), y: thirtyDayArray[26].deaths},
                            {x: thirtyDayArray[27].date.substring(5, thirtyDayArray[27].date.length), y: thirtyDayArray[27].deaths},
                            {x: thirtyDayArray[28].date.substring(5, thirtyDayArray[28].date.length), y: thirtyDayArray[28].deaths},
                            {x: thirtyDayArray[29].date.substring(5, thirtyDayArray[29].date.length), y: thirtyDayArray[29].deaths},
                        ]}
                        animate={{
                            duration: 2000,
                            opacity: 0.0,
                            onLoad: { 
                                duration: 1000,
                                opacity: 1.0
                            },

                        }}
                        domain={{
                            y: [0, this.props.maxDeaths]
                        }}
                    />
                    <VictoryScatter
                        data={[
                            {x: thirtyDayArray[0].date.substring(5, thirtyDayArray[0].date.length), y: thirtyDayArray[0].deaths},
                            {x: thirtyDayArray[1].date.substring(5, thirtyDayArray[1].date.length), y: thirtyDayArray[1].deaths},
                            {x: thirtyDayArray[2].date.substring(5, thirtyDayArray[2].date.length), y: thirtyDayArray[2].deaths},
                            {x: thirtyDayArray[3].date.substring(5, thirtyDayArray[3].date.length), y: thirtyDayArray[3].deaths},
                            {x: thirtyDayArray[4].date.substring(5, thirtyDayArray[4].date.length), y: thirtyDayArray[4].deaths},
                            {x: thirtyDayArray[5].date.substring(5, thirtyDayArray[5].date.length), y: thirtyDayArray[5].deaths},
                            {x: thirtyDayArray[6].date.substring(5, thirtyDayArray[6].date.length), y: thirtyDayArray[6].deaths},
                            {x: thirtyDayArray[7].date.substring(5, thirtyDayArray[7].date.length), y: thirtyDayArray[7].deaths},
                            {x: thirtyDayArray[8].date.substring(5, thirtyDayArray[8].date.length), y: thirtyDayArray[8].deaths},
                            {x: thirtyDayArray[9].date.substring(5, thirtyDayArray[9].date.length), y: thirtyDayArray[9].deaths},
                            {x: thirtyDayArray[10].date.substring(5, thirtyDayArray[10].date.length), y: thirtyDayArray[10].deaths},
                            {x: thirtyDayArray[11].date.substring(5, thirtyDayArray[11].date.length), y: thirtyDayArray[11].deaths},
                            {x: thirtyDayArray[12].date.substring(5, thirtyDayArray[12].date.length), y: thirtyDayArray[12].deaths},
                            {x: thirtyDayArray[13].date.substring(5, thirtyDayArray[13].date.length), y: thirtyDayArray[13].deaths},
                            {x: thirtyDayArray[14].date.substring(5, thirtyDayArray[14].date.length), y: thirtyDayArray[14].deaths},
                            {x: thirtyDayArray[15].date.substring(5, thirtyDayArray[15].date.length), y: thirtyDayArray[15].deaths},
                            {x: thirtyDayArray[16].date.substring(5, thirtyDayArray[16].date.length), y: thirtyDayArray[16].deaths},
                            {x: thirtyDayArray[17].date.substring(5, thirtyDayArray[17].date.length), y: thirtyDayArray[17].deaths},
                            {x: thirtyDayArray[18].date.substring(5, thirtyDayArray[18].date.length), y: thirtyDayArray[18].deaths},
                            {x: thirtyDayArray[19].date.substring(5, thirtyDayArray[19].date.length), y: thirtyDayArray[19].deaths},
                            {x: thirtyDayArray[20].date.substring(5, thirtyDayArray[20].date.length), y: thirtyDayArray[20].deaths},
                            {x: thirtyDayArray[21].date.substring(5, thirtyDayArray[21].date.length), y: thirtyDayArray[21].deaths},
                            {x: thirtyDayArray[22].date.substring(5, thirtyDayArray[22].date.length), y: thirtyDayArray[22].deaths},
                            {x: thirtyDayArray[23].date.substring(5, thirtyDayArray[23].date.length), y: thirtyDayArray[23].deaths},
                            {x: thirtyDayArray[24].date.substring(5, thirtyDayArray[24].date.length), y: thirtyDayArray[24].deaths},
                            {x: thirtyDayArray[25].date.substring(5, thirtyDayArray[25].date.length), y: thirtyDayArray[25].deaths},
                            {x: thirtyDayArray[26].date.substring(5, thirtyDayArray[26].date.length), y: thirtyDayArray[26].deaths},
                            {x: thirtyDayArray[27].date.substring(5, thirtyDayArray[27].date.length), y: thirtyDayArray[27].deaths},
                            {x: thirtyDayArray[28].date.substring(5, thirtyDayArray[28].date.length), y: thirtyDayArray[28].deaths},
                            {x: thirtyDayArray[29].date.substring(5, thirtyDayArray[29].date.length), y: thirtyDayArray[29].deaths},
                        ]}
                        animate={{
                            duration: 2000,
                            opacity: 0.0,
                            onLoad: { 
                                duration: 1000,
                                opacity: 1.0
                            },

                        }}
                        domain={{
                            y: [0, this.props.maxDeaths]
                        }}
                        size={6}
                        labels={({ datum }) => `${datum.y} fatalities on ${datum.x}`}
                        labelComponent={<VictoryTooltip dy={0}/>}
                    />
                </VictoryChart>
            </div>
        )
    }
}

class NotFound extends React.Component {
    render() {
        return (
            <div class="info">
                <p>
                No data found, are you sure you entered the correct County and State?
                </p>
                <p>
                Please note that data from John Hopkins University may contain some
                discrepencies.
                </p>
            </div>
        )
    }
}

class WorldData extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: null,
            loading: true
        }
    }
    componentDidMount() {
        axios({
            "method":"GET",
            "url":"https://covid-19-statistics.p.rapidapi.com/reports/total",
            "headers":{
                "content-type":"application/octet-stream",
                "x-rapidapi-host": process.env.REACT_APP_HOST,
                "x-rapidapi-key": process.env.REACT_APP_API_KEY
            },"params":{
                "date":currentDate
            }
            })
            .then((response)=>{
              this.setState({ data: response.data.data, loading: false});
            })
            .catch((error)=>{
              console.log(error)
            })
    }
    render() {
        let data;
        let loadImage;
        if(this.state.loading) {
            loadImage = <LoadingScreen />
        }
        if(this.state.data) {
            data = <DisplayWorldData data={this.state.data}/>
        }
        return (
            <div>
                {loadImage}
                {data}
            </div>
        )
    }
}

class LoadingScreen extends React.Component {
    render() {
        return (
            <CircularProgress />
        )
    }
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

class DisplayWorldData extends React.Component {
    render() {
        let rate = (this.props.data.fatality_rate * 100);
        return(
            <div>
                <h3>World Statistics</h3>
                <div>
                    <Chip 
                        id="world-confirmed" 
                        label={
                            <div>
                                <div><b>Confirmed:</b></div>
                                <div>{numberWithCommas(this.props.data.confirmed)}</div>
                            </div>
                        }>
                    </Chip>
                    <Chip 
                        id="world-recovered" 
                        label={
                            <div>
                                <div><b>Recovered:</b></div>
                                <div>{numberWithCommas(this.props.data.recovered)}</div>
                            </div>
                        }>
                    </Chip>
                    <Chip 
                        id="world-deaths" 
                        label={
                            <div>
                                <div><b>Fatalities:</b></div>
                                <div>{numberWithCommas(this.props.data.deaths)}</div>
                            </div>
                        }>
                    </Chip>
                    <Chip 
                        id="world-rate" 
                        label={
                            <div>
                                <div><b>Fatality Rate:</b></div>
                                <div>{rate.toFixed(3) + "%"}</div>
                            </div>
                        }>
                    </Chip>
                </div>
                <p></p>
            </div>
        )
    }
}

function App() {
    return (
        <Grid container justify="center">
            <Grid item xs={12}>
                <div class="main">
                    <div class="headers">
                        <h1>COVID-19 County Tracker</h1>
                        <Chip id="date-chip" label={"Data as of " + moment().subtract(1, 'days').format("MMMM DD, YYYY")}></Chip>
                    </div>
                    <header class="world-data"><WorldData /></header>
                    <div class="search"><CountyData /></div>
                    <footer>
                        <Link href="https://www.linkedin.com/in/davidhan93/"><Chip id="chip" avatar={<Avatar>DH</Avatar>} label="Created by David Han" clickable></Chip></Link>
                        <Link href="https://www.linkedin.com/in/john-son-997aaa175/"><Chip id="chip" avatar={<Avatar>JS</Avatar>} label="Styled by John Son" clickable></Chip></Link>
                        <Chip id="chip" label="Data provided by John Hopkins University"></Chip>
                    </footer>
                </div>
            </Grid>
        </Grid>
    );
}

ReactDOM.render(<App />, document.getElementById("root"));
