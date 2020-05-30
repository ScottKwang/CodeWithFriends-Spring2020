import { auth, db, fb, authProviders } from '~/plugins/firebase'

export const state = () => ({
    firebase: {
        user: null,
        status: "unauthenticated",
    },
    authUser: {
        name: '',
        thumbnail: '',
        myBooks: {}
    }
})

export const getters = {
    uid (state) {
        if (state.firebase.user || state.firebase.user.uid) {
            return state.firebase.user.uid
        } else {
            return null
        }
    },
    getUserInfo(state) {
        const authUser = {
            name: state.authUser.name,
            email: state.firebase.user.email,
        }
        return authUser
    },
    isAuthenticated(state) {
        return !!state.firebase.user && !!state.firebase.user.uid
    },
    getMyBooksByStatus: state => status => {
        const myBooks = state.authUser.myBooks
        const filteredBooks = Object.keys(myBooks).reduce((ary, key) => {
            if (myBooks[key]["status"] == status) {
                ary.push(myBooks[key])
            }
            return ary
        }, []);
        filteredBooks.sort((a, b) => {
            if (a.title > b.title) {
                return 1;
            } else {
                return -1;
            }
        })
        return filteredBooks
    },
    getBookById: state => id => {
        return state.authUser.myBooks[id] ?? {}
    },
    calculateTotalPriceByStatus: (state, getters) => status => {
        const books = getters.getMyBooksByStatus(status)
        let sum = 0
        Object.keys(books).map(function(key, index) {
            sum += books[key]["price"]
        });
        return sum
    },
    calculateAmountOfBooksByStatus: (state, getters) => status => {
        const books = getters.getMyBooksByStatus(status)
        return Object.keys(books).length ?? 0
    }
}

export const actions = {
    async fetchCurrentFirebaseUser({ commit, state, dispatch }) {
        const firebaseUser =  await new Promise((resolve, reject) => {
            auth.onAuthStateChanged((user) => resolve(user || false))
        })
        if (firebaseUser) {
            const userInfo = {
                email: firebaseUser.email,
                uid: firebaseUser.uid
            }
            await commit("SET_FIREBASE", userInfo)
            await dispatch("fetchAuthUser", firebaseUser)
        }
    },
    async fetchAuthUser({ commit, state }, firebaseUser) {
        const authUser = await db.collection("users")
            .doc(firebaseUser.uid)
            .get()
        const authInfo = {
            name: authUser.data().name,
            thumbnail: authUser.data().thumbnail ?? ''
        }
        await commit("SET_AUTH", authInfo)
    },
    async doSignUp({ commit }, { email, password, name }) {
        commit("RESET_FIREBASE")
        const firebaseUser = await auth.createUserWithEmailAndPassword(email, password)
        const userInfo = {
            email: firebaseUser.user.email,
            uid: firebaseUser.user.uid
        }
        const authInfo = {
            name: name,
            thumbnail: ''
        }
        const authUser = await db.collection("users")
            .doc(firebaseUser.user.uid)
            .set(authInfo)
        await commit("SET_FIREBASE", userInfo)
        await commit("SET_AUTH", authInfo)
    },
    async doSignIn({ commit, dispatch }, { email, password }) {
        commit("RESET_FIREBASE")
        const firebaseUser = await auth.signInWithEmailAndPassword(email, password)
        const userInfo = {
            email: firebaseUser.user.email,
            uid: firebaseUser.user.uid
        }
        await commit("SET_FIREBASE", userInfo)
        await dispatch("fetchAuthUser", firebaseUser.user)
    },
    doLogOut({ commit }) {
        auth.signOut().then(() => {
            commit("RESET_FIREBASE")
        })
    },
    async addUnreadBook({ commit, state }, book) {
        await db.collection("users")
            .doc(state.firebase.user.uid)
            .collection("books")
            .doc(book.id)
            .set(book)
            .then(ref => {
                commit("ADD_BOOK", book)
             }).catch;
    },
    async fetchMyBooks({ commit, state }) {
        const collectionPath = "users/" + state.firebase.user.uid + "/books"
        const myBooks = await db.collection(collectionPath)
            .get()
            .then(querySnapshot => {
                const data = [...querySnapshot.docs].reduce((result, doc) => {
                    let book = doc.data();
                    result[book.id] = book
                    return result
                }, {})
                return data
            }).catch(error => {
                console.log(error)
            })
        commit("SET_MY_BOOKS", myBooks)
    },
    async changeBookStatus({ state, commit, getters }, book) {
        let newStatus = book.status === "unread" ? "read" : "unread"
        await db.collection("users")
            .doc(state.firebase.user.uid)
            .collection("books")
            .doc(book.id)
            .update({status: newStatus})
            .then(ref => {
                commit("CHANGE_BOOK_STATUS", {book, newStatus})
             }).catch(error => {
                console.log(error)
             })
    },
    async updateFirebaseUserPassword({state, commit, getters }, {oldPassword, newPassword}) {
        const user = auth.currentUser
        const email = user.email
        const cred = authProviders.EmailProvider.credential(email, oldPassword)
        return new Promise((resolve, reject) => {
            user.reauthenticateWithCredential(cred).then(res => {
                user.updatePassword(newPassword)
                resolve()
            }).catch((error) => {
                console.log(error)
                reject(error)
            })
        })
    },
    async updateFirebaseUserEmail({state, commit, getters }, {newEmail, password}) {
        const user = auth.currentUser
        const email = user.email
        const cred = authProviders.EmailProvider.credential(email, password)
        return new Promise((resolve, reject) => {
            user.reauthenticateWithCredential(cred).then(res => {
                user.updateEmail(newEmail)
                const newFirebaseInfo = {
                    ...state.firebase.user,
                    email: newEmail
                }
                commit("CHANGE_FIREBASE_INFO", newFirebaseInfo)
                resolve()
            }).catch((error) => {
                console.log(error)
                reject(error)
            })
        })
    },
    async updateAuthUser({state, commit, getters }, userInfo) {
        await db.collection("users")
                .doc(state.firebase.user.uid)
                .update({name: userInfo.name})
        await commit("SET_AUTH", {name: userInfo.name, thumbnail: ""})
    },
    async deleteUser({state, commit, getters }, { password }) {
        const user = auth.currentUser
        const email = user.email
        const uid = user.uid
        const cred = authProviders.EmailProvider.credential(email, password)
        return new Promise((resolve, reject) => {
            user.reauthenticateWithCredential(cred).then(() => {
                user.delete()
            }).then(() => {
                auth.signOut().then(() => {
                    commit("RESET_FIREBASE")
                })
            }).then(() => {
                db.collection('users').doc(uid).delete()
                commit("RESET_AUTH")
                resolve()
            })
            .catch((error) => {
                console.log(error)
                reject(error)
            })
        })
    }
}

export const mutations = {
    SET_FIREBASE(state, userInfo) {
        state.firebase = {
            ...state.firebase,
            user: userInfo,
            status: "authenticated"
        }
    },
    SET_AUTH(state, {name, thumbnail}) {
        state.authUser = {
            ...state.authUser,
            name,
            thumbnail
        }
    },
    RESET_FIREBASE(state) {
        state.firebase = {
            user: null,
            status: "unauthenticated"
        }
    },
    RESET_AUTH(state) {
        state.authUser = {
            name: '',
            thumbnail: '',
            myBooks: {}
        }
    },
    ADD_BOOK(state, book) {
        state.authUser.myBooks = {...state.authUser.myBooks, [book.id]: book}
    },
    SET_MY_BOOKS(state, myBooks) {
        state.authUser.myBooks = myBooks
    },
    CHANGE_BOOK_STATUS(state, {book, newStatus}) {
        let newBook = {...book, status: newStatus}
        state.authUser.myBooks = {...state.authUser.myBooks, [book.id]: newBook}
    },
    CHANGE_FIREBASE_INFO(state, user) {
        state.firebase = {
            ...state.firebase,
            user: user,
        }
    }
}