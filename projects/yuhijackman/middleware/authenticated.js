import { auth } from '~/plugins/firebase';

export default function ({ store, redirect, $axios, app, route }) {
    if (store.state.firebase.status != "authenticated") {
      return redirect('/signin')
    }
}
  