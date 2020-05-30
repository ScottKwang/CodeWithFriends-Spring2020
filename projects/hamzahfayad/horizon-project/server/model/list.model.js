mongoose = require("mongoose");
const Schema = mongoose.Schema;

const List = new Schema(
  {
    title: {
      type: String,
      required: true,
    },
    body: {
      type: String,
      required: true,
    },
    date: {
      type: Date,
      default: Date.now,
    },
  },
  {
    collection: "lists",
  });

module.exports = mongoose.model("List", List);
