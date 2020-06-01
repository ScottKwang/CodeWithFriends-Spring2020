const express = require('express');
const { ensureAuthenticated } = require('../config/auth');

// router
const router = express.Router();

// getting index
router.get('/', (req, res) => {
  res.render('index');
});

module.exports = router;