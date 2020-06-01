const mongoose = require('mongoose');

const FileSchema = new mongoose.Schema({
  userId: {
    type: String,
    required: true
  },
  file: {
    type: Object,
    required: true
  }
});

const File = mongoose.model('File', FileSchema);

module.exports = File;