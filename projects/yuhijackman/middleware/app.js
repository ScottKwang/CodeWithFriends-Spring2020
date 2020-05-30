import { auth } from '~/plugins/firebase'

export default async function ({ store, route, app, error }) {
    if (!store.state.firebase.user) {
        await store.dispatch('fetchCurrentFirebaseUser')
    }    
}