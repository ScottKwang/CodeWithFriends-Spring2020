const express = require("express"),
  router = express.Router(),
  Caption = require("../models/caption"),
  Info = require("../models/info");

// Dashboard
router.get("/dashboard", isLoggedIn, function(req, res) {
  // Get Statics
  var statics = {};
  // Total Captions
  Caption.countDocuments({}, function(err, totalCaptions) {
    statics.totalCaptions = totalCaptions;
    // Total Categories
    Caption.distinct("category", function(err, foundCategories) {
      statics.totalCategories = foundCategories.length;
      // Total Caption Used By
      Caption.aggregate(
        [
          {
            $group: {
              _id: null,
              totalUsedBy: { $sum: "$usedBy" }
            }
          }
        ],
        function(err, usedByResults) {
          if (err || !usedByResults[0]) {
            console.log(err);
          } else {
            statics.totalUsedBy = usedByResults[0].totalUsedBy;
          }
          // Total Hits
          Info.findOne({ name: "Hits" }, function(err, foundInfo) {
            if (err || !foundInfo) {
              console.log(err);
            } else {
              statics.totalHits = foundInfo.value;
            }
            // Display Captions
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
                    // Serve statics and captions based on query
                    res.render("dashboard", {
                      statics: statics,
                      captions: captions,
                      categories: categories,
                      pages: pages,
                      currentPage: currentPage,
                      queryText: req.query.text,
                      queryCategory: req.query.category,
                      page: "dashboard"
                    });
                  }
                });
            });
          });
        }
      );
    });
  });
});

// Add Caption from Dashboard
router.post("/dashboard", isLoggedIn, function(req, res) {
  Caption.create(req.body, function(err, caption) {
    if (err) {
      console.log(err);
      res.redirect("back");
    } else {
      // Redirect to dashboard
      res.redirect("/dashboard");
    }
  });
});

// Send category list
router.get("/categories", function(req, res) {
  var query = new RegExp(escapeRegex(req.query.s), "gi");
  Caption.distinct("category", { category: query }, function(err, categories) {
    if (err) {
      console.log(err);
    } else {
      res.json(categories);
    }
  });
});

// Edit Caption Page
router.get("/dashboard/edit/:id", isLoggedIn, function(req, res) {
  // Fetch all categories
  Caption.distinct("category", function(err, categories) {
    if (err) {
      console.log(err);
    } else {
      // Find caption based on ID
      Caption.findById(req.params.id, function(err, caption) {
        if (err) {
          console.log(err);
        } else {
          res.render("edit", { caption: caption, categories: categories });
        }
      });
    }
  });
});

// Update Caption
router.post("/dashboard/edit/:id", isLoggedIn, function(req, res) {
  // Find the caption from DB
  Caption.findById(req.params.id, function(err, caption) {
    if (err) {
      console.log(err);
    } else {
      // Update the caption
      caption.text = req.body.text;
      caption.category = req.body.category;
      caption.save();
      res.redirect("/dashboard");
    }
  });
});

// Delete Caption
router.delete("/dashboard/:id", isLoggedIn, function(req, res) {
  // Delete the caption from DB
  Caption.deleteOne({ _id: req.params.id }, function(err) {
    if (err) {
      console.log(err);
    } else {
      res.send("Caption deleted!");
    }
  });
});

// Escape regex in search query
function escapeRegex(text) {
  return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
}

// Middleware
function isLoggedIn(req, res, next) {
  if (req.isAuthenticated()) {
    return next();
  } else {
    res.redirect("/login");
  }
}

module.exports = router;
