var barChart, lineChart, wordID, siteTable;
var $chartsSection = $('#charts');
$(document).ready(function(){
    wordID = $('#wordID').val();
    generateSitesTable();
});


function generateSitesTable() {
	wordTable = $('#site-list-table').DataTable({
		dom: "frtip",
		ajax: "../rest/word/"+wordID+"/websites", 
		order: [[ 1, "desc" ]],
		columns: [
		          { data: "id" },
		          { data: "adresse" },
		          { data: "amount"}
		          ],
		          select: 'single'
	} );
}
