<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>ITM13 TermStatistics - Site Overview</title>
    <meta name="description" content="ITM13 Term Statistics">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../css/main.min.css">
</head>

<body>
	<input type="hidden" name="siteID" id="siteID" value="${siteID}">
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/TermStatistics">ITM13 TermStatistics</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li><a href="/TermStatistics">Home</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a class="disabled"><small>&copy; 2015 ITM13</small></a></li>
                </ul>
            </div>
            <!--/.navbar-collapse -->
        </div>
    </nav>
    <div id="main-content" class="container-fluid">
        <section id="website-list" class="row">
            <h1>Site Overview ${domain}</h1>
            <a href="#charts" class="next-section btn btn-default btn-lg"><span class="glyphicon glyphicon-chevron-down"></span> How about a pie?...Chart <span class="glyphicon glyphicon-chevron-down"></span></a>
            <p class="lead">Those websites were included to the crawling process</p>
            <table class="table table-striped" id="site-list-table">
            	<thead>
        			<tr>
             			<th>ID</th>
            			<th>Adresse</th>
            			<th>Description</th>
            			<th>Depth</th>
            			<th>Active</th>
        			</tr>
    			</thead>
            </table>
        </section>
        <section id="charts" class="row overlay">
            <div class="spinner">
                <div class="inner-spinner"></div>
            </div>
            <h1>Charts</h1>
            <a href="#website-list" class="next-section btn btn-default btn-lg"><span class="glyphicon glyphicon-chevron-up"></span> Add an additional resource <span class="glyphicon glyphicon-chevron-up"></span></a>
            <p class="lead">Barchart</p>
            <canvas width="1200" height="800" id="bar-chart"></canvas>
        </section>
    </div>
    <!-- /container-fluid -->
    <a href="#" id="top-scroll"><span class="glyphicon glyphicon-chevron-up"></span></a>
    <script src="../bower_components/jquery/dist/jquery.min.js"></script>
    <script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="../bower_components/Chart.js/Chart.min.js"></script>
    <script src="../bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
    <script src="../bower_components/datatables-editor/js/dataTables.editor.min.js"></script>
    <script src="../datatables-select/js/dataTables.select.min.js"></script>
    <script src="../datatables-buttons/js/dataTables.buttons.min.js"></script>
    <script src="../js/ui-helper.min.js"></script>
    <script src="../js/site-overview.min.js"></script>
</body>

</html>