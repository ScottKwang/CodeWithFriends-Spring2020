"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const node_fetch_1 = __importDefault(require("node-fetch"));
const xml2js_1 = __importDefault(require("xml2js"));
class JobData {
    constructor(inputData) {
        this.queryData = inputData;
    }
    /**
     * @returns The fetch promise used to get and parse data from StackOverflow
     */
    getStackOverflowJobs(twiml) {
        const parser = new xml2js_1.default.Parser();
        return node_fetch_1.default(`https://stackoverflow.com/jobs/feed?q=${this.queryData.options.position}&l=${this.queryData.options.city}+${this.queryData.options.state}&u=Miles&d=20&s=${this.queryData.options.salary}&c=USD`)
            .then((res) => res.text())
            .then((xml) => {
            return parser.parseStringPromise(xml)
                .then((result) => {
                let goodJobs = [];
                const numOfJobs = (result.rss.channel[0]["os:totalResults"][0] > 3 ? 3 : result.rss.channel[0]["os:totalResults"][0]);
                for (let i = 0; i < numOfJobs; i++) {
                    let currJob = result.rss.channel[0].item[i];
                    console.log(`${JSON.stringify(currJob)}\n`);
                    let goodJob = this.queryData.options.searchTerm.map((val, index) => {
                        val = val.toLowerCase();
                        if (currJob.category.includes(val)) {
                            goodJobs.push(currJob);
                        }
                        ;
                    });
                }
                if (goodJobs.length == 0) {
                    twiml.message("I couldn't find a job given your parameters. Trying changing some things around.");
                    return null;
                }
                let msg = ``;
                goodJobs.map((currJob, index) => {
                    msg += `\n${index + 1}.) ${currJob.link[0]}`;
                });
                twiml.message(msg);
            });
        })
            .catch((err) => console.log(err));
    }
}
exports.default = JobData;
