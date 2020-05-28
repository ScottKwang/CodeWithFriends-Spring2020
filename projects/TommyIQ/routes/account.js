const express = require('express');
const { ensureAuthenticated } = require('../config/auth');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
const UIDGenerator = require('uid-generator');
const File = require('../models/File');
const User = require('../models/User');


// generotor of cryptographically UIDs
const uidgen = new UIDGenerator();


// router
const router = express.Router();


let storage, upload;
const multerInit = (userId) => {
  // set storage engine with multer
  storage = multer.diskStorage({
    destination: `./public/upload/${userId}`,
    filename: function(req, file, cb) {
      const fileName = file.fieldname + '-' + uidgen.generateSync() + path.extname(file.originalname);
      cb(null, fileName);
    }
  });

  // init upload variable
  upload = multer({
    storage: storage,
    limits: {
      fileSize: 20000000
    }
  }).single('file');

}


const saveFile = async (userId, file) => {
  file.extname = path.extname(file.originalname);
  const newFile = new File({
    userId: userId,
    file: file
  });

  return newFile.save();
}


// getting account
router.get('/', ensureAuthenticated, (req, res) => {
  
  // getting all file that user has
  File.find({ userId: req.user._id })
    .then(files => {
      res.render('account', {
        user: req.user,
        files: files,
        foundFiles: (req.session.files || []),
        users: (req.session.users || [])
      });
    })
    .catch(err => {
      throw err;
    });
});


// uploading files with multer init
router.post('/upload', ensureAuthenticated, (req, res) => {
  // setting storage and upload variable
  multerInit(req.user._id);

  // sending file to user's folder
  upload(req, res, (err) => {
    if(err) {
      // if err
      res.render('account', {file_msg: err, user: req.user});
    } else {
      // if form has been sent without file
      if(req.file === undefined) {
        req.flash('file_msg', 'You have to upload file before sending!');
        res.redirect('/account');

      } else {
        saveFile(req.user._id, req.file)
          .then(file => {
            
            req.flash('success_file_msg', 'File has been sent');
            res.redirect('/account');
          })
          .catch(err => {
            throw err;
          });

      }
    }
  });
});


// deleting files from DB and file system with fs
router.post('/delete', ensureAuthenticated, (req, res) => {
  const { submit, filename } = req.body;

  // constructing path to chosen file
  const filePath = `./public/upload/${req.user._id}/${filename}`;

  // deleting file from user's dir
  fs.unlink(filePath, (err) => {
    // if the filename was changed
    if(err) {
      console.log(err);
      req.flash('error_delete_msg', 'Something went wrong on the server, try again');
      res.redirect('/account');
    }
    
    // when everything is alright 
    else {
      // deleting file from DB
      File.deleteOne({ "userId": req.user._id, "file.filename": filename })
      .then(file => { 
          console.log(file);
          req.flash('success_delete_msg', 'File has been deleted!');
          res.redirect('/account');
      })
      .catch(err => {
        console.log(err);
        req.flash('error_delete_msg', 'Something went wrong on the server, try again');
        res.redirect('/account');
      } );
    
    }
  });


});


// giving shareLink
router.post('/link', ensureAuthenticated, (req, res) => {
  req.flash('share_link', req.user._id);
  res.redirect('/account');
});


// finding files
router.post('/find', ensureAuthenticated, (req, res) => {
  const { submit, link } = req.body;

  // checking link
  if(link === '') {
    req.flash('find_msg', 'You have to type sharing link before!');
    res.redirect('/account');

  }
  // if link = user._id
  else if(link == req.user._id) {
    req.flash('find_msg', 'Do not try to find your own files, it is stupid!');
    res.redirect('/account');

  }
  else {
    File.find({ userId: link })
      .then(files => {

        if(files.length !== 0) {
          req.session.files = files;
        }
        else {
          req.flash('find_msg', 'No files were found!');
        }

        res.redirect('/account');
      })
      .catch(e => {
        throw err;
      });
  }

});


// Serching for users in search-field
router.post('/search', (req, res) => {
  const { search: searchName } = req.body;

  User.find({ name: searchName })
    .then(users => {
      let usersArr = [];

      // deleting user himself from found users if he is inside
      users.forEach((user, index) => {
        if( String(user._id) !== String(req.user._id) ) {
          usersArr.push(user);
        }
      });

      if(usersArr.length !== 0) {
        req.session.users = usersArr;
      } 
      else {
        req.session.users = undefined;
        req.flash('search_msg_fail', 'Cannot find users with this name!');
      }

      res.redirect('/account');
    })
    .catch(err => {
      throw err;
    });
 
});

module.exports = router;