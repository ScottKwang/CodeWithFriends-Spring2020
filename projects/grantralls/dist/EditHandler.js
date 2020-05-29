"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const utilities_1 = require("./utilities");
const UserData_1 = require("./UserData");
/**
 * register is a function that detects which stage (or state[0]) a user is in.
 * It can then send a message back to the user or save data the user has sent or both.
 *
 * @returns The parameter 'twiml' is used to send messages back to the user but
 * the function returns nothing.
 */
function EditHandler(req, res, twiml, state = [0, "edit"]) {
    let dataCookie;
    const stageNumber = state[0];
    if (req.cookies.data == undefined) {
        dataCookie = { options: {} };
    }
    else {
        dataCookie = req.cookies.data;
    }
    switch (stageNumber) {
        case 0:
            res.cookie("data", dataCookie);
            twiml.message("Which value do you want to edit?\n'Position'\n'City'\n'State'\n'Salary'");
            break;
        case 1:
            dataCookie.options.option = req.body.Body.toLowerCase();
            res.cookie("data", dataCookie);
            twiml.message("What do you want to change the value to?");
            break;
        case 2:
            UserData_1.editUserData(req.body.From, dataCookie.options.option, req.body.Body);
            utilities_1.clearCookies(res);
            break;
        default:
            utilities_1.clearCookies(res);
            twiml.message("There was an error with editing your options. I've reset your progress. Please send 'edit' again to resume.");
    }
    utilities_1.incrementStage(res, "edit", stageNumber, 2);
}
exports.default = EditHandler;
