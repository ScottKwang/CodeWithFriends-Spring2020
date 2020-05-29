# Initial Research
## Table of Contents
- [Development Environment](#development-envrionment)
- [Production Environment](#production-envrionment)
## Dev Environment
### Monorepo or Split repos
There's advantages to both and initially a monorepo seems to be the way to go, especially if we're using Docker Compose to handle setting up all of our services. Docker Compose does allow for a [custom Dockerfile](https://docs.docker.com/compose/compose-file/#dockerfile) to be specified for a particular services, but then issues might come up later when we configure deployments.

### Wordpress and React
React is easy to bootstrap, but I'm not super sure how it'll play with Wordpress. Starting a Wordpress project via Docker seems to make a whole bunch of PHP files, so it probably makes sense to have the below file structure:
```
~
  |- docs
  |- react-app
    |- React app things
    |- Dockerfile
  |- wordpress
    |- Wordpress things
  docker-compose.yml
```

The other thing is that Wordpress requires some kind of database to store stuff, so using PostgreSQL is fine. It also means I get to get better at SQL outside of an ORM like Sequelize.

### React App Libs
Since we're using React, we can also use the various libraries out there to help with developing a React app. Asides from bootstrapping the app with [CRA](https://create-react-app.dev/), I'm probably going to use the below:
- [Cypress](https://cypress.io/) for e2e tests
- [Prettier](https://prettier.io/) for consistent code styling
- [TypeScript](https://www.typescriptlang.org/) for static typing
- [Axios](https://github.com/axios/axios) for network requests
- [Redux/ the Redux Toolkit](https://redux.js.org/) for state management

Pretty easy, I could use a framework, but it's overkill for a small blog. Axios is kind of outdated, but Cypress can't stub/ reroute requests made with the [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch)

- [Back to top](#initial-research)

## Staging/ Testing Environment
Automated testing will be done through CircleCI, or Github Actions. CircleCI has the advantage of being more established, while Github Actions is built into Github. That's a really nice luxury, but since Github Actions is younger, it might have issues that are currently unfixed that are quite serious(e.g not being able to deploy to Github Pages from an Action).

Testing is done with a combination of Jest/ React Testing Library and Cypress. Jest will handle unit tests and partial integration tests while Cypress handles end to end testing.

There's also the option to play with Wordpress staging environments, which seems unnecessary given that we can use Docker Compose.

- [Back to top](#initial-research)
## Production Environment
We're deploying to AWS, so first we need to understand what AWS service does what in relation to our app. I've listed the relevant ones below:
- AWS S3(Simple Storage Service): static hosting of the React app
- AWS EC2(Elastic Compute Cloud): hosts service instances. In this case it's going to be running our headless Wordpress instance
- AWS RDS(Relational Database Service): database hosting and scaling

So if I made a really low budget UML(Unified Modeling Language) diagram, it'd look like:
```
  User Browser => AWS S3 React App <==> AWS EC2 Headless Wordpress Instance <=> AWS RDS SQL Instance
```

The other thing we need to do is synchronize our database between local and production. The reason for this should be straight-forwards, we don't want placeholder content in production and we don't want to develop our React app without solid content. Not sure how that's going to work just yet


- [Back to top](#initial-research)