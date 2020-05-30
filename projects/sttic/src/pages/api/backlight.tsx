import { NextApiRequest, NextApiResponse } from 'next';
import { exec } from 'child_process';
import { clamp } from '../../utils';

export default (req: NextApiRequest, res: NextApiResponse<string | number>) => {
	return new Promise(resolve => {
		switch (req.method) {
			case 'GET': {
				exec(`xbacklight -get`, (error, stdout, stderr) => {
					if (error) {
						console.log(`error: ${error.message}`);
						res.status(500).end();
						return resolve();
					}
					if (stderr) {
						console.log(`stderr: ${stderr}`);
						res.status(400).end();
						return resolve();
					}

					console.log(`stdout: ${stdout.trim()}`);
					res.status(200).send(stdout.trim());
					return resolve();
				});
				return;
			}
			case 'POST': {
				const percentage = clamp(parseFloat(req.body.percentage), 0, 100);

				if (isNaN(percentage) || !isFinite(percentage)) {
					console.log('percentage must be a number');
					res.status(400).send('percentage must be a number');
					return resolve();
				}

				exec(`xbacklight -set ${percentage}`, (error, stdout, stderr) => {
					if (error) {
						console.log(`error: ${error.message}`);
						res.status(500).end();
						return resolve();
					}
					if (stderr) {
						console.log(`stderr: ${stderr}`);
						res.status(400).end();
						return resolve();
					}

					console.log(`setting backlight to ${percentage}`);
					res.status(200).send(percentage);
					return resolve();
				});
				return;
			}
			default: {
				res.status(405).send(`${req.method} Method Not Allowed`);
				return resolve();
			}
		}
	});
};
