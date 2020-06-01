const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const session = require('express-session');
const passport = require('passport');
const nodemailer = require('nodemailer');
const UIDGenerator = require('uid-generator');

// generotor of cryptographically UIDs
const uidgen = new UIDGenerator();

// Requiring models
const User = require('../models/User');
const Uid = require('../models/Uid');


// register page get request
router.get('/register', (req, res) => {
  res.render('registration');
});


// register page post request
router.post('/register', (req, res) => {
  const {
    name,
    email,
    password1,
    password2
  } = req.body;

  const errors = [];

  // Checking required fields
  if (!name || !email || !password1 || !password2) {
    errors.push({
      msg: 'Please, fill in all fields'
    });
  }

  // Checking passowrd match
  if (password1 !== password2) {
    errors.push({
      msg: 'Passwords do not match'
    });
  }

  // Checking password length
  if (password1.length < 6) {
    errors.push({
      msg: 'Password should be at least 6 characters'
    });
  }


  // checking errors array
  if (errors.length > 0) {
    res.render('registration', {
      name,
      email,
      password1,
      password2,
      errors
    });
  } else {

    // looking for email in DB
    User.findOne({
        email: email
      })
      .then(user => {

        if (user) {
          errors.push({
            msg: 'This email is taken'
          });

          res.render('registration', {
            errors,
            name,
            email,
            password1,
            password2
          });
        } else {
          const newUser = new User({
            name,
            email,
            password: password1
          });


          // Hash password
          bcrypt.genSalt(10, (err, salt) => {
            if (err) throw err;

            bcrypt.hash(newUser.password, salt, (err, hash) => {
              if (err) throw err;

              // Set password to a hashed one
              newUser.password = hash;

              // Save user
              newUser.save()
                .then(user => {
                  req.flash('success_msg', 'You are registered now, check a message on your email to log in');
                  req.flash('email', email);

                  res.redirect('/users/login');
                })
                .catch(err => console.log(err))
            });
          });
        }
      });

      uidgen.generate((err, uid) => {
        if(err) throw err;

        // creating new uid for DB
        const newUid = new Uid({
          uid: uid
        });

        // saving newUid in DB
        newUid.save()
          .then(uid => { console.log('Uid has been saved'); } );

        // creating link for email verifying
        const link = `http://${req.get('host')}/users/login/${uid}?email=${email}`;

        // creating output for nodemailer
        const output = `
          <h2>Confirm your email by clicking the link down</h2>
          <p>In case you have not tried to register on our website ShareIT ignore this message</p>
          <a href="${link}">Confirm email</a>
        `;

        // create reusable transporter object using the default SMTP transport
        // these credentials are secret, don't show it to anyone! 
        let transporter = nodemailer.createTransport({
          host: 'smtp.yandex.ru',
          port: 465,
          secure: true, 
          auth: {
              user: 'vladislav.artuhov@yandex.ru', // generated ethereal user
              pass: 'vlad130603' // generated ethereal password
          }
        });

      // send mail with defined transport object
        transporter.sendMail({
          from: '"ShareIT" <vladislav.artuhov@yandex.ru>', // sender address
          to: email,
          subject: 'Confirming email',
          html: output // html body
        })
        .then(info => console.log(info))
        .catch(e => console.log(e));
      });

  }

});


// login page get request
router.get('/login', (req, res) => {
  // cleaning found files
  req.app.locals.files = undefined;

  req.logout();
  res.render('login');
});


// checking uid of user on login/:uid
router.get('/login/:uid', (req, res) => {

  // looking for same uid in DB
  Uid.findOne({ uid: req.params.uid })
    .then(uid => {
      if(uid) {

        // updating user in DB
        User.updateOne(
          { email: req.query.email },
          { confirmed: true }
          )
            .then(obj => {
              if(obj) {

                // deleting uid in DB
                Uid.deleteOne({ uid: req.params.uid })
                  .then(obj => { 
                    console.log('Uid has been deleted');
                    req.flash('success_msg', 'Account has been verified, you can log in');
                    req.flash('email', req.query.email);

                    res.redirect('/users/login');
                  })
                  .catch(err => console.log(err) );
              }
            })
            .catch(err => console.log(err) );
      } else {
        console.log('Uid does not exist...');
        res.redirect('/users/login');
      }
    })
    .catch(err => console.log(err) );

});


// login page post request
router.post('/login', (req, res, next) => {
  passport.authenticate('local', {
    successRedirect: '/account',
    failureRedirect: '/users/login',
    failureFlash: true
  })(req, res, next);
});


// logout get request
router.get('/logout', (req, res) => {
  // cleaning found files
  req.app.locals.files = undefined;

  req.logout();
  req.flash('success_msg', 'You have been logged out');
  res.redirect('/users/login');
});


module.exports = router;