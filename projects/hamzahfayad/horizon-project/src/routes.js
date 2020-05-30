//import Paint from './components/Paint'
//import About from './components/About'

const routes = [
    
    {
        path: '/paint',
        component: require('./components/Paint.vue').default,
        name: 'paint'
    },
    {
        path: '/list',
        component: require('./components/List.vue').default,
        name: 'list'
    },
    {
        path: '/about',
        component: require('./components/About.vue').default,
        name: 'about'
    }
]

export default routes