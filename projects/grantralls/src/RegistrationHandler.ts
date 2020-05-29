import { postUserData } from "./UserData";
import { clearCookies, incrementStage } from "./utilities"

/**
 * register is a function that detects which stage (or state[0]) a user is in.
 * It can then send a message back to the user or save data the user has sent or both.
 *
 * @returns The parameter 'twiml' is used to send messages back to the user but
 * the function returns nothing.
 */
function register(req: any, res: any, twiml: any, state: [number, string] = [0, "registration"]): void {
	const dataCookie = req.cookies.data || {phoneNumber: req.body.From, options: {}};
	const stageNumber = state[0];

	switch(stageNumber) {
		case 0:
			res.cookie("data", dataCookie);
			twiml.message("What is your desired position?");
			break;
		case 1:
			dataCookie.options.position = req.body.Body
			res.cookie("data", dataCookie);
			twiml.message("What state do you wish to work in?");
			break;
		case 2:
			dataCookie.options.state = req.body.Body
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
			postUserData(dataCookie);
			clearCookies(res);
			break;
		default:
			clearCookies(res);
			twiml.message("There was an error with registering. I've reset your registration. Please send 'register' again to resume.");
	}

	incrementStage(res, "registration", stageNumber, 5);
}

export default register;
