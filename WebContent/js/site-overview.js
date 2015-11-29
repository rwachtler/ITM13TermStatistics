var barChart, lineChart, siteID, wordTable;
var $chartsSection = $('#charts');
$(document).ready(function(){
    siteID = $('#siteID').val();
    generateWordsTable();
    getAllWordsForDomain(generateBarChart);
});

var getAllWordsForDomain = function(callback){
    $.getJSON(
        "/TermStatistics/rest/website/"+siteID+"/words/10",
        function(response){
            $chartsSection.removeClass("overlay");
            callback(response.data);
        } 
    );
}


function generateWordsTable() {
	wordTable = $('#word-list-table').DataTable({
		dom: "frtip",
		ajax: "../rest/website/"+siteID+"/words", 
		order: [[ 1, "desc" ]],
		columns: [
		          { data: "word" },
		          { data: "amount" },
		          ],
		          select: 'single'
	} );
}




/**
 * Bar-Chart for all words
 * @param data - crawled words
 */
var generateBarChart = function(data){
    var labels = [];
    var amount = [];
    $(data).each(function (index) {
        labels.push(data[index].word);
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