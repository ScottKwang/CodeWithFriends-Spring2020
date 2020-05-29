"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const twilio_1 = require("twilio");
const body_parser_1 = __importDefault(require("body-parser"));
const cookie_parser_1 = __importDefault(require("cookie-parser"));
const RegistrationHandler_1 = __importDefault(require("./RegistrationHandler"));
const EditHandler_1 = __importDefault(require("./EditHandler"));
const OptionsMenu_1 = __importDefault(require("./OptionsMenu"));
const StateHandler_1 = __importDefault(require("./StateHandler"));
const utilities_1 = require("./utilities");
const UserData_1 = require("./UserData");
const JobData_1 = __importDefault(require("./JobData"));
const serverless_http_1 = __importDefault(require("serverless-http"));
const dotenv_1 = __importDefault(require("dotenv"));
dotenv_1.default.config();
const MessagingResponse = twilio_1.twiml.MessagingResponse;
const app = express_1.default();
app.use(body_parser_1.default.urlencoded({ extended: false }));
app.use(cookie_parser_1.default());
app.get("/sms", (req, res) => {
    res.send("Basu backend");
});
app.post("/sms", (req, res, next) => __awaiter(void 0, void 0, void 0, function* () {
    const twimr = new MessagingResponse();
    const formattedBody = req.body.Body.toLowerCase().replace(/ /g, "");
    if (req.cookies.state && formattedBody != "q") {
        StateHandler_1.default(req, res, twimr, req.cookies.state);
    }
    else {
        switch (formattedBody) {
            case "register":
                RegistrationHandler_1.default(req, res, twimr);
                break;
            case "edit":
                EditHandler_1.default(req, res, twimr);
                break;
            case "deleteaccount":
                yield UserData_1.deleteUserData(req.body.From)
                    .then((responseJSON) => {
                    twimr.message("I'm sorry to see you go. All of your data has been deleted like you were never here.");
                });
                utilities_1.clearCookies(res);
            case "viewoptions":
                yield UserData_1.getUserData(req.body.From)
                    .then((userOptions) => {
                    twimr.message(`State: ${userOptions.options.state}\nCity: ${userOptions.options.city}\nPosition: ${userOptions.options.position}\nSalary: ${userOptions.options.salary}\nSearch Terms: ${userOptions.options.searchTerm}\nSend 'Edit' to change one of these`);
                })
                    .catch(err => {
                    twimr.message("There was a problem. Most likely you haven't registered yet.");
                });
            case "q":
                utilities_1.clearCookies(res);
                break;
            case "find":
                yield UserData_1.getUserData(req.body.From)
                    .then((userOptions) => __awaiter(void 0, void 0, void 0, function* () {
                    const jobClient = new JobData_1.default(userOptions);
                    if (userOptions == null) {
                        twimr.message("");
                        return;
                    }
                    else {
                        yield jobClient.getStackOverflowJobs(twimr);
                    }
                })).catch(err => {
                    console.log(`Error with finding user: ${err}`);
                    twimr.message("There was a problem. Most likely you haven't registered yet.");
                });
                break;
            default:
                OptionsMenu_1.default(twimr);
        }
    }
    console.log(req.cookies);
    res.writeHead(200, { "Content-Type": "text/xml" });
    res.end(twimr.toString());
}));
module.exports.handler = serverless_http_1.default(app);
