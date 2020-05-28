const express = require('express');
const { ensureAuthenticated } = require('../config/auth');
const Message = require('../models/Message');
const User = require('../models/User');


const router = express.Router();


// lobby for all user's DMs
router.get('/', ensureAuthenticated, (req, res) => {
  const userId = req.user._id;
  const renderedUsers = new Map();
  

  // users that can be rendered
  const validUsers = [];


  // finding all msgs and searching for uses's ones ('from' or 'to' user)
  Message.find({  $or: [ { from: userId }, { to: userId } ]  })
    .then((messages) => {

      messages.forEach((msg) => {

        // if ('from' == userId) and DM user has not been rendered
        if(msg.from === String(userId) && renderedUsers[ msg.to ] === undefined) {
          renderedUsers[ msg.to ] = 1;
          validUsers.push({ id: msg.to, name: msg.reciever });
        }

        // if ('to' == userId) and DM  user has not been rendered
        else if(msg['to'] === String(userId) && renderedUsers[msg['from']] === undefined) {
          renderedUsers[msg['from']] = 1;
          validUsers.push({ id: msg.from, name: msg.sender });
        }

      });

      res.render('lobby', {
        user: req.user,
        DMs: validUsers
      });

    })
    .catch((err) => {
      console.log(err);

      res.render('lobby', {
        user: req.user,
        DMs: undefined
      });

    });

});

// saving messages
router.post('/save' , (req, res) => {
  //DMuser data
  const msg = req.body.message;
  const name = req.body.name;
  const DMid = req.body.DMid;

  // user id
  const userId = req.user._id;

  const newMessage = new Message();
  // setting sender (user)
  newMessage.from = userId;
  newMessage.sender = req.user.name;
  // setting reciever
  newMessage.reciever = name;
  newMessage.to = DMid;
  // text content
  newMessage.content = msg;

  newMessage.save()
  .then((message) => {
    // console.log(message);
  })
  .catch(err => console.error(err) );

});

router.get('/:room', ensureAuthenticated, (req, res) => {
  const room = req.params.room;
  

  Message.find({ $or: [ { from: room, to: req.user._id }, { from: req.user._id, to: room } ] })
    .then(messages => {
      // console.log(messages);
      let name;

      User.findOne({ _id: room })
        .then(user => {
          // console.log(user);
          name = user.name;

          res.render('room', {
            user: req.user,
            id: room,
            DMuser: name,
            msgs: messages
          });

        })
        .catch(e => {
          console.log(e);

          res.redirect('/account');
        });

    })
    .catch(e => console.log(e));
});


module.exports = router;