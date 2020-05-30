var mongoose = require("mongoose"),
  uniqueValidator = require("mongoose-unique-validator");

var captionSchema = new mongoose.Schema({
  text: {
    type: String,
    required: true,
    unique: true,
    uniqueCaseInsensitive: true
  },
  author: String,
  category: String,
  usedBy: {
    type: Number,
    default: 0
  },
  language: String,
  createdAt: {
    type: Date,
    default: Date.now
  }
});

captionSchema.plugin(uniqueValidator);

module.exports = mongoose.model("Caption", captionSchema);
