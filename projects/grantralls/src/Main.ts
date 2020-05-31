import http from "http";
import express from "express";
import { twiml } from "twilio";
import bodyParser from "body-parser";
import cookieParser from "cookie-parser";
import RegistrationHandler from "./RegistrationHandler";
import EditHandler from "./EditHandler";
import optionsMenu from "./OptionsMenu";
import StateHandler from "./StateHandler";
import { clearCookies } from "./utilities";
import { getUserData, deleteUserData } from "./UserData";
import JobData from "./JobData";
import { userData } from "./interfaces";
import serverless from "serverless-http";
import dotenv from "dotenv";
dotenv.config();

const MessagingResponse = twiml.MessagingResponse;
const app = express();

app.use(bodyParser.urlencoded({extended: false}));
app.use(cookieParser());

app.get("/sms", (req, res): void => {
	res.send("Basu backend");
})

app.post("/sms", async (req, res, next): Promise<any> => {
	const twimr = new MessagingResponse();
	const formattedBody = req.body.Body.toLowerCase().replace(/ /g, "");

	if(req.cookies.state && formattedBody != "q") {
		StateHandler(req, res, twimr, req.cookies.state);
	} else {
		switch(formattedBody) {
			case "register":
				RegistrationHandler(req, res, twimr);
				break;

			case "edit":
				EditHandler(req, res, twimr);
				break;

			case "deleteaccount":
				await deleteUserData(req.body.From)
					.then((responseJSON: any) => {
						twimr.message("I'm sorry to see you go. All of your data has been deleted like you were never here.");
					});
				clearCookies(res);

			case "viewoptions":
				await getUserData(req.body.From)
					.then((userOptions: userData) => {
						twimr.message(`State: ${userOptions.options.state}\nCity: ${userOptions.options.city}\nPosition: ${userOptions.options.position}\nSalary: ${userOptions.options.salary}\nSearch Terms: ${userOptions.options.searchTerm}\nSend 'Edit' to change one of these`);
					})
					.catch(err => {
						twimr.message("There was a problem. Most likely you haven't registered yet.")
					});

			case "q":
				clearCookies(res);
				break;

			case "find":
				await getUserData(req.body.From)
					.then(async (userOptions: userData) => {
						const jobClient = new JobData(userOptions);
						if(userOptions == null) {
							twimr.message("");
							return;
						} else  {
							await jobClient.getStackOverflowJobs(twimr);
						}
					}).catch(err => {
						console.log(`Error with finding user: ${err}`);
						twimr.message("There was a problem. Most likely you haven't registered yet.");
					});
				break;

			default:
				optionsMenu(twimr);
		}
	}
	console.log(req.cookies);
	res.writeHead(200, {"Content-Type": "text/xml"});
	res.end(twimr.toString());
});

module.exports.handler = serverless(app);
