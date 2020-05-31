const express = require("express"),
  router = express.Router(),
  Caption = require("../models/caption"),
  Info = require("..//models/info");

// Display random caption at home page
router.get("/", function(req, res) {
  Caption.find({}, function(err, captions) {
    if (err) {
      console.log(err);
    } else {
      // Update Total Hits
      Info.findOne({ name: "Hits" }, function(err, foundInfo) {
        if (err || !foundInfo) {
          console.log(err);
        } else {
          foundInfo.value++;
          foundInfo.save();
        }
      });
      // Generate random caption
      var randomNum = Math.floor(Math.random() * captions.length);
      res.render("home", { caption: captions[randomNum], page: "home" });
    }
  });
});

// Browse captions
router.get("/browse", function(req, res) {
  // Retrive all categories from DB
  var categories;
  Caption.distinct("category", function(err, foundCategories) {
    if (err) {
      console.log(err);
    } else {
      categories = foundCategories;
    }
  });
  // Make query object
  var query = {};
  // Check if user requests a query or not
  if (req.query.text) {
    query.text = new RegExp(escapeRegex(req.query.text), "gi");
  }
  if (req.query.category && req.query.category !== "all") {
    query.category = req.query.category;
  }
  // Get total page numbers based on query
  var pages = 1;
  Caption.countDocuments(query, function(err, count) {
    pages = Math.ceil(count / 10); // Each page has 10 captions
    // Get current page
    var currentPage = 1;
    if (req.query.page) {
      currentPage = req.query.page;
    }
    // Fetch captions based on query
    Caption.find(query)
      .limit(10)
      .skip((currentPage - 1) * 10)
      .sort({ $natural: -1 })
      .exec(function(err, captions) {
        if (err) {
          console.log(err);
        } else {
          // Render browse page based on query
          res.render("browse", {
            captions: captions,
            categories: categories,
            pages: pages,
            currentPage: currentPage,
            queryText: req.query.text,
            queryCategory: req.query.category,
            page: "browse"
          });
        }
      });
  });
});

// Update usedBy property route
router.get("/copy/:id", function(req, res) {
  //Find the caption
  Caption.findById(req.params.id, function(err, foundCaption) {
    if (err || !foundCaption) {
      console.log(err);
      res.send("Failed!");
    } else {
      foundCaption.usedBy++;
      foundCaption.save();
      res.send("Done!");
    }
  });
});

// Escape regex in search query
function escapeRegex(text) {
  return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
}

module.exports = router;
