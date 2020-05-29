"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const UserData_1 = require("./UserData");
const utilities_1 = require("./utilities");
/**
 * register is a function that detects which stage (or state[0]) a user is in.
 * It can then send a message back to the user or save data the user has sent or both.
 *
 * @returns The parameter 'twiml' is used to send messages back to the user but
 * the function returns nothing.
 */
function register(req, res, twiml, state = [0, "registration"]) {
    const dataCookie = req.cookies.data || { phoneNumber: req.body.From, options: {} };
    const stageNumber = state[0];
    switch (stageNumber) {
        case 0:
            res.cookie("data", dataCookie);
            twiml.message("What is your desired position?");
            break;
        case 1:
            dataCookie.options.position = req.body.Body;
            res.cookie("data", dataCookie);
            twiml.message("What state do you wish to work in?");
            break;
        case 2:
            dataCookie.options.state = req.body.Body;
            res.cookie("data", dataCookie);
            twiml.message("What city do you wish to work in?");
            break;
        case 3:
            dataCookie.options.city = req.body.Body;
            res.cookie("data", dataCookie);
            twiml.message("What salary do you wish to earn (eg. 50000)?");
            break;
        case 4:
            dataCookie.options.salary = req.body.Body;
            res.cookie("data", dataCookie);
            twiml.message("Search term(s) such as 'Javascript Node Express'");
            break;
        case 5:
            dataCookie.options.searchTerm = req.body.Body.split(" ");
            UserData_1.postUserData(dataCookie);
            utilities_1.clearCookies(res);
            break;
        default:
            utilities_1.clearCookies(res);
            twiml.message("There was an error with registering. I've reset your registration. Please send 'register' again to resume.");
    }
    utilities_1.incrementStage(res, "registration", stageNumber, 5);
}
exports.default = register;
