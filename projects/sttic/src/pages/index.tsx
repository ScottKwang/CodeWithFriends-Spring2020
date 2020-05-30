import Head from 'next/head';
import { useEffect, useState } from 'react';

export default function Home() {
	const hourToImageMap: { [hour: number]: string } = {
		0: 'Lakeside-11.jpg',
		1: 'Lakeside-11.jpg',
		2: 'Lakeside-11.jpg',
		3: 'Lakeside-11.jpg',
		4: 'Lakeside-0.jpg',
		5: 'Lakeside-0.jpg',
		6: 'Lakeside-1.jpg',
		7: 'Lakeside-2.jpg',
		8: 'Lakeside-3.jpg',
		9: 'Lakeside-3.jpg',
		10: 'Lakeside-4.jpg',
		11: 'Lakeside-4.jpg',
		12: 'Lakeside-5.jpg',
		13: 'Lakeside-5.jpg',
		14: 'Lakeside-6.jpg',
		15: 'Lakeside-6.jpg',
		16: 'Lakeside-7.jpg',
		17: 'Lakeside-7.jpg',
		18: 'Lakeside-8.jpg',
		19: 'Lakeside-8.jpg',
		20: 'Lakeside-9.jpg',
		21: 'Lakeside-10.jpg',
		22: 'Lakeside-11.jpg',
		23: 'Lakeside-11.jpg'
	};
	const hourToBrightnessMap: { [hour: number]: number } = {
		0: 0,
		1: 0,
		2: 0,
		3: 0,
		4: 0,
		5: 10,
		6: 25,
		7: 50,
		8: 70,
		9: 90,
		10: 100,
		11: 100,
		12: 100,
		13: 100,
		14: 100,
		15: 100,
		16: 100,
		17: 100,
		18: 90,
		19: 70,
		20: 50,
		21: 50,
		22: 25,
		23: 10
	};
	const emptyPng =
		'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNgYAAAAAMAASsJTYQAAAAASUVORK5CYII=';
	const intervalTime = 1000 * 60;

	const [imageToggle, setImageToggle] = useState(false);
	const [img1, setImg1] = useState(emptyPng);
	const [img2, setImg2] = useState(emptyPng);
	const [hour, setHour] = useState(0);
	const [dummy, setDummy] = useState({});

	const updateBrightness = (hour: number) => {
		const nextBrightness = Object.values(hourToBrightnessMap)[hour % 24];

		fetch('/api/backlight', {
			method: 'POST',
			headers: {
				Accept: 'application/json',
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				percentage: nextBrightness
			})
		}).catch(console.log);
	};

	const updateBackground = (hour: number) => {
		const nextImage = Object.values(hourToImageMap)[hour % 24] || emptyPng;

		setImageToggle(!imageToggle);

		if (imageToggle) {
			setImg1(nextImage);
		} else {
			setImg2(nextImage);
		}
	};

	useEffect(() => {
		const date = new Date();
		setHour(date.getHours());

		updateBrightness(hour);
		updateBackground(hour);

		const interval = setInterval(() => setDummy({}), intervalTime);

		return () => {
			clearInterval(interval);
		};
	}, [dummy]);

	return (
		<div className="container">
			<Head>
				<title>Digital Window</title>
			</Head>
			<main>
				<div className="imgbox">
					<img id="img1" src={img1} />
					<img id="img2" src={img2} />
				</div>
			</main>

			<style jsx>{`
				.imgbox {
					position: relative;
					height: calc(100vw / (1920 / 1080));
					max-width: 100vw;
				}

				.imgbox img {
					position: absolute;
					top: 0;
					left: 0;
					width: 100%;
					height: 100%;
				}

				#img2 {
					opacity: ${imageToggle ? 1 : 0};
					transition: opacity 2s;
				}
			`}</style>
		</div>
	);
}