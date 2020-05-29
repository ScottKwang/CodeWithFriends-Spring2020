import RegistrationHandler from "./RegistrationHandler";
import EditHandler from "./EditHandler";
import { clearCookies } from "./utilities";

/**
 * This function is called when a user has a saved state. This function
 * detects which state that is and calls the proper function.
 * @returns The twiml parameter is used to send messages back to the user 
 */
function stateHandler(req: any, res: any, twiml: any, state: string): void {
	switch(req.cookies.state[1]) {
		case "registration":
			RegistrationHandler(req, res, twiml, req.cookies.state);
			break;
		case "edit":
			EditHandler(req, res, twiml, req.cookies.state);
			break;
		default:
			clearCookies(res);
			break;
	}
}

export default stateHandler;
