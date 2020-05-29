import React, { Component } from 'react';
import Button from 'react-bootstrap/Button';
import Navbar from 'react-bootstrap/Navbar';
import { BsGearFill, BsGear } from "react-icons/bs"

class Timer extends Component {

    render() {
        return (
            <Navbar className="bg-transparent justify-content-between">
                <Navbar.Brand className="text-white" href="/">Pomodoro Sound Focus</Navbar.Brand>
                <div className="gear-container">
                    <Button className="bg-transparent on-top" type="submit"><BsGear /></Button>
                    <Button className="bg-transparent underneath" type="submit"><BsGearFill /></Button>
                </div>
            </Navbar>
        )
    }

}

export default Timer