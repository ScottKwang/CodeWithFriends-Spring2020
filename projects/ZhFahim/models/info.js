const mongoose = require("mongoose");

const infoSchema = new mongoose.Schema({
  name: String,
  value: {
    type: Number,
    default: 0
  }
});

module.exports = mongoose.model("Info", infoSchema);
