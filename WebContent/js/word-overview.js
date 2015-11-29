var barChart, lineChart, wordID, siteTable;
var $chartsSection = $('#charts');
$(document).ready(function(){
    wordID = $('#wordID').val();
    generateSitesTable();
    getWebsitesForWord(generateBarChart);

});

/**
 * Requests the crawled websites for the given word-ID, passes the result to a callback method
 * @param callback - Performs further operations with retrieved data
 */
var getWebsitesForWord = function(callback){
    $.getJSON(
        "../rest/word/"+wordID+"/websites",
        function(response){
            $chartsSection.removeClass("overlay");
            callback(response.data);
        }
    );
}

function generateSitesTable() {
	siteTable = $('#site-list-table').DataTable({
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

/**
 * Bar-Chart for all websites where the specified word was crawled
 * @param data - websites
 */
var generateBarChart = function(data){
    var labels = [];
    var amount = [];
    $(data).each(function (index) {
        labels.push(data[index].adresse);
        amount.push(data[index].amount);
    });
    var data = {
        labels: labels,
        datasets: [{
            label: "",
            fillColor: "rgba(22, 160, 133, 0.6)",
            strokeColor: "rgba(38, 166, 91, 0.4)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount
        }]
    };
    var $ctx = $("#bar-chart").get(0).getContext("2d");
    barChart = new Chart($ctx).Bar(data);

}
