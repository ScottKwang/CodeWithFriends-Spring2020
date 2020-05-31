var fetchInteractions = function(){
	// for each rxcui
	var idList = [];
	$('.search tbody').children().each(function(){
	    idList.push($(this).data("rxcui")); 
	});

	if(idList.length != 0){
		$.ajax({
			url: `https://rxnav.nlm.nih.gov/REST/interaction/list.json?rxcuis=${idList.join("+")}`,
			async: false
		}).done(function(data){
			if(data.hasOwnProperty('fullInteractionTypeGroup')){
				// print all interactions under each source
				$("#interactionBox").html("");
				var interactions = data.fullInteractionTypeGroup;
				for(var source of interactions){
					printSource(source);
				}
			} else {
				$("#interactionBox").html("<p>No Interactions</p>");
			}
		});
	}
}

var printSource = function(source){
	/**
	 * prints the source to the text
	 */
	$("#interactionBox").append(`<h2>${source.sourceName}</h2>`);

	// draws table
	$("#interactionBox").append(`
		<table class="table">
			<thead class="thead-light">
				<tr>
					<th scope="col">1</th>
					<th scope="col">2</th>
					<th scope="col">Severity</th>
					<th scope="col">Description</th>
				</tr>
			</thead>
			<tbody id="${source.sourceName}">
			</tbody>
		</table>`
	);

	// print the interaction pairs in rows
	for(var pair of source.fullInteractionType){
		var data = pair.interactionPair[0];

		// loop interaction
		var concept = data.interactionConcept;
		$("#" + source.sourceName).append(`
			<tr>
	      		<td>${concept[0].sourceConceptItem.name}</td>
	      		<td>${concept[1].sourceConceptItem.name}</td>
	      		<td>${data.severity}</td>
	      		<td>${data.description}</td>
	    	</tr>
	    `);
	}
}

$(document).ready(function(){
  	$("#interaction").click(fetchInteractions);
});
