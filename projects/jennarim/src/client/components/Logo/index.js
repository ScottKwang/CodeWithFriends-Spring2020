import React from 'react';

import LOGO from './../../public/ahri.png';
import LOGO2 from './../../public/ahri copy.png';

class Logo extends React.Component {
	render() {
		return (
			<div className="ahri-img">
				<img src={LOGO} alt="Page Logo" />
				<img src={LOGO2} alt="Page Logo" />
			</div>
		);
	}
}

export default Logo;