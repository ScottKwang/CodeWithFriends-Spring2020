const LocalStrategy = require('passport-local').Strategy;
const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

// Load User Modal
const User = require('../models/User');

module.exports = function(passport) {

  passport.use(
    new LocalStrategy({ usernameField: 'email' }, (email , password, done) => {

      // Match user in DB
      User.findOne({ email: email })
        .then(user => {
          if(!user) {
            return done(null, false, { message: 'That email is not registered' });
          }

          if(!user.confirmed) {
            return done(null, false, { message: 'You have not confirmed your account. Check your email' });
          }

          // Match the password
          bcrypt.compare(password, user.password, (err, isMatched) => {
            if(err) throw err;

            if(isMatched) {
              return done(null, user);
            } else {
              return done(null, false, { message: 'Password incorrect' });
            }

          });
        })
        .catch(err => console.log(err));
    })
  );

  passport.serializeUser((user, done) => {
    done(null, user.id);
  });

  passport.deserializeUser((id, done) => {
    User.findById(id, (err, user) => {
      done(err, user);
    });
  });


};