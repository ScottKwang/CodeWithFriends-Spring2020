// Update usedBy property using AJAX
var request = new XMLHttpRequest();
function copy(captionId) {
  request.open("GET", "/copy/" + captionId);
  request.send();
}

// Copy text to clipboard and display message
var clipboard = new ClipboardJS(".copy-btn");
var num;
var msgDisplay = document.querySelector(".msg");

clipboard.on("success", function(e) {
  msgDisplay.classList.add("show");
  clearTimeout(num);
  num = setTimeout(function() {
    msgDisplay.classList.remove("show");
  }, 3000);
});

clipboard.on("error", function(e) {
  msgDisplay.innerHTML = "Unable to copy! please copy manually.";
  msgDisplay.classList.add("show");
  clearTimeout(num);
  num = setTimeout(function() {
    msgDisplay.classList.remove("show");
  }, 3000);
});

// Display previous categories under input field
var categoryInput = document.getElementById("category-input");
var suggestionBox = document.getElementById("suggestion-box");
var request = new XMLHttpRequest();
categoryInput.oninput = function() {
  request.abort();
  suggestionBox.innerHTML = "";
  request.open("GET", "/categories?s=" + this.value);
  request.onreadystatechange = function() {
    if (request.readyState == 4 && request.status == 200) {
      var categories = JSON.parse(request.responseText);
      categories.forEach(function(category) {
        var list = document.createElement("div");
        list.classList.add("suggestion-item");
        list.innerHTML = category;
        list.addEventListener("click", function() {
          categoryInput.value = category;
          suggestionBox.innerHTML = "";
        });
        suggestionBox.appendChild(list);
      });
    }
  };
  request.send();
};
categoryInput.onblur = function() {
  setTimeout(function() {
    suggestionBox.innerHTML = "";
  }, 300);
};

// Delete caption
function deleteCaption(captionId) {
  var request = new XMLHttpRequest();
  var confirmMsg = prompt(
    "Do you really want to delete? Type 'YES' to proceed."
  );
  if (confirmMsg === "YES") {
    request.open("DELETE", "/dashboard/" + captionId);
    request.send();
    window.location.reload();
  }
}
