var barChart, lineChart, wordID, siteTable, startRange, endRange;
var $chartsSection = $('#charts');
$(document).ready(function(){
    wordID = $('#wordID').val();
    generateSitesTable();
    getWebsitesForWord(generateBarChart);
});

/**
 * Triggers on change of #fromDate
 * Removes the disabled state from #toDate
 * Sets the min attribute to the #fromDate value
 */
$('#fromDate').change(function(){
    if(typeof $(this).val() !== 'undefined'){
        var dateArr = $(this).val().split("-")
        startRange = dateArr[2]+"."+dateArr[1]+"."+dateArr[0];
        $('#toDate').removeAttr('disabled');
        $('#toDate').attr('min', $(this).val());
    }
});

/**
 * Triggers on change of #toDate
 * Triggers --> getAmountsForWordWithDateRange(...)
 */
$('#toDate').change(function(){
    if(typeof $(this).val() !== 'undefined'){
        var dateArr = $(this).val().split("-")
        endRange = dateArr[2]+"."+dateArr[1]+"."+dateArr[0];
        getAmountsForWordWithDateRange(startRange, endRange, generateLineChart);
    }
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

/**
 * Requests the word logs for a given range
 * @param startDate - From date
 * @param endDate - To date
 * @param callback - Performs further operations with retrieved data
 */
var getAmountsForWordWithDateRange = function(startDate, endDate, callback){
    $.getJSON(
        "../rest/word/"+wordID+"/period/"+startDate+"/"+endDate+"",
        function(response){
            $chartsSection.removeClass("overlay");
            callback(response);
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
    var cData = {
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
    barChart = new Chart($ctx).Bar(cData);

}

/**
 * Line-Chart for all dates on which the specified word was crawled
 * @param data - word-logs
 */
var generateLineChart = function(data){
    var labels = [];
    var amount = [];
    var keyWord = Object.keys(data)[0];
    var wordLogs = data[keyWord];
    $(wordLogs).each(function (index) {
        labels.push(wordLogs[index].date);
        amount.push(wordLogs[index].amount);
    });
    var cData = {
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
    var $ctx = $("#line-chart").get(0).getContext("2d");
    lineChart = new Chart($ctx).Line(cData);
}
