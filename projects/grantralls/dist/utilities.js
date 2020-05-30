"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.clearCookies = exports.incrementStage = void 0;
/**
 * Takes the stage the user is currently in and increments it and sends
 * the new stage to the user.
 */
function incrementStage(res, state, oldStage, finalStage) {
    if (oldStage < finalStage) {
        const newStage = oldStage + 1;
        res.cookie("state", [newStage, state]);
    }
}
exports.incrementStage = incrementStage;
/**
 * clears all relevant cookies
 */
function clearCookies(res) {
    res.clearCookie("state");
    res.clearCookie("data");
}
exports.clearCookies = clearCookies;
