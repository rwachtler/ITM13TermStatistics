var barChart, lineChart, siteID, wordTable, startRange, endRange;
var $chartsSection = $('#charts');
$(document).ready(function(){
    siteID = $('#siteID').val();
    generateWordsTable();
    getAllWordsForDomain(generateBarChart);
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
        getAmountsForSiteWithDateRange(startRange, endRange, generateLineChart);
    }
});

/**
 * Requests the crawled words for the given domain-ID, passes the result to a callback method
 * @param callback - Performs further operations with retrieved data
 */
var getAllWordsForDomain = function(callback){
    $.getJSON(
        "/TermStatistics/rest/website/"+siteID+"/words/10",
        function(response){
            $chartsSection.removeClass("overlay");
            callback(response.data);
        } 
    );
}

/**
 * Requests the words + amount for a given range
 * @param startDate - From date
 * @param endDate - To date
 * @param callback - Performs further operations with retrieved data
 */
var getAmountsForSiteWithDateRange = function(startDate, endDate, callback){
    $.getJSON(
        "../rest/website/"+siteID+"/period/"+startDate+"/"+endDate+"",
        function(response){
            $chartsSection.removeClass("overlay");
            callback(response);
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
    var options = {
        legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"><%if(datasets[i].label){%><%=datasets[i].label%><%}%></span></li><%}%></ul>"
    };
    var keys = Object.keys(data);
    $(keys).each(function (index, value) {
        var tmpArr = [];
        $(data[value]).each(function () {
            if (labels.indexOf($(this)[0].date) < 0){
                labels.push($(this)[0].date);

            } else{}
            tmpArr.push($(this)[0].amount);
        });
        amount.push(tmpArr);
    });
    var lineColors = [
        "rgba(38, 166, 91, 0.8)",
        "rgba(34, 167, 240, 0.8)",
        "rgba(219, 10, 91, 0.8)",
        "rgba(108, 122, 137, 0.8)",
        "rgba(207, 0, 15, 0.8)",
        "rgba(247, 202, 24, 0.8)",
        "rgba(242, 38, 19, 0.8)",
        "rgba(154, 18, 179, 0.8)",
        "rgba(249, 105, 14, 0.8)",
        "rgba(34, 49, 63, 0.8)"
    ];

    var cData = {
        labels: labels,
        datasets: [{
            label: keys[0],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[0],
            pointColor: lineColors[0],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[0]
        }, {
            label: keys[1],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[1],
            pointColor: lineColors[1],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[1]
        }, {
            label: keys[2],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[2],
            pointColor: lineColors[2],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[2]
        }, {
            label: keys[3],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[3],
            pointColor: lineColors[3],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[3]
        }, {
            label: keys[4],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[4],
            pointColor: lineColors[4],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[4]
        }, {
            label: keys[5],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[5],
            pointColor: lineColors[5],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[5]
        }, {
            label: keys[6],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[6],
            pointColor: lineColors[6],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[6]
        }, {
            label: keys[7],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[7],
            pointColor: lineColors[7],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[7]
        }, {
            label: keys[8],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[8],
            pointColor: lineColors[8],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[8]
        }, {
            label: keys[9],
            fillColor: "rgba(22, 160, 133, 0.0)",
            strokeColor: lineColors[9],
            pointColor: lineColors[9],
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: amount[9]
        }]
    };

    var $ctx = $("#line-chart").get(0).getContext("2d");
    lineChart = new Chart($ctx).Line(cData, options);
    $("#line-chart-container").append(lineChart.generateLegend());
}