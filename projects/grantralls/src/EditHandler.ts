import { clearCookies, incrementStage } from "./utilities"
import { editUserData } from "./UserData";

/**
 * register is a function that detects which stage (or state[0]) a user is in.
 * It can then send a message back to the user or save data the user has sent or both.
 *
 * @returns The parameter 'twiml' is used to send messages back to the user but
 * the function returns nothing.
 */
function EditHandler(req: any, res: any, twiml: any, state: [number, string] = [0, "edit"]): void {
	let dataCookie: any;
	const stageNumber = state[0];

	if(req.cookies.data == undefined) {
		dataCookie = {options: {}};
	} else {
		dataCookie = req.cookies.data
	}

	switch(stageNumber) {
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
			editUserData(req.body.From, dataCookie.options.option, req.body.Body);
			clearCookies(res);
			break;
		default:
			clearCookies(res);
			twiml.message("There was an error with editing your options. I've reset your progress. Please send 'edit' again to resume.");
	}

	incrementStage(res, "edit", stageNumber, 2);
}

export default EditHandler;
