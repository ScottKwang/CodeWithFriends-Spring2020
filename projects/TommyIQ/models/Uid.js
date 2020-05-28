const mongoose = require('mongoose');

const UidSchema = new mongoose.Schema({
  uid: {
    type: String,
    required: true
  }
});

const Uid = mongoose.model('Uid', UidSchema);

module.exports = Uid;