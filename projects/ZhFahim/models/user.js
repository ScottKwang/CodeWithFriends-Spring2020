var mongoose = require("mongoose"),
  passprtLocalMongoose = require("passport-local-mongoose");

var userSchema = new mongoose.Schema({
  username: String,
  password: String
});

userSchema.plugin(passprtLocalMongoose);

module.exports = mongoose.model("User", userSchema);
