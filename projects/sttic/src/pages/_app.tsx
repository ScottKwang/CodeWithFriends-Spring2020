import { AppProps } from 'next/app';
import '../global-styles/main.css';

export default function MyApp({ Component, pageProps }: AppProps) {
	return <Component {...pageProps} />;
}
