var getId = function() {
	/**
	 * finds the id of the drugs
	 */

	var drugs = $('#addDrug').val().split("\n"); 

	var areaText = [];

	// add each drug
	for(var drug of drugs){
		var searchString = drug.trim();

		if(searchString != ""){
			// check the url
			$.ajax({
				url: `https://rxnav.nlm.nih.gov/REST/rxcui.json?name=${searchString}&search=2`,
				async: false
			}).done(function(data) {
				// process information
				// check if id returned
				if(data.idGroup.hasOwnProperty('rxnormId')){
					// add to list
					$(".search tbody").append(`
						<tr data-rxcui="${data.idGroup.rxnormId[0]}">
							<td>${searchString}</td>
							<td class="drugHover">
								<img class="drugIcon remove" src="img/icons/x.svg">
							</td>
						</tr>
					`);

					/* ADD LISTENER */
					$(".remove").click(function(){
						// remove row
						$(this).parent().parent().remove();
					});
				} else {
					// add to textarea
					areaText.push(searchString);
				}
			});
		}
	}

	// add failed to textarea
	$('#addDrug').val(areaText.join("\n"));
	if(areaText.length != 0){ $('.alert').show(); }
}

var alertDismiss = function(){
	$('.alert').hide();
}

var clearList = function(){
	$(".search").empty();
}

$(document).ready(function(){
  	$("#searchBtn").click(getId);
  	$('.drugError').click(alertDismiss);
  	$('#clear').click(clearList);
  	$('.alert').hide();
});

