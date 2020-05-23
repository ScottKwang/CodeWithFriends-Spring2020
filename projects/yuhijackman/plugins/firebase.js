import firebase from 'firebase';
import config from '~/firebase.config';

if (!firebase.apps.length) {
    firebase.initializeApp(config);
    firebase.auth().setPersistence(firebase.auth.Auth.Persistence.SESSION);
}

export const authProviders = {
    GoogleProvider: firebase.auth.GoogleAuthProvider.PROVIDER_ID,
    TwitterProvider: firebase.auth.TwitterAuthProvider.PROVIDER_ID,
    EmailProvider: firebase.auth.EmailAuthProvider
};
export const auth = firebase.auth()
export const fb = firebase
export const db = firebase.firestore()