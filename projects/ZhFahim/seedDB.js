var mongoose = require("mongoose"),
  Caption = require("./models/caption.js"),
  Info = require("./models/info");

// Demo Captions
var captions = [
  {
    text: "When you can't find the sunshine, be the sunshine.",
    author: "admin",
    category: "motivation",
    usedBy: 345,
    language: "en"
  },
  {
    text:
      "The happiest people don't have the best of everything, they make the best of everything.",
    author: "admin",
    category: "motivation",
    usedBy: 746,
    language: "en"
  },
  {
    text: "Do more things that make you forget to check your phone.",
    author: "admin",
    category: "motivation",
    usedBy: 456,
    language: "en"
  },
  {
    text:
      "F.R.I.E.N.D.S. Fight for you. Respect you. Include you. Encourage you. Need you. Deserve you. Stand by you.",
    author: "admin",
    category: "friends",
    usedBy: 836,
    language: "en"
  },
  {
    text:
      "She's the exclamation mark in the happiest sentence that I could ever possibly write.",
    author: "admin",
    category: "love",
    usedBy: 923,
    language: "en"
  },
  {
    text: "Feelings are just visitors, let them come and go.",
    author: "admin",
    category: "motivation",
    usedBy: 482,
    language: "en"
  },
  {
    text:
      "Do not wait on the PERFECT MOMENT, take the minute and make it BEST.",
    author: "admin",
    category: "motivation",
    usedBy: 658,
    language: "en"
  },
  {
    text: "The great pleasure in life is doing what people say you cannot do.",
    author: "admin",
    category: "motivation",
    usedBy: 524,
    language: "en"
  },
  {
    text:
      "Life is like a balloon. If you never let go, you will certainly not understand how high you can climb.",
    author: "admin",
    category: "motivation",
    usedBy: 238,
    language: "en"
  },
  {
    text:
      "Take a piece of my heart and make it all your own, so when we are apart, you’ll never be alone.",
    author: "admin",
    category: "love",
    usedBy: 195,
    language: "en"
  },
  {
    text: "7 billion smiles, and yours is my favorite.",
    author: "admin",
    category: "love",
    usedBy: 745,
    language: "en"
  },
  {
    text:
      "Some people arrive and make such a beautiful impact on your life, you can barely remember what life was like without them.",
    author: "admin",
    category: "friends",
    usedBy: 157,
    language: "en"
  },
  {
    text: "I'll stop wearing black when they make a darker color.",
    author: "admin",
    category: "attitude",
    usedBy: 953,
    language: "en"
  },
  {
    text:
      "Sometimes, someone comes into your life so unexpectedly, takes your heart by surprise, and changes your life forever.",
    author: "admin",
    category: "love",
    usedBy: 348,
    language: "en"
  },
  {
    text: "Every day may not be good but there's good in every day.",
    author: "admin",
    category: "motivation",
    usedBy: 567,
    language: "en"
  }
];

module.exports = function() {
  Caption.create(captions);
  Info.create({
    name: "Hits"
  });
};
