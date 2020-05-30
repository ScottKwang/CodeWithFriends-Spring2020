"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const RegistrationHandler_1 = __importDefault(require("./RegistrationHandler"));
const EditHandler_1 = __importDefault(require("./EditHandler"));
const utilities_1 = require("./utilities");
/**
 * This function is called when a user has a saved state. This function
 * detects which state that is and calls the proper function.
 * @returns The twiml parameter is used to send messages back to the user
 */
function stateHandler(req, res, twiml, state) {
    switch (req.cookies.state[1]) {
        case "registration":
            RegistrationHandler_1.default(req, res, twiml, req.cookies.state);
            break;
        case "edit":
            EditHandler_1.default(req, res, twiml, req.cookies.state);
            break;
        default:
            utilities_1.clearCookies(res);
            break;
    }
}
exports.default = stateHandler;
