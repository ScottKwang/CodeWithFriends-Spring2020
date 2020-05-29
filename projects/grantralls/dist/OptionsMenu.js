"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
function displayMenu(twiml) {
    twiml.message(`I'm not sure what you mean by that. Here are some options.\n
'Register' - Begins the registration process.\n
'Jobs' - Delivers a list of curated jobs.\n
'View options' - Lists the your parameters.\n
'Edit' - Changes one of the parameters\n
'Delete account' - Deletes all user data.`);
}
exports.default = displayMenu;
