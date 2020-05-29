import fetch from "node-fetch";
import xml2js from "xml2js";
import { userData } from "./interfaces";

class JobData {
	private queryData: userData;

	constructor(inputData: userData) {
		this.queryData = inputData;
	}
	/**
	 * @returns The fetch promise used to get and parse data from StackOverflow
	 */
	getStackOverflowJobs(twiml: any): Promise<any> {
		const parser = new xml2js.Parser();

		return fetch(`https://stackoverflow.com/jobs/feed?q=${this.queryData.options.position}&l=${this.queryData.options.city}+${this.queryData.options.state}&u=Miles&d=20&s=${this.queryData.options.salary}&c=USD`)
			.then((res: any) => res.text())
			.then((xml: any) => {
				return parser.parseStringPromise(xml)
					.then((result: any) => {
						let goodJobs: any[] = [];
						const numOfJobs: number = (result.rss.channel[0]["os:totalResults"][0] > 3 ? 3 : result.rss.channel[0]["os:totalResults"][0]);

						for(let i = 0; i < numOfJobs; i++) {
							let currJob = result.rss.channel[0].item[i];
							console.log(`${JSON.stringify(currJob)}\n`);
							let goodJob = this.queryData.options.searchTerm.map((val: string, index: number): any => {
								val = val.toLowerCase();
								if(currJob.category.includes(val)) {
									goodJobs.push(currJob);
								};
							});
						}
						
						if(goodJobs.length == 0) {
							twiml.message("I couldn't find a job given your parameters. Trying changing some things around.");
							return null;
						}
						
						let msg = ``;
						goodJobs.map((currJob: any, index: number) => {	
							msg += `\n${index + 1}.) ${currJob.link[0]}`
						});

						twiml.message(msg);

					});
			})
			.catch((err: any) => console.log(err));
	}
}

export default JobData;

