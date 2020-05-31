const express = require("express"),
  router = express.Router(),
  passport = require("passport");

// Show Login Form
router.get("/login", function(req, res) {
  res.render("login");
});

// Handle Login
router.post(
  "/login",
  passport.authenticate("local", {
    successRedirect: "/dashboard",
    failureRedirect: "login"
  }),
  function(req, res) {}
);

module.exports = router;
