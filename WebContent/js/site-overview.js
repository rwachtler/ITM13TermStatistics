var barChart, lineChart, siteID;
var $chartsSection = $('#charts');
$(document).ready(function(){
    siteID = $('#siteID').val();
    getAllWordsForDomain(generateBarChart);
});

var getAllWordsForDomain = function(callback){
    $.getJSON(
        "/TermStatistics/rest/container/oneSite/"+siteID,
        function(response){
            $chartsSection.removeClass("overlay");
            callback(response.data);
        }
    );
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