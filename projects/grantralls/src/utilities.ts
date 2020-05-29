
/**
 * Takes the stage the user is currently in and increments it and sends
 * the new stage to the user.
 */
function incrementStage(res: any, state: string, oldStage: number, finalStage: any): void {
	if(oldStage < finalStage) {
		const newStage = oldStage + 1;
		res.cookie("state", [newStage, state])
	}
}

/**
 * clears all relevant cookies
 */
function clearCookies(res: any): void {
	res.clearCookie("state");
	res.clearCookie("data");
}

export { incrementStage, clearCookies }

